package com.shq.oper.service.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.req.*;
import com.shq.oper.model.dto.api.res.*;

public interface CouponsService extends BaseService<Coupons, Long> {

	Msg<String> batchUpdateFinanStatusToOk(String couponsIds,Coupons coupons);
	
	Page<Coupons> queryByActivity(Coupons tmpCoupons);
	
	int disabledCouponsById(Coupons record);

	Page<ResActivityCouponsListDataDto> queryCouponsListByApi(ReqActivityCouponsListDataDto apiDto, PageDto pageDto);

	/**爱之家 品牌广场**/
	Page<ResActivityCouponsListDataDto> queryCouponsListByApiBrand(ReqBrandCouponsListDataDto apiDto, PageDto pageDto);


	Page<ResBrandCouponsListDto> queryCouponsBrandByActive(ReqBrandActivityListDataDto apiDto, PageDto pageDto);

	Page<ResBrandCouponsGoodsListDto> queryGoodsBrandByApi(ReqGoodsListDataDto apiDto, PageDto pageDto);

	Msg<String> BrandAddCategoryRule(ReqBrandCouponsCategoryAddDto aipDto);

	Msg<String> BrandUpdateCoupons(Coupons coupons);

	Msg<String> BrandBatchEnbleCoupons(ReqBrandCouponsBatchEnbleDto apiDto);

	Msg<ResCouponsReturnDto> BrandAddCouponsByActivityId(ReqBrandCouponsAddByActid2011Dto apiDto);

	Msg<String> BrandBatchRemoveCouponsCateRule(ReqRemoveGoodsListDataDto2010 apiDto);

	Page<ResActivityCouponsListDataDto> queryDabingCouponsListByApi(ReqActivityCouponsListDataDto apiDto, PageDto pageDto);


	/*Msg<String> updateCouponsReceiveNum(Long id, int receive,String userSeq);*/
}