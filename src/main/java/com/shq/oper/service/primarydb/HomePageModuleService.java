package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.mongo.HomePageModule;
import com.shq.oper.model.dto.Msg;

public interface HomePageModuleService {

	Msg<String> save(HomePageModule homePageModule);

	
}