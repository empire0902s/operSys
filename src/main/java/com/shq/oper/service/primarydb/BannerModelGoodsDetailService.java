package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.BannerModelGoodsDetail;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;

public interface BannerModelGoodsDetailService extends BaseService<BannerModelGoodsDetail, Long> {

	Msg<String> saveBannerModelGoodsDetail(BannerModelGoodsDetail bannerModelGoodsDetail,
			DistributionProduct distributionProduct);

	Data<BannerModelGoodsDetail> queryList(BannerModelGoodsDetail entity, PageDto page);
}