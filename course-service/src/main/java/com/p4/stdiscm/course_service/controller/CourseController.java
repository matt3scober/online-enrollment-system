package com.p4.stdiscm.course_service.controller;

import com.p4.stdiscm.course_service.model.Course;
import com.p4.stdiscm.course_service.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    // Only allow teachers (mod) and admins to create courses
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course savedCourse = courseRepository.save(course);
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
    }

    // GET endpoints can be left open or secured differently
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    // Other endpoints can be secured similarly as needed
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // For update and delete, you might also restrict them using @PreAuthorize
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        if (!courseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        course.setId(id);
        Course updatedCourse = courseRepository.save(course);
        return ResponseEntity.ok(updatedCourse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (!courseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
