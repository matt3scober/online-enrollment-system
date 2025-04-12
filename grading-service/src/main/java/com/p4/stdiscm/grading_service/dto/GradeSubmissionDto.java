package com.p4.stdiscm.grading_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GradeSubmissionDto {
    private Long studentId;
    private Long courseId;
    private Long termId;
    private String gradeValue;
    private BigDecimal numericGrade;
    private String comments;
    private Long submittedBy;
    private String status;
    private List<ComponentGradeDto> componentGrades;
    private Long version; // For optimistic locking
}
