-- Schema initialization for enrollment database
CREATE SCHEMA IF NOT EXISTS enrollment;

-- Enrollments table to track student course registrations
CREATE TABLE IF NOT EXISTS enrollment.enrollments (
    id SERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL, -- 'ENROLLED', 'DROPPED', 'WAITLISTED'
    enrollment_date TIMESTAMP NOT NULL,
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    term_id BIGINT NOT NULL,
    UNIQUE(student_id, course_id, term_id)
);

-- Terms table to manage academic terms
CREATE TABLE IF NOT EXISTS enrollment.terms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    enrollment_start DATE NOT NULL,
    enrollment_end DATE NOT NULL,
    active BOOLEAN DEFAULT FALSE
);

-- Waitlist table for handling course capacity overflow
CREATE TABLE IF NOT EXISTS enrollment.waitlist (
    id SERIAL PRIMARY KEY,
    enrollment_id BIGINT REFERENCES enrollment.enrollments(id),
    position INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enrollment history for auditing
CREATE TABLE IF NOT EXISTS enrollment.enrollment_history (
    id SERIAL PRIMARY KEY,
    enrollment_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    action VARCHAR(20) NOT NULL, -- 'ADDED', 'DROPPED', 'WAITLISTED', etc.
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    term_id BIGINT NOT NULL
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_enrollment_student ON enrollment.enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_enrollment_course ON enrollment.enrollments(course_id);
CREATE INDEX IF NOT EXISTS idx_enrollment_term ON enrollment.enrollments(term_id);
CREATE INDEX IF NOT EXISTS idx_enrollment_status ON enrollment.enrollments(status);