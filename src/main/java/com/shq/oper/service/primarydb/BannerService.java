package com.shq.oper.service.primarydb;

import java.util.List;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.dto.PageDto;

public interface BannerService extends BaseService<Banner, Long>{

	Page<Banner> queryHomePageBanner(Banner entity, PageDto page);

	Page<Banner> queryAppAdvBanner(Banner entity);
	
	List<Banner> queryAppAdvBannerByApi(Banner entity);

	void enableBannerById(Banner banner);

}
