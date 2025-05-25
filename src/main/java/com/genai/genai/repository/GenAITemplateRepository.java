package com.genai.genai.repository;

import com.genai.genai.model.GenaiTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenAITemplateRepository extends JpaRepository<GenaiTemplate, String> {

    // Fetch all GenaiTemplate entries that are not soft-deleted
    List<GenaiTemplate> findByDeletedFalse();
}
