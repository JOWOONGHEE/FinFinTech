package com.fin.finfintech.service;

import com.fin.finfintech.domain.AccountUser;
import com.fin.finfintech.domain.User;
import com.fin.finfintech.dto.Auth;
import com.fin.finfintech.repository.AccountUserRepository;
import com.fin.finfintech.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_Success() {
        Auth.SignUp signUpRequest = new Auth.SignUp();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword("password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("test@example.com");
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.register(signUpRequest);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository, times(1)).existsByEmail(signUpRequest.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(accountUserRepository, times(1)).save(any(AccountUser.class));
    }

    @Test
    public void testRegister_EmailAlreadyExists() {
        Auth.SignUp signUpRequest = new Auth.SignUp();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword("password");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register(signUpRequest);
        });

        assertEquals("이미 존재하는 사용자입니다.", exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(signUpRequest.getEmail());
        verify(userRepository, times(0)).save(any(User.class));
        verify(accountUserRepository, times(0)).save(any(AccountUser.class));
    }

    @Test
    public void testAuthenticate_Success() {
        Auth.SignIn signInRequest = new Auth.SignIn();
        signInRequest.setEmail("test@example.com");
        signInRequest.setPassword("password");

        User foundUser = new User();
        foundUser.setEmail("test@example.com");
        foundUser.setPassword("encodedPassword");

        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(foundUser));
        when(passwordEncoder.matches(signInRequest.getPassword(), foundUser.getPassword())).thenReturn(true);

        User result = userService.authenticate(signInRequest);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository, times(1)).findByEmail(signInRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(signInRequest.getPassword(), foundUser.getPassword());
    }

    @Test
    public void testAuthenticate_EmailNotFound() {
        Auth.SignIn signInRequest = new Auth.SignIn();
        signInRequest.setEmail("test@example.com");
        signInRequest.setPassword("password");

        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.authenticate(signInRequest);
        });

        assertEquals("존재하지 않는 email 입니다.", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(signInRequest.getEmail());
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
    }

    @Test
    public void testAuthenticate_PasswordMismatch() {
        Auth.SignIn signInRequest = new Auth.SignIn();
        signInRequest.setEmail("test@example.com");
        signInRequest.setPassword("password");

        User foundUser = new User();
        foundUser.setEmail("test@example.com");
        foundUser.setPassword("encodedPassword");

        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(foundUser));
        when(passwordEncoder.matches(signInRequest.getPassword(), foundUser.getPassword())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.authenticate(signInRequest);
        });

        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(signInRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(signInRequest.getPassword(), foundUser.getPassword());
    }
}
