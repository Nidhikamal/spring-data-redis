package com.bourntec.redis.config;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.bourntec.redis.queue.MessagePublisher;
import com.bourntec.redis.queue.RedisMessagePublisher;
import com.bourntec.redis.queue.RedisMessageSubscriberFirst;

//@Configuration
//@EnableRedisRepositories(basePackages = "com.bourntec.redis.repository")
//@PropertySource("classpath:application.properties")
public class LettuceConfig {
	@Value("${spring.redis.host}")
	String hostName;

	@Value("${spring.redis.port}")
	Integer port;

	@Autowired
	RedisConnectionFactory factory;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName, port);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);

	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

		template.setConnectionFactory(redisConnectionFactory());

		template.setKeySerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);

		template.setValueSerializer(jdkSerializationRedisSerializer);
		template.setHashValueSerializer(jdkSerializationRedisSerializer);

		template.setEnableTransactionSupport(true);
		template.afterPropertiesSet();

		return template;
		/*
		 * final RedisTemplate<String, Object> template1 = new RedisTemplate<String,
		 * Object>(); template.setConnectionFactory(redisConnectionFactory());
		 * template.setValueSerializer(new
		 * GenericToStringSerializer<Object>(Object.class)); return template;
		 */

	}
	
	@Bean
	MessagePublisher redisPublisher() {
		return new RedisMessagePublisher(redisTemplate(), topic());
	}
	@Bean
	MessageListenerAdapter messageListener() {
		return new MessageListenerAdapter(new RedisMessageSubscriberFirst());
	}
	@Bean
	ChannelTopic topic() {
		return new ChannelTopic("messageQueue");// pubsub:queue
	}
	@PreDestroy
	public void cleanRedis() {
		factory.getConnection().flushDb();
	}
}
