package com.shoping.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka // dùng để bật cơ chế xử lý Kafka Listener của Spring Kafka.
@ComponentScan({ "com.shoping.notificationservice", "com.shoping.commonservice" })
public class NotificationserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationserviceApplication.class, args);
	}

}
