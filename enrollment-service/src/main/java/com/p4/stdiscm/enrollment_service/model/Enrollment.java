package com.p4.stdiscm.enrollment_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Enrollment {

    @Id
    private Long id;
    private Long courseId;
    private Long studentId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
