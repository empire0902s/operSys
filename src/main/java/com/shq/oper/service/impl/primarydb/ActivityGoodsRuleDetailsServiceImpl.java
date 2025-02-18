package com.shq.oper.service.impl.primarydb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.ActivityGoodsRuleDetailsMapper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRule;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRuleDetails;
import com.shq.oper.model.domain.shq520new.DistrFirstCategoty;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.homepage.HomePageInvitationPageDataDto;
import com.shq.oper.model.dto.api.homepage.HomePageInvitationPageGoodsDto;
import com.shq.oper.model.dto.api.req.HomePageInvitationPageParamDto;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.primarydb.ActivityGoodsRuleDetailsService;
import com.shq.oper.service.primarydb.ActivityGoodsRuleService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.Constant;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ltg
 * @date: Created in 16:07 2018/6/7
 */
@Slf4j
@Service("activityGoodsRuleDetailsService")
public class ActivityGoodsRuleDetailsServiceImpl extends BaseServiceImpl<ActivityGoodsRuleDetails, Long> implements ActivityGoodsRuleDetailsService {

	@Autowired
	private ActivityGoodsRuleDetailsMapper activityGoodsRuleDetailsMapper;

	@Autowired
	private ActivityGoodsRuleService activityGoodsRuleService;

	@Autowired
	private ShopperMapper shopperMapper;

	@Autowired
	private DistributionProductMapper distributionProductMapper;
	
	/**根据规则活动商品规则Id，查询规则明细**/
    @Cacheable(value = Constant.CACHEKEY_HOUR_12, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_GOODS_RULE_QUERY +#activityGoodsRuleId+'--cache--'", unless = "#result eq null")
	@Override
	public List<ActivityGoodsRuleDetails> queryByActivityGoodsRuleIdList(Long activityGoodsRuleId){
    	ActivityGoodsRuleDetails details = new ActivityGoodsRuleDetails();
    	details.setActivityGoodsRuleId(activityGoodsRuleId);
		return activityGoodsRuleDetailsMapper.queryByList(details);
	}
    
    /**所有规则明细**/
    @Cacheable(value = Constant.CACHEKEY_HOUR_12, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_GOODS_RULE_QUERY + #details +'--all--cache--'", unless = "#result eq null")
    @Override
    public List<ActivityGoodsRuleDetails> queryByAll(ActivityGoodsRuleDetails details){
    	log.debug("----------------endter=---ActivityGoodsRuleDetails---queryByAll---"+JsonUtils.toDefaultJson(details));
    	return activityGoodsRuleDetailsMapper.queryByList(details);
    }
    
    @Override
    public Map<Long,List<ActivityGoodsRuleDetails>> queryActivityGoodsRuleMapBy(List<Long> couponsGoodsRuleIdList){
    	//log.debug("----------------endter=------queryActivityGoodsRuleMapBy----GoodsRuleIdList---"+couponsGoodsRuleIdList.size());
    	ActivityGoodsRuleDetails details = new ActivityGoodsRuleDetails();
    	details.setCouponsGoodsRuleIdList(couponsGoodsRuleIdList);
    	//log.debug("----Next query------DB?--------------endter=---");
    	List<ActivityGoodsRuleDetails> listAll = SpringContextHolder.getBean(ActivityGoodsRuleDetailsServiceImpl.class).queryByAll(details);
    	Map<Long,List<ActivityGoodsRuleDetails>> map = 
    			listAll.stream().collect( Collectors.groupingBy(ActivityGoodsRuleDetails::getActivityGoodsRuleId));
    	//log.debug("----------------endter=------mapAll---"+map.size());
    	return map;
    	
    }

	@Transactional
    @Override
	public Msg<String> insertDetails(String adminstr, long ruleId, String code, LocalDateTime dtNow,Integer sort){

		DistributionProduct distributionProductParam=new DistributionProduct();
		distributionProductParam.setProductCode(code);
		DistributionProduct distributionProduct=distributionProductMapper.selectOne(distributionProductParam);
		if (distributionProduct==null){
			return Msg.error("没有查到code值为"+code+"的商品");
		}

		ActivityGoodsRule ruleparam = new ActivityGoodsRule();
		ruleparam.setId(ruleId);
		ActivityGoodsRule activityGoodsRule = activityGoodsRuleService.selectOne(ruleparam);

		ActivityGoodsRuleDetails activityGoodsRuleDetails = new ActivityGoodsRuleDetails();
		activityGoodsRuleDetails.setActivityGoodsRuleId(ruleId);
		activityGoodsRuleDetails.setRefSignLocalId(distributionProduct.getProductCode());
		if (activityGoodsRuleDetailsMapper.selectOne(activityGoodsRuleDetails)!=null){
			return Msg.error("已经存在");
		}

		activityGoodsRuleDetails.setRefSignName(distributionProduct.getProductName());
		activityGoodsRuleDetails.setRefSignValue(code);
		activityGoodsRuleDetails.setRuleType(activityGoodsRule.getType());
		activityGoodsRuleDetails.setUpdateAdmin(adminstr);
		activityGoodsRuleDetails.setUpdateTime(dtNow);
		activityGoodsRuleDetails.setSort(sort);

		activityGoodsRuleDetailsMapper.insert(activityGoodsRuleDetails);
		
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_GOODS_RULE_QUERY);

		return Msg.ok("保存完成");
	}
	
	@Transactional
    @Override
	public Msg<String> insertinvitationPageDetails(String adminstr, long ruleId, String code, LocalDateTime dtNow,Integer sort){

		DistributionProduct distributionProductParam=new DistributionProduct();
		distributionProductParam.setProductCode(code);
		DistributionProduct distributionProduct=distributionProductMapper.selectOne(distributionProductParam);
		if (distributionProduct==null){
			return Msg.error("没有查到code值为"+code+"的商品");
		}

		ActivityGoodsRule ruleparam = new ActivityGoodsRule();
		ruleparam.setId(ruleId);
		ActivityGoodsRule activityGoodsRule = activityGoodsRuleService.selectOne(ruleparam);

		ActivityGoodsRuleDetails temp = new ActivityGoodsRuleDetails();
		//设置邀请页栏目，，排序
		temp.setActivityGoodsRuleId(ruleId);
		temp.setSort(sort);
	
		ActivityGoodsRuleDetails activityGoodsRuleDetails = new ActivityGoodsRuleDetails();
		activityGoodsRuleDetails.setActivityGoodsRuleId(ruleId);
		activityGoodsRuleDetails.setRefSignLocalId(distributionProduct.getProductCode());
		if (activityGoodsRuleDetailsMapper.selectOne(activityGoodsRuleDetails)!=null){
			return Msg.error("已经存在");
		}
		if(sort!=1000) {
			List<ActivityGoodsRuleDetails> select = activityGoodsRuleDetailsMapper.select(temp);
			if(select.size()>0) {
				ActivityGoodsRuleDetails activityGoodsRuleDetailsTemp = select.get(0);
				//将该栏目下相同的sort的数据的sort修改为1000
				activityGoodsRuleDetailsTemp.setSort(1000);
				activityGoodsRuleDetailsTemp.setUpdateTime(dtNow);
				activityGoodsRuleDetailsMapper.updateByPrimaryKey(activityGoodsRuleDetailsTemp);
			}
		}
		activityGoodsRuleDetails.setRefSignName(distributionProduct.getProductName());
		activityGoodsRuleDetails.setRefSignValue(code);
		activityGoodsRuleDetails.setRuleType(activityGoodsRule.getType());
		activityGoodsRuleDetails.setUpdateAdmin(adminstr);
		activityGoodsRuleDetails.setUpdateTime(dtNow);
		activityGoodsRuleDetails.setSort(sort);

		activityGoodsRuleDetailsMapper.insert(activityGoodsRuleDetails);
		
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_GOODS_RULE_QUERY);

		return Msg.ok("保存完成");
	}
	
	
	
	

	@Transactional
	@Override
	public Msg<String> saveSupplierDetails(long ruleId, Integer seq,  String adminStr){
		Shopper shopperParam=new Shopper();
		shopperParam.setSeq(seq);
		LocalDateTime dtNow = LocalDateTime.now();

		Shopper shopper=shopperMapper.queryBySupplier(shopperParam);
		if (shopper==null){
			return Msg.error("查询不到该供应商");
		}

		ActivityGoodsRule ruleparam = new ActivityGoodsRule();
		ruleparam.setId(ruleId);
		ActivityGoodsRule activityGoodsRule = activityGoodsRuleService.selectOne(ruleparam);

		ActivityGoodsRuleDetails activityGoodsRuleDetails = new ActivityGoodsRuleDetails();
		activityGoodsRuleDetails.setActivityGoodsRuleId(ruleId);
		activityGoodsRuleDetails.setRefSignLocalId(shopper.getSeq()+"");
		if (activityGoodsRuleDetailsMapper.selectOne(activityGoodsRuleDetails)!=null){
			return Msg.error("已经存在");
		}

		activityGoodsRuleDetails.setRefSignName(shopper.getShopname());
		activityGoodsRuleDetails.setRefSignValue(shopper.getSeq()+"");
		activityGoodsRuleDetails.setRuleType(activityGoodsRule.getType());
		activityGoodsRuleDetails.setUpdateAdmin(adminStr);
		activityGoodsRuleDetails.setUpdateTime(dtNow);

		activityGoodsRuleDetailsMapper.insert(activityGoodsRuleDetails);
		
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_GOODS_RULE_QUERY);
		return Msg.ok("保存完成");
	}

	@Transactional
	@Override
	public Msg<String> bathSaveSupDetails(String adminStr, long ruleId, List<Shopper> shopperList){
		LocalDateTime dtNow = LocalDateTime.now();

		ActivityGoodsRule ruleparam = new ActivityGoodsRule();
		ruleparam.setId(ruleId);
		ActivityGoodsRule activityGoodsRule = activityGoodsRuleService.selectOne(ruleparam);

		List<ActivityGoodsRuleDetails> activityGoodsRuleDetailsList=new ArrayList<>();
		shopperList.forEach(
				p->{
					ActivityGoodsRuleDetails activityGoodsRuleDetails = new ActivityGoodsRuleDetails();
					activityGoodsRuleDetails.setActivityGoodsRuleId(ruleId);
					activityGoodsRuleDetails.setRefSignLocalId(p.getSeq()+"");
					if (activityGoodsRuleDetailsMapper.selectOne(activityGoodsRuleDetails)==null){
						activityGoodsRuleDetails.setRefSignName(p.getShopname());
						activityGoodsRuleDetails.setRefSignValue(p.getSeq()+"");
						activityGoodsRuleDetails.setRuleType(activityGoodsRule.getType());
						activityGoodsRuleDetails.setUpdateAdmin(adminStr);
						activityGoodsRuleDetails.setUpdateTime(dtNow);

						activityGoodsRuleDetailsList.add(activityGoodsRuleDetails);
					}

				}
		);

		if (activityGoodsRuleDetailsList.size()>0){
			activityGoodsRuleDetailsMapper.insertList(activityGoodsRuleDetailsList);
		}
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_GOODS_RULE_QUERY);
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ);
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONS_QUERY);
		SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_ACTIVITY_QUERY);
		//Msg<String> clearCacheByConstantKey = SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_OPERSYS_KEY_SET);
		return Msg.ok("保存完成");
	}

	@Transactional
	@Override
	public Msg<String> bathSaveCategoryDetails(String adminStr, long ruleId, List<DistrFirstCategoty> distrFirstCategotiesList){
		LocalDateTime dtNow = LocalDateTime.now();

		ActivityGoodsRule ruleparam = new ActivityGoodsRule();
		ruleparam.setId(ruleId);
		ActivityGoodsRule activityGoodsRule = activityGoodsRuleService.selectOne(ruleparam);

		List<ActivityGoodsRuleDetails> activityGoodsRuleDetailsList=new ArrayList<>();
		distrFirstCategotiesList.forEach(
				p->{
					ActivityGoodsRuleDetails activityGoodsRuleDetails = new ActivityGoodsRuleDetails();
					activityGoodsRuleDetails.setActivityGoodsRuleId(ruleId);
					activityGoodsRuleDetails.setRefSignLocalId(p.getId()+"");
					if (activityGoodsRuleDetailsMapper.selectOne(activityGoodsRuleDetails)==null){
						activityGoodsRuleDetails.setRefSignName(p.getName());
						activityGoodsRuleDetails.setRefSignValue(p.getId()+"");
						activityGoodsRuleDetails.setRuleType(activityGoodsRule.getType());
						activityGoodsRuleDetails.setUpdateAdmin(adminStr);
						activityGoodsRuleDetails.setUpdateTime(dtNow);

						activityGoodsRuleDetailsList.add(activityGoodsRuleDetails);
					}

				}
		);

		if (activityGoodsRuleDetailsList.size()>0){
			activityGoodsRuleDetailsMapper.insertList(activityGoodsRuleDetailsList);
		}
    	return Msg.ok("保存完成");
	}

	
	
	@Cacheable(value = Constant.CACHEKEY_SECOND_15, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_CLOUMN_GOODS_API +#homePageInvitationPageParamDto", unless = "#result eq null")
	@Override
	public List<HomePageInvitationPageDataDto> queryApiLoveOfHomeInvitationPageDataList(HomePageInvitationPageParamDto homePageInvitationPageParamDto) {
		
		//系统来源或者来源Code为空，兼容上一版本的数据，手动设置查询数据为
		if(StringUtils.isEmpty(homePageInvitationPageParamDto.getFromSys())
			&&StringUtils.isEmpty(homePageInvitationPageParamDto.getFromSysCode())) {
			homePageInvitationPageParamDto.setFromSys("homeOfLove");
			homePageInvitationPageParamDto.setFromSysCode("invitationPage_Code");
		}
		List<ActivityGoodsRuleDetails> ativityGoodsRuleDetailsList = activityGoodsRuleDetailsMapper
				.queryApiLoveOfHomeInvitationPageDataList(homePageInvitationPageParamDto);
		if(ativityGoodsRuleDetailsList.size()<=0) {
			return null;
		}
		Set<String> set = new HashSet<>();
		Map<String,List<HomePageInvitationPageGoodsDto>>map=new HashMap<>();
		List<HomePageInvitationPageDataDto>result=new ArrayList<>();
		for (ActivityGoodsRuleDetails activityGoodsRuleDetails : ativityGoodsRuleDetailsList) {
			set.add(activityGoodsRuleDetails.getGoodsRuleName());
			if(map.get(activityGoodsRuleDetails.getGoodsRuleName())!=null) {
				HomePageInvitationPageGoodsDto temp=new HomePageInvitationPageGoodsDto();
				temp.setGoodsCode(activityGoodsRuleDetails.getRefSignValue());
				temp.setSort(activityGoodsRuleDetails.getSort().toString());
				map.get(activityGoodsRuleDetails.getGoodsRuleName()).add(temp);
				
			}else {
				List<HomePageInvitationPageGoodsDto>tmp=new ArrayList<>();
				HomePageInvitationPageGoodsDto temp=new HomePageInvitationPageGoodsDto();
				temp.setGoodsCode(activityGoodsRuleDetails.getRefSignValue());
				temp.setSort(activityGoodsRuleDetails.getSort().toString());
				tmp.add(temp);
				map.put(activityGoodsRuleDetails.getGoodsRuleName(),tmp);
			}
		}
		if(set.size()<=0||map.size()<=0) {
			return null;
		}
		for (String string : set) {
			HomePageInvitationPageDataDto tmp=new HomePageInvitationPageDataDto();
			List<HomePageInvitationPageGoodsDto> goodsCodeList=new ArrayList<>();
			if(!StringUtils.isEmpty(map.get(string))) {
				goodsCodeList=map.get(string);
			}
			tmp.setColumnName(string);
			tmp.setGoodsList(goodsCodeList);
			result.add(tmp);
		}
		return result;
	}

	@Override
	public Page<ActivityGoodsRuleDetails> queryLoveOfHomeInvitationPageDataList(ActivityGoodsRuleDetails entity) {
		return activityGoodsRuleDetailsMapper.queryLoveOfHomeInvitationPageDataList(entity);
	}
	
}
