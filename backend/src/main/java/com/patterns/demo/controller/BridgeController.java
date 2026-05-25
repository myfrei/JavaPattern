package com.patterns.demo.controller;

import com.patterns.demo.dto.PatternDemoResponse;
import com.patterns.demo.patterns.structural.bridge.BridgeDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patterns/structural/bridge")
public class BridgeController {

    private final BridgeDemoService service;

    public BridgeController(BridgeDemoService service) {
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
