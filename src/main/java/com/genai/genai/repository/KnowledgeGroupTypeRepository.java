package com.genai.genai.repository;

import com.genai.genai.model.KnowledgeGroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeGroupTypeRepository extends JpaRepository<KnowledgeGroupType, Long> {
}