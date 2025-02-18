package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Circle;
import com.shq.oper.model.dto.api.req.ReqCircleDataDto;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CircleMapper extends BaseMapper<Circle> {

    Page<Circle> queryPageSort(Circle Circle);

    //更新点击量，次数，查看数
    int updateSetCircle(ReqCircleDataDto Circle);

}
