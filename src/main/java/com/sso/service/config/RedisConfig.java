package com.sso.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.lang.reflect.Method;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

   /**
    * 通过Spring进行注入，参数在application.properties进行配置{RedisProperties}；
    */
   @Autowired
   private JedisConnectionFactory factory;

   @Bean
   @Override
   public CacheManager cacheManager() {
      RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
      return cacheManager;
   }

   @Bean
   public RedisTemplate<Object, Object> redisTemplate() {
      RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
      redisTemplate.setConnectionFactory(factory);

      redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());

      return redisTemplate;
   }

   /**
    * 自定义key.
    * 此方法将会根据完全限定类名+方法名+所有参数的值生成唯一的一个key,即使@Cacheable中的value属性一样，key也会不一样。
    */
   @Bean
   @Override
   public KeyGenerator keyGenerator() {
      return new KeyGenerator() {
         @Override
         public Object generate(Object o, Method method, Object... objects) {
            StringBuilder sb = new StringBuilder();
            sb.append(o.getClass().getName());
            sb.append(method.getName());
            for (Object obj : objects) {
               sb.append(obj.toString());
            }
            return sb.toString();
         }
      };
   }

   @Bean
   @Override
   public CacheResolver cacheResolver() {
      return new SimpleCacheResolver(cacheManager());
   }

   @Bean
   @Override
   public CacheErrorHandler errorHandler() {
      // 用于捕获从Cache中进行CRUD时的异常的回调处理器。
      return new SimpleCacheErrorHandler();
   }
}