package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.BannerModelGoodsDetail;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BannerModelGoodsDetailMapper extends BaseMapper<BannerModelGoodsDetail> {
	Page<BannerModelGoodsDetail> queryBannerModelGoodsDetail(BannerModelGoodsDetail param);
}