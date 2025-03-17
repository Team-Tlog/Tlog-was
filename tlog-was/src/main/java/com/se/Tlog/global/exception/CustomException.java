package com.se.Tlog.global.exception;

import com.se.Tlog.global.response.error.ErrorType;
import lombok.Getter;



@Getter
public class CustomException extends RuntimeException{
    private final ErrorType errorType;

    public CustomException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
