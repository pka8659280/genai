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
        return repository.save(knowledgeGroupType);
    }

    public List<KnowledgeGroupType> getAllKnowledgeGroupTypes() {
        return repository.findByDeletedFalse();
    }

    public KnowledgeGroupType updateKnowledgeGroupType(Long id, KnowledgeGroupType knowledgeGroupType) {
        Optional<KnowledgeGroupType> existingOpt = repository.findById(id);
        if (existingOpt.isPresent()) {
            KnowledgeGroupType existing = existingOpt.get();
            existing.setGroupName(knowledgeGroupType.getGroupName());
            existing.setDescription(knowledgeGroupType.getDescription());
            existing.setLastEditDateTime(LocalDateTime.now());
            existing.setLastEditedByUserId(knowledgeGroupType.getLastEditedByUserId());
            return repository.save(existing);
        } else {
            knowledgeGroupType.setId(id);
            return repository.save(knowledgeGroupType);
        }
    }

    public void deleteKnowledgeGroupType(Long id) {
        Optional<KnowledgeGroupType> optional = repository.findById(id);
        optional.ifPresent(entity -> {
            entity.setDeleted(true);
            repository.save(entity);
        });
    }
}
