package com.shq.oper.service.impl.primarydb;

import com.github.pagehelper.Page;
import com.github.pagehelper.util.StringUtil;
import com.shq.oper.mapper.primarydb.MessageSendDetailMapper;
import com.shq.oper.mapper.primarydb.MessageSendMapper;
import com.shq.oper.model.domain.primarydb.MessageSendDetail;
import com.shq.oper.model.dto.api.message.ResMessageListDto;
import com.shq.oper.model.dto.api.message.ResposeDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.MessageSendDetailService;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;


import  org.springframework.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("messageSendDetailService")
public class MessageSendDetailServiceImpl extends BaseServiceImpl<MessageSendDetail, Long> implements MessageSendDetailService {

    @Autowired
    private MessageSendDetailMapper messageSendDetailMapper;

    @Autowired
    private ShqApi shqApi;


//    @Transactional
    @Override
    public void batchUpdateMessageStatus() {

        LocalDateTime localDateTime=LocalDateTime.now();
        LocalDateTime startTime= DateUtils.addDaysFormatStart(localDateTime,-1);
        LocalDateTime endTime=DateUtils.addDaysFormatEnd(localDateTime,0);
        MessageSendDetail messageSendDetail=new MessageSendDetail();
        messageSendDetail.setSendStatus((short)0);
        messageSendDetail.setStartTime(startTime);
        messageSendDetail.setEndTime(endTime);

        int cout=messageSendDetailMapper.selectCout(messageSendDetail);
        log.debug("----短信发送中记录条目数："+cout);
        while (cout>=2000){
            Page<MessageSendDetail> pageResult=messageSendDetailMapper.queryMessage(messageSendDetail);
            if (pageResult==null||pageResult.size()<=0){
                cout=-2000;
                continue;
            }
            //获取返回list
            List<ResMessageListDto> list = getResMessageListDtos(pageResult);
            //批量更新
            if (list==null||list.size()<=0){
                cout=-2000;
                continue;
            }
            batchUpdateMessage(list);

            cout=-2000;
        }

        if (cout>0){
            Page<MessageSendDetail> pageResult=messageSendDetailMapper.queryMessage(messageSendDetail);
            if (pageResult==null||pageResult.size()<=0){
                return;
            }
            //获取返回list
            List<ResMessageListDto> list = getResMessageListDtos(pageResult);
            if (list==null||list.size()<=0){
                return;
            }
            //批量更新
            batchUpdateMessage(list);
        }
    }

    @Override
    public void batchUpdateHistoryMessageStatus() {

        LocalDateTime localDateTime=LocalDateTime.now();
        LocalDateTime endTime= DateUtils.addDaysFormatStart(localDateTime,-2);
        MessageSendDetail messageSendDetail=new MessageSendDetail();
        messageSendDetail.setSendStatus((short)0);
        messageSendDetail.setEndTime(endTime);
        messageSendDetail.setOffset(0);

        int cout=messageSendDetailMapper.selectHistoryCout(messageSendDetail);
        log.debug("----短信发送中记录条目数："+cout);
        while (cout>=2000){
            Page<MessageSendDetail> pageResult=messageSendDetailMapper.queryHistoryMessage(messageSendDetail);
            messageSendDetail.setOffset(messageSendDetail.getOffset()+2000);
            if (pageResult==null||pageResult.size()<=0){
                cout=-2000;
                continue;
            }
            //获取返回list
            List<ResMessageListDto> list = getResMessageListDtos(pageResult);
            //批量更新
            if (list==null||list.size()<=0){
                cout=-2000;
                continue;
            }
            batchUpdateHistoryMessage(list);

            cout=-2000;
        }

        if (cout>0){
            Page<MessageSendDetail> pageResult=messageSendDetailMapper.queryHistoryMessage(messageSendDetail);
            if (pageResult==null||pageResult.size()<=0){
                return;
            }
            //获取返回list
            List<ResMessageListDto> list = getResMessageListDtos(pageResult);
            if (list==null||list.size()<=0){
                return;
            }
            //批量更新
            batchUpdateHistoryMessage(list);
        }

    }

    private List<ResMessageListDto> getResMessageListDtos(Page<MessageSendDetail> pageResult) {
        List<String> msgids=new ArrayList<>();
        pageResult.forEach(
                p->{
                    msgids.add(p.getMsgId()+"");
                }
        );

        String body=String.format("{ \"idList\":%s}",JsonUtils.toDefaultJson(msgids));
        String str=HttpClientUtil.doPostJson(shqApi.getGetMessageListUrl(),body);
        Assert.notNull(str,"请求发送记录异常");
        ResposeDto resposeDto=JsonUtils.parse(str,ResposeDto.class);
        return resposeDto.getData();
    }

    private void batchUpdateMessage(List<ResMessageListDto> list) {
        List<MessageSendDetail> updateList=new ArrayList<>();
        list.forEach(
                p->{
                    MessageSendDetail messageSendDetail1=new MessageSendDetail();

                    messageSendDetail1.setMsgId(p.getMsgId());
                    if (StringUtil.isNotEmpty(p.getSendTime())){
                        messageSendDetail1.setFinishTime(DateUtils.getDateTimeOfTimestamp(Long.parseLong(p.getSendTime())));
                    }
                    if ("1".equals( p.getStatus().trim())|| "2".equals(p.getStatus().trim())){
                        messageSendDetail1.setSendStatus((short) 0);
                    }
                    if ("0".equals(p.getStatus().trim())||"3".equals(p.getStatus().trim())||"5".equals(p.getStatus().trim())){
                        messageSendDetail1.setSendStatus((short) 2);
                    }
                    if ( "4".equals(p.getStatus().trim())){
                        messageSendDetail1.setSendStatus((short) 1);
                    }
                    if ( "6".equals(p.getStatus().trim())){
                        messageSendDetail1.setSendStatus((short) 3);
                    }
                    if (null==messageSendDetail1.getSendStatus()){
                        messageSendDetail1.setSendStatus((short)2);
                    }
                    if (null!=p.getStatus()){
                        messageSendDetail1.setMessageReturnType(p.getStatus());
                    }
                    messageSendDetail1.setRemark(p.getRemark());
                    updateList.add(messageSendDetail1);
                }
        );
        if (updateList.size()>0){
        	log.debug("----更新记录条目数："+updateList.size());
            messageSendDetailMapper.batchUpdate(updateList);
        }

    }

    private void batchUpdateHistoryMessage(List<ResMessageListDto> list) {
        List<MessageSendDetail> updateList=new ArrayList<>();
        list.forEach(
                p->{
                    MessageSendDetail messageSendDetail1=new MessageSendDetail();

                    messageSendDetail1.setMsgId(p.getMsgId());
                    if (StringUtil.isNotEmpty(p.getSendTime())){
                        messageSendDetail1.setFinishTime(DateUtils.getDateTimeOfTimestamp(Long.parseLong(p.getSendTime())));
                    }
                    if ("0".equals(p.getStatus().trim())||"3".equals(p.getStatus().trim())||"5".equals(p.getStatus().trim())){
                        messageSendDetail1.setSendStatus((short) 2);
                    }
                    if ( "4".equals(p.getStatus().trim())){
                        messageSendDetail1.setSendStatus((short) 1);
                    }
                    if ( "6".equals(p.getStatus().trim())){
                        messageSendDetail1.setSendStatus((short) 3);
                    }

                    //历史数据，两天后还是返回发送中，直接设置失败
                    if ("1".equals( p.getStatus().trim())|| "2".equals(p.getStatus().trim())){
                        messageSendDetail1.setSendStatus((short) 2);
                    }
                    if (null==messageSendDetail1.getSendStatus()){
                        messageSendDetail1.setSendStatus((short)2);
                    }
                    if (null!=p.getStatus()){
                        messageSendDetail1.setMessageReturnType(p.getStatus());
                    }
                    messageSendDetail1.setRemark(p.getRemark());
                    updateList.add(messageSendDetail1);
                }
        );
        if (updateList.size()>0){
            log.debug("----更新记录条目数："+updateList.size());
            messageSendDetailMapper.batchUpdate(updateList);
        }

    }
}