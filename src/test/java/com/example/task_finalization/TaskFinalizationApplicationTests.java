package com.example.task_finalization;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class TaskFinalizationApplicationTests {

	@Test
	void contextLoads() {
	}

}
