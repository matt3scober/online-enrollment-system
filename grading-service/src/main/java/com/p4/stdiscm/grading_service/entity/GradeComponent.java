package com.p4.stdiscm.grading_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "grade_components", schema = "grading")
public class GradeComponent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "course_id", nullable = false)
    private Long courseId;
    
    @Column(name = "term_id", nullable = false)
    private Long termId;
    
    @Column(name = "component_name", nullable = false, length = 100)
    private String componentName;
    
    @Column(name = "weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
