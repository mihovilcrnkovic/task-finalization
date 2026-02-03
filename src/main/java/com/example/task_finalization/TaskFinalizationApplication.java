package com.example.task_finalization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class TaskFinalizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskFinalizationApplication.class, args);
	}

}
