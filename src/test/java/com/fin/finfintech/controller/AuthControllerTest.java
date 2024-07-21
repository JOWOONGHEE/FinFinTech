package com.fin.finfintech.controller;

import com.fin.finfintech.domain.User;
import com.fin.finfintech.dto.Auth;
import com.fin.finfintech.security.TokenProvider;
import com.fin.finfintech.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegister_Success() throws Exception {
        Auth.SignUp signUpRequest = new Auth.SignUp();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword("password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("test@example.com");
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");

        when(userService.register(any(Auth.SignUp.class))).thenReturn(savedUser);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticate_Success() throws Exception {
        Auth.SignIn signInRequest = new Auth.SignIn();
        signInRequest.setEmail("test@example.com");
        signInRequest.setPassword("password");

        User foundUser = new User();
        foundUser.setId(1L);
        foundUser.setEmail("test@example.com");
        foundUser.setUsername("testuser");
        foundUser.setPassword("encodedPassword");

        when(userService.authenticate(any(Auth.SignIn.class))).thenReturn(foundUser);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }
}
