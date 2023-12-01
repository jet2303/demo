package com.daily.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestVueController {

    @GetMapping("/vue")
    public String show() {
        return "index.html";
    }
}
