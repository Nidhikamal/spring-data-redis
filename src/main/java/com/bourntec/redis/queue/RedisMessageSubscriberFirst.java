package com.bourntec.redis.queue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class RedisMessageSubscriberFirst implements MessageListener {

	public static List<String> messageList = new ArrayList<String>();

	@Override
	public void onMessage(final Message message, final byte[] pattern) {
		messageList.add(message.toString());
		System.out.println("Message received: in messageQueue" + new String(message.getBody()));
	}

	public void firstQueue(String message) {
		messageList.add(message.toString());
		System.out.println("Message Received from firstQueue channel: <" + message + ">");
	}
}