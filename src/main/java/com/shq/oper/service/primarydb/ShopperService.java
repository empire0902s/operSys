package com.shq.oper.service.primarydb;

import java.util.Map;
import java.util.Set;

import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.dto.salestatistics.LoveOfHomeDataCountStatisticsDto;

public interface ShopperService extends BaseService<Shopper, Long> {

	Map<String, Shopper> queryShopperBySeqSet(Set<String> seqSet);

	LoveOfHomeDataCountStatisticsDto countLoveOfHomeUsersStatistics();
}