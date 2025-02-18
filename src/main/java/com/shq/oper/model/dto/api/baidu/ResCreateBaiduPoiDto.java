package com.shq.oper.model.dto.api.baidu;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: ltg
 * @date: Created in 10:59 2018/8/4
 */
@Data
public class ResCreateBaiduPoiDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Short status;

    private String message;

    private Long id;


}
