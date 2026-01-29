package com.example.task_finalization.controller;

import com.example.task_finalization.model.Finalization;
import com.example.task_finalization.model.ProcessingJob;
import com.example.task_finalization.service.FinalizationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api")
public class FinalizationController {

    FinalizationService finalizationService;

    @PostMapping("finalize")
    public ResponseEntity finalizeProcessingJob(@RequestBody @Valid ProcessingJob processingJob) {
        Finalization finalization = finalizationService.finalizeJob(processingJob);
        return new ResponseEntity(finalization, HttpStatus.CREATED);
    }
}
