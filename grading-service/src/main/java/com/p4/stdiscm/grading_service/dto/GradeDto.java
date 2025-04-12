package com.p4.stdiscm.grading_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GradeDto {
    private Long id;
    private Long studentId;
    private Long courseId;
    private Long termId;
    private String gradeValue;
    private BigDecimal numericGrade;
    private String comments;
    private Long submittedBy;
    private LocalDateTime submittedAt;
    private LocalDateTime lastModified;
    private String status;
    private Long version;
}
