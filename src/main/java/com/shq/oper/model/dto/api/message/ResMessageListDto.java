package com.shq.oper.model.dto.api.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: ltg
 * @date: Created in 14:36 2018/7/23
 */
@Data
public class ResMessageListDto implements Serializable {
    private Long msgId;

    //1成功，0失败
    private String status;

    private String submitTime;

    private String sendTime;

    private String remark;

    private static final long serialVersionUID = 1L;
}
