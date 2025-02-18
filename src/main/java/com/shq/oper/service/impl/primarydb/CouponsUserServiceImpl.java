package com.shq.oper.service.impl.primarydb;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.shq.oper.mapper.primarydb.DeductibleMapper;
import com.shq.oper.model.domain.primarydb.deductible.Deductible;
import com.shq.oper.model.dto.api.req.*;
import com.shq.oper.model.dto.api.res.ResActivityCouponsListDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.shq.oper.enums.ActivityGoodsRuleTypeEnum;
import com.shq.oper.enums.CouponsStatusEnum;
import com.shq.oper.enums.CouponsTypeEnum;
import com.shq.oper.enums.CouponsValiDayTypeEnum;
import com.shq.oper.lock.RedisLock;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.CouponsMapper;
import com.shq.oper.mapper.primarydb.CouponsUserMapper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.Activity;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRuleDetails;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.domain.primarydb.CouponsUser;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.ValidityDetailDto;
import com.shq.oper.model.dto.api.res.ResGoodsCouponsListDataDto;
import com.shq.oper.model.dto.api.res.ResUserCouponsPackDataDto;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.primarydb.ActivityGoodsRuleDetailsService;
import com.shq.oper.service.primarydb.ActivityService;
import com.shq.oper.service.primarydb.CouponsService;
import com.shq.oper.service.primarydb.CouponsUserService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.Constant;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("couponsUserService")
public class CouponsUserServiceImpl extends BaseServiceImpl<CouponsUser, Long> implements CouponsUserService {
	@Autowired
	private CouponsService couponsService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private CouponsMapper couponsMapper;
	
	@Autowired
	private CouponsUserMapper couponsUserMapper;

	@Autowired
	private ActivityGoodsRuleDetailsService activityGoodsRuleDetailsService;
	
	
	//----------sqlserver---
	@Autowired
	private ShopperMapper shopperMapper;

	@Autowired
	private DistributionProductMapper distributionProductMapper;

	@Autowired
	private DeductibleMapper deductibleMapper;
	
	@RedisLock(lockPrefix="CouponsUserServiceImpl-receiveCoupons-")
	@Transactional
	@Override
	public Msg<String> receiveCoupons(ReqUserReciveCouponsApiDataDto dto) {
		//单次两次优惠券数量
		int receiveAddNum = 1;
		
		if (StringUtils.isEmpty(dto.getUserSeq()) ) {
			return Msg.error("用户标识不能为空");
		}
		if (!dto.getUserSeq().matches("^[0-9]*$") ) {
			return Msg.error("用户标识有误。");
		}
		if (StringUtils.isEmpty(dto.getUserPhone())) {
			return Msg.error("用户手机号不能为空");
		}
		if(StringUtils.isEmpty(dto.getActBatch()) && StringUtils.isEmpty(dto.getCouponsId()) ) {
			return Msg.error("活动标识和优惠券标识不能同时为空");
		}
		if(StringUtils.isEmpty(dto.getCouponsId()) ) {
			return Msg.error("请选择要领取的优惠券");
		}

		//-------sqlserver 验证 用户信息验证
		Shopper shopTmp = new Shopper();
		shopTmp.setSeq(Integer.parseInt(dto.getUserSeq()));
		shopTmp = shopperMapper.queryBySeqOrMobile(shopTmp);
		if(shopTmp == null) {
			return Msg.error("没有查到该领取用户。");
		}
		if(!dto.getUserPhone().equals(shopTmp.getMobile())) {
			return Msg.error("请确认用户信息提交是否正确。");
		}
		//-------end sqlserver 验证 用户信息验证-----------
		
		List<Coupons> couponsList = null;	//优惠券集合
		
		CouponsUser userReviceCoupons = new CouponsUser();
		userReviceCoupons.setUserSeq(dto.getUserSeq());
		
		LocalDateTime dtNow = LocalDateTime.now();
		if (!StringUtils.isEmpty(dto.getActBatch())) {//按活动批次获取优惠券
			Activity activityBatch = activityService.queryByBatch(dto.getActBatch());
			if(activityBatch == null) {
				return Msg.error("无此活动批次号");
			}
			//活动资格验证
			if(dtNow.isBefore(activityBatch.getSendTimeStart())) {
				return Msg.error("活动时间是["+DateUtils.to(activityBatch.getSendTimeStart())+"]");
			}
			if(dtNow.isAfter(activityBatch.getSendTimeEnd())) {
				return Msg.error("活动已经结束");
			}
			
//			//根据活动批次号，查询优惠券列表
//			Coupons tmpCoupons = new Coupons();
//			tmpCoupons.setActBatchNo(dto.getActBatch());
//			couponsList = couponsService.queryByActivity(tmpCoupons);	//根据活动批次号，查询活动所有优惠券
//			
//			if (CollectionUtils.isEmpty(couponsList)) {
//				return Msg.error("活动暂无可领取优惠券");
//			}
			
			userReviceCoupons.setActivityId(activityBatch.getId().toString());
		}
		
		if(!StringUtils.isEmpty(dto.getCouponsId())) {//指定优惠券..
			if(CollectionUtils.isEmpty(couponsList)) {
				//根据活动批次号，查询优惠券列表
				Coupons tmpCoupons = new Coupons();
				tmpCoupons.setId(dto.getCouponsId());
				if (!StringUtils.isEmpty(dto.getActBatch())) {
					tmpCoupons.setActBatchNo(dto.getActBatch());
				}
				couponsList = couponsService.queryByActivity(tmpCoupons);	//根据优惠券Ids 查询优惠券
			}else {
				//筛选 指定优惠券对象
				couponsList=couponsList.stream().filter(c -> dto.getCouponsId().equals(c.getId())).collect(Collectors.toList());
			}
			// 根据批次号查询对应优惠券
			if (CollectionUtils.isEmpty(couponsList)) {
				return Msg.error("暂无可领取优惠券");
			}
			
			userReviceCoupons.setCouponsId(couponsList.get(0).getId().toString());
		}
		
		//用户领取的优惠券
		List<CouponsUser> userReviceCouponsList =  couponsUserMapper.queryByReciveCoupons(userReviceCoupons);
		
		//用户对每个优惠券Id 领取次数<key:couponsId,value:领取次数>
		Map<String,Integer> mapUserReciveNums = new HashMap<String,Integer>();
		userReviceCouponsList.forEach(u -> {
			mapUserReciveNums.merge(u.getCouponsId(), 1, (value, newValue) -> value+1);
		});
		
		
		//验证优惠券领取规则
		Msg<String> validReviceErrorMsg = null;
		for (Coupons tmpC : couponsList) {
			//活动资格验证
			if(dtNow.isBefore(tmpC.getSendTimeStart())) {
				validReviceErrorMsg = Msg.error("优惠券发放时间是["+DateUtils.to(tmpC.getSendTimeStart())+"]");
				break;
			}
			if(dtNow.isAfter(tmpC.getSendTimeEnd())) {
				validReviceErrorMsg = Msg.error("优惠券发放已经结束");
				break;
			}
			
			//总数量限制  0默认不限制
			if(tmpC.getSendNum() != 0 && tmpC.getReceiveNum() + receiveAddNum > tmpC.getSendNum()) {
				validReviceErrorMsg =Msg.error("您来晚了,优惠券["+tmpC.getName()+"]已被全部领光.");
				break;
			}
			//用户领取数量限制  0默认不限制
			int userReviceNums = getUserReviceNums(tmpC,dto.getUserSeq());
			if(userReviceNums != 0 && mapUserReciveNums.getOrDefault(tmpC.getId().toString(),0) >= userReviceNums) {
				validReviceErrorMsg =Msg.error("只能领取["+userReviceNums+"]张优惠券["+tmpC.getName()+"]");
				break;
			}
		}
		if(validReviceErrorMsg != null) {
			return validReviceErrorMsg;
		}


		//清除CouponsUser-userSeq- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+dto.getUserSeq());
		
		//发放优惠券
		dto.setUserPhone(shopTmp.getMobile());
		List<CouponsUser> couponsUserList = initCouponsUserData(dto, couponsList);
		int result = couponsUserMapper.insertList(couponsUserList);
		Assert.isTrue(result == couponsList.size(), "领取优惠券出错出错");
		
		//发放总数量限制
		for (Coupons tmpC : couponsList) {
			int updateUum = couponsMapper.updateCouponsReceiveNum(tmpC.getId(),receiveAddNum,dto.getUserSeq());
			Assert.isTrue(String.valueOf(updateUum).equals("1"), "优惠券["+tmpC.getName()+"]已发放完.");
		}
				
		return Msg.ok("领取优惠券成功", "");
	}



	/**
	 *  每个用户可以领取优惠券次数
	 * @param coupons 
	 * @param userSeq 
	 */
	private int getUserReviceNums(Coupons coupons, String userSeq) {
		int num = -1 ;
		String receiveNumRule = coupons.getReceiveNumRule();
		if(!StringUtils.isEmpty(receiveNumRule)) {	//没有设置用户领取次数，默认不限制
			try {
				if(coupons.getReceiveNumRule().matches("^[0-9]*$")) {
					num = Integer.valueOf(coupons.getReceiveNumRule());
				}
				/*Map<String,Integer> map = JsonUtils.parse(receiveNumRule, HashMap.class);
				if(map.get(CouponsUserReceiveRuleEnum.RECEIVE_ALL_NUM.getKey()) != null) {
					num = map.get(CouponsUserReceiveRuleEnum.RECEIVE_ALL_NUM.getKey());
				}*/
			} catch (Exception e) {
				log.error("用户领取规则设置有误----["+coupons.getId()+"]"+coupons.getName(),e);
			}
			
			if(num == 0) {	//0 不限制领取数量
				num = Integer.MAX_VALUE;
			}
		}
		return num;
	}



	private List<CouponsUser> initCouponsUserData(ReqUserReciveCouponsApiDataDto dto, List<Coupons> couponsList) {
		int couponsNums = couponsList.size();
		LocalDateTime dtNow = LocalDateTime.now();
		List<CouponsUser> couponsUserList = new ArrayList<>(couponsNums);
		couponsList.stream().forEach( tmpC -> {
			Assert.isTrue(!StringUtils.isEmpty(tmpC.getMinSpendMoney()),"优惠券最低消费金额设置有误("+tmpC.getId()+")"); 
			CouponsUser couponsUserAdd = new CouponsUser();
			couponsUserAdd.setUpdateTime(dtNow);
			couponsUserAdd.setUpdateAdmin(dto.getActBatch());
			couponsUserAdd.setCreateAdmin(dto.getActBatch());;
			couponsUserAdd.setCreateTime(dtNow);
			couponsUserAdd.setIsDisabled(false);
			//couponsUserAdd.setName("");//用户用户名 关联用户数据
			couponsUserAdd.setPhone(dto.getUserPhone());//用户手机号 关联用户数据
			couponsUserAdd.setUserSeq(dto.getUserSeq());
			couponsUserAdd.setActivityId(tmpC.getActivityId());
			couponsUserAdd.setCouponsId(tmpC.getId().toString());// 优惠券id
			couponsUserAdd.setReceiveSrc(tmpC.getBatch());//领取来源
			//优惠券 金额规则
			couponsUserAdd.setCouponsTypeModel(tmpC.getCouponsTypeModel());
			couponsUserAdd.setCouponsType(tmpC.getCouponsType());
			couponsUserAdd.setMinSpendMoney(tmpC.getMinSpendMoney());
			couponsUserAdd.setAmtFullOver(tmpC.getMinSpendMoney());//设置最小金额
			couponsUserAdd.setAmtFullReduce(tmpC.getAmtFullReduce());
			couponsUserAdd.setAmtDiscount(tmpC.getAmtDiscount());
			couponsUserAdd.setAmtSub(tmpC.getAmtSub());
			couponsUserAdd.setCouponsTypeDesc(tmpC.getCouponsTypeDesc());
			
			// 判断优惠券的有效期
			String valiDayTypeDetail = tmpC.getValiDayTypeDetail();
			ValidityDetailDto valiDayTypeDetailBean = JSON.parseObject(valiDayTypeDetail, ValidityDetailDto.class);
			if(CouponsValiDayTypeEnum.USE_AFTER_DAYS.getCode() == tmpC.getValiDayType()) {
				couponsUserAdd.setValiDayStart(DateUtils.addDaysFormatStart(dtNow, 0));
				couponsUserAdd.setValiDayEnd(DateUtils.addDaysFormatEnd(couponsUserAdd.getValiDayStart(), valiDayTypeDetailBean.getVali_day_num()-1));
			}else if(CouponsValiDayTypeEnum.USE_DATE_TIME.getCode() == tmpC.getValiDayType()){
				couponsUserAdd.setValiDayStart(DateUtils.parse(valiDayTypeDetailBean.getVali_day_start()));
				couponsUserAdd.setValiDayEnd(DateUtils.parse(valiDayTypeDetailBean.getVali_day_end()));
			}
			couponsUserAdd.setCouponsStatus(CouponsStatusEnum.UNUSERED.getCode());//未使用状态
			couponsUserList.add(couponsUserAdd);
			
		});
		return couponsUserList;
	}

	
	/**1002用户优惠券卡包数据**/
    @Cacheable(value = Constant.CACHEKEY_SECOND_15, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_COUPONSUSER_USERSEQ +#queryDto.userSeq+'--package--' + #queryDto", unless = "#result eq null")
	@Override
	public Page<ResUserCouponsPackDataDto> queryByQuickPurchase(ReqUserCouponsPackDto queryDto, PageDto pageDto) {
    	log.debug("-----Query_CouponsUserBy DB----queryByQuickPurchase用户卡包");
		// 根据	用户seq 查询初用户优惠券信息。	
		queryDto.setNowTime(DateUtils.now());	//未使用状态 过滤 过期状态数据
    	com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
		Page<ResUserCouponsPackDataDto> pagesDto = couponsUserMapper.queryByQuickPurchase(queryDto);

		if (pagesDto.size()>=pageDto.getLimit()){
			return pagesDto;
		}
		/**
		 * modify by ltg 2018.11.28
		 */
		Deductible deductibleParam=new Deductible();
		Deductible deductible=null;
		deductibleParam.setUserSeq(queryDto.getUserSeq());
		int couponsStatus=queryDto.getCouponsStatus();
//		int status=couponsStatus==1? 1:couponsStatus==3? 3:0;
		deductibleParam.setType(1);
//		deductibleParam.setStatus((short)status);
		if (couponsStatus==1){

			deductible=deductibleMapper.queryDeductible(queryDto.getUserSeq());
		}
		if (couponsStatus==3){
			deductibleParam.setStatus((short)3);
			deductible=deductibleMapper.selectOne(deductibleParam);
		}
		if (couponsStatus==4){
			deductibleParam.setStatus((short)4);
			deductible=deductibleMapper.selectOne(deductibleParam);
		}

		if (null==deductible){
			return pagesDto;
		}

		ResUserCouponsPackDataDto resUserCouponsPackDataDto=new ResUserCouponsPackDataDto();
		//代金券4
		resUserCouponsPackDataDto.setCouponsId(deductible.getId()+"");
		resUserCouponsPackDataDto.setCouponsType("4");
		resUserCouponsPackDataDto.setAmount(deductible.getAmount());
		resUserCouponsPackDataDto.setBalance(deductible.getBalance());
		resUserCouponsPackDataDto.setUsedBalance(deductible.getUsedBalance());
		resUserCouponsPackDataDto.setCouponsStatus(deductible.getStatus()==1? "1":deductible.getStatus()==2?
				"1":deductible.getStatus()==3? "3":"3");
		resUserCouponsPackDataDto.setCouponsName("代金券");
		resUserCouponsPackDataDto.setCouponsTypeDesc("代金券余额:"+deductible.getBalance());
		resUserCouponsPackDataDto.setCouponDes(
				"<p>1、全品类通用（品牌广场商品无法使用）<br/>2、不可用于提现<br/>3、购物后申请退款将不会退还额度<br/>4、不可与其他优惠券同时使用&nbsp;</p><p><br/></p>");
		pagesDto.add(resUserCouponsPackDataDto);
		return pagesDto;
	}

    
    /**订单结算页 可用优惠券列表**/
    @Override
    public Page<ResUserCouponsPackDataDto> queryByQuickPurchaseOrder(ReqUserCouponsOrderPrevDto orderPrevDto, PageDto pageDto) {
    	//筛选符合规则的优惠券
    	Page<ResUserCouponsPackDataDto> usableList = new Page<ResUserCouponsPackDataDto>();

		//商品Code
		String[] codeArr = orderPrevDto.getGoodsCodeList().replaceAll("[\\[\\]]", "").trim().split(",");
		List<String> codeList = Arrays.asList(codeArr);

		//商品Code 对应商品信息
		Page<DistributionProduct> distributionProducts=distributionProductMapper.queryDiatributionProductByCodeList(codeList);
		boolean isBand=false;
		for (DistributionProduct distributionProduct:distributionProducts){
			if (distributionProduct.getIsstandard()==1){
				isBand=true;
			}
		}

		if (!isBand){
			Deductible deductibleParam=new Deductible();
			deductibleParam.setType(1);
			deductibleParam.setUserSeq(orderPrevDto.getUserSeq());
			Deductible deductible=deductibleMapper.selectOne(deductibleParam);
			ResUserCouponsPackDataDto resUserCouponsPackDataDto=new ResUserCouponsPackDataDto();
			if (null!=deductible&&deductible.getStatus()<3){
				String amtSub="";
				int a = deductible.getBalance().compareTo(orderPrevDto.getOrderMoney());
				if (a<=0){
					amtSub=deductible.getBalance().toString();
				}else {
					amtSub=orderPrevDto.getOrderMoney().toString();
				}

				resUserCouponsPackDataDto.setCouponsId(deductible.getId()+"");
				resUserCouponsPackDataDto.setCouponsStatus(1+"");
				resUserCouponsPackDataDto.setUsedBalance(deductible.getUsedBalance());
				resUserCouponsPackDataDto.setCouponsName("代金券");
				resUserCouponsPackDataDto.setAmount(deductible.getAmount());
				resUserCouponsPackDataDto.setBalance(deductible.getBalance());
				resUserCouponsPackDataDto.setCouponsType("4");
				resUserCouponsPackDataDto.setAmtSub(amtSub);
				resUserCouponsPackDataDto.setCouponsTypeDesc("代金劵立减金额:"+amtSub);
				usableList.add(resUserCouponsPackDataDto);
			}
		}

        LocalDateTime dtNow = LocalDateTime.now();
    	//用户所有的优惠券
    	Page<ResUserCouponsPackDataDto> listCouponsUserData = SpringContextHolder.getBean(CouponsUserServiceImpl.class).queryBySeqAllUsableUseCache(orderPrevDto.getUserSeq());
    	log.debug("-----listCouponsUserData----"+listCouponsUserData.size());
    	//优惠券中包含的商品规则
    	Set<Long> couponsGoodsRuleIdList = new HashSet<Long>();
    	
    	//符合基本规则的用户优惠券
    	List<ResUserCouponsPackDataDto> couponsUserList = new ArrayList<>();
    	for(ResUserCouponsPackDataDto dtoTmp : listCouponsUserData) {
    		String validStart = parseDateStr(dtoTmp.getValiDayStart());
    		String validEnd = parseDateStr(dtoTmp.getValiDayEnd());
    		
    		//优惠券状态验证
    		if(! String.valueOf(CouponsStatusEnum.UNUSERED.getCode()).equals(dtoTmp.getCouponsStatus())) {
    			//log.debug(orderPrevDto.getUserSeq()+"---状态验证排除----状态="+dtoTmp.getCouponsStatus());
    			continue;
    		}
    		//有效期验证
    		if( dtNow.isBefore(DateUtils.parse(validStart)) ||  dtNow.isAfter(DateUtils.parse(validEnd))) {
    			//log.debug(orderPrevDto.getUserSeq()+"---时间验证排除----now="+DateUtils.now()+"-----start="+dtoTmp.getValiDayStart()+"---------end="+dtoTmp.getValiDayEnd());
    			continue;
    		}
    		//结算金额验证
    		BigDecimal subAmt = BigDecimal.ZERO;
    		CouponsTypeEnum couponsTypeEnum_Tmp = CouponsTypeEnum.getNameByCode(dtoTmp.getCouponsType());
			System.out.println(dtoTmp.getCouponsUserId()+"id:"+dtoTmp.getCouponsId()+"____"+dtoTmp.getCouponsType());
    		switch (couponsTypeEnum_Tmp) {
			case RANGEREDUCE:
				subAmt = new BigDecimal(dtoTmp.getAmtFullReduce());
				break;
			case ERECTREDUCE:
				subAmt = new BigDecimal(dtoTmp.getAmtSub());
				break;
			default:
				break;
			}
    		//优惠里的，不能大于订单金额
    		if( subAmt.compareTo(orderPrevDto.getOrderMoney()) >= 0 ) {
    			//log.debug(orderPrevDto.getUserSeq()+"---结算金额验证排除---subAmt="+subAmt+"------OrderMoney="+orderPrevDto.getOrderMoney());
    			continue;
    		}
    		
    		//金额验证
    		BigDecimal minAmt = new BigDecimal(dtoTmp.getMinSpendMoney());
    		if( minAmt.compareTo(orderPrevDto.getOrderMoney()) > 0 ) {
    			//log.debug(orderPrevDto.getUserSeq()+"---金额验证排除---minAmt="+minAmt+"------OrderMoney="+orderPrevDto.getOrderMoney());
    			continue;
    		}
    		if(!StringUtils.isEmpty(dtoTmp.getActivityGoodsRuleId())) {
    			couponsGoodsRuleIdList.add(Long.parseLong(dtoTmp.getActivityGoodsRuleId()));
    		}
    		couponsUserList.add(dtoTmp);
    	}

    	log.debug("---------couponsUserList-------"+couponsUserList.size());
    	//没有有效的用户优惠券，直接返回
    	if(couponsUserList.isEmpty()) {
    		return usableList;
    	}
    	
		/**用户优惠券中，包含的所有商品规则**/
    	Map<Long,List<ActivityGoodsRuleDetails>> goodsRuleMaps = activityGoodsRuleDetailsService.queryActivityGoodsRuleMapBy(new ArrayList<Long>(couponsGoodsRuleIdList));
    	//log.debug("---------goodsRuleMaps-------"+goodsRuleMaps.size());

    	for(ResUserCouponsPackDataDto dtoTmp : couponsUserList) {

			/**
			 * 	全品类 modifyBy ltg 2018.10.11
			 */
			if (StringUtils.isEmpty(dtoTmp.getActivityGoodsRuleId())){
				usableList.add(dtoTmp);
				continue;
			}

    		boolean isValidGoodsRule = validGoodsRule(dtoTmp.getActivityGoodsRuleId(), dtoTmp.getGoodsRuleType(), orderPrevDto.getGoodsCodeList(), goodsRuleMaps,distributionProducts);
			if(!isValidGoodsRule) {
    			continue;	//继续遍历下张优惠券
    		}
    		
    		//验证通过
    		usableList.add(dtoTmp);
    	}

    	usableList.setPageNum(pageDto.getPage());
    	usableList.setPageSize(pageDto.getLimit());
    	usableList.setTotal(usableList.size());
    	log.debug("----------usableList---"+usableList.size());
    	return usableList;
    }


    /**
     * 验证 商品是否 符合优惠券规则
     * @param activityGoodsRuleId 规则Id
     * @param goodsRuleType 规则类型(自由选择商品,类目,供应商)
     * @param goodsListStr 要订单的商品
     * @param goodsRuleMaps 规则明细数据
     * @return
     */
    private boolean validGoodsRule(String activityGoodsRuleId,String goodsRuleType,String goodsListStr,Map<Long,List<ActivityGoodsRuleDetails>> goodsRuleMaps,Page<DistributionProduct> distributionProducts) {
    	boolean isValidGoodsRule = true;
		//商品规则验证
		if(!StringUtils.isEmpty(activityGoodsRuleId)) {//没有设置商品规则，则默认全场通用
			Long goodsRuleId = Long.parseLong(activityGoodsRuleId);
			List<ActivityGoodsRuleDetails> ruleList = goodsRuleMaps.getOrDefault(goodsRuleId, new ArrayList<>());

			ActivityGoodsRuleTypeEnum enumTem = ActivityGoodsRuleTypeEnum.getByCode(goodsRuleType);
			switch (enumTem) {
				case SUPPLIER:
					boolean validSupGoodsRule = validSupplierRule(ruleList, distributionProducts);
					if (!validSupGoodsRule) {//供应商规则验证不通过
						isValidGoodsRule = false;
					}
					break;
				case PORTFOLIO:
					boolean validGoodsRule = validGoodsCodeRule(goodsListStr, ruleList);
					if (!validGoodsRule) {//自由选择商品规则验证不通过
						isValidGoodsRule = false;
					}
					break;
				case FIRST_CATEGORY:
					boolean validFirstRule = validFirstRule(ruleList, distributionProducts);
					if (!validFirstRule) {//一级规则验证不通过
						isValidGoodsRule = false;
					}
					break;
				case TWO_CATEGORY:
					boolean validTwoRule = validSecondRule(ruleList, distributionProducts);
					if (!validTwoRule) {//二级类目规则验证不通过
						isValidGoodsRule = false;
					}
					break;
				case THREE_CATEGORY:
					boolean validThreeRule = validThirdRule(ruleList, distributionProducts);
					if (!validThreeRule) {//三级类目规则验证不通过
						isValidGoodsRule = false;
					}
					break;
				case FOUR_CATEGORY:
					boolean validFourRule = validFourRule(ruleList, distributionProducts);
					if (!validFourRule) {//四级类目规则验证不通过
						isValidGoodsRule = false;
					}
					break;
				default:
					break;
			}

		}
		return isValidGoodsRule;//
		
    }

	//验证一级类目商品规则
	private boolean validFirstRule(List<ActivityGoodsRuleDetails> ruleList,Page<DistributionProduct> distributionProducts) {
		boolean validGoodsRule = true;
		Map<String,ActivityGoodsRuleDetails> goodsCodeMap = ruleList.stream().collect(
				Collectors.toMap(ActivityGoodsRuleDetails::getRefSignValue, a -> a,
						(oldValue, newValue) -> oldValue
				)
		);

		for (DistributionProduct d:distributionProducts){
			if (!goodsCodeMap.containsKey(d.getCategoryid()+"")) {
				validGoodsRule = false;
				//log.debug("----一级类目Code验证排除-----code="+d.getProductCode()+"----ruleCode="+goodsCodeMap.keySet());
				break;
			}
		}
		return validGoodsRule;
	}


	//验证二级类目商品规则
	private boolean validSecondRule(List<ActivityGoodsRuleDetails> ruleList,Page<DistributionProduct> distributionProducts) {
		boolean validGoodsRule = true;
		Map<String,ActivityGoodsRuleDetails> goodsCodeMap = ruleList.stream().collect(
				Collectors.toMap(ActivityGoodsRuleDetails::getRefSignValue, a -> a,
						(oldValue, newValue) -> oldValue
				)
		);

		for (DistributionProduct d:distributionProducts){
			if (!goodsCodeMap.containsKey(d.getGenreid()+"")) {
				validGoodsRule = false;
				//log.debug("----二级类目Code验证排除-----code="+d.getProductCode()+"----ruleCode="+goodsCodeMap.keySet());
				break;
			}
		}
		return validGoodsRule;
	}


	//验证三级类目商品规则
	private boolean validThirdRule(List<ActivityGoodsRuleDetails> ruleList,Page<DistributionProduct> distributionProducts) {
		boolean validGoodsRule = true;
		Map<String,ActivityGoodsRuleDetails> goodsCodeMap = ruleList.stream().collect(
				Collectors.toMap(ActivityGoodsRuleDetails::getRefSignValue, a -> a,
						(oldValue, newValue) -> oldValue
				)
		);

		for (DistributionProduct d:distributionProducts){
			if (!goodsCodeMap.containsKey(d.getThreeid()+"")) {
				validGoodsRule = false;
				//log.debug("----三级类目Code验证排除-----code="+d.getProductCode()+"----ruleCode="+goodsCodeMap.keySet());
				break;
			}
		}
		return validGoodsRule;
	}


	//验证四级类目商品规则
	private boolean validFourRule(List<ActivityGoodsRuleDetails> ruleList,Page<DistributionProduct> distributionProducts) {
		boolean validGoodsRule = true;
		Map<String,ActivityGoodsRuleDetails> goodsCodeMap = ruleList.stream().collect(
				Collectors.toMap(ActivityGoodsRuleDetails::getRefSignValue, a -> a,
						(oldValue, newValue) -> oldValue
				)
		);

		for (DistributionProduct d:distributionProducts){
			if (!goodsCodeMap.containsKey(d.getFourid()+"")) {
				validGoodsRule = false;
				//log.debug("----四级类目验证排除-----code="+d.getProductCode()+"----ruleCode="+goodsCodeMap.keySet());
				break;
			}
		}
		return validGoodsRule;
	}


	//验证供应商商品规则
	private boolean validSupplierRule(List<ActivityGoodsRuleDetails> ruleList,Page<DistributionProduct> distributionProducts) {
		boolean validGoodsRule = true;
		Map<String,ActivityGoodsRuleDetails> goodsCodeMap = ruleList.stream().collect(
				Collectors.toMap(ActivityGoodsRuleDetails::getRefSignValue, a -> a,
						(oldValue, newValue) -> oldValue
				)
		);

		for (DistributionProduct d:distributionProducts){
			if (!goodsCodeMap.containsKey(d.getCompany())) {
				validGoodsRule = false;
				//log.debug("----供应商商品Code验证排除-----code="+d.getProductCode()+"----ruleCode="+goodsCodeMap.keySet());
				break;
			}
		}
		return validGoodsRule;
	}


	//验证组合商品规则
	private boolean validGoodsCodeRule(String goodsListStr, List<ActivityGoodsRuleDetails> ruleList) {
		boolean validGoodsRule = true;
		Map<String,ActivityGoodsRuleDetails> goodsCodeMap = ruleList.stream().collect(
		        Collectors.toMap(ActivityGoodsRuleDetails::getRefSignValue, a -> a,
		                (oldValue, newValue) -> oldValue
		        )
		);
		//结算包含的商品Code
		String[] codeArr = goodsListStr.replaceAll("[\\[\\]]", "").trim().split(",");
		List<String> codeList = Arrays.asList(codeArr);
		for(String code : codeList) {
			ActivityGoodsRuleDetails tmpRule = goodsCodeMap.get(code.trim());
			if(tmpRule == null) {
				validGoodsRule = false;	
				//log.debug("----自由选择商品Code验证排除-----code="+code+"----ruleCode="+goodsCodeMap.keySet());
				break;
			}
		}
		return validGoodsRule;
	}
	
	
	/**查询商品对应的所有优惠券**/
	@Override
	public Page<ResGoodsCouponsListDataDto> queryCouponsListByGoodsCode(ReqGoodsCouponsListDataDto apiDto,PageDto pageDto) {
		
		apiDto.setNowTime(DateUtils.to(LocalDateTime.now()));
		DistributionProduct product_db = new DistributionProduct();
		product_db.setProductCode(apiDto.getGoodsCode());
		product_db = distributionProductMapper.selectOne(product_db);
		
		if(product_db != null) {
			if(! StringUtils.isEmpty(product_db.getCategoryid())) {//1级分类
				apiDto.setCategoryid(product_db.getCategoryid());
			}
			if(! StringUtils.isEmpty(product_db.getGenreid())) {//2级分类
				apiDto.setGenreid(product_db.getGenreid());
			}
			if(! StringUtils.isEmpty(product_db.getThreeid())) {//3级分类
				apiDto.setThreeid(product_db.getThreeid());
			}
			if(! StringUtils.isEmpty(product_db.getFourid())) {//4级分类
				apiDto.setFourid(product_db.getFourid());
			}
			if(! StringUtils.isEmpty(product_db.getCompany())) {//供应商
				apiDto.setCompany(product_db.getCompany());
			}
			com.github.pagehelper.PageHelper.startPage(pageDto.getPage(), pageDto.getLimit());
			Page<ResGoodsCouponsListDataDto> listData = couponsMapper.queryCouponsListByGoodsCode(apiDto);
			return listData;
		}else {
			log.info("商品Code--selectOne查无信息--"+apiDto.getGoodsCode());
			return new Page<>();
		}
		
	}
	
 
    //用户所有可用优惠券 
    @Cacheable(value = Constant.CACHEKEY_HOUR_12, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_COUPONSUSER_USERSEQ +#userSeq+'--queryBySeqAllUsable--'", unless = "#result eq null")
    @Override
    public Page<ResUserCouponsPackDataDto> queryBySeqAllUsableUseCache(String userSeq) {
    	log.debug("----------queryBySeqAllUsableUseCache----By-----DB----------");
    	ReqUserCouponsOrderPrevDto orderPrevDto = new ReqUserCouponsOrderPrevDto();
    	orderPrevDto.setUserSeq(userSeq);
    	// 根据	用户seq 查询 用户所有可用优惠券	
    	return couponsUserMapper.queryBySeqAllUsable(orderPrevDto);
    }

	
	private String parseDateStr(String dateStr) {
		if(!StringUtils.isEmpty(dateStr) && dateStr.length() >=19 ) {
			dateStr = dateStr.substring(0, 19);
		}
		return dateStr;
	}

	
    /**
     * 订单提交锁定优惠券。状态变更 2：锁定中
     */
	@Transactional
	@Override
	public Msg<String> updateCouponsUserOrderUsed(ReqUserCouponsOrderUseDto dto){
		if (StringUtils.isEmpty(dto.getGoodsCodeList())) {
			return Msg.error("订单商品Code不能为空");
		}
		if (StringUtils.isEmpty(dto.getUserSeq())) {
			return Msg.error("该用户标识不能为空");
		}
		if (StringUtils.isEmpty(dto.getUseOrderNo())) {
			return Msg.error("结算订单号不能为空");
		}
		if (StringUtils.isEmpty(dto.getCouponsUserId())) {
			return Msg.error("优惠券标识不能为空");
		}
		if (StringUtils.isEmpty(dto.getUseOrderMoney()) || dto.getUseOrderMoney().compareTo(BigDecimal.ZERO)<=0) {
			return Msg.error("订单金额不能为空");
		}
		if (StringUtils.isEmpty(dto.getUseSpendMoney()) || dto.getUseSpendMoney().compareTo(BigDecimal.ZERO)<=0) {
			return Msg.error("优惠金额不能为空");
		}
		
		LocalDateTime dtNow = LocalDateTime.now();
		CouponsUser couponsUser = couponsUserMapper.queryAndCouponsById(dto.getCouponsUserId());
		if(couponsUser == null) {
			return Msg.error("优惠券状标识["+dto.getCouponsUserId()+"]有误");
		}
		//优惠券使用资格验证
		if(couponsUser.getCouponsStatus() != CouponsStatusEnum.UNUSERED.getCode()  ) {
			CouponsStatusEnum enums = CouponsStatusEnum.getMapCodeEnum().get(couponsUser.getCouponsStatus().toString());
			return Msg.error("优惠券状态为["+enums.getKey()+"]");
		}
		if(dtNow.isBefore(couponsUser.getValiDayStart()) || dtNow.isAfter(couponsUser.getValiDayEnd()) ) {
			return Msg.error("优惠券有效时间是["+DateUtils.to(couponsUser.getValiDayStart())+"-"+DateUtils.to(couponsUser.getValiDayEnd())+"]");
		}
		
		if( dto.getUseOrderMoney().compareTo(couponsUser.getMinSpendMoney()) < 0 ) {
			return Msg.error("优惠券最低消费金额为"+couponsUser.getMinSpendMoney()+"元");
		}

		/**
		 * modifyBy ltg 2018.10.11 使用赠送优惠券,1000来自赠送
		 */

		if (!"1000".equals(couponsUser.getReceiveSrc())&&couponsUser.getCouponsType()!=4) {
			//优惠券中包含的商品规则
			List<Long> couponsGoodsRuleIdList = new ArrayList<>();
			couponsGoodsRuleIdList.add(Long.parseLong(couponsUser.getActivityGoodsRuleId()));
			Map<Long, List<ActivityGoodsRuleDetails>> goodsRuleMaps = activityGoodsRuleDetailsService.queryActivityGoodsRuleMapBy(couponsGoodsRuleIdList);

			//商品规则
			boolean isValidGoodsRule = validGoodsRule(couponsUser.getActivityGoodsRuleId(), couponsUser.getGoodsRuleType(), dto.getGoodsCodeList(), goodsRuleMaps, new Page<>());
			if (!isValidGoodsRule) {
				return Msg.error("部分商品无法使用优惠券[" + couponsUser.getCouponsName() + "]结算");
			}
		}
		dto.setNowTime(DateUtils.to(dtNow));
		String desc = JsonUtils.toDefaultJson(dto.getGoodsCodeList())
				.concat("订单金额")
				.concat(dto.getUseOrderMoney().setScale(2,BigDecimal.ROUND_HALF_DOWN).toString())
				.concat("优惠金额为:")
				.concat(dto.getUseSpendMoney().setScale(2,BigDecimal.ROUND_HALF_DOWN).toString())
				;
		dto.setUseDesc(desc);
		int result = couponsUserMapper.updateByOrderUsedLock(dto);
		
		Assert.isTrue(result == 1, "使用优惠券失败。");
		//清除 CouponsUser--userSeq-- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ +dto.getUserSeq());
		
		return Msg.ok("锁定["+dto.getCouponsUserId()+"]优惠券成功", "");
    }
	
	@Transactional
	@Override
	public Msg<String> updateCouponsUserOrderByLockSuccess(ReqUserCouponsOrderLock dto){
		if (StringUtils.isEmpty(dto.getUserSeq())) {
			return Msg.error("该用户标识不能为空");
		}
		if (StringUtils.isEmpty(dto.getUseOrderNo())) {
			return Msg.error("结算订单号不能为空");
		}
		if (StringUtils.isEmpty(dto.getCouponsUserId())) {
			return Msg.error("优惠券标识不能为空");
		}
		
		CouponsUser record = new CouponsUser();
		record.setUserSeq(dto.getUserSeq());
		record.setUseOrder(dto.getUseOrderNo());
		record.setId(dto.getCouponsUserId());
		CouponsUser couponsUser = couponsUserMapper.selectOne(record);
		if(couponsUser == null) {
			return Msg.error("优惠券订单信息有误");
		}
		if(couponsUser.getCouponsStatus() != CouponsStatusEnum.LOCKED.getCode()  ) {
			CouponsStatusEnum enums = CouponsStatusEnum.getMapCodeEnum().get(couponsUser.getCouponsStatus().toString());
			return Msg.error("无法结算,优惠券状态为["+enums.getKey()+"]");
		}
		int result = couponsUserMapper.updateOrderByLockSuccess(dto);
		
		Assert.isTrue(result == 1, "结算优惠券失败。");
		//清除 CouponsUser--userSeq-- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+dto.getUserSeq());
		
		return Msg.ok("结算["+dto.getCouponsUserId()+"]优惠券成功", "");
	}
	
	@Transactional
	@Override
	/**订单取消优惠券还原。状态变更 1：未使用**/
	public Msg<String> updateCouponsUserOrderByLockCannel(ReqUserCouponsOrderLock dto){
		if (StringUtils.isEmpty(dto.getUserSeq())) {
			return Msg.error("该用户标识不能为空");
		}
		if (StringUtils.isEmpty(dto.getUseOrderNo())) {
			return Msg.error("结算订单号不能为空");
		}
		if (StringUtils.isEmpty(dto.getCouponsUserId())) {
			return Msg.error("优惠券标识不能为空");
		}
		
		CouponsUser record = new CouponsUser();
		record.setUserSeq(dto.getUserSeq());
		record.setUseOrder(dto.getUseOrderNo());
		record.setId(dto.getCouponsUserId());
		CouponsUser couponsUser = couponsUserMapper.selectOne(record);
		if(couponsUser == null) {
			return Msg.error("优惠券订单信息有误");
		}
		if(couponsUser.getCouponsStatus() != CouponsStatusEnum.LOCKED.getCode()  ) {
			CouponsStatusEnum enums = CouponsStatusEnum.getMapCodeEnum().get(couponsUser.getCouponsStatus().toString());
			return Msg.error("无法还原,优惠券状态为["+enums.getKey()+"]");
		}
		dto.setUseDesc("订单号["+dto.getUseOrderNo()+"]还原");	//还原添加描述
		int result = couponsUserMapper.updateOrderByLockCannel(dto);
		Assert.isTrue(result == 1, "还原优惠券失败。");
		
		//清除 CouponsUser--userSeq-- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ +dto.getUserSeq());
		return Msg.ok("还原["+dto.getCouponsUserId()+"]优惠券成功", "");
	}
	
	/**定时任务，优惠券过期 状态4过期**/	
	public Msg<String> updateCouponsStatusExpiredByJob(String expiredDay,Long couponsUserId){
		int num = couponsUserMapper.updateCouponsStatusExpiredByJob(expiredDay, couponsUserId);
		if(num > 0) {
			//清除 CouponsUser--userSeq-- 的缓存
			SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ);
		}
				
		return Msg.ok("过期优惠券["+num+"]条记录", "");
	}


	/**
	 * 
	 * 判断该用户是否有领取优惠券的记录
	 */
	@Override
	public boolean checkCouponsGetOrNotByUser(String userSeq) {
		int couponsCounts = couponsUserMapper.checkCouponsGetOrNotByUser(userSeq);
		if(couponsCounts>=1) {
			return true;
		}else {
			return false;
		}
	}


	@RedisLock(lockPrefix="CouponsUserServiceImpl-receiveNewPeopleCoupons-")
	@Transactional
	@Override
	public Msg<String> receiveNewPeopleCoupons(ReqUserReciveCouponsApiDataDto dto) {
		//单次两次优惠券数量
				int receiveAddNum = 1;
				
				if (StringUtils.isEmpty(dto.getUserSeq()) ) {
					return Msg.error("用户标识不能为空");
				}
				if (!dto.getUserSeq().matches("^[0-9]*$") ) {
					return Msg.error("用户标识有误。");
				}
				if (StringUtils.isEmpty(dto.getUserPhone())) {
					return Msg.error("用户手机号不能为空");
				}
				if(StringUtils.isEmpty(dto.getActBatch()) ) {
					return Msg.error("活动标识不能为空");
				}
				
				boolean flag = checkCouponsGetOrNotByUser(dto.getUserSeq());
				if(flag) {
					return Msg.error("该用户已领取过优惠券","");
				}
				//-------sqlserver 验证 用户信息验证
				Shopper shopTmp = new Shopper();
				shopTmp.setSeq(Integer.parseInt(dto.getUserSeq()));
				shopTmp = shopperMapper.queryBySeqOrMobile(shopTmp);
				if(shopTmp == null) {
					return Msg.error("没有查到该领取用户。");
				}
				if(!dto.getUserPhone().equals(shopTmp.getMobile())) {
					return Msg.error("请确认用户信息提交是否正确。");
				}
				//-------end sqlserver 验证 用户信息验证-----------
				dto.setUserPhone(shopTmp.getTelephone());
				List<Coupons> couponsList = null;	//优惠券集合
				
				CouponsUser userReviceCoupons = new CouponsUser();
				userReviceCoupons.setUserSeq(dto.getUserSeq());
				
				LocalDateTime dtNow = LocalDateTime.now();
				if (!StringUtils.isEmpty(dto.getActBatch())) {//按活动批次获取优惠券
					Activity activityBatch = activityService.queryByBatch(dto.getActBatch());
					if(activityBatch == null) {
						return Msg.error("活动暂不可用");
					}
					
					if(activityBatch.getIsDisabled()==true) {
						return Msg.error("活动暂不可用");
					}
					//活动资格验证
					if(dtNow.isBefore(activityBatch.getSendTimeStart())) {
						return Msg.error("活动时间是["+DateUtils.to(activityBatch.getSendTimeStart())+"]");
					}
					if(dtNow.isAfter(activityBatch.getSendTimeEnd())) {
						return Msg.error("活动已经结束");
					}
					
					//根据活动批次号，查询优惠券列表
					Coupons tmpCoupons = new Coupons();
					tmpCoupons.setActBatchNo(dto.getActBatch());
					couponsList = couponsService.queryByActivity(tmpCoupons);	//根据活动批次号，查询活动所有优惠券
				
					if (CollectionUtils.isEmpty(couponsList)) {
						return Msg.error("活动暂无可领取优惠券");
					}
					
					userReviceCoupons.setActivityId(activityBatch.getId().toString());
				}
				//用户领取的优惠券
				List<CouponsUser> userReviceCouponsList =  couponsUserMapper.queryByReciveCoupons(userReviceCoupons);
				
				//用户对每个优惠券Id 领取次数<key:couponsId,value:领取次数>
				Map<String,Integer> mapUserReciveNums = new HashMap<String,Integer>();
				userReviceCouponsList.forEach(u -> {
					mapUserReciveNums.merge(u.getCouponsId(), 1, (value, newValue) -> value+1);
				});
				
				//可领取的优惠券集合
				List<Coupons> couponsTmpList = null;	//优惠券集合
				//验证优惠券领取规则
				Msg<String> validReviceErrorMsg = null;
				if(couponsList.size()>0) {
					for (int i = 0; i < couponsList.size(); i++) {
						Coupons tmpC=couponsList.get(i);
						//活动资格验证
						if(dtNow.isBefore(tmpC.getSendTimeStart())) {
							//validReviceErrorMsg = Msg.error("优惠券["+tmpC.getName()+"]的发放时间是["+DateUtils.to(tmpC.getSendTimeStart())+"]");
							couponsList.remove(couponsList.get(i));
							i--; // 索引改变!
							continue;
						}
						if(dtNow.isAfter(tmpC.getSendTimeEnd())) {
						//	validReviceErrorMsg = Msg.error("优惠券["+tmpC.getName()+"]发放已经结束");
							couponsList.remove(couponsList.get(i));
							i--; // 索引改变!
							continue;
						}
						
						//总数量限制  0默认不限制
						if(tmpC.getSendNum() != 0 && tmpC.getReceiveNum() + receiveAddNum > tmpC.getSendNum()) {
							//validReviceErrorMsg =Msg.error("您来晚了,优惠券["+tmpC.getName()+"]已被全部领光.");
							couponsList.remove(couponsList.get(i));
							i--; // 索引改变!
							continue;
						}
						//用户领取数量限制  0默认不限制
						int userReviceNums = getUserReviceNums(tmpC,dto.getUserSeq());
						if(userReviceNums != 0 && mapUserReciveNums.getOrDefault(tmpC.getId().toString(),0) >= userReviceNums) {
							//validReviceErrorMsg =Msg.error("只能领取["+userReviceNums+"]张["+tmpC.getName()+"]优惠券");
							couponsList.remove(couponsList.get(i));
							i--; // 索引改变!
							continue;
						}
						}
				}
		

			/*	for (Coupons tmpC : couponsList) {
					//活动资格验证
					if(dtNow.isBefore(tmpC.getSendTimeStart())) {
						validReviceErrorMsg = Msg.error("优惠券["+tmpC.getName()+"]的发放时间是["+DateUtils.to(tmpC.getSendTimeStart())+"]");
						break;
					}
					if(dtNow.isAfter(tmpC.getSendTimeEnd())) {
						validReviceErrorMsg = Msg.error("优惠券["+tmpC.getName()+"]发放已经结束");
						break;
					}
					
					//总数量限制  0默认不限制
					if(tmpC.getSendNum() != 0 && tmpC.getReceiveNum() + receiveAddNum > tmpC.getSendNum()) {
						validReviceErrorMsg =Msg.error("您来晚了,优惠券["+tmpC.getName()+"]已被全部领光.");
						continue;
					}
					//用户领取数量限制  0默认不限制
					int userReviceNums = getUserReviceNums(tmpC,dto.getUserSeq());
					if(userReviceNums != 0 && mapUserReciveNums.getOrDefault(tmpC.getId().toString(),0) >= userReviceNums) {
						validReviceErrorMsg =Msg.error("只能领取["+userReviceNums+"]张["+tmpC.getName()+"]优惠券");
						break;
					}
					//couponsTmpList.add(tmpC);
				}*/
				
			/*	if(validReviceErrorMsg != null) {
					return validReviceErrorMsg;
				}*/
				//清除CouponsUser-userSeq- 的缓存
				SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+dto.getUserSeq());
				
				//发放优惠券
				List<CouponsUser> couponsUserList = initCouponsUserData(dto, couponsList);
				if(couponsUserList.size()<=0) {
					return Msg.error("暂无可领取优惠券");
				}
				int result = couponsUserMapper.insertList(couponsUserList);
				Assert.isTrue(result == couponsList.size(), "领取优惠券出错出错");
				
				//发放总数量限制
				for (Coupons tmpC : couponsList) {
					int updateUum = couponsMapper.updateCouponsReceiveNum(tmpC.getId(),receiveAddNum,dto.getUserSeq());
					Assert.isTrue(String.valueOf(updateUum).equals("1"), "优惠券["+tmpC.getName()+"]已发放完.");
				}
						
				return Msg.ok("领取优惠券成功", "");
	}

	/**
	 * 赠送用户优惠券
	 * @param dto
	 * @return
	 */
	@Transactional
	@Override
	public Msg<String> giftUserCoupons(ReqGiftUserCoupons dto) {

		Shopper tagetshopperParam=new Shopper();
		tagetshopperParam.setSeq(Integer.parseInt(dto.getTargetUserSeq()));
		Shopper tagetShopper=shopperMapper.selectOne(tagetshopperParam);
		if (null==tagetShopper){
			return Msg.error("查询不到目标用户");
		}

		Shopper shopperParam=new Shopper();
		shopperParam.setSeq(Integer.parseInt(dto.getUserSeq()));
		Shopper shopper=shopperMapper.selectOne(shopperParam);
		if (null==shopper){
			return Msg.error("查询不到赠送用户");
		}

		LocalDateTime now=LocalDateTime.now();

		CouponsUser couponsUser=new CouponsUser();
		couponsUser.setUserSeq(dto.getTargetUserSeq());
//		couponsUser.setActivityId(0);
//		couponsUser.setCouponsId();
		couponsUser.setValiDayStart(now);
		couponsUser.setValiDayEnd(now.plusMonths(1));
//		couponsUser.setAmtDiscount();
		couponsUser.setAmtSub(dto.getAmount());
		couponsUser.setCouponsStatus(CouponsStatusEnum.UNUSERED.getCode());
		couponsUser.setCouponsType(Integer.parseInt(CouponsTypeEnum.ERECTREDUCE.getCode()));
		couponsUser.setCouponsTypeDesc("赠送立减"+dto.getAmount()+"元");
		couponsUser.setCreateAdmin(dto.getUserSeq());
		couponsUser.setCreateTime(now);
		couponsUser.setCouponsTypeModel(1);
		couponsUser.setIsDisabled(false);
		couponsUser.setPhone(tagetShopper.getMobile()!=null? tagetShopper.getMobile():"");
		couponsUser.setUpdateAdmin(dto.getUserSeq());
		couponsUser.setUpdateTime(now);
		couponsUser.setReceiveSrc("1000");
		couponsUser.setMinSpendMoney(BigDecimal.ZERO);
		couponsUser.setGiveSeq(shopper.getSeq()+"");
		couponsUser.setGiveCouponsName(shopper.getUsername());

		couponsUserMapper.insert(couponsUser);

		//清除CouponsUser-userSeq- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+dto.getTargetUserSeq());

		return Msg.ok("赠送优惠券成功",couponsUser.getId()+"");
	}

	@Override
	public Msg<String> updateUserCouponsStatus(ReqUserCouponsOrderLock dto) {

		int i=couponsUserMapper.updateCouponsStatus(dto.getCouponsUserId());

		if (i<=0){
			return Msg.error("操作失败");
		}
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+dto.getUserSeq());
		return Msg.ok("操作成功");
	}


	/**
	 * 大兵一键领取优惠券
	 * @param dto
	 * @return
	 */
	@Transactional
	@Override
	public Msg<String> userReciveCoupons(ReqUserReciveCouponsApiDataDto dto) {
		ReqActivityCouponsListDataDto apiDto=new ReqActivityCouponsListDataDto();
		com.github.pagehelper.PageHelper.startPage(1, 200);
		apiDto.setNowTime(DateUtils.to(LocalDateTime.now()));
		Page<Coupons> couponsList = couponsMapper.queryCouponsList(apiDto);

		CouponsUser userReviceCoupons=new CouponsUser();
		userReviceCoupons.setUserSeq(dto.getUserSeq());
		//用户领取的优惠券
		List<CouponsUser> userReviceCouponsList =  couponsUserMapper.queryByReciveCoupons(userReviceCoupons);

		//用户对每个优惠券Id 领取次数<key:couponsId,value:领取次数>
		Map<String,Integer> mapUserReciveNums = new HashMap<String,Integer>();
		userReviceCouponsList.forEach(u -> {
			mapUserReciveNums.merge(u.getCouponsId(), 1, (value, newValue) -> value+1);
		});

		//可领取的优惠券集合
//		List<Coupons> couponsTmpList = null;	//优惠券集合
//		//验证优惠券领取规则
//		Msg<String> validReviceErrorMsg = null;
		if(couponsList.size()>0) {
			for (int i = 0; i < couponsList.size(); i++) {
				Coupons tmpC=couponsList.get(i);

				//总数量限制  0默认不限制
				if(tmpC.getSendNum() != 0 && tmpC.getReceiveNum() + 1 > tmpC.getSendNum()) {
					//validReviceErrorMsg =Msg.error("您来晚了,优惠券["+tmpC.getName()+"]已被全部领光.");
					couponsList.remove(couponsList.get(i));
					i--; // 索引改变!
					continue;
				}
				//用户领取数量限制  0默认不限制
				int userReviceNums = getUserReviceNums(tmpC,dto.getUserSeq());
				if(userReviceNums != 0 && mapUserReciveNums.getOrDefault(tmpC.getId().toString(),0) >= userReviceNums) {
					//validReviceErrorMsg =Msg.error("只能领取["+userReviceNums+"]张["+tmpC.getName()+"]优惠券");
					couponsList.remove(couponsList.get(i));
					i--; // 索引改变!
					continue;
				}
			}
		}

		//清除CouponsUser-userSeq- 的缓存
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+dto.getUserSeq());

		//发放优惠券
		dto.setActBatch(dto.getUserSeq());

		List<CouponsUser> couponsUserList = initCouponsUserData(dto, couponsList);
		if (couponsList==null||couponsList.size()<=0){
			return Msg.error("优惠券已经领取");
		}
		int result = couponsUserMapper.insertList(couponsUserList);
		Assert.isTrue(result == couponsList.size(), "领取优惠券出错出错");

		int receiveAddNum=1;
		//发放总数量限制
		for (Coupons tmpC : couponsList) {
			int updateUum = couponsMapper.updateCouponsReceiveNum(tmpC.getId(),receiveAddNum,dto.getUserSeq());
			Assert.isTrue(String.valueOf(updateUum).equals("1"), "优惠券["+tmpC.getName()+"]已发放完.");
		}

		return Msg.ok("领取优惠券成功", "");

	}

	/**
	 * 赠送代金券和抵扣券
	 *@author ltg
	 *@date 2018/10/24 13:45
	 * @params:[dto]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@Transactional
	@Override
	public Msg<String> giftUserCouponsBusiness(ReqUserCouponsBusinessDto dto) {
		List<BusinessParamDto> businessParamList=dto.getBusinessParam();

		List<CouponsUser> couponsUserList=new ArrayList<>();

		for (BusinessParamDto businessParamDto:businessParamList){
			if (StringUtils.isEmpty(businessParamDto.getSeq())){
				return Msg.error("参数错误，请输入seq");
			}
			if (StringUtils.isEmpty(businessParamDto.getCouponType())){
				return Msg.error("参数错误，请输入优惠券类型");
			}

			BigDecimal big_decimal=new BigDecimal(businessParamDto.getAmount());
			int r=big_decimal.compareTo(BigDecimal.ZERO); //和0，Zero比较
			if (r<=0){
				return Msg.error("金额参数错误，必须大于0");
			}
		}

		for (BusinessParamDto businessParamDto:businessParamList){

			Shopper shopperParam=new Shopper();
			shopperParam.setSeq(Integer.parseInt(businessParamDto.getSeq()));
			Shopper shopper=shopperMapper.selectOne(shopperParam);
			if (null==shopper){
				return Msg.error("查询不到seq"+businessParamDto.getSeq()+"为用户");
			}
			LocalDateTime now=LocalDateTime.now();

			CouponsUser couponsUser=new CouponsUser();
			couponsUser.setUserSeq(businessParamDto.getSeq());
	//		couponsUser.setActivityId(0);
	//		couponsUser.setCouponsId();
	//		couponsUser.setAmtDiscount();
//			couponsUser.setAmtSub(dto.getAmount());
			couponsUser.setCouponsStatus(CouponsStatusEnum.UNUSERED.getCode());

			if (businessParamDto.getCouponType().trim().equals("1")) {
				couponsUser.setCouponsTypeDesc("代金券" + businessParamDto.getAmount() + "元");
				couponsUser.setAmount(new BigDecimal(businessParamDto.getAmount()));
//				couponsUser.setCouponsType(Integer.parseInt(CouponsTypeEnum.CASH.getCode()));
				couponsUser.setGiveCouponsName( businessParamDto.getAmount()+"元代金券");
				couponsUser.setValiDayStart(now);
				couponsUser.setValiDayEnd(now.plusMonths(1));

			}else if (businessParamDto.getCouponType().trim().equals("2")){
				couponsUser.setCouponsTypeDesc("抵扣券" + businessParamDto.getAmount() + "元");
				couponsUser.setBalance(new BigDecimal(businessParamDto.getAmount()));
				couponsUser.setUsedBalance(BigDecimal.ZERO);
				couponsUser.setAmount(new BigDecimal(businessParamDto.getAmount()));
//				couponsUser.setCouponsType(Integer.parseInt(CouponsTypeEnum.DEDUCTIBLE.getCode()));
				couponsUser.setGiveCouponsName(businessParamDto.getAmount()+"元抵扣券" );
				couponsUser.setValiDayStart(now);
				couponsUser.setValiDayEnd(now.plusYears(1));
			}
			couponsUser.setReceiveSrc(dto.getFromSysCode());
			couponsUser.setCreateAdmin(dto.getFromSysCode());
			couponsUser.setCreateTime(now);
			couponsUser.setCouponsTypeModel(1);
			couponsUser.setIsDisabled(false);
//			couponsUser.setPhone(tagetShopper.getMobile()!=null? tagetShopper.getMobile():"");
			couponsUser.setUpdateAdmin(dto.getFromSysCode());
			couponsUser.setUpdateTime(now);

			couponsUser.setMinSpendMoney(BigDecimal.ZERO);
//			couponsUser.setGiveSeq(shopper.getSeq()+"");

			couponsUserList.add(couponsUser);
//清除CouponsUser-userSeq- 的缓存
			SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+businessParamDto.getSeq());

			}
		couponsUserMapper.insertList(couponsUserList);
//		couponsUserMapper.insert(couponsUser);


		return Msg.ok("赠送券成功");
	}


	/**
	 * 抵扣券使用及还原
	 *@author ltg
	 *@date 2018/10/27 10:20
	 * @params:[dto]
	 * @return: com.shq.oper.model.dto.Msg<java.lang.String>
	 */
	@Transactional
	@Override
	public Msg<String> useDeductibleCoupons(ReqUserCouponsDeductible dto) {

		CouponsUser couponsUserParam=new CouponsUser();
		couponsUserParam.setUserSeq(dto.getUserSeq());
		couponsUserParam.setId(dto.getCouponsUserId());

//		CouponsUser couponsUser=couponsUserMapper.queryAndCouponsById(dto.getCouponsUserId());
		CouponsUser couponsUser=couponsUserMapper.selectOne(couponsUserParam);
		if (null==couponsUser){
			return Msg.error("查询不到该券");
		}

		LocalDateTime now=LocalDateTime.now();
		//1:使用
		Msg<String> msg=Msg.error("参数错误");
		if (dto.getType()==1){
			msg= getSettlementMsg(dto, couponsUserParam, couponsUser, now);
		}
		if (dto.getType()==2){
			msg= getReductionMsg(dto, couponsUserParam, couponsUser, now);
		}
		//清除缓存
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+dto.getUserSeq());

		return msg;

	}

	private Msg<String> getReductionMsg(ReqUserCouponsDeductible dto, CouponsUser couponsUserParam, CouponsUser couponsUser, LocalDateTime now) {
		if (couponsUser.getValiDayStart().isAfter(now)){
			return Msg.error("该券没到使用期");
		}
		if (couponsUser.getValiDayEnd().isBefore(now)){
			return Msg.error("该券已过期");
		}

		//余额
		BigDecimal useBalance=couponsUser.getAmount().subtract(dto.getDeductibleAmount());
		int r=useBalance.compareTo(BigDecimal.ZERO); //和0，Z
		if (r==-1){
			return Msg.error("抵扣金额过大");
		}
		couponsUserParam.setUsedBalance(useBalance);
		couponsUserParam.setBalance(couponsUser.getAmount().subtract(useBalance));
		couponsUserParam.setUseDesc("金额为："+couponsUser.getAmount()+"元的抵扣券已使用："+useBalance+"元");

		int i=couponsUserMapper.updateUserDeductible(couponsUserParam);

		if (i==1){
			return Msg.ok("操作成功");
		}else {
			return Msg.error("操作失败");
		}
	}

	private Msg<String> getSettlementMsg(ReqUserCouponsDeductible dto, CouponsUser couponsUserParam, CouponsUser couponsUser, LocalDateTime now) {
		switch (couponsUser.getCouponsStatus()){
			case 2:return Msg.error("该已在券锁定中");
			case 3:return Msg.error("该券已使用");
			case 4:return Msg.error("该券已过期");
		}

		if (couponsUser.getValiDayStart().isAfter(now)){
			return Msg.error("该券没到使用期");
		}
		if (couponsUser.getValiDayEnd().isBefore(now)){
			return Msg.error("该券已过期");
		}

		//已抵扣金额
		BigDecimal usedBalance=couponsUser.getUsedBalance().add(dto.getDeductibleAmount());
		int r=usedBalance.compareTo(couponsUser.getAmount());
		if (r==1){
			return Msg.error("抵扣金额超过抵扣券金额");
		}
		if (r==0){
			couponsUserParam.setCouponsStatus(3);
		}
		if (r==-1){
			couponsUserParam.setCouponsStatus(1);
		}
		couponsUserParam.setUsedBalance(usedBalance);
		couponsUserParam.setBalance(couponsUser.getAmount().subtract(usedBalance));
		couponsUserParam.setUseDesc("金额为："+couponsUser.getAmount()+"元的抵扣券已使用："+usedBalance+"元");

		int i=couponsUserMapper.updateUserDeductible(couponsUserParam);
		if (i==1){
			return Msg.ok("操作成功");
		}else {
			return Msg.error("操作失败");
		}
	}


}