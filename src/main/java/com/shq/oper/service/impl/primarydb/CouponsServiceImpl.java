package com.shq.oper.service.impl.primarydb;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import com.shq.oper.enums.CouponsTypeEnum;
import com.shq.oper.enums.CouponsValiDayTypeEnum;
import com.shq.oper.mapper.primarydb.*;
import com.shq.oper.model.domain.primarydb.*;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.api.req.*;
import com.shq.oper.model.dto.api.res.*;
import com.shq.oper.util.Constant;
import com.shq.oper.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.shq.oper.lock.RedisLock;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.ValidityDetailDto;
import com.shq.oper.service.primarydb.CouponsService;
import com.shq.oper.service.primarydb.RedisService;
import com.shq.oper.util.DateUtils;
import org.springframework.util.StringUtils;

@Service("couponsService")
@Transactional
public class CouponsServiceImpl extends BaseServiceImpl<Coupons, Long> implements CouponsService {

	@Autowired
	private CouponsMapper couponsMapper;

	@Autowired
	private DistributionProductMapper distributionProductMapper;
	@Autowired
	private RedisService redisService;

	@Autowired
	private ActivityGoodsRuleDetailsMapper activityGoodsRuleDetailsMapper;

	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private ActivityGoodsRuleMapper activityGoodsRuleMapper;

	@Autowired
	private CouponsTypeRuleMapper couponsTypeRuleMapper;

	private Map<String, Object> result;

	@Transactional
	@Override
	public Msg<String> batchUpdateFinanStatusToOk(String couponsIds, Coupons coupons) {
		List<Coupons> couponsList = couponsMapper.selectByIds(couponsIds);
		for (Coupons tmpCoupons : couponsList) {
			tmpCoupons.setFinanStatus(1);
			tmpCoupons.setFinanAuditor(coupons.getFinanAuditor());
			tmpCoupons.setFinanAuditTime(coupons.getFinanAuditTime());
			tmpCoupons.setFinanAuditRemark(coupons.getFinanAuditRemark().trim());
		}
		try {
			couponsMapper.batchUpdateFinanStatusToOk(couponsList);
			return Msg.ok("审核成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Msg.error("审核失败");
		}
	}
	@Override
	public Page<Coupons> queryByActivity(Coupons tmpCoupons) {
		return couponsMapper.queryByActivity(tmpCoupons);
	}
	
	/**查询活动所有优惠券**/
//	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_COUPONS_QUERY.concat('ApiData-') + #apiDto", unless = "#result eq null")
	@Override
	public Page<ResActivityCouponsListDataDto> queryCouponsListByApi(ReqActivityCouponsListDataDto apiDto, PageDto pageDto) {
		LocalDateTime dtNow = LocalDateTime.now();
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		apiDto.setNowTime(DateUtils.to(LocalDateTime.now()));
		Page<ResActivityCouponsListDataDto> listData = couponsMapper.queryCouponsListByApi(apiDto);
		if(listData.getResult().size()>0) {
			for (ResActivityCouponsListDataDto temp : listData.getResult()) {
				if(!StringUtils.isEmpty(temp.getValiDayTypeDetail())) {
					// 判断优惠券的有效期
					String valiDayTypeDetail = temp.getValiDayTypeDetail();
					ValidityDetailDto valiDayTypeDetailBean = JSON.parseObject(valiDayTypeDetail, ValidityDetailDto.class);
					if(!StringUtils.isEmpty(valiDayTypeDetailBean.getVali_day_num())) {
						temp.setValiDayNum(valiDayTypeDetailBean.getVali_day_num());
					}
				}
			}
		}
		return listData;
	}

	/**爱之家 品牌广场  所有优惠券**/
//	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_COUPONS_QUERY.concat('ApiData-') + #apiDto", unless = "#result eq null")
	@Override
	public Page<ResActivityCouponsListDataDto> queryCouponsListByApiBrand(ReqBrandCouponsListDataDto apiDto, PageDto pageDto) {
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		apiDto.setNowTime(DateUtils.to(LocalDateTime.now()));
		Page<ResActivityCouponsListDataDto> listData = couponsMapper.queryCouponsListByApiBrand(apiDto);
		return listData;
	}
	
	@Override
	public Page<ResBrandCouponsListDto> queryCouponsBrandByActive(ReqBrandActivityListDataDto apiDto, PageDto pageDto) {
		Activity activityParam=new Activity();
		if (!StringUtils.isEmpty(apiDto.getId())){
			activityParam.setId(apiDto.getId());
		}
		if (!StringUtils.isEmpty(apiDto.getBatch())){
			activityParam.setBatch(apiDto.getBatch());
		}

		activityParam.setFromSys(apiDto.getFromSys());
		activityParam.setFromSysCode(apiDto.getFromSysCode());
		Activity activity=activityMapper.selectOne(activityParam);

		if (activity==null){
			return new Page<ResBrandCouponsListDto>();
		}

		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		apiDto.setNowTime(DateUtils.to(LocalDateTime.now()));
		Page<ResBrandCouponsListDto> listData = couponsMapper.queryCouponsBrandByActive(activity);
		listData.forEach(
				p->{//{"vali_day_end":"2018-07-31 23:59:59","vali_day_start":"2018-07-18 17:00:48"}    {"vali_day_num":30}
//					if (p.getValiDayType()==0){
						if (!StringUtils.isEmpty(p.getValiDayTypeDetail())){
							String[] strs=p.getValiDayTypeDetail().split(",");
							String start=strs[0].substring(strs[0].indexOf(":")+2,strs[0].length()-1);
							String end=strs[1].substring(strs[1].indexOf(":")+2,strs[1].length()-2);
							p.setValiDayStart(start);
							p.setValiDayEnd(end);
						}
//					}
//					if (p.getValiDayType()==1){
//						if (!StringUtils.isEmpty(p.getValiDayTypeDetail())){
//							String str=p.getValiDayTypeDetail();
//							String num=str.substring(str.indexOf(":")+1,str.length()-1);
//							p.setValiDayNum(num);
//						}
//
//					}
				}
		);
		return listData;
	}

	@Transactional
	@Override
	public Page<ResBrandCouponsGoodsListDto> queryGoodsBrandByApi(ReqGoodsListDataDto apiDto, PageDto pageDto) {
		Coupons couponsParam =new Coupons();
		couponsParam.setId(apiDto.getId());
		Coupons coupons=couponsMapper.selectOne(couponsParam);
		if (coupons==null){
			return new Page<ResBrandCouponsGoodsListDto>();
		}
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<ResBrandCouponsGoodsListDto> pageResult=activityGoodsRuleDetailsMapper.queryListByCouponsId(coupons.getCategoryRuleId().longValue());
		return pageResult;
	}

	@Transactional
	@Override
	public Msg<String> BrandAddCategoryRule(ReqBrandCouponsCategoryAddDto aipDto) {
		DistributionProduct productParam=new DistributionProduct();
//		productParam.setProductCode(aipDto.getCode());
		List<DistributionProduct> distributionProductList=distributionProductMapper.queryDiatributionProductByCodeList(aipDto.getCodes());
		if (null==distributionProductList||distributionProductList.size()<=0){
			return Msg.error("查不到商品");
		}
		//		DistributionProduct product= distributionProductMapper.selectOne(productParam);
//		if (product==null){
//			return Msg.error("查不到该商品");
//		}

		Coupons couponsParam=new Coupons();
		couponsParam.setId(Long.parseLong(aipDto.getId()));
		Coupons coupons=couponsMapper.selectOne(couponsParam);

		LocalDateTime localDateTime=LocalDateTime.now();

		if (StringUtils.isEmpty(coupons.getCategoryRuleId())){
			ActivityGoodsRule activityGoodsRule=new ActivityGoodsRule();
			activityGoodsRule.setName("["+aipDto.getBrandShopSeq()+"]"+coupons.getName());
			activityGoodsRule.setUpdateAdmin(aipDto.getBrandShopSeq());
			activityGoodsRule.setUpdateTime(localDateTime);
			activityGoodsRule.setCreateAdmin(aipDto.getBrandShopSeq());
			activityGoodsRule.setCreateTime(localDateTime);
			activityGoodsRule.setType(0);
			activityGoodsRule.setFromSys(aipDto.getFromSys());
			activityGoodsRule.setFromSysCode(aipDto.getFromSysCode());
			activityGoodsRule.setIsDisabled(false);

			activityGoodsRuleMapper.insert(activityGoodsRule);

			List<ActivityGoodsRuleDetails> activityGoodsRuleDetailsList=new ArrayList<>();
			distributionProductList.forEach(
					p->{
						ActivityGoodsRuleDetails activityGoodsRuleDetails=new ActivityGoodsRuleDetails();
						activityGoodsRuleDetails.setUpdateAdmin(aipDto.getBrandShopSeq());
						activityGoodsRuleDetails.setRuleType(0);
						activityGoodsRuleDetails.setRefSignValue(p.getProductCode());
						activityGoodsRuleDetails.setRefSignName(p.getProductName());
						activityGoodsRuleDetails.setRefSignLocalId(p.getProductCode());
						activityGoodsRuleDetails.setActivityGoodsRuleId(activityGoodsRule.getId());
						activityGoodsRuleDetails.setUpdateTime(localDateTime);
						activityGoodsRuleDetailsList.add(activityGoodsRuleDetails);
					}
			);
			activityGoodsRuleDetailsMapper.insertList(activityGoodsRuleDetailsList);
//			activityGoodsRuleDetailsMapper.insert(activityGoodsRuleDetails);

			coupons.setCategoryRuleId(new BigDecimal(activityGoodsRule.getId()));
			coupons.setUpdateTime(localDateTime);
//			coupons.setIsDisabled(false);
			couponsMapper.updateCouponsCategoryRule(coupons);

		}else {

			List<ActivityGoodsRuleDetails> activityGoodsRuleDetailsList=new ArrayList<>();
			distributionProductList.forEach(
					p->{
						ActivityGoodsRuleDetails activityGoodsRuleDetails=new ActivityGoodsRuleDetails();
						activityGoodsRuleDetails.setUpdateAdmin(aipDto.getBrandShopSeq());
						activityGoodsRuleDetails.setRuleType(0);
						activityGoodsRuleDetails.setRefSignValue(p.getProductCode());
						activityGoodsRuleDetails.setRefSignName(p.getProductName());
						activityGoodsRuleDetails.setRefSignLocalId(p.getProductCode());
						activityGoodsRuleDetails.setActivityGoodsRuleId(coupons.getCategoryRuleId().longValue());
						activityGoodsRuleDetails.setUpdateTime(localDateTime);
						activityGoodsRuleDetailsList.add(activityGoodsRuleDetails);
					}
			);
			activityGoodsRuleDetailsMapper.insertList(activityGoodsRuleDetailsList);
		}

		return Msg.ok("操作成功");
	}

	@Transactional
	@Override
	public Msg<String> BrandUpdateCoupons(Coupons coupons) {
		coupons.setUpdateTime(LocalDateTime.now());
		couponsMapper.updateBrandCoupons(coupons);
		return Msg.ok("操作成功");
	}

	@Override
	public Msg<String> BrandBatchEnbleCoupons(ReqBrandCouponsBatchEnbleDto apiDto) {
		String [] ids=apiDto.getIds().split(",");
		List<String> idList=Arrays.asList(ids);
		List<Long> longList=new ArrayList<>();
		idList.forEach(
				p->{
					longList.add(Long.parseLong(p));
				}
		);
		List<Coupons> couponsList=this.selectByIds(longList);
		if (couponsList==null||couponsList.size()<=0){
			return Msg.error("查询优惠券失败");
		}
		LocalDateTime localDateTime=LocalDateTime.now();

		for (Coupons coupons:couponsList){
			if (StringUtils.isEmpty(coupons.getCategoryRuleId())){
				Msg<String> msg= Msg.error("名称为【"+coupons.getName()+"】的优惠未绑定商品,请先绑定商品");
				msg.setCode(300);
				return msg;
			}
			coupons.setUpdateTime(localDateTime);
			if (apiDto.isDisabled()){
				coupons.setIsDisabled(true);
			}else {
				coupons.setIsDisabled(false);
			}
			coupons.setUpdateAdmin(apiDto.getBrandShopSeq());
		}

		couponsMapper.batchEnbleCouponsList(couponsList);
		return Msg.ok("操作成功");
	}

	@Transactional
	@Override
	public Msg<ResCouponsReturnDto> BrandAddCouponsByActivityId(ReqBrandCouponsAddByActid2011Dto apiDto) {
		Activity activityParam=new Activity();
		activityParam.setId(Long.parseLong(apiDto.getActivityId()));
		Activity activity=activityMapper.selectOne(activityParam);
		if (activity==null){
			return Msg.error("改活动不存在",new ResCouponsReturnDto());
		}

		if (apiDto.getBrandCouponsList()==null||apiDto.getBrandCouponsList().size()<=0){
			return Msg.error("请输入优惠券列表",new ResCouponsReturnDto());
		}

		List<ReqBrandCouponsDto> brandCouponsList=apiDto.getBrandCouponsList();
		//验证数据
		Msg<ResCouponsReturnDto> p = valiDate(brandCouponsList);
		if (p != null) return p;
		List<Coupons> couponsList=new ArrayList<>();

		prosessData(apiDto,brandCouponsList,couponsList,activity);

		couponsMapper.insertList(couponsList);

		ResCouponsReturnDto resCouponsReturnDto=new ResCouponsReturnDto();
		resCouponsReturnDto.setActivityId(activity.getId());
		List<ResCouponsReturnDto.CouponsEntity> couponsEntityList =new ArrayList<>();

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


	private void  prosessData(ReqBrandCouponsAddByActid2011Dto dto, List<ReqBrandCouponsDto> couponsDtoList, List<Coupons> couponsList,Activity activity) {

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

	private Coupons getCoupons(ReqBrandCouponsAddByActid2011Dto dto, Activity activity, LocalDateTime localDateTime, ReqBrandCouponsDto p, CouponsTypeRule couponsTypeRule) {
		result = new HashMap<>();
		// 设置对应的有效期详情
		result.put("vali_day_start",p.getValiDayStart());
		result.put("vali_day_end", p.getValiDayEnd());

		Coupons coupons=new Coupons();
		coupons.setCouponsType(p.getCouponsType());
		coupons.setSendTimeEnd(activity.getSendTimeEnd());
		coupons.setSendTimeStart(activity.getSendTimeStart());
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

	private Msg<ResCouponsReturnDto> valiDate(List<ReqBrandCouponsDto> couponsDtoList) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (ReqBrandCouponsDto p:couponsDtoList){

			if (!StringUtils.isEmpty(p.getReceiveNumRule())&&!p.getReceiveNumRule().matches("^[0-9]*$")){
				return Msg.error("抱歉，"+p.getName()+"请正确输入用户可领数量",new ResCouponsReturnDto());
			}

			if (p.getCouponsType()==Integer.parseInt(CouponsTypeEnum.getCode("满减"))) {

				if (StringUtils.isEmpty(p.getMinSpendMoney() )) {
					return Msg.error("最低消费金额有误",new ResCouponsReturnDto());
				}
				if (StringUtils.isEmpty(p.getAmt())) {
					return Msg.error("满减金额有误",new ResCouponsReturnDto());
				}
//				if (p.getMinSpendMoney().compareTo(p.getAmt()) <= 0) {
//					return Msg.error("最低消费金额不能小于满减金额",new ResCouponsReturnDto());
//				}
			}

			if (p.getCouponsType()==Integer.parseInt(CouponsTypeEnum.getCode("立减"))) {

				if (StringUtils.isEmpty(p.getAmt())) {
					return Msg.error("立减金额有误",new ResCouponsReturnDto());
				}
			}

			if (p.getCouponsType()==Integer.parseInt(CouponsTypeEnum.getCode("折扣"))) {

				if (StringUtils.isEmpty(p.getAmt())) {
					return Msg.error("请输入折扣",new ResCouponsReturnDto());
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



	/**
	 * 品牌广场批量删除优惠券品类规则
	 *@author ltg
	 *@date 2018/7/30 14:47
	 * @params:[apiDto]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@Transactional
	@Override
	public Msg<String> BrandBatchRemoveCouponsCateRule(ReqRemoveGoodsListDataDto2010 apiDto) {

		Coupons couponsParam=new Coupons();
		couponsParam.setId(apiDto.getCouponsId());
		Coupons coupons=couponsMapper.selectOne(couponsParam);
		if (coupons==null){
			return Msg.error("查不到该优惠券");
		}
		if (coupons.getCategoryRuleId()==null){
			return Msg.error("该优惠券还没有设置品类规则");
		}
		int i=activityGoodsRuleDetailsMapper.BrandRemoveCouponsCateRule(coupons.getCategoryRuleId().intValue(),apiDto.getCodes());

		return Msg.ok("操作成功","成功删除"+i+"个商品");
	}


	/**
	 * 获取大兵优惠券
	 * @param apiDto
	 * @param pageDto
	 * @return
	 */
//	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_COUPONS_QUERY.concat('ApiData-') + #apiDto"+"dabing", unless = "#result eq null")
	@Override
	public Page<ResActivityCouponsListDataDto> queryDabingCouponsListByApi(ReqActivityCouponsListDataDto apiDto, PageDto pageDto) {
		com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		apiDto.setNowTime(DateUtils.to(LocalDateTime.now()));
		Page<ResActivityCouponsListDataDto> listData = couponsMapper.queryCouponsListByApiDabing(apiDto);
		return listData;
	}


//	//更新优惠券发放数量
//	@Transactional
//	public Msg<String> updateCouponsReceiveNum(Long id, int receive,String userSeq) {
//		int updateUum = couponsMapper.updateCouponsReceiveNum(id,receive,userSeq);
//		return Msg.ok("更新优惠券发放数量成功", String.valueOf(updateUum));
//	}

	@Override
	public int disabledCouponsById(Coupons record) {
		int num =  disabledById(record);
		Assert.isTrue(num == 1, "禁用优惠券异常");
		return num;
	}

}