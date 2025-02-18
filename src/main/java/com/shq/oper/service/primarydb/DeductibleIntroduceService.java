package com.shq.oper.service.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduce;
import com.shq.oper.model.dto.Msg;

public interface DeductibleIntroduceService extends BaseService<DeductibleIntroduce, Long> {

	Msg<String> enableOneAndDisabledOthers(DeductibleIntroduce deductibleIntroduce);

	Page<DeductibleIntroduce> queryAllDeductibleIntroduce(DeductibleIntroduce entity);

}