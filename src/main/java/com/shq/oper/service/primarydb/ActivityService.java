package com.shq.oper.service.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Activity;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.req.ReqActivityListDataDto;
import com.shq.oper.model.dto.api.req.ReqBrandActivityAddDto;
import com.shq.oper.model.dto.api.req.ReqBrandActivityListDataDto;
import com.shq.oper.model.dto.api.res.ResActivityListDataDto;
import com.shq.oper.model.dto.api.res.ResBrandActivityListDto;
import com.shq.oper.model.dto.api.res.ResCouponsReturnDto;

public interface ActivityService extends BaseService<Activity, Long> {
	
	public Activity queryByBatch(String actBatch);
	
	public Page<ResActivityListDataDto> queryListByApi(ReqActivityListDataDto apiDto, PageDto pageDto);

	Msg<String> disabled(Activity activity);

	public Msg<String> addBrandActivityByApi(ReqBrandActivityAddDto dto);

	Msg<ResCouponsReturnDto> addBrandActivityCouponTypeByApi(ReqBrandActivityAddDto dto);

	Page<ResBrandActivityListDto> queryBrandListByApi(ReqBrandActivityListDataDto apiDto, PageDto pageDto);

	Msg<String> updateBrandActivity(Activity activity);

	Page<Activity> queryActivity(Activity activity);

	public Page<ResActivityListDataDto> queryNewPeopleActivityListByApi(ReqActivityListDataDto dto_1011,
			PageDto initQueryPageDtp);
	
}