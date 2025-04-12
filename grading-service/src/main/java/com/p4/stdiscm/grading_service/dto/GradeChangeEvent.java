package com.p4.stdiscm.grading_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GradeChangeEvent {
    private Long gradeId;
    private Long studentId;
    private Long courseId;
    private Long termId;
    private String oldGrade;
    private String newGrade;
    private BigDecimal oldNumeric;
    private BigDecimal newNumeric;
    private Long changedBy;
    private String changeReason;
    private LocalDateTime timestamp;
    private String eventType;
}
