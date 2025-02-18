package com.shq.oper.service.impl.primarydb;

import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.dto.salestatistics.LoveOfHomeDataCountStatisticsDto;
import com.shq.oper.service.primarydb.ShopperService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("shopperService")
public class ShopperServiceImpl extends BaseServiceImpl<Shopper, Long> implements ShopperService {

	@Autowired
	private ShopperMapper shopperMapper;

	@Override
	public Map<String, Shopper> queryShopperBySeqSet(Set<String> seqSet) {
		List<Shopper> resultList = shopperMapper.queryShopperBySeqSet(seqSet);
		Map<String, Shopper> map = new HashMap<>(0);
		if (resultList.size() > 0) {
			for (Shopper shopper : resultList) {
				map.put(shopper.getSeq().toString(), shopper);
			}
		}
		return map;
	}

	@Override
	public LoveOfHomeDataCountStatisticsDto countLoveOfHomeUsersStatistics() {
		return shopperMapper.countLoveOfHomeUsersStatistics();
	}
}