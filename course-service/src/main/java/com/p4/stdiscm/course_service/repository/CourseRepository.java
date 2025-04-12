package com.p4.stdiscm.course_service.repository;

import com.p4.stdiscm.course_service.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
