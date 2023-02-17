package com.joinproject.global.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    // 에러 코드
    int getErrorCode();

    // Http 상태
    HttpStatus getHttpStatus();

    // 에러 메세지
    String getErrorMessage();

}
