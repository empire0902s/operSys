package com.shq.oper.service.impl.primarydb;

import com.shq.oper.model.domain.primarydb.Role;
import com.shq.oper.service.primarydb.RoleService;

import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {
}