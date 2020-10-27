package com.bourntec.redis.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import com.bourntec.redis.queue.MessagePublisher;
import com.bourntec.redis.queue.RedisMessagePublisher;
import com.bourntec.redis.queue.RedisMessageSubscriberFirst;
import com.bourntec.redis.queue.RedisMessageSubscriberSecond;
import com.bourntec.redis.queue.RedisReceiver;

@Configuration
@ComponentScan("com.bourntec.redis")
@EnableRedisRepositories(basePackages = "com.bourntec.redis.repository")
@PropertySource("classpath:application.properties")
public class RedisConfig {
	@Value("${spring.redis.host}")
	String hostName;

	@Value("${spring.redis.port}")
	Integer port;

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
//		return new JedisConnectionFactory();
		// JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		// jedisConFactory.setHostName("192.168.10.140");
		// jedisConFactory.setPassword("Global12$");
		// jedisConFactory.setPort(6379);
		// return jedisConFactory;
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName, port);
		// redisStandaloneConfiguration.setPassword(RedisPassword.of("yourRedisPasswordIfAny"));
		return new JedisConnectionFactory(redisStandaloneConfiguration);

	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return template;
	}

	@Bean("firstQueueListenerAdapter")
	MessageListenerAdapter firstMessageListener() {
		return new MessageListenerAdapter(new RedisMessageSubscriberFirst(), "firstQueue");
	}

	@Bean("secondQueueListenerAdapter")
	MessageListenerAdapter secondMessageListener() {
		return new MessageListenerAdapter(new RedisMessageSubscriberSecond(), "secondQueue");
	}

	/*
	 * @Bean RedisMessageListenerContainer container(RedisConnectionFactory
	 * connectionFactory,
	 * 
	 * @Qualifier("firstQueueListenerAdapter") MessageListenerAdapter
	 * firstQueueListenerAdapter,
	 * 
	 * @Qualifier("secondQueueListenerAdapter") MessageListenerAdapter
	 * secondQueueListenerAdapter) {
	 * 
	 * RedisMessageListenerContainer container = new
	 * RedisMessageListenerContainer();
	 * container.setConnectionFactory(connectionFactory);
	 * container.addMessageListener(firstQueueListenerAdapter, new
	 * PatternTopic("firstQ"));
	 * container.addMessageListener(secondQueueListenerAdapter, new
	 * PatternTopic("secondQ")); return container; }
	 */

	@Bean
	RedisMessageListenerContainer redisContainer() {
		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(jedisConnectionFactory());

		container.addMessageListener(firstMessageListener(), topic("firstQ"));
		container.addMessageListener(secondMessageListener(), topic("firstQ"));

		/*
		 * container.addMessageListener(notificationListenerAdapter(receiver()), new
		 * PatternTopic("receiveNotificationMessage"));
		 * container.addMessageListener(countListenerAdapter(receiver()), new
		 * PatternTopic("receiveCountMessage"));
		 */

		return container;
	}

	/*
	 * @Bean RedisReceiver receiver() { return new RedisReceiver(); }
	 * 
	 * @Bean("notificationListenerAdapter") MessageListenerAdapter
	 * notificationListenerAdapter(RedisReceiver redisReceiver) { return new
	 * MessageListenerAdapter(redisReceiver, "receiveNotificationMessage"); }
	 * 
	 * @Bean("countListenerAdapter") MessageListenerAdapter
	 * countListenerAdapter(RedisReceiver redisReceiver) { return new
	 * MessageListenerAdapter(redisReceiver, "receiveCountMessage"); }
	 */
	@Bean
	MessagePublisher redisPublisher() {// String channelName
		return new RedisMessagePublisher(redisTemplate(), topic("firstQ"));
	}

	@Bean
	ChannelTopic topic(String channelName) {
		return new ChannelTopic(channelName);
	}
}
