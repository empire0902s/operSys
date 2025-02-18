package com.shq.oper.service.impl.primarydb;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.OrderEvaluateLikesMapper;
import com.shq.oper.mapper.primarydb.OrderEvaluateMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.OrderEvaluate;
import com.shq.oper.model.domain.primarydb.OrderEvaluateLikes;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.req.ReqOrderEvaluateLikesAddDto;
import com.shq.oper.service.primarydb.OrderEvaluateLikesService;

@Service("orderEvaluateLikesService")
public class OrderEvaluateLikesServiceImpl extends BaseServiceImpl<OrderEvaluateLikes, Long>
		implements OrderEvaluateLikesService {

	@Autowired
	private OrderEvaluateLikesMapper orderEvaluateLikesMapper;

	@Autowired
	private OrderEvaluateMapper orderEvaluateMapper;
	@Autowired
	private ShopperMapper shopperMapper;

	@Override
	public int queryLikesCount(OrderEvaluateLikes tmp) {
		return orderEvaluateLikesMapper.queryLikesCount(tmp);
	}

	@Transactional
	@Override
	public Msg<String> addOrderEvaluateLikesByApi(ReqOrderEvaluateLikesAddDto reqOrderEvaluateLikesAddDto) {
		//验证数据
		if(StringUtils.isEmpty(reqOrderEvaluateLikesAddDto.getOrderEvaluateId())) {
			return Msg.error("评价订单编号不能为空");
		}
		if(StringUtils.isEmpty(reqOrderEvaluateLikesAddDto.getUserSeq())) {
			return Msg.error("评论用户SEQ不能为空");
		}
		if(!isNumeric(reqOrderEvaluateLikesAddDto.getUserSeq())) {
			return Msg.error("Seq【"+reqOrderEvaluateLikesAddDto.getUserSeq()+"】为非法字符串，必须是数字或数字字符串");
		}
		if(!isNumeric(reqOrderEvaluateLikesAddDto.getOrderEvaluateId())) {
			return Msg.error("评价订单编号【"+reqOrderEvaluateLikesAddDto.getOrderEvaluateId()+"】为非法字符串，必须是数字或数字字符串");
		}
		try {
			OrderEvaluate tmp = new OrderEvaluate();
			tmp.setId(Long.parseLong(reqOrderEvaluateLikesAddDto.getOrderEvaluateId()));
			OrderEvaluate orderEvaluate = orderEvaluateMapper.selectOne(tmp);
			if (StringUtils.isEmpty(orderEvaluate)) {
				return Msg.error("评价订单编号【" + reqOrderEvaluateLikesAddDto.getOrderEvaluateId() + "】不存在", "");
			}
			Shopper tmpShopper = new Shopper();
			tmpShopper.setSeq(Integer.parseInt(reqOrderEvaluateLikesAddDto.getUserSeq()));
			Shopper shopper = shopperMapper.selectOne(tmpShopper);
			if (StringUtils.isEmpty(shopper)) {
				return Msg.error("不存在SEQ为【" + reqOrderEvaluateLikesAddDto.getUserSeq() + "】的用户");
			}
			OrderEvaluateLikes entity=new OrderEvaluateLikes();
			entity.setUserSeq(reqOrderEvaluateLikesAddDto.getUserSeq());
			entity.setOrderEvaluateId(Long.parseLong(reqOrderEvaluateLikesAddDto.getOrderEvaluateId()));
			OrderEvaluateLikes selectOne = orderEvaluateLikesMapper.selectOne(entity);
			if(!StringUtils.isEmpty(selectOne)) {
				return Msg.error("您已经点赞过了，不能再点赞了哦~");
			}
			entity.setLikesTime(LocalDateTime.now());

			orderEvaluateLikesMapper.insertSelective(entity);
			return Msg.ok("操作成功","");
		} catch (NumberFormatException e) {
			String err = "调用接口出错," + e.getLocalizedMessage();
			return Msg.error(err);
		}

	}
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}