package com.shq.oper.mapper.primarydb;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.CouponsTypeRule;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface CouponsTypeRuleMapper extends BaseMapper<CouponsTypeRule> {
	Page<CouponsTypeRule> selectTOCouponsTypeRuleAll(CouponsTypeRule couponsTypeRule);

	Page<CouponsTypeRule> selectToModel(CouponsTypeRule couponsTypeRule);

	Page<CouponsTypeRule> selectToType(CouponsTypeRule couponsTypeRule);

	int delToCouponsTypeRule(int id);

	int updToCouponsTypeRule(int id);

	Long selectToMaxId();

	Page<CouponsTypeRule> queryLikeCouponsTypeRule(CouponsTypeRule entity);

}