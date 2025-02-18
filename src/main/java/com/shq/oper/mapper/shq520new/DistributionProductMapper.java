package com.shq.oper.mapper.shq520new;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.salestatistics.ProductShelfStatusCountStatisticsDto;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributionProductMapper extends BaseMapper<DistributionProduct> {
	Page<DistributionProduct> queryDiatributionProductByCodeList(List<String> codeList);

	Page<DistributionProduct> queryDiatributionProductByProductName(@Param("productName") String productName);

	ProductShelfStatusCountStatisticsDto queryFinalProductShelfStatusCountStatistics();
	
	Page<DistributionProduct> queryDiatributionProductByCodeListAndGoodsName(@Param("list") List<String> list,@Param("productName")String productName);

}