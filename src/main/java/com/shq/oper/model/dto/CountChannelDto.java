package com.shq.oper.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: ltg
 * @date: Created in 11:12 2018/11/7
 */
@Data
public class CountChannelDto implements Serializable {
    private Integer _id;
    private long count;

    private static final long serialVersionUID = 1L;
}
