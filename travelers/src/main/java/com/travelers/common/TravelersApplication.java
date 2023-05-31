package com.travelers.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.travelers")
@MapperScan("com.travelers.**.mapper")
public class TravelersApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelersApplication.class, args);
	}

}
