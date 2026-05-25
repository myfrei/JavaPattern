package com.patterns.demo.controller;

import com.patterns.demo.dto.PatternDemoResponse;
import com.patterns.demo.patterns.behavioral.memento.MementoDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patterns/behavioral/memento")
public class MementoController {

    private final MementoDemoService service;

    public MementoController(MementoDemoService service) {
        this.service = service;
    }

    @GetMapping("/good")
    public PatternDemoResponse good() {
        return service.runGood();
    }

    @GetMapping("/bad")
    public PatternDemoResponse bad() {
        return service.runBad();
    }
}
