package com.genai.genai.restController;

import com.genai.genai.model.Knowledge;
import com.genai.genai.service.KnowledgeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeRESTController {

    @Autowired
    private KnowledgeServices service;

    @PostMapping
    public Knowledge createKnowledge(@RequestBody Knowledge knowledge) {
        return service.saveKnowledge(knowledge);
    }

    @GetMapping
    public List<Knowledge> getAllKnowledge() {
        return service.getAllKnowledges(); // Return only non-deleted entries
    }

    @PutMapping("/{id}")
    public Knowledge updateKnowledge(@PathVariable String id, @RequestBody Knowledge knowledge) {
        return service.updateKnowledge(id, knowledge);
    }

    @DeleteMapping("/{id}")
    public void deleteKnowledge(@PathVariable String id) {
        service.deleteKnowledge(id);
    }
}
