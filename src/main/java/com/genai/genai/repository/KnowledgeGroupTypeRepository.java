package com.genai.genai.repository;

import com.genai.genai.model.KnowledgeGroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeGroupTypeRepository extends JpaRepository<KnowledgeGroupType, Long> {

    // Fetch all KnowledgeGroupType entries that are not soft-deleted
    List<KnowledgeGroupType> findByDeletedFalse();
}
