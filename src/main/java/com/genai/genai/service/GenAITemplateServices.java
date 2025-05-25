package com.genai.genai.service;

import com.genai.genai.model.GenaiTemplate;
import com.genai.genai.repository.GenAITemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GenAITemplateServices {

    @Autowired
    private GenAITemplateRepository repository;

    public GenaiTemplate saveTemplate(GenaiTemplate template) {
        template.setDeleted(false); // Ensure it's saved as active
        return repository.save(template);
    }

    public List<GenaiTemplate> getAllTemplates() {
        return repository.findByDeletedFalse(); // For backward compatibility
    }

    public List<GenaiTemplate> getAllActiveTemplates() {
        return repository.findByDeletedFalse(); // For REST controller
    }

    public GenaiTemplate updateTemplate(String id, GenaiTemplate template) {
        Optional<GenaiTemplate> existingOpt = repository.findById(id);
        if (existingOpt.isPresent()) {
            GenaiTemplate existing = existingOpt.get();

            if (Boolean.TRUE.equals(existing.getDeleted())) {
                throw new IllegalStateException("Cannot update a deleted template.");
            }

            existing.setTitle(template.getTitle());
            existing.setDescription(template.getDescription());
            existing.setSystemInstruction(template.getSystemInstruction());
            existing.setKnowledgeGroupType(template.getKnowledgeGroupType());
            existing.setLastEditDateTime(LocalDateTime.now());
            existing.setLastEditedByUserId(template.getLastEditedByUserId());

            return repository.save(existing);
        } else {
            template.setId(id);
            template.setDeleted(false);
            return repository.save(template);
        }
    }

    public void deleteTemplate(String id) {
        Optional<GenaiTemplate> existingOpt = repository.findById(id);
        if (existingOpt.isPresent()) {
            GenaiTemplate template = existingOpt.get();
            if (Boolean.TRUE.equals(template.getDeleted())) {
                throw new IllegalStateException("Template already deleted.");
            }
            template.setDeleted(true);
            repository.save(template);
        } else {
            throw new IllegalArgumentException("Template with ID " + id + " not found.");
        }
    }
}
