package com.example.task_finalization;

import org.springframework.boot.SpringApplication;

public class TestTaskFinalizationApplication {

	public static void main(String[] args) {
		SpringApplication.from(TaskFinalizationApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
