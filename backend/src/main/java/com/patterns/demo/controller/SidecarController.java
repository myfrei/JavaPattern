package com.patterns.demo.controller;

import com.patterns.demo.dto.PatternDemoResponse;
import com.patterns.demo.patterns.microservices.communication.sidecar.SidecarDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patterns/microservices/communication/sidecar")
public class SidecarController {

    private final SidecarDemoService service;

    public SidecarController(SidecarDemoService service) {
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
