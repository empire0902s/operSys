package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.Banner;
import com.shq.oper.model.dto.Msg;

public interface BannerAppAdvService extends BaseService<Banner, Long>  {

	Msg<String> saveAdvAppBanner(Banner banner);

	Msg<String> dataInitialization(String createOrUpdateAdminName);
}