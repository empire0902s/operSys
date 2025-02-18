package com.shq.oper.service.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.ticket.FieldArea;

public interface FieldAreaService extends BaseService<FieldArea, Long> {

	Page<FieldArea> queryFieldAreaList(FieldArea entity);
}