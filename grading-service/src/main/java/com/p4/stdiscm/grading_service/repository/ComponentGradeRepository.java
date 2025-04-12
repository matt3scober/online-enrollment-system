package com.p4.stdiscm.grading_service.repository;

import com.p4.stdiscm.grading_service.entity.ComponentGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentGradeRepository extends JpaRepository<ComponentGrade, Long> {
    
    List<ComponentGrade> findByStudentIdAndComponentIdIn(Long studentId, List<Long> componentIds);
    
    List<ComponentGrade> findByStudentId(Long studentId);
    
    List<ComponentGrade> findByComponentId(Long componentId);
}
