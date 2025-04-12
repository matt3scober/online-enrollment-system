package com.p4.stdiscm.grading_service.controller;

import com.p4.stdiscm.grading_service.entity.GradeHistory;
import com.p4.stdiscm.grading_service.service.GradeHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grade-history")
public class GradeHistoryController {

    private final GradeHistoryService gradeHistoryService;

    public GradeHistoryController(GradeHistoryService gradeHistoryService) {
        this.gradeHistoryService = gradeHistoryService;
    }

    @GetMapping("/grade/{gradeId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GradeHistory>> getGradeHistory(@PathVariable Long gradeId) {
        List<GradeHistory> history = gradeHistoryService.getGradeHistory(gradeId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/student/{studentId}/course/{courseId}/term/{termId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GradeHistory>> getStudentGradeHistory(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            @PathVariable Long termId) {
        
        List<GradeHistory> history = gradeHistoryService.getStudentGradeHistory(
                studentId, courseId, termId);
        
        return ResponseEntity.ok(history);
    }
}
