package com.shq.oper.mapper.primarydb;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Activity;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.dto.api.req.ReqActivityCouponsListDataDto;
import com.shq.oper.model.dto.api.req.ReqBrandCouponsListDataDto;
import com.shq.oper.model.dto.api.req.ReqGoodsCouponsListDataDto;
import com.shq.oper.model.dto.api.res.ResActivityCouponsListDataDto;
import com.shq.oper.model.dto.api.res.ResBrandCouponsListDto;
import com.shq.oper.model.dto.api.res.ResGoodsCouponsListDataDto;
import com.shq.oper.util.BaseMapper;

@Mapper
public interface CouponsMapper extends BaseMapper<Coupons> {
	Page<Coupons> queryCouponsAndCouponsCategoryRuleAndCouponsTypeRule(Coupons entity);
	Page<Coupons> queryName(Coupons entity);
	Page<Coupons> queryCouponsAndCouponsCategoryRuleAndCouponsTypeRuleForCheck(Coupons entity);
	void batchUpdateFinanStatusToOk(List<Coupons> list);
	
	Page<Coupons> queryByActivity(Coupons tmpCoupons);
	
	Page<ResActivityCouponsListDataDto> queryCouponsListByApi(ReqActivityCouponsListDataDto apiDto);

	/**爱之家 品牌广场**/
	Page<ResActivityCouponsListDataDto> queryCouponsListByApiBrand(ReqBrandCouponsListDataDto apiDto);
	
	Page<ResGoodsCouponsListDataDto> queryCouponsListByGoodsCode(ReqGoodsCouponsListDataDto apiDto);
	
	int updateCouponsReceiveNum(@Param("id")Long id, @Param("receiveNum")int receiveNum,@Param("userSeq")String userSeq);

	int updateBath(List<Coupons> list);

	Page<ResBrandCouponsListDto> queryCouponsBrandByActive(Activity activity);

	int updateCouponsCategoryRule(Coupons coupons);

	int updateBrandCoupons(Coupons coupons);

	int batchEnbleCouponsList(List<Coupons> list);

	/**
	 *获取大兵优惠券
	 */
	Page<ResActivityCouponsListDataDto> queryCouponsListByApiDabing(ReqActivityCouponsListDataDto apiDto);

	/**
	 *可用的优惠券
	 */
	Page<Coupons> queryCouponsList(ReqActivityCouponsListDataDto apiDto);





}