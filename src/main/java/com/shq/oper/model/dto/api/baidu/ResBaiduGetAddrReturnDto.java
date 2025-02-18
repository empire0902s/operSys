package com.shq.oper.model.dto.api.baidu;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: ltg
 * @date: Created in 16:04 2018/8/3
 */
@Data
public class ResBaiduGetAddrReturnDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer status;

    private Result result;

    @Data
    public static class Result{
        private String formatted_address;
    }



}
