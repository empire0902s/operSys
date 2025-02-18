package com.shq.oper.service.primarydb;

import java.util.List;

import com.shq.oper.model.domain.primarydb.Admin;
import com.shq.oper.model.domain.primarydb.AdminRole;

public interface AdminRoleService extends BaseService<AdminRole, Long> {

	void clear(Admin admin);

	void insertList(List<AdminRole> adminRoles);

	boolean updateAdminAndRole(List<AdminRole> adminRoles, Admin admin) throws Exception;
	void deleteRelationOfadminAndRole(Admin admin)throws Exception;
}