package com.shq.oper.model.dto.yml;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Api 地址配置
 * @author tjun
 */
@lombok.Data
@Configuration
@ConfigurationProperties(prefix = "shq-api")
@PropertySource("classpath:/config/system-setting.properties")
public class ShqApi {

	private int testNum;
	
	@NestedConfigurationProperty
	private NetGoodsApi netGoodsApi;
	
	private String imageUploadUrl;
	
	private String imageAddrUrl;

	private String sendMessageUrl;

	private String getAppUrl;

	private String getMessageListUrl;
	
	private String getBannerUrl;

	private String verificationSku;
	
	private String getOsbrandSquareUrl;
	
}
