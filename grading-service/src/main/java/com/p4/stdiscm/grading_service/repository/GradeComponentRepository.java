package com.p4.stdiscm.grading_service.repository;

import com.p4.stdiscm.grading_service.entity.GradeComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeComponentRepository extends JpaRepository<GradeComponent, Long> {
    
    List<GradeComponent> findByCourseIdAndTermId(Long courseId, Long termId);
}
