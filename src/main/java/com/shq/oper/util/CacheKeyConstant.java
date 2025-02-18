package com.shq.oper.util;

/**
 * 缓存Key常量
 * 
 *
 * <br><font color='red' ><B>
 * 
 * <br>注：	本类添加的key 需要再 AdminServiceImpl-clearCacheByConstantKey方法 维护
 * 
 * </B></font>
 */
public class CacheKeyConstant {
	/**Key Set**/
	public final static String BASE_OPERSYS_KEY_SET = "OPERSYS_";
	
	/**活动数据缓存**/
	public final static String BASE_ACTIVITY_QUERY = "Activity-query-";

	/**用户优惠券**/
	public final static String BASE_COUPONSUSER_USERSEQ = "CouponsUser-userSeq-";

	/**优惠券信息缓存**/
	public final static String BASE_COUPONS_QUERY = "Coupons-query-";
	
	/**活动商品详情规则**/
	public final static String BASE_GOODS_RULE_QUERY = "GoodsRule-query-";
	

	/**订单统计**/
	public final static String BASE_ORDER_STATISTICS = "order_statistics-query-";
	/**供应商统计**/
	public final static String BASE_SUPILER_STATISTICS = "supiler_statistics-query-";

	/**app广告启动页**/
	public final static String BASE_BANNER_APP_ADV_API = "bannerAppAdv-query-";
	
	/**栏目商品**/
	public final static String BASE_CLOUMN_GOODS_API = "cloumn-goods-query-";
	
	/**订单评价**/
	public final static String BASE_ORDER_EVALUATE = "orderEvaluate-query-";

	/**
	 * 发圈列表
	 */
	public final static String BASE_CIRCLE_LIST="circle-query-list-";

    /**用户抵扣券**/
    public final static String BASE_DEDUCTIBLE_USERSEQ = "deductible-userSeq-";

	/**
	 *代金券使用金额
	 */
	public final static String BASE_DEDUCTIBLE_USEDBALANCE_1 = "deductible-used-balance-1";

	/**
	 *抵扣券使用金额
	 */
	public final static String BASE_DEDUCTIBLE_USEDBALANCE_2 = "deductible-used-balance-2";

	/**
	 *抵扣券使用用户seq
	 */
	public final static String BASE_DEDUCTIBLE_USERSEQ_LIST_2 = "deductible_key_userseq_list_2";

	/**
	 *代金券使用用户seq
	 */
	public final static String BASE_DEDUCTIBLE_USERSEQ_LIST_1 = "deductible_key_userseq_list_1";

	/**
	 * 领取抵扣券seq
	 */
	public final static String BASE_DEDUCTIBLE_SEQ_LIST = "deductible_seq_list";

	/**
	 * 取消订单key+订单号
	 */
	public final static String BASE_ORDER_RETURN = "deductible-order-return-";


}
