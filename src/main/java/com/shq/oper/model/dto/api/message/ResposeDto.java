package com.shq.oper.model.dto.api.message;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: ltg
 * @date: Created in 15:48 2018/7/23
 */
@Data
public class ResposeDto implements Serializable {
    private String responseTime;
    private String code;
    private String msg;
    private List<ResMessageListDto> data;
    private static final long serialVersionUID = 1L;
}
