package com.genai.genai.restController;

import com.genai.genai.model.KnowledgeGroupType;
import com.genai.genai.service.KnowledgeGroupTypeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledgeGroupType")
public class KnowledgeGroupTypeRESTController {

    @Autowired
    private KnowledgeGroupTypeServices service;

    @PostMapping
    public KnowledgeGroupType createKnowledgeGroupType(@RequestBody KnowledgeGroupType knowledgeGroupType) {
        return service.saveKnowledgeGroupType(knowledgeGroupType);
    }

    @GetMapping
    public List<KnowledgeGroupType> getAllKnowledgeGroupTypes() {
        return service.getAllKnowledgeGroupTypes(); // Return only non-deleted entries
    }

    @PutMapping("/{id}")
    public KnowledgeGroupType updateKnowledgeGroupType(@PathVariable Long id, @RequestBody KnowledgeGroupType knowledgeGroupType) {
        return service.updateKnowledgeGroupType(id, knowledgeGroupType);
    }

    @DeleteMapping("/{id}")
    public void deleteKnowledgeGroupType(@PathVariable Long id) {
        service.deleteKnowledgeGroupType(id);
    }
}
