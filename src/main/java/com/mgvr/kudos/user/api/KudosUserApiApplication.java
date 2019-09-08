package com.mgvr.kudos.user.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication (exclude = HibernateJpaAutoConfiguration.class)
public class KudosUserApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KudosUserApiApplication.class, args);
	}

}
