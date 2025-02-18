package com.shq.oper.mapper.shq520new;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.shq520new.DistrSecondCategoty;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistrSecondCategotyMapper extends BaseMapper<DistrSecondCategoty> {
    Page<DistrSecondCategoty> queryDistrSecondCategotyList(DistrSecondCategoty distrSecondCategoty);
}