package com.genai.genai.repository;

import com.genai.genai.model.Knowledge;
import com.genai.genai.model.KnowledgeGroupType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeGroupTypeRepository extends JpaRepository<KnowledgeGroupType, Long> {
    List<Knowledge> findByDeletedFalse();
}