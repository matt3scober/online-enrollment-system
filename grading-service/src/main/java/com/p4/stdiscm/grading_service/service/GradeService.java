package com.p4.stdiscm.grading_service.service;

import com.p4.stdiscm.grading_service.dto.GradeChangeEvent;
import com.p4.stdiscm.grading_service.dto.GradeDto;
import com.p4.stdiscm.grading_service.dto.GradeSubmissionDto;
import com.p4.stdiscm.grading_service.dto.StudentGradesResponse;
import com.p4.stdiscm.grading_service.entity.Grade;
import com.p4.stdiscm.grading_service.entity.GradeHistory;
import com.p4.stdiscm.grading_service.repository.GradeHistoryRepository;
import com.p4.stdiscm.grading_service.repository.GradeRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.p4.stdiscm.grading_service.config.RabbitMQConfig.GRADE_CHANGE_ROUTING_KEY;
import static com.p4.stdiscm.grading_service.config.RabbitMQConfig.GRADE_EXCHANGE;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final GradeHistoryRepository gradeHistoryRepository;
    private final RabbitTemplate rabbitTemplate;

    public GradeService(GradeRepository gradeRepository, 
                        GradeHistoryRepository gradeHistoryRepository,
                        RabbitTemplate rabbitTemplate) {
        this.gradeRepository = gradeRepository;
        this.gradeHistoryRepository = gradeHistoryRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Cacheable(value = "studentGrades", key = "#studentId + '-' + #termId")
    public StudentGradesResponse getStudentGrades(Long studentId, Long termId) {
        List<Grade> grades = gradeRepository.findByStudentIdAndTermId(studentId, termId);
        
        StudentGradesResponse response = new StudentGradesResponse();
        response.setStudentId(studentId);
        response.setTermId(termId);
        response.setGrades(grades.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
        
        return response;
    }

    public GradeDto getGrade(Long studentId, Long courseId, Long termId) {
        Optional<Grade> gradeOpt = gradeRepository.findByStudentIdAndCourseIdAndTermId(
                studentId, courseId, termId);
        
        return gradeOpt.map(this::convertToDto).orElse(null);
    }

    @Transactional
    @CacheEvict(value = "studentGrades", key = "#submission.studentId + '-' + #submission.termId")
    public GradeDto submitGrade(GradeSubmissionDto submission) {
        // Get existing grade or create new one
        Optional<Grade> existingGradeOpt = gradeRepository.findByStudentIdAndCourseIdAndTermId(
                submission.getStudentId(), submission.getCourseId(), submission.getTermId());
        
        Grade grade;
        boolean isNew = false;
        
        if (existingGradeOpt.isPresent()) {
            grade = existingGradeOpt.get();
            
            // Check version for optimistic locking
            if (submission.getVersion() != null && 
                !submission.getVersion().equals(grade.getVersion())) {
                throw new OptimisticLockException("The grade has been modified by another user");
            }
            
        } else {
            grade = new Grade();
            grade.setStudentId(submission.getStudentId());
            grade.setCourseId(submission.getCourseId());
            grade.setTermId(submission.getTermId());
            isNew = true;
        }
        
        // Create grade history for change tracking
        GradeHistory history = new GradeHistory();
        history.setGradeId(grade.getId());
        history.setStudentId(grade.getStudentId());
        history.setCourseId(grade.getCourseId());
        history.setTermId(grade.getTermId());
        history.setOldGrade(grade.getGradeValue());
        history.setNewGrade(submission.getGradeValue());
        history.setOldNumeric(grade.getNumericGrade());
        history.setNewNumeric(submission.getNumericGrade());
        history.setChangedBy(submission.getSubmittedBy());
        history.setChangedAt(LocalDateTime.now());
        
        // Update grade
        grade.setGradeValue(submission.getGradeValue());
        grade.setNumericGrade(submission.getNumericGrade());
        grade.setComments(submission.getComments());
        grade.setSubmittedBy(submission.getSubmittedBy());
        grade.setStatus(submission.getStatus());
        
        try {
            // Save grade and history
            Grade savedGrade = gradeRepository.save(grade);
            
            if (!isNew) {
                gradeHistoryRepository.save(history);
                
                // Publish grade change event
                GradeChangeEvent event = new GradeChangeEvent();
                event.setGradeId(savedGrade.getId());
                event.setStudentId(savedGrade.getStudentId());
                event.setCourseId(savedGrade.getCourseId());
                event.setTermId(savedGrade.getTermId());
                event.setOldGrade(history.getOldGrade());
                event.setNewGrade(history.getNewGrade());
                event.setOldNumeric(history.getOldNumeric());
                event.setNewNumeric(history.getNewNumeric());
                event.setChangedBy(history.getChangedBy());
                event.setTimestamp(LocalDateTime.now());
                event.setEventType("GRADE_CHANGED");
                
                rabbitTemplate.convertAndSend(GRADE_EXCHANGE, GRADE_CHANGE_ROUTING_KEY, event);
            }
            
            return convertToDto(savedGrade);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new OptimisticLockException("The grade has been modified by another user");
        }
    }

    private GradeDto convertToDto(Grade grade) {
        GradeDto dto = new GradeDto();
        BeanUtils.copyProperties(grade, dto);
        return dto;
    }
}
