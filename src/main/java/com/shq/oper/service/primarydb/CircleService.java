package com.shq.oper.service.primarydb;


import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Circle;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.req.ReqCircleDataDto;

public interface CircleService extends BaseService<Circle, Long>{


    Msg<String>  updateSetCircle(ReqCircleDataDto circle);

    Page<Circle> selectCirclePage(Circle circle,PageDto page);
	
}
