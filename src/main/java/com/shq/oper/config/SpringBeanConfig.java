package com.shq.oper.config;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.thymeleaf.dialect.IDialect;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.plugin.SqlMapper;
import com.shq.oper.tag.MyDialect;
import com.shq.oper.util.Constant;

/**
 * Spring Bean 声明
 *
 * @author tjun
 */
@Configuration
@Order(999)
public class SpringBeanConfig {
	@Bean
	public IDialect dialectProvider() {
		return new MyDialect();
	}
	
    /**
     * jackson转换器
     * @return
     */
    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);// 跳过空对象
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 跳过未知字段
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(datetime));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(date));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(time));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(datetime));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(date));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(time));
        om.registerModule(javaTimeModule);
        return om;
    }

    @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisFactory){
        StringRedisTemplate template = new StringRedisTemplate(redisFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        
        ObjectMapper om = initRedisJsonObjectMapper();
        jackson2JsonRedisSerializer.setObjectMapper(om);
        
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

	private ObjectMapper initRedisJsonObjectMapper() {
		ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);// 跳过空对象
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 跳过未知字段
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(datetime));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(date));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(time));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(datetime));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(date));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(time));
        om.registerModule(javaTimeModule);
		return om;
	}

	@Bean
	public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
		RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
		// 设置缓存过期时间
		rcm.setDefaultExpiration(Long.valueOf(60*20));// 默认20分钟缓存
		// 设置value的过期时间
		Map<String, Long> timseMap = new HashMap<>();
		timseMap.put(Constant.CACHEKEY_SECOND_15, Long.valueOf(15));
		timseMap.put(Constant.CACHEKEY_MINUTE_20, Long.valueOf(60*20));
		timseMap.put(Constant.CACHEKEY_HOUR_12, Long.valueOf(60*60*12));
		rcm.setExpires(timseMap);
		return rcm;
	}

	/**
	 * 注册一个容器启动监听器
	 * 
	 * @return
	 */
	@Bean
	public ServletListenerRegistrationBean<ServletContextListener> servletListenerRegistrationBean() {
		ServletListenerRegistrationBean<ServletContextListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<ServletContextListener>();
		servletListenerRegistrationBean.setListener(new ServletContextListener() {
			@Override
			public void contextInitialized(ServletContextEvent sce) {
				sce.getServletContext().setAttribute("version", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss")));
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {

			}
		});
		return servletListenerRegistrationBean;
	}

	@Bean
	public static SpringContextHolder springContextHolder() {
		return new SpringContextHolder();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public SqlMapper sqlMapper(SqlSession sqlSession) {
		return new SqlMapper(sqlSession);
	}

}