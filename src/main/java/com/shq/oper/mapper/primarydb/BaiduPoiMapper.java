package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.BaiduPoi;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaiduPoiMapper extends BaseMapper<BaiduPoi> {

    Page<BaiduPoi> queryPageSort (BaiduPoi baiduPoi);
}
