package com.shq.oper.mapper.primarydb;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRuleDetails;
import com.shq.oper.model.dto.api.req.HomePageInvitationPageParamDto;
import com.shq.oper.model.dto.api.res.ResBrandCouponsGoodsListDto;
import com.shq.oper.util.BaseMapper;

@Mapper
public interface ActivityGoodsRuleDetailsMapper extends BaseMapper<ActivityGoodsRuleDetails> {
	
	List<ActivityGoodsRuleDetails> queryByList(ActivityGoodsRuleDetails details);

	Page<ResBrandCouponsGoodsListDto> queryListByCouponsId(@Param("activityGoodsRuleId")Long activityGoodsRuleId);

	int BrandRemoveCouponsCateRule(@Param("ruleId") Integer ruleId, @Param("codes") List<String> codes);
	List<ActivityGoodsRuleDetails>queryApiLoveOfHomeInvitationPageDataList(HomePageInvitationPageParamDto homePageInvitationPageParamDto);
	Page<ActivityGoodsRuleDetails> queryLoveOfHomeInvitationPageDataList(ActivityGoodsRuleDetails entity);
}