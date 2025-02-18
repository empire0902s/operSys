package com.shq.oper.service.impl.primarydb;

import com.shq.oper.mapper.primarydb.AdminMapper;
import com.shq.oper.mapper.primarydb.AdminRoleMapper;
import com.shq.oper.model.domain.primarydb.Admin;
import com.shq.oper.model.domain.primarydb.AdminRole;
import com.shq.oper.service.primarydb.AdminRoleService;
import com.shq.oper.util.BaseMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adminRoleService")
@Transactional
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRole, Long> implements AdminRoleService {

	@Autowired
	private AdminRoleMapper adminRoleMapper;
	@Autowired
	private AdminMapper adminMapper;

	@Override
	@Transactional
	public void clear(Admin admin) {
		AdminRole adminRole = new AdminRole();
		adminRole.setAdminId(admin.getId());
		adminRoleMapper.delete(adminRole);
	}

	@Override
	@Transactional
	public void insertList(List<AdminRole> adminRoles) {
		adminRoleMapper.insertList(adminRoles);
	}

	@Override
	@Transactional
	public boolean updateAdminAndRole(List<AdminRole> adminRoles, Admin admin)throws Exception {
		// 清空该admin与role的关联关系
			AdminRole adminRole = new AdminRole();
			adminRole.setAdminId(admin.getId());
			adminRoleMapper.delete(adminRole);
			// 批量插入数据
			adminRoleMapper.insertList(adminRoles);
			return true;
	}
	@Transactional
	public void deleteRelationOfadminAndRole(Admin admin)throws Exception {
		// 清空该admin与role的关联关系
			AdminRole adminRole = new AdminRole();
			adminRole.setAdminId(admin.getId());
			adminRoleMapper.delete(adminRole);
	}
}