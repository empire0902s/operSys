package com.shq.oper.service.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.PubliceNum;
import com.shq.oper.model.dto.Msg;

public interface PubliceNumService extends BaseService<PubliceNum, Long> {

	Page<PubliceNum> queryAllPubliceNum(PubliceNum entity);

	Msg<String> enableAndDisbled(PubliceNum entity);
	
}