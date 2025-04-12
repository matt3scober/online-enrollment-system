package com.p4.stdiscm.grading_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "grades", schema = "grading", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_id", "term_id"})
})
public class Grade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    
    @Column(name = "course_id", nullable = false)
    private Long courseId;
    
    @Column(name = "term_id", nullable = false)
    private Long termId;
    
    @Column(name = "grade_value")
    private String gradeValue;
    
    @Column(name = "numeric_grade", precision = 5, scale = 2)
    private BigDecimal numericGrade;
    
    @Column(name = "comments")
    private String comments;
    
    @Column(name = "submitted_by", nullable = false)
    private Long submittedBy;
    
    @CreationTimestamp
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    @UpdateTimestamp
    @Column(name = "last_modified")
    private LocalDateTime lastModified;
    
    @Column(name = "status", length = 20)
    private String status = "DRAFT";
    
    @Version
    private Long version; // Added for optimistic locking
}
