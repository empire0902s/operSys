package com.shq.oper.service.primarydb;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRuleDetails;
import com.shq.oper.model.domain.shq520new.DistrFirstCategoty;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.homepage.HomePageInvitationPageDataDto;
import com.shq.oper.model.dto.api.req.HomePageInvitationPageParamDto;

/**
 * @author: ltg
 * @date: Created in 16:06 2018/6/7
 */
public interface ActivityGoodsRuleDetailsService extends BaseService<ActivityGoodsRuleDetails, Long>  {

	Msg<String> saveSupplierDetails(long ruleId, Integer seq, String adminStr);
	Msg<String> insertDetails(String adminstr, long ruleId, String code, LocalDateTime dtNow, Integer sort);
	/**根据规则活动商品规则Id，查询规则明细**/
	List<ActivityGoodsRuleDetails> queryByActivityGoodsRuleIdList(Long activityGoodsRuleId);
	
	/**查询所有未禁用 规则明细**/
	public List<ActivityGoodsRuleDetails> queryByAll(ActivityGoodsRuleDetails details);
	
	
	/**activityGoodsRuleId 为Key 的所有规则明细**/
	public Map<Long,List<ActivityGoodsRuleDetails>> queryActivityGoodsRuleMapBy(List<Long> couponsGoodsRuleIdList);

	//批量插入供应商规则明细
	Msg<String> bathSaveSupDetails(String adminStr, long ruleId, List<Shopper> shopperList);

	Msg<String> bathSaveCategoryDetails(String adminStr, long ruleId, List<DistrFirstCategoty> distrFirstCategotiesList);
	List<HomePageInvitationPageDataDto> queryApiLoveOfHomeInvitationPageDataList(HomePageInvitationPageParamDto homePageInvitationPageParamDto);
	Page<ActivityGoodsRuleDetails> queryLoveOfHomeInvitationPageDataList(ActivityGoodsRuleDetails entity);
	Msg<String> insertinvitationPageDetails(String adminstr, long ruleId, String code, LocalDateTime dtNow,
			Integer sort);
}
