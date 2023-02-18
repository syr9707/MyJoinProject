package com.joinproject.global.file.exception;

import com.joinproject.global.exception.BaseException;
import com.joinproject.global.exception.BaseExceptionType;

public class FileException extends BaseException {

    private BaseExceptionType exceptionType;

    public FileException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
