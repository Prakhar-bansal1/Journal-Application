package com.journalapp.bansal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BansalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BansalApplication.class, args);
	}

}
 