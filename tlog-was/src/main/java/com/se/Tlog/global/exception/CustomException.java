package com.se.Tlog.global.exception;

import com.se.Tlog.global.response.error.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final ErrorType errorType;
}
