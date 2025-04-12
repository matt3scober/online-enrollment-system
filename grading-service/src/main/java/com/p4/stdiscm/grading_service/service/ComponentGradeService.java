package com.p4.stdiscm.grading_service.service;

import com.p4.stdiscm.grading_service.dto.ComponentGradeDto;
import com.p4.stdiscm.grading_service.entity.ComponentGrade;
import com.p4.stdiscm.grading_service.entity.GradeComponent;
import com.p4.stdiscm.grading_service.repository.ComponentGradeRepository;
import com.p4.stdiscm.grading_service.repository.GradeComponentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ComponentGradeService {

    private final ComponentGradeRepository componentGradeRepository;
    private final GradeComponentRepository gradeComponentRepository;

    public ComponentGradeService(ComponentGradeRepository componentGradeRepository,
                                GradeComponentRepository gradeComponentRepository) {
        this.componentGradeRepository = componentGradeRepository;
        this.gradeComponentRepository = gradeComponentRepository;
    }

    public List<ComponentGradeDto> getStudentComponentGrades(Long studentId, Long courseId, Long termId) {
        // Get all components for the course/term
        List<GradeComponent> components = gradeComponentRepository.findByCourseIdAndTermId(courseId, termId);
        
        // Get component grades for student
        List<Long> componentIds = components.stream()
                .map(GradeComponent::getId)
                .collect(Collectors.toList());
        
        List<ComponentGrade> grades = componentGradeRepository.findByStudentIdAndComponentIdIn(
                studentId, componentIds);
        
        // Map components by ID for easy lookup
        Map<Long, GradeComponent> componentMap = components.stream()
                .collect(Collectors.toMap(GradeComponent::getId, c -> c));
                
        // Convert to DTOs with calculated weighted scores
        return grades.stream().map(grade -> {
            ComponentGradeDto dto = new ComponentGradeDto();
            BeanUtils.copyProperties(grade, dto);
            
            GradeComponent component = componentMap.get(grade.getComponentId());
            if (component != null) {
                dto.setComponentName(component.getComponentName());
                dto.setWeight(component.getWeight());
                
                // Calculate weighted score: (score/maxScore) * weight
                BigDecimal scoreRatio = grade.getScore().divide(grade.getMaxScore(), 4, RoundingMode.HALF_UP);
                dto.setWeightedScore(scoreRatio.multiply(component.getWeight())
                        .setScale(2, RoundingMode.HALF_UP));
            }
            
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void saveComponentGrades(Long studentId, List<ComponentGradeDto> componentGradeDtos, Long submittedBy) {
        for (ComponentGradeDto dto : componentGradeDtos) {
            ComponentGrade grade = new ComponentGrade();
            grade.setStudentId(studentId);
            grade.setComponentId(dto.getComponentId());
            grade.setScore(dto.getScore());
            grade.setMaxScore(dto.getMaxScore());
            grade.setComments(dto.getComments());
            grade.setSubmittedBy(submittedBy);
            
            componentGradeRepository.save(grade);
        }
    }
}