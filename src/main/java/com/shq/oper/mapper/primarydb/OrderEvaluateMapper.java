package com.shq.oper.mapper.primarydb;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.github.pagehelper.Page;

import com.shq.oper.model.domain.primarydb.OrderEvaluate;
import com.shq.oper.model.dto.api.req.ReqOrderEvaluateListDataDto;
import com.shq.oper.model.dto.api.res.ResOrderEvaluateListDataDto;
import com.shq.oper.util.BaseMapper;

@Mapper
public interface OrderEvaluateMapper extends BaseMapper<OrderEvaluate> {

	void orderEvaluateExamine(List<OrderEvaluate> orderEvaluateList);

	Page<ResOrderEvaluateListDataDto> queryOrderEvaluateListData(ReqOrderEvaluateListDataDto param);
	Page<OrderEvaluate>queryOrderEvaluateOrderByIsTop(OrderEvaluate orderEvaluate);
	Page<OrderEvaluate>queryManualEvaluateList(OrderEvaluate orderEvaluate);
	int updateOrderEvaluateAuditStats(OrderEvaluate orderEvaluate);
}