package mx.ipn.ambienta2mx.smartOwl.config

import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Configuration

@Configuration
class DataAccessConfiguration{
  
  @Bean
  StringRedisTemplate template(RedisConnectionFactory connectionFactory){
    return new StringRedisTemplate(connectionFactory)
  }
  
}
