package com.est.jobshin.domain.interview.service;

import com.est.jobshin.domain.interview.dto.QuestionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageQueueService {
    private final RabbitTemplate rabbitTemplate;

    public void enqueueQuestion(Long interviewDetailsId, String question) {
        String queueName = "interview." + interviewDetailsId;
        rabbitTemplate.convertAndSend(queueName, new QuestionMessage(interviewDetailsId, question));
    }

    public String dequeueQuestion(Long interviewDetailsId) {
        String queueName = "interview." + interviewDetailsId;
        QuestionMessage questionMessage = (QuestionMessage) rabbitTemplate.receiveAndConvert(queueName);
        return questionMessage != null ? questionMessage.getQuestion() : null;
    }
}
