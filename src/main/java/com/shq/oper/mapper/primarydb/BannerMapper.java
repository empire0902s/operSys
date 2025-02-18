package com.shq.oper.mapper.primarydb;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.util.BaseMapper;

@Mapper
public interface BannerMapper extends BaseMapper<Banner> {
	Page<Banner> queryHomePageBanner(Banner banner);
	Page<Banner> queryAppAdvBanner(Banner banner);
	void updateBannerStatusToDisabled(String type);
	
}
