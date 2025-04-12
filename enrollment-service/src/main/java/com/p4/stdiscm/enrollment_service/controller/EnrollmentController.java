package com.p4.stdiscm.enrollment_service.controller;

import com.p4.stdiscm.enrollment_service.model.Enrollment;
import com.p4.stdiscm.enrollment_service.repository.EnrollmentRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private RedissonClient redissonClient;

    @PostMapping
    @Transactional
    public ResponseEntity<Enrollment> enrollStudent(@RequestBody Enrollment enrollment) {
        // Create a lock key based on courseId to prevent simultaneous enrollments for
        // the same course
        String lockKey = "course_" + enrollment.getCourseId() + "_lock";
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock();
            // (Add your seat availability checks and idempotency logic here as needed)
            Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
            return new ResponseEntity<>(savedEnrollment, HttpStatus.CREATED);
        } finally {
            lock.unlock();
        }
    }
}
