package com.shq.oper.mapper.primarydb;

import java.time.LocalDateTime;

import com.shq.oper.model.dto.salestatistics.GiveCouponsDayStatisticsDto;
import com.shq.oper.model.dto.salestatistics.GiveCouponsStatisticsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.CouponsUser;
import com.shq.oper.model.dto.CouponsStatisticsDto;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.ReqCouponsStatisticsDtoDto;
import com.shq.oper.model.dto.api.req.ReqUserCouponsOrderLock;
import com.shq.oper.model.dto.api.req.ReqUserCouponsOrderPrevDto;
import com.shq.oper.model.dto.api.req.ReqUserCouponsOrderUseDto;
import com.shq.oper.model.dto.api.req.ReqUserCouponsPackDto;
import com.shq.oper.model.dto.api.res.ResUserCouponsPackDataDto;
import com.shq.oper.util.BaseMapper;

@Mapper
public interface CouponsUserMapper extends BaseMapper<CouponsUser> {
	
	Page<CouponsUser> queryCouponsUserAndCoupons(CouponsUser entity);

	/**用户领取的优惠券**/
	Page<CouponsUser> queryByReciveCoupons(CouponsUser userReviceCoupons);

	/**用户领取的优惠券卡包列表 **/
	Page<ResUserCouponsPackDataDto> queryByQuickPurchase(ReqUserCouponsPackDto queryDto);
	
	/**  用户所有可用优惠券  **/
	Page<ResUserCouponsPackDataDto> queryBySeqAllUsable(ReqUserCouponsOrderPrevDto queryDto);

	/**订单提交锁定优惠券。状态变更 2：锁定中**/
	int updateByOrderUsedLock(ReqUserCouponsOrderUseDto dto);

	/**订单结算成功。状态变更 3：已使用**/
	int updateOrderByLockSuccess(ReqUserCouponsOrderLock dto);
	
	/**订单取消优惠券还原。状态变更 1：未使用**/
	int updateOrderByLockCannel(ReqUserCouponsOrderLock dto);

	/**定时任务，优惠券过期 状态4过期**/
	int updateCouponsStatusExpiredByJob(@Param("expiredDay")String expiredDay,@Param("couponsUserId")Long couponsUserId);
	
	int queryCouponsSellNum(Long couponsId);

	CouponsStatisticsDto queryCouponsUseStatistics(@Param("start") LocalDateTime start,
												   @Param("end") LocalDateTime end);

	Page<CouponsStatisticsDto> queryAllCouponsUseStatistics(ReqCouponsStatisticsDtoDto param);

	CouponsUser queryAndCouponsById(Long couponsUserId);

	int checkCouponsGetOrNotByUser(@Param("userSeq") String userSeq);

	/**设置优惠券过期 状态4过期**/
	int updateCouponsStatus(Long couponsUserId);

	/**
	 *获取赠送优惠券日统计
	 * @return
	 */
	Page<GiveCouponsDayStatisticsDto> queryGiveDayCouponsPage(@Param("beginTime")LocalDateTime beginTime,@Param("endTime")LocalDateTime endTime);

	/**
	 *获取赠送优惠券统计
	 * @return
	 */
	GiveCouponsStatisticsDto queryGiveCouponsStatic();

	/**
	 * 使用抵扣券
	 * @param dto
	 * @return
	 */
	int updateUserDeductible(CouponsUser dto);



//	/**
//	 * 获取赠送优惠券
//	 */
//	Page<ResUserCouponsPackDataDto> queryByGive(ReqUserCouponsPackDto queryDto);
}