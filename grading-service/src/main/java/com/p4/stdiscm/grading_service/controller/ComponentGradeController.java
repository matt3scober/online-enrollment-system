package com.p4.stdiscm.grading_service.controller;

import com.p4.stdiscm.grading_service.dto.ComponentGradeDto;
import com.p4.stdiscm.grading_service.service.ComponentGradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/component-grades")
public class ComponentGradeController {

    private final ComponentGradeService componentGradeService;

    public ComponentGradeController(ComponentGradeService componentGradeService) {
        this.componentGradeService = componentGradeService;
    }

    @GetMapping("/student/{studentId}/course/{courseId}/term/{termId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.claims['sub'])")
    public ResponseEntity<List<ComponentGradeDto>> getStudentComponentGrades(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            @PathVariable Long termId) {
        
        List<ComponentGradeDto> grades = componentGradeService.getStudentComponentGrades(
                studentId, courseId, termId);
        
        return ResponseEntity.ok(grades);
    }

    @PostMapping("/student/{studentId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> saveComponentGrades(
            @PathVariable Long studentId,
            @RequestBody List<ComponentGradeDto> componentGrades,
            Authentication authentication) {
        
        try {
            // Get faculty ID from authentication
            Long facultyId = Long.parseLong(authentication.getName());
            
            componentGradeService.saveComponentGrades(studentId, componentGrades, facultyId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving component grades: " + e.getMessage());
        }
    }
}
