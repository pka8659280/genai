package com.genai.genai.service;

import com.genai.genai.model.Knowledge;
import com.genai.genai.repository.KnowledgeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class KnowledgeServices {

    @Autowired
    private KnowledgeRepository repository;

    public Knowledge saveKnowledge(Knowledge knowledge) {
        knowledge.setDeleted(false); // Always ensure it's not marked deleted on save
        return repository.save(knowledge);
    }

    public List<Knowledge> getAllKnowledges() {
        return repository.findByDeletedFalse(); // <-- Filter out deleted ones
    }

    public Knowledge updateKnowledge(String id, Knowledge knowledge) {
        Optional<Knowledge> existingOpt = repository.findById(id);
        if (existingOpt.isPresent()) {
            Knowledge existing = existingOpt.get();
            existing.setDescription(knowledge.getDescription());
            existing.setKnowledgeGroupType(knowledge.getKnowledgeGroupType());
            existing.setLastEditDateTime(LocalDateTime.now());
            existing.setLastEditedByUserId(knowledge.getLastEditedByUserId());
            return repository.save(existing);
        } else {
            knowledge.setId(id);
            return repository.save(knowledge);
        }
    }

    public void deleteKnowledge(String id) {
        Optional<Knowledge> knowledge = repository.findById(id);
        knowledge.ifPresent(k -> {
            k.setDeleted(true); // <-- Soft delete here
            repository.save(k);
        });
    }
}
