package com.shq.oper.service.impl.primarydb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.github.pagehelper.util.StringUtil;
import com.shq.oper.enums.MessagePlamEnum;
import com.shq.oper.enums.api.CouponsFromSysEnums;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.MessageSendDetailMapper;
import com.shq.oper.mapper.primarydb.MessageSendMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.model.domain.primarydb.MessageSend;
import com.shq.oper.model.domain.primarydb.MessageSendDetail;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.BaseRequestParamDto;
import com.shq.oper.model.dto.api.message.AppMessageDto;
import com.shq.oper.model.dto.api.message.SendMessageParamDto;
import com.shq.oper.model.dto.api.req.ReqSendMessageDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.service.primarydb.DictService;
import com.shq.oper.service.primarydb.MessageSendService;
import com.shq.oper.service.primarydb.RedisService;
import com.shq.oper.util.Constant;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("messageSendService")
public class MessageSendServiceImpl extends BaseServiceImpl<MessageSend, Long> implements MessageSendService {

    @Autowired
    private ShopperMapper shopperMapper;

    @Autowired
    private MessageSendDetailMapper messageSendDetailMapper;

    @Autowired
    private MessageSendMapper messageSendMapper;

    @Autowired
    private DictService dictService;
    
    @Autowired
    private RedisService redisService;
    
	@Autowired
	SystemProperties systemProperties;

    @Autowired
    private ShqApi shqApi;

    /**
     *@author ltg
     *@date 2018/7/5 18:13
     * @params:[messageSend]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @Override
    @Transactional
    public Msg<String> sendMessage(MessageSend messageSend) {

        List<MessageSendDetail> detailList=new ArrayList<>();

        String appStr=HttpClientUtil.doGet(shqApi.getGetAppUrl());
        if(StringUtils.isEmpty(appStr)) {
        	return Msg.error("消息推送接口["+shqApi.getGetAppUrl()+"]异常.");
        }
        appStr=appStr.substring(appStr.indexOf("[")+1,appStr.indexOf("]"));
        AppMessageDto appMessageDto=JsonUtils.parse(appStr,AppMessageDto.class);

        Dict dictParam=new Dict();
        dictParam.setCode("messageParam");
        List<Dict> dictList=dictService.select(dictParam);
//        Map<String,String> keyMap=new HashMap<>();
        Map<String,String> valueMap=new HashMap<>();
        dictList.forEach(
                p->{
//                    keyMap.put(p.getDictKey(),p.getDictValue());
                    valueMap.put(p.getDictValue(),p.getName());
                }
        );

        int messageNum=0;

        if (messageSend.getReceiveType()==1){

            List<Integer> list=new ArrayList<>();
            String[] seqs=messageSend.getSeqs().split(",");
            List<Shopper> shopperlist=null;
            if (seqs.length>=1){
               for(int i=0;i<seqs.length;i++){
                   list.add(Integer.parseInt(seqs[i]));
               }
                shopperlist=shopperMapper.queryShopperSeqs(list);
            }else {
                return Msg.error("请添加明细");
            }
            messageNum=shopperlist.size();
            sendMessageByList(messageSend, appMessageDto, shopperlist,detailList,valueMap);
        }

        if (messageSend.getReceiveType()==2){
            Shopper shopperParam=new Shopper();
            shopperParam.setUsertype(messageSend.getSeqs());
            List<Shopper> list=shopperMapper.select(shopperParam);
            messageNum=list.size();
            sendMessageByList(messageSend, appMessageDto, list,detailList,valueMap);
        }

        if (messageSend.getReceiveType()==3){
            Shopper shopperParam=new Shopper();
//            if (messageSend.getSeqs())
            shopperParam.setArea(messageSend.getSeqs());
            List<Shopper> list=shopperMapper.select(shopperParam);
            messageNum=list.size();
            sendMessageByList(messageSend, appMessageDto, list,detailList,valueMap);
        }


        if (messageSend.getReceiveType()==4){
            sendPushByAllPlat(messageSend,appMessageDto,detailList);
            messageNum=0;
        }


        messageSend.setMessageNum(messageNum);
        messageSendMapper.insert(messageSend);
        //保存明细
        detailList.forEach(
                p->{
                    p.setMsgSendId(messageSend.getId());
                }
        );
        insertMessageDetailList(detailList);
        return Msg.ok("发送成功");
    }

    @Transactional
    @Override
    public Msg<String> sendMessageByApi(BaseRequestParamDto dto) {
        ReqSendMessageDto reqSendMessageDto=JsonUtils.parse(JsonUtils.toDefaultJson(dto.getRequestData()),ReqSendMessageDto.class);

        Msg<String> x =verificatData(reqSendMessageDto);
        if (x != null) return x;

        Dict dictParam=new Dict();
        dictParam.setCode("messageParam");
        List<Dict> dictList=dictService.select(dictParam);
        Map<String,String> keyMap=new HashMap<>();
        Map<String,String> valueMap=new HashMap<>();
        dictList.forEach(
                p->{
                    keyMap.put(p.getDictKey(),p.getDictValue());
                    valueMap.put(p.getDictValue(),p.getName());
                }
        );

        MessageSend messageSend = getMessageSend(dto, reqSendMessageDto,keyMap);

        String appStr=HttpClientUtil.doGet(shqApi.getGetAppUrl());
        if(StringUtils.isEmpty(appStr)) {
            return Msg.error("消息推送接口["+shqApi.getGetAppUrl()+"]异常.");
        }
        appStr=appStr.substring(appStr.indexOf("[")+1,appStr.indexOf("]"));
        AppMessageDto appMessageDto=JsonUtils.parse(appStr,AppMessageDto.class);

        List<Shopper> shopperList=shopperMapper.queryShopperphones( reqSendMessageDto.getMobiles());

        List<MessageSendDetail> detailList=new ArrayList<>();
        sendMessageListByApi(shopperList,messageSend, appMessageDto, reqSendMessageDto.getMobiles(),detailList,valueMap);

        messageSendMapper.insert(messageSend);
        //保存明细
        detailList.forEach(
                p->{
                    p.setMsgSendId(messageSend.getId());
                }
        );
        insertMessageDetailList(detailList);
        return Msg.ok("发送成功");
    }

    private Msg<String> verificatData(ReqSendMessageDto reqSendMessageDto) {
        if (StringUtils.isEmpty(reqSendMessageDto.getMobiles())||reqSendMessageDto.getMobiles().size()<=0){
            return Msg.error("请输入手机号码");
        }
        if (StringUtil.isEmpty(reqSendMessageDto.getFromSys())){
            return Msg.error("请输入系统来源");
        }else {
            CouponsFromSysEnums fromSysEnums=CouponsFromSysEnums.getByCode(reqSendMessageDto.getFromSys());
            if (fromSysEnums==null){
                return Msg.error("系统来源["+reqSendMessageDto.getFromSys()+"]不正确");
            }
        }
        if(StringUtil.isEmpty(reqSendMessageDto.getFromSysCode())){
            return Msg.error("请输入系统来源code");
        }
        if (StringUtils.isEmpty(reqSendMessageDto.getIsMessage())){
            return Msg.error(" 请输入是否发送手机短信");
        }
        if (reqSendMessageDto.getIsMessage()==1){
            if (StringUtils.isEmpty(reqSendMessageDto.getMessageType()) ){
                return  Msg.error("请输入手机发送类型");
            }
        }
        if (StringUtil.isEmpty(reqSendMessageDto.getMsgContent())){
            return Msg.error("请输入发送内容");
        }

        if (StringUtil.isEmpty(reqSendMessageDto.getMsgDesc())){
            return Msg.error("请输入标题");
        }

        return null;
    }

    private void insertMessageDetailList(List<MessageSendDetail> detailList) {
        List<MessageSendDetail> insertList=new ArrayList<>();

        for (MessageSendDetail tmp:detailList){
            if (StringUtil.isNotEmpty(tmp.getUserPhone())){
                tmp.setMqQueue("OPERSYS_MSG");
                insertList.add(tmp);
            }
            if (insertList.size()==2000){
                messageSendDetailMapper.insertList(insertList);
                insertList.clear();
            }
        }
        if (insertList.size()>0){
            messageSendDetailMapper.insertList(insertList);
        }
    }


    private MessageSend getMessageSend(BaseRequestParamDto dto, ReqSendMessageDto reqSendMessageDto,Map<String,String> map) {
        MessageSend messageSend=new MessageSend();
        CouponsFromSysEnums fromSysEnums=CouponsFromSysEnums.getByCode(reqSendMessageDto.getFromSys());

        StringBuffer sb=new StringBuffer();
        if (reqSendMessageDto.getIsMessage()!=null&&reqSendMessageDto.getIsMessage()==1){
            sb.append("{isMsg:true,");
            messageSend.setMessage("1");
        }else {
            sb.append("{isMsg:false,");
        }
        if (reqSendMessageDto.getIsPush()!=null&&reqSendMessageDto.getIsPush()==1){
            messageSend.setPush("1");
            sb.append("isApp:true");
        }else {
            sb.append("isApp:false");
        }
        if (reqSendMessageDto.getIsMail()!=null&&reqSendMessageDto.getIsMail()==1){
            messageSend.setMail("1");
            sb.append("isEmail:true}");
        }else {
            sb.append("isEmail:false}");
        }

        String typeJson=sb.toString();

        LocalDateTime localDateTime=LocalDateTime.now();

        messageSend.setSendingPlatform(Integer.parseInt(map.get(dto.getCode())));

        messageSend.setReceiveType((short) 1);
        messageSend.setMsgDesc("["+fromSysEnums.getName()+"]"+reqSendMessageDto.getMsgDesc());
        messageSend.setCreateTime(localDateTime);
        messageSend.setCreateAdmin(reqSendMessageDto.getOperateAdmin());
        messageSend.setUpdateTime(localDateTime);
        messageSend.setUpdateAdmin(reqSendMessageDto.getOperateAdmin());
        messageSend.setMessageNum(reqSendMessageDto.getMobiles()!=null? reqSendMessageDto.getMobiles().size():reqSendMessageDto.getUserSeqs().size());
        messageSend.setMsgTypeJson(typeJson);
        String receiveTypeDetail=String.format("发送平台:%s",fromSysEnums.getName());
        messageSend.setReceiveTypeDetail(receiveTypeDetail);
        messageSend.setFromSys(reqSendMessageDto.getFromSys());
        messageSend.setFromSysCode(reqSendMessageDto.getFromSysCode());
        messageSend.setMsgContent(reqSendMessageDto.getMsgContent());
        messageSend.setMessageType(reqSendMessageDto.getMessageType());
        return messageSend;
    }

    private void sendMessageListByApi( List<Shopper> shopperList,MessageSend messageSend, AppMessageDto appMessageDto,
                                       List<String> list, List<MessageSendDetail> detailList,Map<String,String> map) {
        if (StringUtil.isNotEmpty(messageSend.getMessage())){
            list.forEach(
                    mobile->{
                        sendMessageByApi(messageSend, appMessageDto, mobile,detailList,map);
                    }
            );
        }

        if (StringUtil.isNotEmpty(messageSend.getPush())&&shopperList!=null&&shopperList.size()>0){
            shopperList.forEach(
                    shopper->{
                        sendPush(messageSend, appMessageDto,shopper,detailList);
                    }
            );
        }
    }


    private void sendMessageByList(MessageSend messageSend, AppMessageDto appMessageDto, List<Shopper> list, List<MessageSendDetail> detailList,Map<String,String> map) {
        if (messageSend.getMsgType()==1){
            list.forEach(
                    shopper->{
                        sendMessage(messageSend, appMessageDto, shopper,detailList,map);
                    }
            );
        }
        if (messageSend.getMsgType()==2){
            list.forEach(
                    shopper->{
                        sendPush(messageSend, appMessageDto,shopper,detailList);
                    }
            );
        }
//        if (StringUtil.isNotEmpty(messageSend.getMail())){
//            list.forEach(
//                    shopper->{
//                        sendMail(messageSend, appMessageDto, shopper,detailList);
//                    }
//            );
//        }
    }


    private void sendMessageByApi(MessageSend messageSend, AppMessageDto appMessageDto, String mobile , List<MessageSendDetail> detailList,Map<String,String> map) {

        long msgId=Long.valueOf(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));
        LocalDateTime dtNow = LocalDateTime.now();
        SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
        sendMessageParamDto.setMsgId(msgId);
        sendMessageParamDto.setChannel("ucpaas");
//        sendMessageParamDto.setChannel("520Love");
        sendMessageParamDto.setAisle("sms");
        sendMessageParamDto.setAisleAccount(mobile);
        sendMessageParamDto.setTitle(messageSend.getMsgDesc());

        sendMessageParamDto.setContent("【"+map.get(messageSend.getSendingPlatform()+"")+"】"+messageSend.getMsgContent());

        sendMessageParamDto.setOrigin(appMessageDto.getAppName());
        sendMessageParamDto.setUserId(mobile);
        sendMessageParamDto.setType("公告");
        sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));
        HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);
        String str=HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));
        Assert.notNull(str,"请求发送信息接口异常");
        //发送明细
        MessageSendDetail messageSendDetail=new MessageSendDetail();

        messageSendDetail.setMsgId(msgId);
        messageSendDetail.setSendTime(dtNow);
        messageSendDetail.setCreateAdmin(messageSend.getCreateAdmin());
        messageSendDetail.setCreateTime(messageSend.getCreateTime());
//        messageSendDetail.setFinishTime( LocalDateTime.now());
//        messageSendDetail.setMqQueue();
        messageSendDetail.setMsgContent(messageSend.getMsgContent());
        messageSendDetail.setMsgDesc(messageSend.getMsgDesc());
        messageSendDetail.setSendStatus((short)0);
        messageSendDetail.setMsgType("ismsg");
        messageSendDetail.setUserSeq(mobile);
        messageSendDetail.setUserPhone(mobile);
        messageSendDetail.setSendTime(messageSend.getCreateTime());

        if (!StringUtils.isEmpty(messageSend.getMessageType()) ){
            messageSendDetail.setMessageType(messageSend.getMessageType());
        }
        detailList.add(messageSendDetail);

    }

    private void sendMessage(MessageSend messageSend, AppMessageDto appMessageDto, Shopper shopper , List<MessageSendDetail> detailList,Map<String,String> map) {
        MessageSendDetail messageSendDetail=new MessageSendDetail();
        long msgId=Long.valueOf(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));
        LocalDateTime dtNow = LocalDateTime.now();
        SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
        sendMessageParamDto.setMsgId(msgId);
        sendMessageParamDto.setChannel("ucpaas");
//        sendMessageParamDto.setChannel("520Love");
        sendMessageParamDto.setAisle("sms");
        sendMessageParamDto.setAisleAccount(shopper.getMobile());
//        sendMessageParamDto.setAisleAccount("18588829283");
        sendMessageParamDto.setTitle(messageSend.getMsgDesc());
        sendMessageParamDto.setContent("【"+map.get(messageSend.getSendingPlatform()+"")+"】"+messageSend.getMsgContent());
        messageSendDetail.setMsgContent("【"+map.get(messageSend.getSendingPlatform()+"")+"】"+messageSend.getMsgContent());


        sendMessageParamDto.setOrigin(appMessageDto.getAppName());
        sendMessageParamDto.setUserId(shopper.getSeq()+"");
        sendMessageParamDto.setType("公告");
        sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));
        HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);
        String str=HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));
        Assert.notNull(str,"请求发送信息接口异常");
        //发送明细

        messageSendDetail.setMsgId(msgId);
        messageSendDetail.setSendTime(dtNow);
        messageSendDetail.setCreateAdmin(messageSend.getCreateAdmin());
        messageSendDetail.setCreateTime(messageSend.getCreateTime());
//        messageSendDetail.setFinishTime( LocalDateTime.now());
//        messageSendDetail.setMqQueue();

        messageSendDetail.setMsgDesc(messageSend.getMsgDesc());
        messageSendDetail.setSendStatus((short)0);
        messageSendDetail.setMsgType("ismsg");
        messageSendDetail.setUserSeq(shopper.getSeq()+"");
        messageSendDetail.setUserPhone(shopper.getMobile());
        messageSendDetail.setSendTime(messageSend.getCreateTime());

        if (!StringUtils.isEmpty(messageSend.getMessageType()) ){
           messageSendDetail.setMessageType(messageSend.getMessageType());
        }
        detailList.add(messageSendDetail);

    }

    private void sendMail(MessageSend messageSend, AppMessageDto appMessageDto, Shopper shopper, List<MessageSendDetail> detailList) {
        MessageSendDetail messageSendDetail=new MessageSendDetail();
        LocalDateTime dtNow = LocalDateTime.now();
        long msgId=Long.valueOf(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));
        SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();

        sendMessageParamDto.setMsgId(msgId);
        sendMessageParamDto.setAisle("email");
//        shopper.setCheckemail("491839844@qq.com");
        if (StringUtil.isNotEmpty(shopper.getCheckemail())){
            if (shopper.getCheckemail().contains("@qq.com")){
                sendMessageParamDto.setChannel("qq");
            }else if (shopper.getCheckemail().contains("@163.com")){
                sendMessageParamDto.setChannel("netease");

            }else if(shopper.getCheckemail().contains("@sina.com")||shopper.getCheckemail().contains("@sina.cn")){
                sendMessageParamDto.setChannel("sina");
            }
        }
        sendMessageParamDto.setAisleAccount(shopper.getCheckemail());
        sendMessageParamDto.setTitle(messageSend.getMsgDesc());

        if (messageSend.getSendingPlatform()==MessagePlamEnum.getValue("爱之家")){
            sendMessageParamDto.setContent("【520爱之家】"+messageSend.getMsgContent());
            messageSendDetail.setMsgContent("【520爱之家】"+messageSend.getMsgContent());
        }
        if (messageSend.getSendingPlatform()==MessagePlamEnum.getValue("生活圈")){
            sendMessageParamDto.setContent("【520生活圈】"+messageSend.getMsgContent());
            messageSendDetail.setMsgContent("【520生活圈】"+messageSend.getMsgContent());
        }
        if (messageSend.getSendingPlatform()==MessagePlamEnum.getValue("520批发网")){
            sendMessageParamDto.setContent("【520批发网】"+messageSend.getMsgContent());
            messageSendDetail.setMsgContent("【520批发网】"+messageSend.getMsgContent());
        }
        sendMessageParamDto.setOrigin(appMessageDto.getAppName());
        sendMessageParamDto.setUserId(shopper.getSeq()+"");
        sendMessageParamDto.setType("公告");
        sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));
        HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);

        String str=HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));
        Assert.notNull(str,"请求发送信息接口异常");

        //发送明细

        messageSendDetail.setMsgId(msgId);
        messageSendDetail.setSendTime(dtNow);
        messageSendDetail.setCreateAdmin(messageSend.getCreateAdmin());
        messageSendDetail.setCreateTime(messageSend.getCreateTime());
        messageSendDetail.setMsgContent(messageSend.getMsgContent());
        messageSendDetail.setMsgDesc(messageSend.getMsgDesc());
        messageSendDetail.setUserSeq(shopper.getSeq()+"");
        messageSendDetail.setUserPhone(shopper.getMobile());
        messageSendDetail.setSendStatus((short)0);
        messageSendDetail.setMsgType("isEmail");
        detailList.add(messageSendDetail);
    }

    private void sendPush(MessageSend messageSend, AppMessageDto appMessageDto,Shopper shopper, List<MessageSendDetail> detailList) {
        MessageSendDetail messageSendDetail=new MessageSendDetail();
        LocalDateTime dtNow = LocalDateTime.now();
        long msgId=Long.valueOf(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));

        SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
        sendMessageParamDto.setMsgId(msgId);

        if (messageSend.getSendingPlatform()==MessagePlamEnum.getValue("爱之家")){
            sendMessageParamDto.setChannel("520Love");
            sendMessageParamDto.setContent(messageSend.getMsgContent());
            messageSendDetail.setMsgContent("【520爱之家】"+messageSend.getMsgContent());
        }
        if (messageSend.getSendingPlatform()==MessagePlamEnum.getValue("生活圈")){
            sendMessageParamDto.setChannel("aurora");
            sendMessageParamDto.setContent(messageSend.getMsgContent());
            messageSendDetail.setMsgContent("【520生活圈】"+messageSend.getMsgContent());
        }

        sendMessageParamDto.setAisle("sdk");
        sendMessageParamDto.setAisleAccount(shopper.getSeq()+"");//4339177  4339975  4339975  5762301
//        sendMessageParamDto.setAisleAccount("4341545");
        sendMessageParamDto.setTitle(messageSend.getMsgDesc());
        sendMessageParamDto.setOrigin(appMessageDto.getAppName());
        sendMessageParamDto.setUserId(shopper.getSeq()+"");
        sendMessageParamDto.setType("公告");
        sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));

        Map<String,String> map=new HashMap<>();
        //type:1:通知，2：公告，2跳转
        map.put("msgType","2");
        map.put("jumpUrl",messageSend.getJumpUrl());
        map.put("seq",shopper.getSeq()+"");

        sendMessageParamDto.setExtras(JsonUtils.toDefaultJson(map));

        HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);
        String str=HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));
        Assert.notNull(str,"请求发送信息接口异常");

        //发送明细

        messageSendDetail.setMsgId(msgId);
        messageSendDetail.setSendTime(dtNow);
        messageSendDetail.setCreateAdmin(messageSend.getCreateAdmin());
        messageSendDetail.setCreateTime(messageSend.getCreateTime());
        messageSendDetail.setMsgContent(messageSend.getMsgContent());
        messageSendDetail.setMsgDesc(messageSend.getMsgDesc());
        messageSendDetail.setUserSeq(shopper.getSeq()+"");
        messageSendDetail.setUserPhone(shopper.getMobile());
        messageSendDetail.setSendStatus((short)0);
        messageSendDetail.setMsgType("isApp");
        detailList.add(messageSendDetail);
    }

    private void sendPushByAllPlat(MessageSend messageSend,  AppMessageDto appMessageDto,List<MessageSendDetail> detailList) {
        MessageSendDetail messageSendDetail=new MessageSendDetail();
        LocalDateTime dtNow = LocalDateTime.now();
        long msgId=Long.valueOf(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));

        SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
        sendMessageParamDto.setMsgId(msgId);

        if (messageSend.getSendingPlatform()==MessagePlamEnum.getValue("爱之家")){
            sendMessageParamDto.setChannel("520Love");
            sendMessageParamDto.setContent(messageSend.getMsgContent());
            messageSendDetail.setMsgContent(messageSend.getMsgContent());

        }
        if (messageSend.getSendingPlatform()==MessagePlamEnum.getValue("生活圈")){
            sendMessageParamDto.setChannel("aurora");
            sendMessageParamDto.setContent(messageSend.getMsgContent());
            messageSendDetail.setMsgContent(messageSend.getMsgContent());
        }
        sendMessageParamDto.setAisle("sdk");
        if (messageSend.getAppType()==1){
            sendMessageParamDto.setAisleAccount("ios");
            messageSendDetail.setUserSeq("ios推送");
        }
        if (messageSend.getAppType()==2){
            sendMessageParamDto.setAisleAccount("android");
            messageSendDetail.setUserSeq("安卓推送");
        }
        if (messageSend.getAppType()==3){
            sendMessageParamDto.setAisleAccount("all");
            messageSendDetail.setUserSeq("ios+安卓推送");
        }
        sendMessageParamDto.setTitle(messageSend.getMsgDesc());
        sendMessageParamDto.setOrigin(appMessageDto.getAppName());
        sendMessageParamDto.setType("公告");
        sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));
        Map<String,String> map=new HashMap<>();
        //type:1:通知，2：公告，2跳转
        map.put("msgType","2");
        map.put("jumpUrl",messageSend.getJumpUrl());

        sendMessageParamDto.setExtras(JsonUtils.toDefaultJson(map));

        HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);
        String str=HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));
        Assert.notNull(str,"请求发送信息接口异常");

        //发送明细
        messageSendDetail.setMsgId(msgId);
        messageSendDetail.setSendTime(dtNow);
        messageSendDetail.setCreateAdmin(messageSend.getCreateAdmin());
        messageSendDetail.setCreateTime(messageSend.getCreateTime());
        messageSendDetail.setMsgContent(messageSend.getMsgContent());
        messageSendDetail.setMsgDesc(messageSend.getMsgDesc());
        messageSendDetail.setUserPhone("平台推送");
        messageSendDetail.setSendStatus((short)0);
        messageSendDetail.setMsgType("isApp");
        detailList.add(messageSendDetail);
    }

	@Override
	public Msg<String> sendCheckCode(String mobile) {
		//准备六位随机验证码数字
		String randNum = 100000 + (int)(Math.random() * ((999999 - 100000) + 1))+"";
		//String randNumString = DigestUtils.md5DigestAsHex(randNum.concat(systemProperties.getMd5Key()).getBytes());
		//验证码存到redis1分钟
		redisService.set(Constant.OPERSYS_USER_LOGIN_VERIFICATION_CODE+mobile, randNum, 60L);
		
		//获取请求头信息准备
    	String appStr=HttpClientUtil.doGet(shqApi.getGetAppUrl());
        if(StringUtils.isEmpty(appStr)) {
        	return Msg.error("消息推送接口["+shqApi.getGetAppUrl()+"]异常.");
        }
        appStr=appStr.substring(appStr.indexOf("[")+1,appStr.indexOf("]"));
        AppMessageDto appMessageDto=JsonUtils.parse(appStr,AppMessageDto.class);
    	
        //准备发送验证码参数
    	long msgId=Long.valueOf(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));
        LocalDateTime dtNow = LocalDateTime.now();
        SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
        sendMessageParamDto.setMsgId(msgId);
        sendMessageParamDto.setChannel("ucpaas");
        sendMessageParamDto.setAisle("sms");
        //接收验证码手机号码
        sendMessageParamDto.setAisleAccount(mobile);
        sendMessageParamDto.setTitle("运营后台登陆短信验证码");

        sendMessageParamDto.setContent("【运营后台系统】 "+randNum+"，（运营后台系统的短信登陆验证码），请勿随意告诉他人 ，该验证码一分钟内有效。");

        sendMessageParamDto.setOrigin(appMessageDto.getAppName());
        sendMessageParamDto.setUserId(mobile);
        sendMessageParamDto.setType("通知");
        sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));
        try {
           HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);
           String str=HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));
           Assert.notNull(str,"请求发送信息接口异常");
	    	return Msg.ok("发送成功","");
		} catch (Exception e) {
			log.error("发送短信失败:	"+e);
	    	return Msg.error("发送失败，每日发送验证码次数用完或其他错误","");
		}
	}

    @Override
    public Msg<String> pushMessageByApi(BaseRequestParamDto dto) {
        ReqSendMessageDto reqSendMessageDto=JsonUtils.parse(JsonUtils.toDefaultJson(dto.getRequestData()),ReqSendMessageDto.class);

        if (StringUtil.isEmpty(reqSendMessageDto.getFromSys())){
            return Msg.error("请输入系统来源");
        }else {
            CouponsFromSysEnums fromSysEnums=CouponsFromSysEnums.getByCode(reqSendMessageDto.getFromSys());
            if (fromSysEnums==null){
                return Msg.error("系统来源["+reqSendMessageDto.getFromSys()+"]不正确");
            }
        }
        if(StringUtil.isEmpty(reqSendMessageDto.getFromSysCode())){
            return Msg.error("请输入系统来源code");
        }
        if (StringUtils.isEmpty(reqSendMessageDto.getUserSeqs())||reqSendMessageDto.getUserSeqs().size()<=0){
            return Msg.error("请输入用户seq");
        }
        if (StringUtil.isEmpty(reqSendMessageDto.getMsgContent())){
            return Msg.error("请输入发送内容");
        }

        if (StringUtil.isEmpty(reqSendMessageDto.getMsgDesc())){
            return Msg.error("请输入标题");
        }
        if (StringUtils.isEmpty(reqSendMessageDto.getMsgType())){
            return Msg.error("请输入推送类型");
        }
        if (reqSendMessageDto.getMsgType()==1){
            if (StringUtils.isEmpty(reqSendMessageDto.getOrderId())){
                return Msg.error("请输入订单id");
            }
            if (StringUtils.isEmpty(reqSendMessageDto.getOrderStatus())){
                return Msg.error("请输入订单状态");
            }
        }
        if (reqSendMessageDto.getMsgType()==2){
            if (StringUtils.isEmpty(reqSendMessageDto.getJumpUrl())){
                return Msg.error("请输入跳转url");
            }
        }

        Dict dictParam=new Dict();
        dictParam.setCode("messageParam");
        List<Dict> dictList=dictService.select(dictParam);
        Map<String,String> keyMap=new HashMap<>();
        Map<String,String> valueMap=new HashMap<>();
        dictList.forEach(
                p->{
                    keyMap.put(p.getDictKey(),p.getDictValue());
                    valueMap.put(p.getDictValue(),p.getName());
                }
        );

        reqSendMessageDto.setIsPush((short)1);
        MessageSend messageSend = getMessageSend(dto, reqSendMessageDto,keyMap);
        messageSend.setSendingPlatform(1);
        messageSend.setJumpUrl(reqSendMessageDto.getJumpUrl());

        String appStr=HttpClientUtil.doGet(shqApi.getGetAppUrl());
        if(StringUtils.isEmpty(appStr)) {
            return Msg.error("消息推送接口["+shqApi.getGetAppUrl()+"]异常.");
        }
        appStr=appStr.substring(appStr.indexOf("[")+1,appStr.indexOf("]"));
        AppMessageDto appMessageDto=JsonUtils.parse(appStr,AppMessageDto.class);

        List<Shopper> shopperlist=shopperMapper.queryShopperSeqs(reqSendMessageDto.getUserSeqs());
        if (null==shopperlist||shopperlist.size()<=0){
            return Msg.error("查询不到该用户");
        }

        List<MessageSendDetail> detailList=new ArrayList<>();
        shopperlist.forEach(
                p->{
                    sendPushByApi(reqSendMessageDto,messageSend,appMessageDto,p,detailList);
                }
        );
        messageSendMapper.insert(messageSend);
        //保存明细
        detailList.forEach(
                p->{
                    p.setMsgSendId(messageSend.getId());
                }
        );
        insertMessageDetailList(detailList);
        return Msg.ok("发送成功");
    }


    /**
     * 新消息推送
     *@author ltg
     *@date 2018/10/12 11:26
     * @params:[messageSend, appMessageDto, shopper, detailList]
     * @return: void
     */
    private void sendPushByApi(ReqSendMessageDto reqSendMessageDto,MessageSend messageSend, AppMessageDto appMessageDto,Shopper shopper, List<MessageSendDetail> detailList) {
        MessageSendDetail messageSendDetail=new MessageSendDetail();
        LocalDateTime dtNow = LocalDateTime.now();
        long msgId=Long.valueOf(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));

        SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
        sendMessageParamDto.setMsgId(msgId);

        if (messageSend.getSendingPlatform()==MessagePlamEnum.getValue("爱之家")){
            sendMessageParamDto.setChannel("520Love");
            sendMessageParamDto.setContent(messageSend.getMsgContent());
            messageSendDetail.setMsgContent("【520爱之家】"+messageSend.getMsgContent());
        }
        if (messageSend.getSendingPlatform()==MessagePlamEnum.getValue("生活圈")){
            sendMessageParamDto.setChannel("aurora");
            sendMessageParamDto.setContent(messageSend.getMsgContent());
            messageSendDetail.setMsgContent("【520生活圈】"+messageSend.getMsgContent());
        }

        sendMessageParamDto.setAisle("sdk");
        sendMessageParamDto.setAisleAccount(shopper.getSeq()+"");//4339177 4339975  5762301  4339975
//        sendMessageParamDto.setAisleAccount("4341545");
//        sendMessageParamDto.setAisleAccount(reqSendMessageDto.getUserSeqs().get(0)+"");
        sendMessageParamDto.setTitle(messageSend.getMsgDesc());
        sendMessageParamDto.setOrigin(appMessageDto.getAppName());
        sendMessageParamDto.setUserId(shopper.getSeq()+"");
        sendMessageParamDto.setType("公告");
        sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));

        Map<String,String> map=new HashMap<>();
        //type:1:订单消息推送，2：公告，跳转，3：升级推送
        if (reqSendMessageDto.getMsgType()==1){
            map.put("seq",shopper.getSeq()+"");
            map.put("orderId",reqSendMessageDto.getOrderId());
            map.put("orderStatus",reqSendMessageDto.getOrderStatus()+"");
            map.put("orderType",reqSendMessageDto.getOrderType()+"");
        }
        if (reqSendMessageDto.getMsgType()==2){
            map.put("jumpUrl",messageSend.getJumpUrl());
            messageSend.setJumpUrl(reqSendMessageDto.getJumpUrl());
        }
        map.put("msgType",reqSendMessageDto.getMsgType()+"");

        sendMessageParamDto.setExtras(JsonUtils.toDefaultJson(map));

        HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);
        String str=HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));
        Assert.notNull(str,"请求发送信息接口异常");

        //发送明细

        messageSendDetail.setMsgId(msgId);
        messageSendDetail.setSendTime(dtNow);
        messageSendDetail.setCreateAdmin(messageSend.getCreateAdmin());
        messageSendDetail.setCreateTime(messageSend.getCreateTime());
//        messageSendDetail.setFinishTime(LocalDateTime.now());
//        messageSendDetail.setMqQueue();
        messageSendDetail.setMsgContent(messageSend.getMsgContent());
        messageSendDetail.setMsgDesc(messageSend.getMsgDesc());
        messageSendDetail.setUserSeq(shopper.getSeq()+"");
        messageSendDetail.setUserPhone(shopper.getMobile());
        messageSendDetail.setSendStatus((short)0);
        messageSendDetail.setMsgType("isApp");
        detailList.add(messageSendDetail);
    }


}