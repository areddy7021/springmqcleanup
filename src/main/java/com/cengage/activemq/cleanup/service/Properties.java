package com.cengage.activemq.cleanup.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {
	@Value("#{applicationProps['jmx.service.url']}")
	private String jmxUrl;
	
	@Value("#{applicationProps['jmx.username']}")
	private String username;
	
	@Value("#{applicationProps['jmx.password']}")
	private String password;
	
	@Value("#{applicationProps['type']}")
	private String type;
	
	@Value("#{applicationProps['brokerName']}")
	private String brokerName;
	
	@Value("#{applicationProps['attr.consumercount']}")
	private String consumerCount;
	
	@Value("#{applicationProps['attr.name']}")
	private String name;
	
	@Value("#{applicationProps['schedule.task.cleanup.interval']}")
	private int cleanupInterval;
	
	@Value("#{applicationProps['schedule.task.cleanup.max.duration']}")
	private int maxDuration;

	public String getJmxUrl() {
		return jmxUrl;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getType() {
		return type;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public String getConsumerCount() {
		return consumerCount;
	}

	public String getName() {
		return name;
	}

	public int getCleanupInterval() {
		return cleanupInterval;
	}

	public int getMaxDuration() {
		return maxDuration;
	}
	
	
	
}
