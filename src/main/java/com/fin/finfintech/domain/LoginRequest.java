package com.fin.finfintech.domain;

import lombok.Data;

@Data
public class LoginRequest {
    // 로그인 요청 데이터
    private String email;
    private String password;
}
