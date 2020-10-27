package com.bourntec.redis.queue;

public class RedisReceiver {
	//private static final Logger LOGGER = LoggerFactory.getLogger(RedisReceiver.class);

	public void receiveNotificationMessage(String message) {
		System.out.println("Message Received from notification channel: <" + message + ">");

	}

	public void receiveCountMessage(String message) {
		System.out.println("Message Received from count channel: <" + message + ">");
	}
}
