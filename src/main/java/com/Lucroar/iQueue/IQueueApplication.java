package com.Lucroar.iQueue;

import com.Lucroar.iQueue.Config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties ({RsaKeyProperties.class})
@SpringBootApplication
public class IQueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(IQueueApplication.class, args);
	}

}
