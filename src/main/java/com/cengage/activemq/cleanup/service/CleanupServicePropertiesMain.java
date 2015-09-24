package com.cengage.activemq.cleanup.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class CleanupServicePropertiesMain {
	
	private final AtomicInteger counter = new AtomicInteger();
	private static HashMap<String, Queue> brokerQueueList = new HashMap<String, Queue>();
	
	@Autowired
	private Properties component;
	
	@Scheduled(cron = "${cron.expression}")
	public void demoServiceMethod()
	{
		JMXServiceURL url = null;
	    System.out.println("counter increment value"+counter.incrementAndGet());
		try {
			url = new JMXServiceURL(component.getJmxUrl());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		String username = component.getUsername();
		String password = component.getPassword();
		Map env = new HashMap();
		String[] credentials = new String[]
		{ username, password };
		env.put(JMXConnector.CREDENTIALS, credentials);
		JMXConnector jmxc = null;
		try {
			jmxc = JMXConnectorFactory.connect(url,env);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MBeanServerConnection conn = null;
		try {
			conn = jmxc.getMBeanServerConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ObjectName activeMQ = null;
		try {
			activeMQ = new ObjectName(component.getType().concat(component.getBrokerName()));
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
		BrokerViewMBean mbean = (BrokerViewMBean) MBeanServerInvocationHandler.newProxyInstance(conn, activeMQ,BrokerViewMBean.class, true);
		for (ObjectName name : mbean.getQueues()) {
			try {
			if(counter.get() == 1){
				Queue queue = new Queue();
				queue.setQueueConsumerCount(Integer.parseInt(conn.getAttribute(name, component.getConsumerCount()).toString()));
				queue.setQueueName((String)conn.getAttribute(name, component.getName()));
				queue.setQueueLiveTime(new Date().getTime());
				brokerQueueList.put(queue.getQueueName(), queue);
				System.out.println("the queue name is "+brokerQueueList.get(queue.getQueueName()).getQueueName()+"the consumer size is are "+brokerQueueList.get(queue.getQueueName()).getQueueConsumerCount());
			}else{
				Queue queueExist = brokerQueueList.get((String)conn.getAttribute(name, component.getName()));
				if(queueExist.getQueueConsumerCount() == 0 && queueExist.getQueueConsumerCount() != Integer.parseInt(conn.getAttribute(name, component.getConsumerCount()).toString())){
					System.out.println("the consumer count difference and the queue count before is  "+queueExist.getQueueConsumerCount());
					queueExist.setQueueLiveTime(new Date().getTime());
					queueExist.setQueueConsumerCount(Integer.parseInt(conn.getAttribute(name, component.getConsumerCount()).toString()));
					brokerQueueList.put(queueExist.getQueueName(), queueExist);
					System.out.println("the consumer count difference and the queue count after is  "+brokerQueueList.get(queueExist.getQueueName()).getQueueConsumerCount());
				}
			}
			} catch (AttributeNotFoundException e) {
				e.printStackTrace();
			} catch (InstanceNotFoundException e) {
				e.printStackTrace();
			} catch (MBeanException e) {
				e.printStackTrace();
			} catch (ReflectionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		System.out.println("the k value is "+counter.get());
		if(counter.get() == component.getCleanupInterval()){
			for(Map.Entry<String, Queue> entry : brokerQueueList.entrySet()){ 
				if(entry.getValue().getQueueConsumerCount() == 0 && (new Date().getTime() - entry.getValue().getQueueLiveTime() > TimeUnit.MINUTES.toMillis(component.getMaxDuration()))){
					try {
						System.out.println("queue name to be deleted is "+entry.getValue().getQueueName());
						mbean.removeQueue(entry.getValue().getQueueName());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				}
			brokerQueueList.clear();
			counter.set(0);
		}
		
	}

}
