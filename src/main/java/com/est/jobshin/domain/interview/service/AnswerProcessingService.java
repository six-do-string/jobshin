package com.est.jobshin.domain.interview.service;

import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
public class AnswerProcessingService {
    private static final Logger log = LoggerFactory.getLogger(AnswerProcessingService.class);
    private final InterviewDetailService interviewDetailService;
    private final Queue<InterviewQuestion> answerQueue = new ConcurrentLinkedQueue<>();
    private final Object lock = new Object();
    private volatile boolean isProcessing = false;
    private int count = 0;

    @PostConstruct
    public void init() {
        new Thread(this::processAnswers).start();
    }

    public void addAnswer(InterviewQuestion interviewQuestion) {
        answerQueue.add(interviewQuestion);
        synchronized (lock) {
            if(!isProcessing) {
                lock.notify();
            }
        }
    }

    public boolean addAnswerAndReturnStatus(InterviewQuestion interviewQuestion, int num) {
        addAnswer(interviewQuestion);
        while(count < num) {

        }
        count = 0;
        return true;
    }

    private void processAnswers() {
        while (true) {
            synchronized (lock) {
                while (answerQueue.isEmpty()) {
                    try {
                        isProcessing = false;
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                isProcessing = true;
            }

            while (!answerQueue.isEmpty()) {
                InterviewQuestion interviewQuestion = answerQueue.poll();
                if (interviewQuestion != null) {
                    try {
                        interviewDetailService.getAnswerByUser(interviewQuestion);
                        count++;
                    } catch (Exception e) {
                        log.error("Error processing answer", e);
                    }
                }
            }
        }
    }

//    public boolean isProcessing() {
//        return isProcessing;
//    }
}
