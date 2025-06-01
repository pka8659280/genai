package com.genai.genai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainGenAIController {

    @GetMapping("/mainGenAI")
    public String index(Model model) {        
        return "mainGenAI";
    }
}