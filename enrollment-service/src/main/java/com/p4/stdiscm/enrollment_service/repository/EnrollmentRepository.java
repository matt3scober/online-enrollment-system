package com.p4.stdiscm.enrollment_service.repository;

import com.p4.stdiscm.enrollment_service.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}
