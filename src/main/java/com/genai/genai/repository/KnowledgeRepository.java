package com.genai.genai.repository;

import com.genai.genai.model.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, String> {
    List<Knowledge> findByDeletedFalse();
    List<Knowledge> findByKnowledgeGroupTypeAndDeletedFalse(String knowledgeGroupTypeId); // NEW
}