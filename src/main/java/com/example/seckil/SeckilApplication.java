package com.example.seckil;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tilldawn
 */
@SpringBootApplication
@MapperScan(basePackages = "com.example.seckil.dao")
public class SeckilApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeckilApplication.class, args);
	}
}