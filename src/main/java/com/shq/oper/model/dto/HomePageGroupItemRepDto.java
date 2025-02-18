package com.shq.oper.model.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author: ltg
 * @date: Created in 19:09 2018/8/21
 */
@lombok.Data
public class HomePageGroupItemRepDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    private List<HomePageItemDto> data;
}
