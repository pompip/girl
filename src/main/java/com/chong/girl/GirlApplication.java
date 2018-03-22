package com.chong.girl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@SpringBootApplication
public class GirlApplication {

	public static void main(String[] args) {

		SpringApplication.run(GirlApplication.class, args);
	}

}
