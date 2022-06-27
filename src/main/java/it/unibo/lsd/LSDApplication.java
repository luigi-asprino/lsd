package it.unibo.lsd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LSDApplication {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/lsd");
		SpringApplication.run(LSDApplication.class, args);
	}

}
