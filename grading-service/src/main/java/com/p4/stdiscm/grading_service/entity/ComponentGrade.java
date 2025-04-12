package com.p4.stdiscm.grading_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "component_grades", schema = "grading")
public class ComponentGrade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    
    @Column(name = "component_id", nullable = false)
    private Long componentId;
    
    @ManyToOne
    @JoinColumn(name = "component_id", insertable = false, updatable = false)
    private GradeComponent component;
    
    @Column(name = "score", nullable = false, precision = 5, scale = 2)
    private BigDecimal score;
    
    @Column(name = "max_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal maxScore;
    
    @Column(name = "comments")
    private String comments;
    
    @Column(name = "submitted_by", nullable = false)
    private Long submittedBy;
    
    @CreationTimestamp
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
}
