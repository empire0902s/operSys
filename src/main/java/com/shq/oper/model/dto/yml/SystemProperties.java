package com.shq.oper.model.dto.yml;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 系统配置
 * @author tjun
 */
@lombok.Data
@Configuration
@ConfigurationProperties(prefix = "system")
@PropertySource("classpath:/config/system-setting.properties")
public class SystemProperties {
	private Boolean test;
	private Boolean weblogTest;
	private String md5Key;
	//跨域请求域名
	private List<String> originList;
}
