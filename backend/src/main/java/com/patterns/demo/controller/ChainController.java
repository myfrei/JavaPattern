package com.patterns.demo.controller;

import com.patterns.demo.dto.PatternDemoResponse;
import com.patterns.demo.patterns.behavioral.chainofresponsibility.ChainDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patterns/behavioral/chain-of-responsibility")
public class ChainController {

    private final ChainDemoService service;

    public ChainController(ChainDemoService service) {
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
