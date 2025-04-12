package com.p4.stdiscm.grading_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "grade_history", schema = "grading")
public class GradeHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "grade_id", nullable = false)
    private Long gradeId;
    
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    
    @Column(name = "course_id", nullable = false)
    private Long courseId;
    
    @Column(name = "term_id", nullable = false)
    private Long termId;
    
    @Column(name = "old_grade")
    private String oldGrade;
    
    @Column(name = "new_grade")
    private String newGrade;
    
    @Column(name = "old_numeric", precision = 5, scale = 2)
    private BigDecimal oldNumeric;
    
    @Column(name = "new_numeric", precision = 5, scale = 2)
    private BigDecimal newNumeric;
    
    @Column(name = "changed_by", nullable = false)
    private Long changedBy;
    
    @Column(name = "change_reason")
    private String changeReason;
    
    @CreationTimestamp
    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}