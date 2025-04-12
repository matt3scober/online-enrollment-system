package com.p4.stdiscm.grading_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComponentGradeDto {
    private Long id;
    private Long studentId;
    private Long componentId;
    private String componentName;
    private BigDecimal weight;
    private BigDecimal score;
    private BigDecimal maxScore;
    private BigDecimal weightedScore;
    private String comments;
}
