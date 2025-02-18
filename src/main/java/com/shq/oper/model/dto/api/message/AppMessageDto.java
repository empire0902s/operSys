package com.shq.oper.model.dto.api.message;

/**
 * @author: ltg
 * @date: Created in 19:35 2018/7/2
 */
@lombok.Data
public class AppMessageDto {
    private static final long serialVersionUID = 1L;

    private String id;

    private String appName;

    private String secret;

    private String appCode;
}
