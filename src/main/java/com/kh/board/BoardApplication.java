package com.kh.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling	// Scheduler를 사용하기 위한 어노테이션
public class boardApplication {

	public static void main(String[] args) {
		SpringApplication.run(boardApplication.class, args);
	}

}
