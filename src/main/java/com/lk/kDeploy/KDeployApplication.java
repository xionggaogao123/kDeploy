package com.lk.kDeploy;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lk.**.mapper")
public class KDeployApplication {
	protected static Logger LOGGER = LoggerFactory.getLogger(KDeployApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KDeployApplication.class, args);
	}
}
