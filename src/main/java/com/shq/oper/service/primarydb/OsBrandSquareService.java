package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.PubliceNum;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.orderingsystem.OSBrasndSquareDto;

public interface OsBrandSquareService  {

	Data<OSBrasndSquareDto> queryList(OSBrasndSquareDto entity, PageDto page);
}