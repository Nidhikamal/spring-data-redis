package com.bourntec.redis.queue;

public interface MessagePublisher {
	 void publish(String channelName, final String message);
}
