package com.shq.oper.mapper.shq520new;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.shq520new.DistrFourCategoty;
import com.shq.oper.util.BaseMapper;

public interface DistrFourCategotyMapper extends BaseMapper<DistrFourCategoty> {

	Page<DistrFourCategoty> queryByPage(DistrFourCategoty shop);
	Page<DistrFourCategoty> queryDistrFourCategotyList(DistrFourCategoty distrFourCategoty);
}