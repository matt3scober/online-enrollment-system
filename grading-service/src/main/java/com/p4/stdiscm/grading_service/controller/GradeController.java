package com.p4.stdiscm.grading_service.controller;

import com.p4.stdiscm.grading_service.dto.GradeDto;
import com.p4.stdiscm.grading_service.dto.GradeSubmissionDto;
import com.p4.stdiscm.grading_service.dto.StudentGradesResponse;
import com.p4.stdiscm.grading_service.service.GradeService;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.claims['sub'])")
    public ResponseEntity<StudentGradesResponse> getStudentGrades(
            @PathVariable Long studentId,
            @RequestParam Long termId) {
        
        StudentGradesResponse response = gradeService.getStudentGrades(studentId, termId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}/course/{courseId}/term/{termId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.claims['sub'])")
    public ResponseEntity<GradeDto> getStudentCourseGrade(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            @PathVariable Long termId) {
        
        GradeDto grade = gradeService.getGrade(studentId, courseId, termId);
        if (grade == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(grade);
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> submitGrade(
            @RequestBody GradeSubmissionDto submission,
            Authentication authentication) {
        
        try {
            // Get faculty ID from authentication
            Long facultyId = Long.parseLong(authentication.getName());
            submission.setSubmittedBy(facultyId);
            
            GradeDto savedGrade = gradeService.submitGrade(submission);
            return ResponseEntity.ok(savedGrade);
        } catch (OptimisticLockException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("The grade has been modified by another user. Please refresh and try again.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while submitting the grade: " + e.getMessage());
        }
    }
}
