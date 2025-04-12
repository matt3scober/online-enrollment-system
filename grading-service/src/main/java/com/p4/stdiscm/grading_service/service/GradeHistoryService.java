package com.p4.stdiscm.grading_service.service;

import com.p4.stdiscm.grading_service.dto.GradeChangeEvent;
import com.p4.stdiscm.grading_service.entity.GradeHistory;
import com.p4.stdiscm.grading_service.repository.GradeHistoryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.p4.stdiscm.grading_service.config.RabbitMQConfig.GRADE_CHANGE_QUEUE;

@Service
public class GradeHistoryService {

    private final GradeHistoryRepository gradeHistoryRepository;

    public GradeHistoryService(GradeHistoryRepository gradeHistoryRepository) {
        this.gradeHistoryRepository = gradeHistoryRepository;
    }

    public List<GradeHistory> getGradeHistory(Long gradeId) {
        return gradeHistoryRepository.findByGradeIdOrderByChangedAtDesc(gradeId);
    }

    public List<GradeHistory> getStudentGradeHistory(Long studentId, Long courseId, Long termId) {
        return gradeHistoryRepository.findByStudentIdAndCourseIdAndTermId(studentId, courseId, termId);
    }

    // This method is a listener for RabbitMQ grade change events
    @RabbitListener(queues = GRADE_CHANGE_QUEUE)
    public void processGradeChangeEvent(GradeChangeEvent event) {
        // Log or process the event
        System.out.println("Received grade change event for grade ID: " + event.getGradeId());
        
        // Here you could implement additional logic like notifying other services
        // or updating analytics data
    }
}
