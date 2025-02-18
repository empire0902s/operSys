package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.OrderEvaluateLikes;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.req.ReqOrderEvaluateLikesAddDto;

public interface OrderEvaluateLikesService extends BaseService<OrderEvaluateLikes, Long> {

	int queryLikesCount(OrderEvaluateLikes tmp);

	Msg<String> addOrderEvaluateLikesByApi(ReqOrderEvaluateLikesAddDto reqOrderEvaluateLikesAddDto);
}