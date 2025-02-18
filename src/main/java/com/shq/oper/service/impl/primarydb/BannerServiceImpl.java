package com.shq.oper.service.impl.primarydb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.shq.oper.enums.BannerTypeEnum;
import com.shq.oper.mapper.primarydb.BannerMapper;
import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.BannerService;
import com.shq.oper.util.Constant;

@Service("bannerService")
public class BannerServiceImpl extends BaseServiceImpl<Banner, Long> implements BannerService {

	@Autowired
	private BannerMapper bannerMapper;
	@Override
	public Page<Banner> queryHomePageBanner(Banner entity,PageDto page) {
		com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
		return bannerMapper.queryHomePageBanner(entity);
	}
	@Override
	public Page<Banner> queryAppAdvBanner(Banner entity) {
		return bannerMapper.queryAppAdvBanner(entity);
	}
	@Override
	public void enableBannerById(Banner banner) {
		//将app启动页广告的状态全部设置为禁用
		bannerMapper.updateBannerStatusToDisabled(BannerTypeEnum.ADVAPP.getCode());
		//修改状态为启用状态
		banner.setIsDisabled(false);
		bannerMapper.updateByPrimaryKeySelective(banner);
	}
	
	@Cacheable(value = Constant.CACHEKEY_SECOND_15, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_BANNER_APP_ADV_API +#entity", unless = "#result eq null")
	@Override
	public List<Banner> queryAppAdvBannerByApi(Banner entity){
		return bannerMapper.select(entity);
	}

}
