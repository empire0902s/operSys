package com.shq.oper.mapper.primarydb;

import com.shq.oper.model.domain.mongo.DeducitbleStatisticsDay;
import com.shq.oper.model.domain.primarydb.deductible.Deductible;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeductibleMapper extends BaseMapper<Deductible> {

    /**
     * 使用抵扣券
     * @param dto
     * @return
     */
    int updateUserDeductible(Deductible dto);

    Deductible selectDeductible(@Param("userSeq")String userSeq );

    Deductible queryDeductible(@Param("userSeq")String userSeq );

    /**
     * 定时器定期过期
     * @param expiredDay
     * @return
     */
    int updateDetuctibleStatusExpiredByJob(@Param("expiredDay")String expiredDay);

    /**
     *
     * @param
     * @return
     */
    DeducitbleStatisticsDay getDeducitbleStatisticsDay(@Param("beTime")String beTime,
                                                       @Param("endTime")String endTime,
                                                       @Param("type")Integer type);



}