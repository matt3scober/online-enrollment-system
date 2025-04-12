package com.p4.stdiscm.grading_service.repository;

import com.p4.stdiscm.grading_service.entity.GradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeHistoryRepository extends JpaRepository<GradeHistory, Long> {
    
    List<GradeHistory> findByGradeIdOrderByChangedAtDesc(Long gradeId);
    
    List<GradeHistory> findByStudentIdAndCourseIdAndTermId(Long studentId, Long courseId, Long termId);
}
