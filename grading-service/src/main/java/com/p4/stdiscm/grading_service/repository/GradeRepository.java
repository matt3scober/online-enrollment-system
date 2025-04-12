package com.p4.stdiscm.grading_service.repository;

import com.p4.stdiscm.grading_service.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    Optional<Grade> findByStudentIdAndCourseIdAndTermId(Long studentId, Long courseId, Long termId);
    
    List<Grade> findByStudentId(Long studentId);
    
    List<Grade> findByStudentIdAndTermId(Long studentId, Long termId);
    
    List<Grade> findByCourseIdAndTermId(Long courseId, Long termId);
    
    @Query("SELECT g FROM Grade g WHERE g.courseId = :courseId AND g.termId = :termId AND g.status = :status")
    List<Grade> findByCourseIdAndTermIdAndStatus(@Param("courseId") Long courseId, 
                                                @Param("termId") Long termId, 
                                                @Param("status") String status);
}
