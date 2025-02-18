package com.shq.oper.model.dto.api.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: ltg
 * @date: Created in 16:05 2018/7/26
 */
@lombok.Data
public class ResCouponsReturnDto implements Serializable {

    private Long activityId;

    private List<CouponsEntity> couponsList;

    @Data
   public class CouponsEntity{
        private String couponsName;

        private Long couponsId;
    }
}
