package com.shq.oper.service.impl.primarydb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.primarydb.FieldAreaMapper;
import com.shq.oper.model.domain.primarydb.ticket.FieldArea;
import com.shq.oper.service.primarydb.FieldAreaService;

@Service("fieldAreaService")
public class FieldAreaServiceImpl  extends BaseServiceImpl<FieldArea, Long> implements FieldAreaService {

	@Autowired
	private FieldAreaMapper fieldAreaMapper;
	
	@Override
	public Page<FieldArea> queryFieldAreaList(FieldArea entity) {
		return fieldAreaMapper.queryFieldAreaList(entity);
	}


}
