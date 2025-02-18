package com.shq.oper.service.primarydb;

import com.github.pagehelper.PageInfo;
import com.shq.oper.model.domain.primarydb.Admin;
import com.shq.oper.model.dto.AdminDto;
import com.shq.oper.model.dto.Msg;

public interface AdminService extends BaseService<Admin, Long> {

	Msg<String> clearCacheByConstantKey(String key);
	Admin queryAdminByLogin(String name);
	AdminDto queryResourceByAdmin(Admin entity);
	
}