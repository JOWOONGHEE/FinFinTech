package com.fin.finfintech.domain;

import lombok.Data;

import java.util.Date;

@Data
public class SignupRequest {
    // 회원가입 요청 데이터
    private String username;
    private String password;
    private String email;
    private Date birthdate;
    private String phone;
}
