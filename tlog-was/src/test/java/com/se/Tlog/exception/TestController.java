package com.se.Tlog.exception;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/not-found")
    public void throwNotFoundException() {
        throw new CustomException(ErrorType.NOT_FOUND);
    }

    @GetMapping("/exception")
    public void throwException() throws Exception {
        throw new Exception("test exception");
    }
}
