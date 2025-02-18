package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.RoleResource;
import com.shq.oper.model.dto.Msg;

public interface RoleResourceService extends BaseService<RoleResource, Long> {

	public Msg<String> saveRoleAndRecource(RoleResource roleResource);

	
}
