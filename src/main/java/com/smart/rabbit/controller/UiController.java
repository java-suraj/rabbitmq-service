package com.smart.rabbit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping("/producer")
    public String producer() {
        return "producer";
    }

    @GetMapping("/consumer")
    public String consumer() {
        return "consumer";
    }
}
