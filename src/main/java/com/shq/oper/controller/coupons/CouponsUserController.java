package com.shq.oper.controller.coupons;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.enums.CouponsStatusEnum;
import com.shq.oper.enums.CouponsTypeEnum;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.CouponsMapper;
import com.shq.oper.mapper.primarydb.CouponsUserMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.domain.primarydb.CouponsUser;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.service.primarydb.CouponsUserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/coupons/user")
public class CouponsUserController extends BaseController {

	@Autowired
	private CouponsUserService couponsUserService;
	@Autowired
	private CouponsUserMapper couponsUserMapper;

	@Autowired
	private CouponsMapper couponsMapper;

	//----------sqlserver---
	@Autowired
	private ShopperMapper shopperMapper;
	//-------end---sqlserver---
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		Map<String, Object> couponsStatusTypeList = CouponsTypeEnum.getMap();
		List<Coupons> couponsList = couponsMapper.selectAll();
		Map<String, Object> couponTypes=CouponsStatusEnum.getMap();
		request.setAttribute("couponsStatusTypeList", couponsStatusTypeList);
		request.setAttribute("couponsList", couponsList);
		request.setAttribute("couponTypes",couponTypes);

		return toPage(request);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public Data<CouponsUser> list(CouponsUser entity, PageDto page, HttpServletRequest request) {
		if(!StringUtils.isEmpty(entity.getPhone())) {
			Shopper shopper = new Shopper();
			shopper.setMobile(entity.getPhone());
			shopper = shopperMapper.queryBySeqOrMobile(shopper);
			if(shopper != null) {
				entity.setUserSeq(shopper.getSeq().toString());
			}else {
				return Data.ok(new Page<CouponsUser>());
			}
		}
		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
		Page<CouponsUser> entitys = couponsUserMapper.queryCouponsUserAndCoupons(entity);
		return Data.ok(entitys);
	}

	@GetMapping(value = "/selectValue")
	public List<SelectValueData<Long>> dict(Coupons entity, boolean isCode) {
		List<Coupons> datas = couponsMapper.selectByRowBounds(entity, RowBounds.DEFAULT);
		List<SelectValueData<Long>> list = datas.stream().map(d -> new SelectValueData<Long>(d.getName(), d.getId()))
				.collect(Collectors.toList());
		return list;
	}
	@RequestMapping("/couponsUserDetails")
	public ModelAndView couponsDetails(HttpServletRequest request, CouponsUser couponsUser) throws Exception {
		CouponsUser couponsUserDetails = couponsUserService.selectOne(couponsUser);
		Integer couponsType = couponsUserDetails.getCouponsType();
		Integer couponsStatus = couponsUserDetails.getCouponsStatus();
		String couponsTypeString="";
		String couponsStatusString="";
		Map<String, Object> map = CouponsTypeEnum.getMap();
		Map<String, Object> couponsStatusTypeMap = CouponsStatusEnum.getMap();
		for (String key : map.keySet()) {
			if(couponsType.toString().equals(map.get(key)))
			{
				couponsTypeString=key;
			}
		}
		for (String key : couponsStatusTypeMap.keySet()) {
			if(couponsStatus.equals(couponsStatusTypeMap.get(key)))
			{
				couponsStatusString=key;
			}
		}
		request.setAttribute("couponsUserDetails", couponsUserDetails);
		request.setAttribute("couponsTypeString", couponsTypeString);
		request.setAttribute("couponsStatusString", couponsStatusString);
		return toPage(request);
	}
	/**
	 * 用户领取优惠券
	 * @param actBatch	优惠券批次号
	 * @param userSeq	用户序列号
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/receiveCoupons")
	public Msg<String> receiveCoupons(String actBatch, String userSeq, HttpServletRequest request) {/*
		Msg<CouponsUser> resultMsg = couponsUserService.receiveCoupons(actBatch, userSeq, request);
		CouponsUser couponsUser = resultMsg.getValue();
		if(StringUtils.isEmpty(couponsUser)) {
			return Msg.error(resultMsg.getMsg());
		}
		couponsUser.setUpdateAdmin(this.getCreateOrUpdateAdminName(request));
		couponsUser.setCreateAdmin(this.getCreateOrUpdateAdminName(request));
		try {
			couponsUserService.insert(couponsUser);
			return Msg.ok("领取成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Msg.error("领取失败");
		}
	*/
		return null;
	}
}