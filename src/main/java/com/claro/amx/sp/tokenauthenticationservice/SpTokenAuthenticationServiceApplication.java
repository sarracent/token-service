package com.claro.amx.sp.tokenauthenticationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.claro.amx"})
@SpringBootApplication
public class SpTokenAuthenticationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpTokenAuthenticationServiceApplication.class, args);
	}

}
