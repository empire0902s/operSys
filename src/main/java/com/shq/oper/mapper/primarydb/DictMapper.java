package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.util.BaseMapper;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {
	Page<Dict> queryLikeDict(Dict dict);

	List<Dict> queryIndexBannerOfDict();
	void updateDict(Dict dict);

}
