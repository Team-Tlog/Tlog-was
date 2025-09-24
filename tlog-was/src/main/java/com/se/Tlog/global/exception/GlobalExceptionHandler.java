package com.se.Tlog.global.exception;

import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.se.Tlog.global.response.error.ErrorType.INTERNAL_SERVER_ERROR;

import java.util.concurrent.RejectedExecutionException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //커스텀 에러
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handleCustomException(CustomException e) {
        ErrorType errorType = e.getErrorType();
        ErrorRes error = ErrorRes.of(errorType.getStatusCode(), errorType.getMessage());
        log.error("Error occurred : [errorCode={}, message={}]",e.getClass(), e.getMessage(),e);;
        return ResponseEntity.status(error.status()).body(error);
    }
    
    // 비동기 요청 거부 에러
    @ExceptionHandler(RejectedExecutionException.class)
    protected ResponseEntity<?> handleRejectedExecutionException(RejectedExecutionException e) {
    	ErrorRes error = ErrorRes.of(INTERNAL_SERVER_ERROR.getStatusCode(), INTERNAL_SERVER_ERROR.getMessage());
    	log.error("Error occurred : Task rejected - Server Thread Overflow!!");
    	log.error("\t\t : [errorCode={}, message={}]", e.getClass(), e.getMessage(), e);
        return ResponseEntity.status(error.status()).body(error);
    }

    // 클라이언트 요청을 파싱하는 중 발생한 에러
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Error occurred : There is a problem with the HTTP Request Body");
        log.error("Exception occurred", e);  // stacktrace 출력!
        ErrorRes error = ErrorRes.of(HttpStatus.BAD_REQUEST.value(), "[Parse Error] HTTP Request Body가 잘못되었습니다.");
        return ResponseEntity.status(error.status()).body(error);
    }

    // 설정하지 않은 예외 전부 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e) {
        ErrorRes error = ErrorRes.of(INTERNAL_SERVER_ERROR.getStatusCode(), INTERNAL_SERVER_ERROR.getMessage());
        log.error("Error occurred : [errorCode={}, message={}]",e.getClass(), e.getMessage());
        log.error("Exception occurred", e);  // stacktrace 출력!
        return ResponseEntity.status(error.status()).body(error);
    }
}
