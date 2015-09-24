package com.cengage.activemq.cleanup.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CleanupService {
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("schedule-CleanupPropertiesConfig-example.xml");
	}	
}
