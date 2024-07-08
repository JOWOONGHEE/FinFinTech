package com.fin.finfintech.dto;


import com.fin.finfintech.entity.User;
import lombok.Data;

import java.util.Date;

public class Auth {

    @Data
    public static class SignIn {
        private String email;
        private String password;

    }

    @Data
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