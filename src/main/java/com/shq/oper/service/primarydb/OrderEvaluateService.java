package com.shq.oper.service.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.OrderEvaluate;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.req.ReqOrderEvaluateAddDto;
import com.shq.oper.model.dto.api.req.ReqOrderEvaluateListDataDto;
import com.shq.oper.model.dto.api.res.ResOrderEvaluateListDataDto;

public interface OrderEvaluateService extends BaseService<OrderEvaluate, Long> {

	Msg<String> save(OrderEvaluate orderEvaluate);

	Msg<String> orderEvaluateExamine(String orderEvaluateIds, OrderEvaluate orderEvaluate);

	Msg<String> addOrderEvaluateByApi(ReqOrderEvaluateAddDto reqOrderEvaluateAddDto);

	Page<ResOrderEvaluateListDataDto> queryOrderEvaluateListByOrderNo(
			ReqOrderEvaluateListDataDto orderEvaluateListDataDto, PageDto initQueryPageDtp);

	Msg<String> orderEvaluateExamineByOne(OrderEvaluate orderEvaluate);

	Page<OrderEvaluate> queryOrderEvaluateOrderByIsTop(OrderEvaluate entity, PageDto page);

	Msg<String> manualEvaluateSave(OrderEvaluate orderEvaluate);

	Page<OrderEvaluate> queryManualEvaluateList(OrderEvaluate entity);

	Msg<String> deleteOrderEvaluateAndOrderEvaluateImg(OrderEvaluate orderEvaluate);

	int updateOrderEvaluateAuditStats(OrderEvaluate result);
}