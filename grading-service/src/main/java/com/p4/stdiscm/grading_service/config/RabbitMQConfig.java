package com.p4.stdiscm.grading_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String GRADE_EXCHANGE = "grade.exchange";
    public static final String GRADE_CHANGE_QUEUE = "grade.change.queue";
    public static final String GRADE_SUBMIT_QUEUE = "grade.submit.queue";
    public static final String GRADE_CHANGE_ROUTING_KEY = "grade.change";
    public static final String GRADE_SUBMIT_ROUTING_KEY = "grade.submit";

    @Bean
    public DirectExchange gradeExchange() {
        return new DirectExchange(GRADE_EXCHANGE);
    }

    @Bean
    public Queue gradeChangeQueue() {
        return new Queue(GRADE_CHANGE_QUEUE, true);
    }

    @Bean
    public Queue gradeSubmitQueue() {
        return new Queue(GRADE_SUBMIT_QUEUE, true);
    }

    @Bean
    public Binding gradeChangeBinding(Queue gradeChangeQueue, DirectExchange gradeExchange) {
        return BindingBuilder.bind(gradeChangeQueue)
                .to(gradeExchange)
                .with(GRADE_CHANGE_ROUTING_KEY);
    }

    @Bean
    public Binding gradeSubmitBinding(Queue gradeSubmitQueue, DirectExchange gradeExchange) {
        return BindingBuilder.bind(gradeSubmitQueue)
                .to(gradeExchange)
                .with(GRADE_SUBMIT_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
