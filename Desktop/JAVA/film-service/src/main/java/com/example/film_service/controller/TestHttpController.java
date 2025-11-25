package com.example.film_service.controller;

import com.example.film_service.service.TestHttpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestHttpController {
    private final TestHttpService testHttpService;

    public TestHttpController(TestHttpService testHttpService) {
        this.testHttpService = testHttpService;
    }

    @GetMapping("/test/json")
    public String testGET(){
        return testHttpService.testGET();
    }

}
