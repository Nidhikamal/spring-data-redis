package com.bourntec.redis.queue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class RedisMessageSubscriberSecond implements MessageListener {

	public static List<String> messageList = new ArrayList<String>();

	@Override
	public void onMessage(Message message, byte[] pattern) {
		messageList.add(message.toString());
		System.out.println("Message received: in second q: " + new String(message.getBody()));
	}

	public void secondQueue(String message) {
		messageList.add(message.toString());
		System.out.println("Message Received from secondQueue channel: <" + message + ">");
	}

}