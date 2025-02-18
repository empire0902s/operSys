package com.shq.oper.mapper.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.MessageSendDetail;
import com.shq.oper.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageSendDetailMapper extends BaseMapper<MessageSendDetail> {

    Page<MessageSendDetail> queryPageSort(MessageSendDetail messageSendDetail);

    Page<MessageSendDetail> queryMessage(MessageSendDetail messageSendDetail);

    Page<MessageSendDetail> queryHistoryMessage(MessageSendDetail messageSendDetail);

    int selectCout(MessageSendDetail messageSendDetail);

    int selectHistoryCout(MessageSendDetail messageSendDetail);

    int batchUpdate(List<MessageSendDetail> list);

}
