package com.comminimizer.Services;

import com.comminimizer.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class ComMinimizer {

	public static Logger log = LogManager.getLogger();
	public static void main(String[] args) {
		SpringApplication.run(ComMinimizer.class, args);
	}

}
