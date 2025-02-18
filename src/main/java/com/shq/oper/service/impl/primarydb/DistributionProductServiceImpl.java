package com.shq.oper.service.impl.primarydb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.salestatistics.ProductShelfStatusCountStatisticsDto;
import com.shq.oper.service.primarydb.DistributionProductService;
import com.shq.oper.util.Constant;

@Service("distributionProductService")
public class DistributionProductServiceImpl extends BaseServiceImpl<DistributionProduct, Long> implements DistributionProductService {

	@Autowired
	private DistributionProductMapper distributionProductMapper;

	@Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_SUPILER_STATISTICS +'--queryFinalProductShelfStatusCountStatistics--'", unless = "#result eq null")
	@Override
	public ProductShelfStatusCountStatisticsDto queryFinalProductShelfStatusCountStatistics() {
		return distributionProductMapper.queryFinalProductShelfStatusCountStatistics();
	}

}