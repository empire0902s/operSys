package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Activity;
import com.shq.oper.model.dto.api.req.ReqActivityListDataDto;
import com.shq.oper.model.dto.api.req.ReqBrandActivityListDataDto;
import com.shq.oper.model.dto.api.res.ResActivityListDataDto;
import com.shq.oper.model.dto.api.res.ResBrandActivityListDto;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

	Page<ResActivityListDataDto> queryListByApi(ReqActivityListDataDto apiDto);

	Page<ResBrandActivityListDto> queryBrandListByApi(ReqBrandActivityListDataDto apiDto);

	int updateBrandActice(Activity activity);
	Page<Activity> queryActivity(Activity activity);
	
	Page<ResActivityListDataDto> queryNewPeopleActivityListByApi(ReqActivityListDataDto apiDto);
}