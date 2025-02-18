package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.deductible.Deductible;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.req.ReqUserCouponsBusinessDto;
import com.shq.oper.model.dto.api.req.ReqUserCouponsDeductible;

import java.time.LocalDateTime;

public interface DeductibleService extends BaseService<Deductible, Long> {

    Msg<String> giftUserDeductible(ReqUserCouponsBusinessDto dto);

    Deductible getDeductible(String userSeq);

    Msg<String> usedDeductible(ReqUserCouponsDeductible dto);


    /**定时任务，优惠券过期**/
    Msg<String> updateDetuctibleStatusExpiredByJob(String expiredDay);

    Msg<String> saveDeducitbleStatisticsDay(LocalDateTime localDateTime);

}