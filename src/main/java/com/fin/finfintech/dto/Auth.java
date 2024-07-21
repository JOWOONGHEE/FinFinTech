package com.fin.finfintech.dto;


import com.fin.finfintech.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class Auth {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignIn {
        private String email;
        private String password;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SignUp {
        private String email;
        private String password;
        private String username;
        private Date birthdate;
        private String phoneNumber;

        public User toEntity(){
            return User.builder()
                    .email(this.email)
                    .password(this.password)
                    .username(this.username)
                    .birthdate(this.birthdate)
                    .phoneNumber(this.phoneNumber)
                    .build();
        }
    }

    // 계정의 인덱스 번호(id), 아이디(signupid)를 가져올 때 사용하는 클래스
    public interface IdInterface {
        int getId();
        String getSignupid();
    }
}