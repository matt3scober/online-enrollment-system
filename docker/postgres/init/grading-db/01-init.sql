-- Schema initialization for grading database
CREATE SCHEMA IF NOT EXISTS grading;

-- Grades table to store student grades
CREATE TABLE IF NOT EXISTS grading.grades (
    id SERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    term_id BIGINT NOT NULL,
    grade_value VARCHAR(5), -- 'A', 'B', 'C', 'D', 'F', 'I', etc.
    numeric_grade DECIMAL(5,2),
    comments TEXT,
    submitted_by BIGINT NOT NULL, -- faculty ID
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'DRAFT', -- 'DRAFT', 'SUBMITTED', 'FINALIZED'
    UNIQUE(student_id, course_id, term_id)
);

-- Grade components (assignments, exams, etc.)
CREATE TABLE IF NOT EXISTS grading.grade_components (
    id SERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL,
    term_id BIGINT NOT NULL,
    component_name VARCHAR(100) NOT NULL,
    weight DECIMAL(5,2) NOT NULL, -- percentage of final grade
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Individual component grades
CREATE TABLE IF NOT EXISTS grading.component_grades (
    id SERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    component_id BIGINT NOT NULL REFERENCES grading.grade_components(id),
    score DECIMAL(5,2) NOT NULL,
    max_score DECIMAL(5,2) NOT NULL,
    comments TEXT,
    submitted_by BIGINT NOT NULL,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Grade change history for auditing
CREATE TABLE IF NOT EXISTS grading.grade_history (
    id SERIAL PRIMARY KEY,
    grade_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    term_id BIGINT NOT NULL,
    old_grade VARCHAR(5),
    new_grade VARCHAR(5),
    old_numeric DECIMAL(5,2),
    new_numeric DECIMAL(5,2),
    changed_by BIGINT NOT NULL,
    change_reason TEXT,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_grade_student ON grading.grades(student_id);
CREATE INDEX IF NOT EXISTS idx_grade_course ON grading.grades(course_id);
CREATE INDEX IF NOT EXISTS idx_grade_term ON grading.grades(term_id);
CREATE INDEX IF NOT EXISTS idx_component_grades_student ON grading.component_grades(student_id);
CREATE INDEX IF NOT EXISTS idx_component_grades_component ON grading.component_grades(component_id);