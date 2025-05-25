package com.genai.genai.restController;

import com.genai.genai.model.GenaiTemplate;
import com.genai.genai.service.GenAITemplateServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genAITemplate")
public class GenAITemplateRESTController {

    @Autowired
    private GenAITemplateServices service;

    @PostMapping
    public GenaiTemplate createTemplate(@RequestBody GenaiTemplate template) {
        return service.saveTemplate(template);
    }

    @GetMapping
    public List<GenaiTemplate> getAllTemplates() {
        return service.getAllActiveTemplates(); // Return only non-deleted entries
    }

    @PutMapping("/{id}")
    public GenaiTemplate updateTemplate(@PathVariable String id, @RequestBody GenaiTemplate template) {
        return service.updateTemplate(id, template);
    }

    @DeleteMapping("/{id}")
    public void deleteTemplate(@PathVariable String id) {
        service.deleteTemplate(id);
    }
}
