package com.shq.oper.service.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.CouponsUser;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.req.*;
import com.shq.oper.model.dto.api.res.ResBrandActivityListDto;
import com.shq.oper.model.dto.api.res.ResGoodsCouponsListDataDto;
import com.shq.oper.model.dto.api.res.ResUserCouponsPackDataDto;

public interface CouponsUserService extends BaseService<CouponsUser, Long> {

	Msg<String> receiveCoupons(ReqUserReciveCouponsApiDataDto dto);

	/**  用户所有可用优惠券  **/
	Page<ResUserCouponsPackDataDto> queryBySeqAllUsableUseCache(String userSeq);
	
	Page<ResUserCouponsPackDataDto> queryByQuickPurchase(ReqUserCouponsPackDto packDataDto, PageDto pageDto);

	/**订单结算页 可用优惠券列表**/
	Page<ResUserCouponsPackDataDto> queryByQuickPurchaseOrder(ReqUserCouponsOrderPrevDto orderPrevDto, PageDto pageDto);

	/**商品详情页 优惠券列表**/
	Page<ResGoodsCouponsListDataDto> queryCouponsListByGoodsCode(ReqGoodsCouponsListDataDto apiDto, PageDto pageDto);

	/**订单提交锁定优惠券。状态变更 2：锁定中**/
	Msg<String> updateCouponsUserOrderUsed(ReqUserCouponsOrderUseDto dto);

	/**订单结算成功。状态变更 3：已使用**/
	Msg<String> updateCouponsUserOrderByLockSuccess(ReqUserCouponsOrderLock dto);
	
	/**订单取消优惠券还原。状态变更 1：未使用**/
	Msg<String> updateCouponsUserOrderByLockCannel(ReqUserCouponsOrderLock dto);

	/**定时任务，优惠券过期**/
	Msg<String> updateCouponsStatusExpiredByJob(String expiredDay,Long couponsUserId);

	boolean checkCouponsGetOrNotByUser(String userSeq);

	Msg<String> receiveNewPeopleCoupons(ReqUserReciveCouponsApiDataDto dto);

	/**
	 * 赠送用户优惠券
	 */
	Msg<String> giftUserCoupons(ReqGiftUserCoupons dto);

	/**
	 * 设置赠送用户优惠券过期
	 */
	Msg<String> updateUserCouponsStatus(ReqUserCouponsOrderLock dto);

	/**
	 * 一键领取大兵优惠券
	 * @param dto
	 * @return
	 */
	Msg<String> userReciveCoupons(ReqUserReciveCouponsApiDataDto dto);


	/**
	 * 赠送代金券和抵扣券
	 */
	Msg<String> giftUserCouponsBusiness(ReqUserCouponsBusinessDto dto);

	/**
	 * 抵扣券使用
	 */
	Msg<String> useDeductibleCoupons(ReqUserCouponsDeductible dto);




}