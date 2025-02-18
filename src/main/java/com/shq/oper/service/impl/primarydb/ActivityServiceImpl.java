package com.shq.oper.service.impl.primarydb;

import com.alibaba.druid.sql.visitor.functions.If;
import com.github.pagehelper.Page;
import com.github.pagehelper.util.StringUtil;
import com.shq.oper.enums.CouponsTypeEnum;
import com.shq.oper.enums.api.CouponsFromSysEnums;
import com.shq.oper.mapper.primarydb.ActivityMapper;
import com.shq.oper.mapper.primarydb.CouponsMapper;
import com.shq.oper.mapper.primarydb.CouponsTypeRuleMapper;
import com.shq.oper.model.domain.primarydb.Activity;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.domain.primarydb.CouponsTypeRule;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.req.ReqActivityListDataDto;
import com.shq.oper.model.dto.api.req.ReqBrandActivityAddDto;
import com.shq.oper.model.dto.api.req.ReqBrandActivityListDataDto;
import com.shq.oper.model.dto.api.req.ReqBrandCouponsDto;
import com.shq.oper.model.dto.api.res.ResActivityListDataDto;
import com.shq.oper.model.dto.api.res.ResBrandActivityListDto;
import com.shq.oper.model.dto.api.res.ResCouponsReturnDto;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.primarydb.ActivityService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.Constant;
import com.shq.oper.util.DateUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import com.shq.oper.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("activityService")
public class ActivityServiceImpl extends BaseServiceImpl<Activity, Long> implements ActivityService {
	
	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private CouponsMapper couponsMapper;

	@Autowired
	private CouponsTypeRuleMapper couponsTypeRuleMapper;

	private Map<String, Object> result;


	/**缓存活动数据**/
	@Cacheable(value = Constant.CACHEKEY_HOUR_12, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ACTIVITY_QUERY.concat('actBatch-') + #actBatch", unless = "#result eq null")
	public Activity queryByBatch(String actBatch) {
		Activity queryTmp = new Activity(actBatch,null);
		return activityMapper.selectOne(queryTmp);
				
	}
	
	/**查询所有活动**/
	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ACTIVITY_QUERY.concat('ApiData-') + #apiDto", unless = "#result eq null")
	@Override
	public Page<ResActivityListDataDto> queryListByApi(ReqActivityListDataDto apiDto, PageDto pageDto) {
		apiDto.setNowTime(DateUtils.to(LocalDateTime.now()));
		
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<ResActivityListDataDto> listData = activityMapper.queryListByApi(apiDto);
		return listData;
	}


	/**
	 * 活动禁用，把该活动关联的优惠券也禁用
	 *@author ltg
	 *@date 2018/6/15 11:34
	 * @params:[activity]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@Transactional
	@Override
	public Msg<String> disabled(Activity activity){

		activityMapper.disabledById(activity);
		Coupons coupons=new Coupons();
		coupons.setActivityId(activity.getId()+"");
		List<Coupons> couponsList=couponsMapper.select(coupons);
		if (couponsList==null||couponsList.size()<=0){
			SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_ACTIVITY_QUERY);
			return Msg.ok("禁用成功");
		}
		couponsList.forEach(
				p->{
					p.setUpdateAdmin(activity.getUpdateAdmin());
					p.setUpdateTime( activity.getUpdateTime());
				}
		);
		couponsMapper.updateBath(couponsList);
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_ACTIVITY_QUERY);
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONS_QUERY);
		return Msg.ok("禁用成功");
	}


	@Transactional
	@Override
	public Msg<String> addBrandActivityByApi(ReqBrandActivityAddDto dto){
		String fromSys = dto.getFromSys();
		String fromSysCode = dto.getFromSysCode();
		LocalDateTime sendTimeStart = null;
		LocalDateTime sendTimeEnd = null;
		//活动数据验证。

		if (StringUtil.isEmpty(fromSys)){
			return Msg.error("请输入系统来源");
		}else {
			CouponsFromSysEnums fromSysEnums=CouponsFromSysEnums.getByCode(fromSys);
			if (fromSysEnums==null){
				return Msg.error("系统来源["+dto.getFromSys()+"]不正确");
			}
		}
		if(StringUtil.isEmpty(dto.getFromSysCode())){
			return Msg.error("请输入系统来源code");
		}
		try {
			sendTimeStart = DateUtils.parse(dto.getSendTimeStart(), DateUtils.DateFormat.LONG_DATE_PATTERN_LINE);
		} catch (Exception e) {
			return Msg.error("活动开始时间有误:"+dto.getSendTimeStart());
		}
		
		try {
			sendTimeEnd = DateUtils.parse(dto.getSendTimeEnd(), DateUtils.DateFormat.LONG_DATE_PATTERN_LINE);
		} catch (Exception e) {
			return Msg.error("活动结束时间有误："+dto.getSendTimeEnd());
		}

		if (sendTimeStart.isAfter(sendTimeEnd)){
			return Msg.error("抱歉,活动开始时间不能大于活动结束时间");
		}

		if (StringUtil.isEmpty(dto.getBatch())){
			return Msg.error("抱歉,活动批次不能为空");
		}

		Activity activityParam = new Activity();
		activityParam.setBatch(dto.getBatch());
		int i=activityMapper.selectCount(activityParam);
		if (i>=1){
			return Msg.error("抱歉,活动此批次已存在");
		}
		
//		if (!dto.getUseType().matches("^[1-2]*$")) {
//			return Msg.error("请正确输活动类型："+dto.getUseType());
//		}
		
		//添加活动
//		Integer useType = Integer.parseInt(dto.getUseType());
		
		LocalDateTime dtNow = LocalDateTime.now();
		
		Activity activity = new Activity();

		activity.setCreateTime(dtNow);
		activity.setCreateAdmin(dto.getBrandShopSeq());
		activity.setUpdateTime(dtNow);
		activity.setUpdateAdmin(dto.getBrandShopSeq());
		activity.setIsDisabled(dto.isDisabled());
		activity.setName(dto.getName());
//		activity.setBatch(dto.getBatch());
//		activity.setBatch(DateUtils.to(LocalDateTime.now(),DateUtils.DateFormat.LONG_DATE_PATTERN_WITH_MILSEC_NONE));
		activity.setBatch(dto.getBatch());

		activity.setTitle(dto.getTitle());
		activity.setFromSys(fromSys);
		activity.setFromSysCode(fromSysCode);
		//activity.setActivityGoodsRuleId(activityGoodsRuleId);
		activity.setSendTimeStart(sendTimeStart);
		activity.setSendTimeEnd(sendTimeEnd);
		//activity.setUserRoleRule(userRoleRule);
		//activity.setReceiveNumRule(receiveNumRule);
		activity.setActivityDesc(dto.getActivityDesc());
		activity.setUseType(2);//活动类型(1:优惠券活动,2:商品活动)

		activityMapper.insert(activity);
		
		return Msg.ok("添加秒杀活动成功",String.valueOf(activity.getId()));
	}

	/**
	 *@author ltg
	 *@date 2018/7/18 11:51
	 * @params:[dto]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@Transactional
	@Override
	public Msg<ResCouponsReturnDto> addBrandActivityCouponTypeByApi(ReqBrandActivityAddDto dto) {

		String fromSys = dto.getFromSys();
		String fromSysCode = dto.getFromSysCode();
		LocalDateTime sendTimeStart = null;
		LocalDateTime sendTimeEnd = null;

		if (StringUtil.isEmpty(fromSys)){
			return Msg.error("请输入系统来源",new ResCouponsReturnDto());
		}else {
			CouponsFromSysEnums fromSysEnums=CouponsFromSysEnums.getByCode(fromSys);
			if (fromSysEnums==null){
				return Msg.error("系统来源["+dto.getFromSys()+"]不正确",new ResCouponsReturnDto());
			}
		}
		if(StringUtil.isEmpty(dto.getFromSysCode())){
			return Msg.error("请输入系统来源code",new ResCouponsReturnDto());
		}
		//活动数据验证。
		try {
			sendTimeStart = DateUtils.parse(dto.getSendTimeStart(), DateUtils.DateFormat.LONG_DATE_PATTERN_LINE);
		} catch (Exception e) {
			return Msg.error("活动开始时间有误:"+dto.getSendTimeStart(),new ResCouponsReturnDto());
		}

		try {
			sendTimeEnd = DateUtils.parse(dto.getSendTimeEnd(), DateUtils.DateFormat.LONG_DATE_PATTERN_LINE);
		} catch (Exception e) {
			return Msg.error("活动结束时间有误："+dto.getSendTimeEnd(),new ResCouponsReturnDto());
		}

		if (sendTimeStart.isAfter(sendTimeEnd)){
			return Msg.error("抱歉,活动开始时间不能大于活动结束时间",new ResCouponsReturnDto());
		}
		if (StringUtil.isEmpty(dto.getBatch())){
			return Msg.error("抱歉,请输入活动批次号",new ResCouponsReturnDto());
		}
		Activity activityParam=new Activity();
		activityParam.setBatch(dto.getBatch());
		int i=activityMapper.selectCount(activityParam);
		if (i>=1){
			return Msg.error("抱歉,此活动批次号已存在",new ResCouponsReturnDto());
		}
		
		List<ReqBrandCouponsDto> couponsDtoList=dto.getBrandCouponsList();
		//验证数据
		Msg<ResCouponsReturnDto> p = valiDate(couponsDtoList);
		if (p != null) return p;

		//添加活动
		Activity activity = getActivity(dto, fromSys, fromSysCode, sendTimeStart, sendTimeEnd);
		activityMapper.insert(activity);

		List<Coupons> couponsList=new ArrayList<>();
		prosessData(dto, couponsDtoList, couponsList, activity);

		ResCouponsReturnDto resCouponsReturnDto=new ResCouponsReturnDto();
		resCouponsReturnDto.setActivityId(activity.getId());

		List<ResCouponsReturnDto.CouponsEntity> couponsEntityList =new ArrayList<>();
		if (couponsList.size()<=0){
			resCouponsReturnDto.setCouponsList(couponsEntityList);
			return Msg.ok("添加优惠券活动成功",resCouponsReturnDto);
		}
		couponsMapper.insertList(couponsList);

		couponsList.forEach(
				coup->{
					ResCouponsReturnDto.CouponsEntity couponsEntity=resCouponsReturnDto.new CouponsEntity();
					couponsEntity.setCouponsId(coup.getId());
					couponsEntity.setCouponsName(coup.getName());
					couponsEntityList.add(couponsEntity);
				}
		);
		resCouponsReturnDto.setCouponsList(couponsEntityList);

		return Msg.ok("添加优惠券活动成功",resCouponsReturnDto);
	}

	@Override
	public Page<ResBrandActivityListDto> queryBrandListByApi(ReqBrandActivityListDataDto apiDto, PageDto pageDto) {
		apiDto.setNowTime(DateUtils.to(LocalDateTime.now()));

		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<ResBrandActivityListDto> listData = activityMapper.queryBrandListByApi(apiDto);
		return listData;
	}

	@Transactional
	@Override
	public Msg<String> updateBrandActivity(Activity activity) {
		activity.setUpdateTime(LocalDateTime.now());
		activity.setUpdateAdmin(activity.getBrandShopSeq());
		activityMapper.updateBrandActice(activity);
		return Msg.ok("操作成功");
	}


	private Msg<ResCouponsReturnDto> valiDate(List<ReqBrandCouponsDto> couponsDtoList) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (ReqBrandCouponsDto p:couponsDtoList){

			if (!StringUtils.isEmpty(p.getReceiveNumRule())&&!p.getReceiveNumRule().matches("^[0-9]*$")){
				return Msg.error("抱歉，"+p.getName()+"请正确输入用户可领数量",new ResCouponsReturnDto());
			}

			if (p.getCouponsType()==Integer.parseInt(CouponsTypeEnum.getCode("满减"))) {
//
//				if (StringUtils.isEmpty(p.getMinSpendMoney() )) {
//					return	Msg.error("最低消费金额有误",new ResCouponsReturnDto());
//				}
				if (StringUtils.isEmpty(p.getAmt())) {
					return	Msg.error("满减金额有误",new ResCouponsReturnDto());
				}
//				if (p.getMinSpendMoney().compareTo(p.getAmt()) <= 0) {
//					return	Msg.error("最低消费金额不能小于满减金额",new ResCouponsReturnDto());
//				}
			}

			if (p.getCouponsType()==Integer.parseInt(CouponsTypeEnum.getCode("立减"))) {

				if (StringUtils.isEmpty(p.getAmt())) {
					return	Msg.error("立减金额有误",new ResCouponsReturnDto());
				}
			}

			if (p.getCouponsType()==Integer.parseInt(CouponsTypeEnum.getCode("折扣"))) {

				if (StringUtils.isEmpty(p.getAmt())) {
					return	Msg.error("请输入折扣",new ResCouponsReturnDto());
				}
				DecimalFormat dfDouble = new DecimalFormat("#.##");

				if (Integer.parseInt(dfDouble.format(p.getAmt())) > 100) {
					return Msg.error("折扣必须在100以内",new ResCouponsReturnDto());
				}
			}

			try {
				Date begin=format.parse(p.getValiDayStart());
				Date end=format.parse(p.getValiDayEnd());
				if (begin.compareTo(end)>=0){
					return	Msg.error("优惠券有效截至开始时间不能大于优惠券有效截至结束时间",new ResCouponsReturnDto());
				}

			} catch (ParseException e) {
				return Msg.error("优惠券有效日期格式错误",new ResCouponsReturnDto());
			}
		}
		return null;
	}

	private Activity getActivity(ReqBrandActivityAddDto dto, String fromSys, String fromSysCode, LocalDateTime sendTimeStart, LocalDateTime sendTimeEnd) {
		Activity activity = new Activity();
		LocalDateTime dtNow = LocalDateTime.now();

		activity.setCreateTime(dtNow);
		activity.setCreateAdmin(dto.getBrandShopSeq());
		activity.setUpdateTime(dtNow);
		activity.setUpdateAdmin(dto.getBrandShopSeq());
		activity.setIsDisabled(dto.isDisabled());
		activity.setName(dto.getName());
		activity.setBatch(dto.getBatch());
		activity.setTitle(dto.getTitle());
		activity.setFromSys(fromSys);
		activity.setFromSysCode(fromSysCode);
		//activity.setActivityGoodsRuleId(activityGoodsRuleId);
		activity.setSendTimeStart(sendTimeStart);
		activity.setSendTimeEnd(sendTimeEnd);
		//activity.setUserRoleRule(userRoleRule);
		//activity.setReceiveNumRule(receiveNumRule);
		activity.setActivityDesc(dto.getActivityDesc());
		activity.setUseType(1);//活动类型(1:优惠券活动,2:商品活动)

		return activity;
	}

	private void  prosessData(ReqBrandActivityAddDto dto, List<ReqBrandCouponsDto> couponsDtoList, List<Coupons> couponsList,Activity activity) {

		LocalDateTime localDateTime=LocalDateTime.now();
		DecimalFormat dfDouble = new DecimalFormat("#.##");
		DecimalFormat dfInt = new DecimalFormat("#");
		couponsDtoList.forEach(

				p->{
					if (p.getCouponsType()==Integer.parseInt(CouponsTypeEnum.getCode("满减"))){

						String typeDesc= "满[" +dfDouble.format(p.getMinSpendMoney())+ "]减["+ dfDouble.format(p.getAmt())+"]";

						CouponsTypeRule couponsTypeRule=new CouponsTypeRule();
						couponsTypeRule.setType(1);
						couponsTypeRule.setIsDisabled(false);
						couponsTypeRule.setAmtFullReduce(p.getAmt());
						couponsTypeRule.setMinSpendMoney(p.getMinSpendMoney());
						couponsTypeRule.setCreateTime(localDateTime);
						couponsTypeRule.setName(dto.getBrandShopSeq()+"-"+p.getName());
						couponsTypeRule.setFromSys(dto.getFromSys());
						couponsTypeRule.setFromSysCode(dto.getFromSysCode());
                        couponsTypeRule.setTypeDesc(typeDesc);
                        couponsTypeRule.setUpdateTime(localDateTime);
                        couponsTypeRule.setUpdateAdmin(dto.getBrandShopSeq());
                        couponsTypeRule.setCreateAdmin(dto.getBrandShopSeq());

						couponsTypeRuleMapper.insert(couponsTypeRule);

						Coupons coupons = getCoupons(dto, activity, localDateTime, p, couponsTypeRule);
						couponsList.add(coupons);

					}

					if (p.getCouponsType()==Integer.parseInt(CouponsTypeEnum.getCode("立减"))){

						String typeDesc = "无门槛立减[" + dfDouble.format(p.getAmt()) + "]元";

						CouponsTypeRule couponsTypeRule=new CouponsTypeRule();
						couponsTypeRule.setType(2);
						couponsTypeRule.setMinSpendMoney(new BigDecimal(0));
						couponsTypeRule.setIsDisabled(false);
						couponsTypeRule.setAmtSub(p.getAmt());
						couponsTypeRule.setCreateTime(localDateTime);
						couponsTypeRule.setName(dto.getBrandShopSeq()+"-"+p.getName());
						couponsTypeRule.setFromSys(dto.getFromSys());
						couponsTypeRule.setFromSysCode(dto.getFromSysCode());
						BigDecimal tmp=BigDecimal.ZERO;
						couponsTypeRule.setMinSpendMoney(tmp);
						couponsTypeRule.setTypeDesc(typeDesc);
						couponsTypeRule.setUpdateTime(localDateTime);
						couponsTypeRule.setUpdateAdmin(dto.getBrandShopSeq());
						couponsTypeRule.setCreateAdmin(dto.getBrandShopSeq());

						couponsTypeRuleMapper.insert(couponsTypeRule);

						Coupons coupons = getCoupons(dto, activity, localDateTime, p, couponsTypeRule);
						couponsList.add(coupons);

					}

					if (p.getCouponsType()==Integer.parseInt(CouponsTypeEnum.getCode("折扣"))){

						String typeDesc = dfInt.format(p.getAmt()) + "%折扣率";
						typeDesc = "["+dfDouble.format(p.getAmt().divide(new BigDecimal("10"))) + "]折折扣券";
						if(p.getMinSpendMoney().compareTo(BigDecimal.ZERO ) > 0) {//有最低消费限制
							typeDesc = "满[" + dfDouble.format(p.getMinSpendMoney()) + "]打"+ dfDouble.format(p.getAmt().divide(new BigDecimal("10"))) + "折券";
						}

						CouponsTypeRule couponsTypeRule=new CouponsTypeRule();
						couponsTypeRule.setType(3);
						couponsTypeRule.setIsDisabled(false);
                        if (StringUtils.isEmpty(p.getMinSpendMoney())){
                            couponsTypeRule.setMinSpendMoney(new BigDecimal(0));
                        }else {
                            couponsTypeRule.setMinSpendMoney(p.getMinSpendMoney());
                        }
						couponsTypeRule.setAmtDiscount(p.getAmt());
						couponsTypeRule.setCreateTime(localDateTime);
						couponsTypeRule.setName(dto.getBrandShopSeq()+"-"+p.getName());
						couponsTypeRule.setFromSys(dto.getFromSys());
						couponsTypeRule.setFromSysCode(dto.getFromSysCode());
						couponsTypeRule.setTypeDesc(typeDesc);
						couponsTypeRule.setUpdateTime(localDateTime);
						couponsTypeRule.setUpdateAdmin(dto.getBrandShopSeq());
						couponsTypeRule.setCreateAdmin(dto.getBrandShopSeq());

						couponsTypeRuleMapper.insert(couponsTypeRule);

						Coupons coupons = getCoupons(dto, activity, localDateTime, p, couponsTypeRule);

						couponsList.add(coupons);

					}
				}
		);
	}

	private Coupons getCoupons(ReqBrandActivityAddDto dto, Activity activity, LocalDateTime localDateTime, ReqBrandCouponsDto p, CouponsTypeRule couponsTypeRule) {
		result = new HashMap<>();
		// 设置对应的有效期详情
		result.put("vali_day_start",p.getValiDayStart());
		result.put("vali_day_end", p.getValiDayEnd());

		Coupons coupons=new Coupons();
		coupons.setCouponsType(p.getCouponsType());
		coupons.setSendTimeEnd(DateUtils.parse(dto.getSendTimeEnd()));
		coupons.setSendTimeStart(DateUtils.parse(dto.getSendTimeStart()));
		coupons.setUpdateTime(localDateTime);
		coupons.setActivityId(activity.getId()+"");
		result.put("vali_day_start", p.getValiDayStart());
		result.put("vali_day_end", p.getValiDayEnd());
		coupons.setValiDayTypeDetail(JsonUtils.toDefaultJson(result));
		coupons.setValiDayType(0);
		coupons.setSendNum(p.getSendNum());
		if (StringUtils.isEmpty(p.getReceiveNumRule())){
			coupons.setReceiveNumRule("1");
		}else {
			coupons.setReceiveNumRule(p.getReceiveNumRule());
		}
		coupons.setName(p.getName());
		coupons.setCouponDes(p.getCouponDes());
		coupons.setCreateTime(localDateTime);
		coupons.setUpdateTime(localDateTime);
//						coupons.setCreateAdmin(p.g);
		coupons.setValiDayTypeDetail(JsonUtils.toDefaultJson(result));
		coupons.setFromSys(dto.getFromSys());
		coupons.setFromSysCode(dto.getFromSysCode());
		coupons.setCouponsRuleId(couponsTypeRule.getId()+"");
		coupons.setBatch(activity.getBatch());
		coupons.setReceiveNum(0);
		coupons.setSendType(1);
		coupons.setIsDisabled(true);
		coupons.setCouponDes(p.getCouponDes());
		coupons.setCreateAdmin(dto.getBrandShopSeq());
		coupons.setUpdateAdmin(dto.getBrandShopSeq());
		coupons.setFinanStatus(1);
		coupons.setFinanAuditor(dto.getBrandShopSeq());
		coupons.setFinanAuditTime(localDateTime);
		coupons.setContentImg(p.getContentImg());
		coupons.setContentDisabledImg(p.getContentDisabledImg());
		coupons.setCouponsHrefUrl(p.getCouponsHrefUrl());

		return coupons;
	}

	@Transactional
	@Override
	public Page<Activity> queryActivity(Activity activity) {
		return activityMapper.queryActivity(activity);
	}
	/**查询所有新人优惠券活动**/
	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_ACTIVITY_QUERY.concat('ApiData-') +'--queryNewPeopleActivityListByApi--' +#entity ", unless = "#result eq null")
	@Override
	public Page<ResActivityListDataDto> queryNewPeopleActivityListByApi(ReqActivityListDataDto apiDto,
			PageDto pageDto) {
		apiDto.setNowTime(DateUtils.to(LocalDateTime.now()));
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<ResActivityListDataDto> listData = activityMapper.queryNewPeopleActivityListByApi(apiDto);
		return listData;
	}

}