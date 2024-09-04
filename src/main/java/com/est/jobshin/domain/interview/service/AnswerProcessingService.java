package com.est.jobshin.domain.interview.service;

import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
public class AnswerProcessingService {
    private static final Logger log = LoggerFactory.getLogger(AnswerProcessingService.class);
    private final InterviewDetailService interviewDetailService;
    private final Map<Long, Queue<InterviewQuestion>> answerQueues = new ConcurrentHashMap<>();
    private final Map<Long, Integer> answerCounts = new ConcurrentHashMap<>();
    private final Object lock = new Object();
    private volatile boolean isProcessing = false;

    @PostConstruct
    public void init() {
        new Thread(this::processAnswers).start();
    }

    public void addAnswer(Long interviewId, InterviewQuestion interviewQuestion) {
        answerQueues.computeIfAbsent(interviewId, k -> new ConcurrentLinkedQueue<>()).add(interviewQuestion);
        synchronized (lock) {
            if(!isProcessing) {
                lock.notify();
            }
        }
    }

    public boolean addAnswerAndReturnStatus(Long interviewId, InterviewQuestion interviewQuestion, int num) {
        addAnswer(interviewId, interviewQuestion);
        while(answerCounts.getOrDefault(interviewId, 0) < num) {

        }
        answerCounts.remove(interviewId);
        return true;
    }

    private void processAnswers() {
        while (true) {
            synchronized (lock) {
                while (answerQueues.values().stream().allMatch(Queue::isEmpty)) {
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

            for(Map.Entry<Long, Queue<InterviewQuestion>> entry : answerQueues.entrySet()) {
                Long interviewId = entry.getKey();
                Queue<InterviewQuestion> answerQueue = entry.getValue();

                while (!answerQueue.isEmpty()) {
                    InterviewQuestion interviewQuestion = answerQueue.poll();
                    if (interviewQuestion != null) {
                        try {
                            interviewDetailService.getAnswerByUser(interviewQuestion);
                            answerCounts.put(interviewId, answerCounts.getOrDefault(interviewId, 0) + 1);
                        } catch (Exception e) {
                            log.error("Error processing answer", e);
                        }
                    }
                }
            }

        }
    }
}
