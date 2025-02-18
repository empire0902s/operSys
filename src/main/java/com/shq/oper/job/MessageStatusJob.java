package com.shq.oper.job;

import com.shq.oper.model.dto.Msg;
import com.shq.oper.service.primarydb.CouponsUserService;
import com.shq.oper.service.primarydb.MessageSendDetailService;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.DateUtils.DateFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@EnableScheduling
public class MessageStatusJob {

	@Autowired
	private MessageSendDetailService messageSendDetailService;
	
	@Scheduled(cron = "0 0/5 * * * ?")
	public void runByMin() {
		log.debug("-----------------变更信息状态开始----------"+DateUtils.now());
		messageSendDetailService.batchUpdateMessageStatus();
		log.debug("-----------------变更信息状态结束----------"+DateUtils.now());
	}
	

}
