package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.BaiduPoi;
import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduceDetail;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeductibleIntroduceDetailMapper extends BaseMapper<DeductibleIntroduceDetail> {


    Page<DeductibleIntroduceDetail> selectDeductibleDetailList(@Param("introduceId")Long introduceId);

}
