package com.genai.genai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenAITemplateController {

    @GetMapping("/genAiTemplate")
    public String index(Model model) {        
        return "genAiTemplate";
    }
}