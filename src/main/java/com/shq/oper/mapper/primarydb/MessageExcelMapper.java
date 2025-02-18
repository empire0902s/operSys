package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.MessageExcel;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageExcelMapper extends BaseMapper<MessageExcel> {

    int updateBath(List<MessageExcel> list);

    Page<MessageExcel> queryPageSort (MessageExcel messageExcel);

}
