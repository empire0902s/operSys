package com.shq.oper.mapper.shq520new;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.shq520new.DistrThirdCategoty;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistrThirdCategotyMapper extends BaseMapper<DistrThirdCategoty> {
    Page<DistrThirdCategoty> queryDistrThirdCategotyList(DistrThirdCategoty distrThirdCategoty);
}