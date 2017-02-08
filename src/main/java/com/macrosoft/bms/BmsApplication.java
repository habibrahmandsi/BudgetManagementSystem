package com.macrosoft.bms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BmsApplication extends AsyncConfigurerSupport {
	public static void main(String[] args) {
		SpringApplication.run(BmsApplication.class, args);
	}
}
