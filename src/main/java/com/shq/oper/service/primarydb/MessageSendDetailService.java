package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.MessageSendDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


public interface MessageSendDetailService extends BaseService<MessageSendDetail, Long> {

    void batchUpdateMessageStatus();
    void batchUpdateHistoryMessageStatus();

}
