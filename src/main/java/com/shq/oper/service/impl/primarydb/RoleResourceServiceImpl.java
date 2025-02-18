package com.shq.oper.service.impl.primarydb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.shq.oper.mapper.primarydb.RoleResourceMapper;
import com.shq.oper.model.domain.primarydb.RoleResource;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.service.primarydb.RoleResourceService;

@Service("roleResourceService")
public class RoleResourceServiceImpl extends BaseServiceImpl<RoleResource, Long> implements RoleResourceService {
	

	@Autowired
	private RoleResourceMapper roleResourceMapper;

	@Transactional
	@Override
	public Msg<String> saveRoleAndRecource(RoleResource entity) {
		// 先删除角色在某个系统下的资源，再重新插入
		Long roleId = entity.getRoleId();
		RoleResource record = new RoleResource();
		record.setRoleId(roleId);
		roleResourceMapper.delete(record);
		
		//批量插入数据
		if (!CollectionUtils.isEmpty(entity.getResourceIdList())) {
			List<RoleResource> roleResources = new ArrayList<RoleResource>();
			for (Long resourceId : entity.getResourceIdList()) {
				RoleResource tmp = new RoleResource();
				tmp.setRoleId(roleId);
				tmp.setResourceId(resourceId);
				tmp.setCreateTime(entity.getCreateTime());
				tmp.setCreateAdmin(entity.getCreateAdmin());
				roleResources.add(tmp);
			}
			roleResourceMapper.insertList(roleResources);
		}
		return Msg.ok();
	}

	
}