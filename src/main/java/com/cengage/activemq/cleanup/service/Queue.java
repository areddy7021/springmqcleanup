package com.cengage.activemq.cleanup.service;

public class Queue {
	
	private String queueName;
	private int queueConsumerCount;
	private Long queueLiveTime;
	
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public int getQueueConsumerCount() {
		return queueConsumerCount;
	}
	public void setQueueConsumerCount(int queueConsumerCount) {
		this.queueConsumerCount = queueConsumerCount;
	}
	public Long getQueueLiveTime() {
		return queueLiveTime;
	}
	public void setQueueLiveTime(Long queueLiveTime) {
		this.queueLiveTime = queueLiveTime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + queueConsumerCount;
		result = prime * result
				+ ((queueLiveTime == null) ? 0 : queueLiveTime.hashCode());
		result = prime * result
				+ ((queueName == null) ? 0 : queueName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Queue other = (Queue) obj;
		if (queueConsumerCount != other.queueConsumerCount)
			return false;
		if (queueLiveTime == null) {
			if (other.queueLiveTime != null)
				return false;
		} else if (!queueLiveTime.equals(other.queueLiveTime))
			return false;
		if (queueName == null) {
			if (other.queueName != null)
				return false;
		} else if (!queueName.equals(other.queueName))
			return false;
		return true;
	}
	
}
