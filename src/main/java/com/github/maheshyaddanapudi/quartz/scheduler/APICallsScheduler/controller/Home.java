package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Home {

    @RequestMapping("/")
    public String swaggerUI() {
        return "redirect:/swagger-ui.html";
    }
}
