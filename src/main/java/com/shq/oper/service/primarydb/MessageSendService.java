package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.MessageSend;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.BaseRequestParamDto;

public interface MessageSendService extends BaseService<MessageSend, Long> {

    Msg<String> sendMessage(MessageSend messageSend);

    Msg<String> sendMessageByApi(BaseRequestParamDto dto);

	Msg<String> sendCheckCode(String mobile);

    Msg<String> pushMessageByApi(BaseRequestParamDto dto);

}
