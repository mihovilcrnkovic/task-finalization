package com.example.task_finalization.service;

import com.example.task_finalization.model.Finalization;
import com.example.task_finalization.model.ProcessingJob;
import com.example.task_finalization.repository.FinalizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class FinalizationService {

    FinalizationRepository finalizationRepository;
    RabbitMessageSender rabbitMessageSender;

    public Finalization finalizeJob(ProcessingJob processingJob) {
        Finalization finalization = Finalization.builder()
                .taskId(processingJob.getTaskId())
                .finalizedAt(LocalDateTime.now())
                .outcome("Success!")
                .build();

        finalization = finalizationRepository.save(finalization);
        rabbitMessageSender.sendMessage(finalization);
        return finalization;
    }

}
