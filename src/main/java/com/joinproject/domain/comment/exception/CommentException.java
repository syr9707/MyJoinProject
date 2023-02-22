package com.joinproject.domain.comment.exception;

import com.joinproject.global.exception.BaseException;
import com.joinproject.global.exception.BaseExceptionType;

public class CommentException extends BaseException {

    private BaseExceptionType baseExceptionType;

    public CommentException(BaseExceptionType baseExceptionType) {
        this.baseExceptionType = baseExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.baseExceptionType;
    }
}
