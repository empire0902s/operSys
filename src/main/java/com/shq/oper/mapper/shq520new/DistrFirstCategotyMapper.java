package com.shq.oper.mapper.shq520new;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.shq520new.DistrFirstCategoty;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DistrFirstCategotyMapper extends BaseMapper<DistrFirstCategoty> {
    Page<DistrFirstCategoty> queryDistrFirstCategotyList(DistrFirstCategoty distrFirstCategoty);
}