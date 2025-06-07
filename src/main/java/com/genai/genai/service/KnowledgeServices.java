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
        knowledge.setDeleted(false); // Ensure soft-delete is false on creation
        return repository.save(knowledge);
    }

    public List<Knowledge> getAllKnowledges() {
        return repository.findByDeletedFalse(); // Return only active (not deleted)
    }

    public Knowledge updateKnowledge(String id, Knowledge knowledge) {
        Optional<Knowledge> existingOpt = repository.findById(id);
        if (existingOpt.isPresent()) {
            Knowledge existing = existingOpt.get();

            if (Boolean.TRUE.equals(existing.getDeleted())) {
                throw new IllegalStateException("Cannot update a deleted knowledge entry.");
            }

            existing.setKnowledgeGroupType(knowledge.getKnowledgeGroupType());
            existing.setLastEditDateTime(LocalDateTime.now());
            existing.setLastEditedByUserId(knowledge.getLastEditedByUserId());
            existing.setKnowledge(knowledge.getKnowledge());
            existing.setKnowledgeName(knowledge.getKnowledgeName());

            return repository.save(existing);
        } else {
            knowledge.setId(id);
            knowledge.setDeleted(false);
            return repository.save(knowledge);
        }
    }

    public void deleteKnowledge(String id) {
        Optional<Knowledge> knowledge = repository.findById(id);
        if (knowledge.isPresent()) {
            Knowledge k = knowledge.get();
            if (Boolean.TRUE.equals(k.getDeleted())) {
                throw new IllegalStateException("Knowledge entry already deleted.");
            }
            k.setDeleted(true);
            repository.save(k);
        } else {
            throw new IllegalArgumentException("Knowledge entry with ID " + id + " not found.");
        }
    }

    public List<Knowledge> getKnowledgeByGroupTypeId(String groupTypeId) {
        return repository.findByKnowledgeGroupTypeAndDeletedFalse(groupTypeId);
    }
}
