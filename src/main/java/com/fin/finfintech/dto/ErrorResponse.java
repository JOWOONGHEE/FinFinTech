package com.fin.finfintech.dto;

import com.fin.finfintech.type.ErrorCode;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}
