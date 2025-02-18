package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.BaiduPoi;
import com.shq.oper.model.dto.Msg;

public interface BaiduPoiService extends BaseService<BaiduPoi, Long> {

    Msg<String> saveBaiduPoi(BaiduPoi baiduPoi);

    Msg<String> deleteBaiduPoi(BaiduPoi baiduPoi);

    Msg<String> updateBaiduPoi(BaiduPoi baiduPoi);
}
