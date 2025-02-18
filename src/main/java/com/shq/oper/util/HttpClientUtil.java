package com.shq.oper.util;

import static org.springframework.util.Assert.state;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.shq.oper.model.dto.api.message.AppMessageDto;
import com.shq.oper.model.dto.api.message.SendMessageParamDto;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

/**
 * http工具包
 * 
 * @author tjun
 */
public class HttpClientUtil {
	private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
	private static RestTemplate restTemplate;

	static {
		Charset charset = Charset.forName("UTF-8");
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new FormHttpMessageConverter());
		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(new StringHttpMessageConverter(charset));
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
		converters.add(new MappingJackson2HttpMessageConverter(om));

		restTemplate = new RestTemplate(converters);

		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout(15000);//读写超时8s
		factory.setConnectTimeout(16000);//连接超时10s--修改为15秒

		restTemplate.setRequestFactory(factory);
	}

	public static String doGetUrl(String strurl) {
		try {
			URL url = new URL(strurl);
			URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
			ResponseEntity<String> entity = restTemplate.getForEntity(uri,String.class);
			state(HttpStatus.OK == entity.getStatusCode(), "url请求失败：" + url);
			return entity.getBody();
		} catch (Exception e) {
			log.error("[doGet]", e);
		}
		return "";
	}


	public static String doGet(String url) {
		try {
			ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
			state(HttpStatus.OK == entity.getStatusCode(), "url请求失败：" + url);
			return entity.getBody();
		} catch (Exception e) {
			log.error("[doGet]", e);
		}
		return "";
	}

	public static <T> T doGet(String url, Class<T> resultType) {
		try {
			ResponseEntity<T> entity = restTemplate.getForEntity(url, resultType);
			state(HttpStatus.OK == entity.getStatusCode(), "url请求失败：" + url);
			return entity.getBody();
		} catch (Exception e) {
			log.error("[doGet]", e);
		}
		return null;
	}

	public static String doPost(String url, Map<String, Object> params) {
		try {
			MultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
			params.forEach((key, value) -> valueMap.add(key, JsonUtils.toDefaultJson(value)));
			ResponseEntity<String> entity = restTemplate.postForEntity(url, valueMap, String.class);
			state(HttpStatus.OK == entity.getStatusCode(), "url请求失败：" + url);
			return entity.getBody();
		} catch (Exception e) {
			log.error("[doPost]", e);
		}
		return "";
	}

	public static String doPostJson(String url, String body) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> strEntity = new HttpEntity<String>(body, headers);
 			ResponseEntity<String> entity = restTemplate.postForEntity(url, strEntity, String.class);
			state(HttpStatus.OK == entity.getStatusCode(), "url请求失败：" + url);
			return entity.getBody();
		} catch (Exception e) {
			log.error("[doPost]", e);
		}
		return null;
	}

	public static String doPostHeader(HttpHeaders headers, String url, String body) {
		try {
			HttpEntity<String> strEntity = new HttpEntity<String>(body, headers);
			ResponseEntity<String> entity = restTemplate.postForEntity(url, strEntity, String.class);
			state(HttpStatus.OK == entity.getStatusCode(), "url请求失败：" + url);
			return entity.getBody();
		} catch (Exception e) {
			log.error("[doPost]", e);
		}
		return null;
	}


	/**
	 * @author ltg
	 * @date 2018/7/3 10:37
	 * @params:[appMessageDto]
	 * @return: org.springframework.http.HttpHeaders
	 */
	public static HttpHeaders getSendMessageHeaders(AppMessageDto appMessageDto) {

		HttpHeaders headers = new HttpHeaders();
		long time = System.currentTimeMillis();
		Map<String, String> map = new HashMap<>();
		map.put("sysCode", appMessageDto.getAppCode());
		map.put("timestamp", time + "");
		String sign = Sign.sign(map, appMessageDto.getSecret());

		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("sysCode", appMessageDto.getAppCode());
		headers.add("timestamp", time + "");
		headers.add("sign", sign);

		return headers;

	}

	public static String doPostUrlencode(HttpHeaders headers, String url, String body) {
		try {
			HttpEntity<String> strEntity = new HttpEntity<String>(body, headers);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			ResponseEntity<String> entity = restTemplate.postForEntity(url, strEntity, String.class);
			state(HttpStatus.OK == entity.getStatusCode(), "url请求失败：" + url);
			return entity.getBody();
		} catch (Exception e) {
			log.error("[doPost]", e);
		}
		return null;
	}

	public static String postFileMultiPart(String url, Map<String, String> reqParam) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			for (Map.Entry<String, String> entry : reqParam.entrySet()) {
				map.add(entry.getKey() ,  entry.getValue());

				}

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

			state(HttpStatus.OK == response.getStatusCode(), "url请求失败：" + url);
			return response.getBody();
		} catch (Exception e) {
			log.error("[doPost]", e);
			return null;
		}

	}
}