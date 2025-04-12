package com.p4.stdiscm.grading_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentGradesResponse {
    private Long studentId;
    private Long termId;
    private List<GradeDto> grades;
}
