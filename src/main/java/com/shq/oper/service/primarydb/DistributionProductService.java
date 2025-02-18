package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.salestatistics.ProductShelfStatusCountStatisticsDto;

public interface DistributionProductService extends BaseService<DistributionProduct, Long> {

	ProductShelfStatusCountStatisticsDto queryFinalProductShelfStatusCountStatistics();
}