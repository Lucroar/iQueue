package com.Lucroar.iQueue;

import com.Lucroar.iQueue.Config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties ({RsaKeyProperties.class})
@SpringBootApplication
@EnableScheduling
public class IQueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(IQueueApplication.class, args);
	}

}
