package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.MessageSend;
import com.shq.oper.model.domain.primarydb.MessageSendDetail;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageSendMapper extends BaseMapper<MessageSend> {
    Page<MessageSend> queryPageSort(MessageSend messageSend);

}
