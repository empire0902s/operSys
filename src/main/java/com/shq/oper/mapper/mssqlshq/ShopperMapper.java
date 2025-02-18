package com.shq.oper.mapper.mssqlshq;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.dto.salestatistics.CommodityOrderStatisticsDto;
import com.shq.oper.model.dto.salestatistics.LoveOfHomeDataCountStatisticsDto;
import com.shq.oper.util.BaseMapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface ShopperMapper extends BaseMapper<Shopper> {
	Shopper queryBySeqOrMobile(Shopper shopper);
	
	/**
	 * 供应商查询
	 * <br>支持shopName和SEQ 唯一查询
	 * @param shopper
	 * @return
	 */
	Shopper queryBySupplier(Shopper shopper);
	
	Page<Shopper> queryBy(Shopper shopper);

	Page<Shopper> queryByShopper(Shopper shopper);

	Page<Shopper> queryShopper(Shopper shopper);

	Page<Shopper> queryRoleShopper(Shopper shopper);

	Page<Shopper> queryAereShopper(Shopper shopper);

	Page<Shopper> queryShopperSeqs(List<Integer> seqs);
	Page<Shopper> queryShopperphones (List<String> phones);

	List<Shopper> queryShopperBySeqSet(Set<String> seqs);
	
	LoveOfHomeDataCountStatisticsDto countLoveOfHomeUsersStatistics();
	//统计爱之家每日新增注册人数
	List<CommodityOrderStatisticsDto> queryHomeOfLoveUserStatisticsData(CommodityOrderStatisticsDto entity);
	


}