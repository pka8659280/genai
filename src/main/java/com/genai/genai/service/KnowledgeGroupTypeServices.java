package com.genai.genai.service;

import com.genai.genai.model.KnowledgeGroupType;
import com.genai.genai.repository.KnowledgeGroupTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class KnowledgeGroupTypeServices {

    @Autowired
    private KnowledgeGroupTypeRepository repository;

    public KnowledgeGroupType saveKnowledgeGroupType(KnowledgeGroupType knowledgeGroupType) {
        knowledgeGroupType.setDeleted(false); // Ensure soft-delete is false on creation
        return repository.save(knowledgeGroupType);
    }

    public List<KnowledgeGroupType> getAllKnowledgeGroupTypes() {
        return repository.findByDeletedFalse(); // Return only active (not deleted)
    }

    public KnowledgeGroupType updateKnowledgeGroupType(String id, KnowledgeGroupType knowledgeGroupType) {
        Optional<KnowledgeGroupType> existingOpt = repository.findById(id);
        if (existingOpt.isPresent()) {
            KnowledgeGroupType existing = existingOpt.get();

            if (Boolean.TRUE.equals(existing.getDeleted())) {
                throw new IllegalStateException("Cannot update a deleted group type.");
            }

            existing.setGroupName(knowledgeGroupType.getGroupName());
            existing.setSystemInstruction(knowledgeGroupType.getSystemInstruction());
            existing.setLastEditDateTime(LocalDateTime.now());
            existing.setLastEditedByUserId(knowledgeGroupType.getLastEditedByUserId());

            return repository.save(existing);
        } else {
            knowledgeGroupType.setId(id);
            knowledgeGroupType.setDeleted(false);
            return repository.save(knowledgeGroupType);
        }
    }

    public void deleteKnowledgeGroupType(String id) {
        Optional<KnowledgeGroupType> optional = repository.findById(id);
        if (optional.isPresent()) {
            KnowledgeGroupType entity = optional.get();
            if (Boolean.TRUE.equals(entity.getDeleted())) {
                throw new IllegalStateException("Knowledge group type already deleted.");
            }
            entity.setDeleted(true);
            repository.save(entity);
        } else {
            throw new IllegalArgumentException("Knowledge group type with ID " + id + " not found.");
        }
    }

    public Optional<KnowledgeGroupType> getKnowledgeGroupTypeById(String id) {
        Optional<KnowledgeGroupType> entity = repository.findById(id);
        if (entity.isPresent() && Boolean.FALSE.equals(entity.get().getDeleted())) {
            return entity;
        }
        return Optional.empty();
    }
}
