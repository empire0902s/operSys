package com.shq.oper.service.impl.primarydb;

import com.github.pagehelper.util.StringUtil;
import com.shq.oper.enums.MessagePlamEnum;
import com.shq.oper.enums.api.CouponsFromSysEnums;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.MessageExcelMapper;
import com.shq.oper.mapper.primarydb.MessageSendDetailMapper;
import com.shq.oper.mapper.primarydb.MessageSendMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.MessageExcel;
import com.shq.oper.model.domain.primarydb.MessageSend;
import com.shq.oper.model.domain.primarydb.MessageSendDetail;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.message.AppMessageDto;
import com.shq.oper.model.dto.api.message.SendMessageParamDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.MessageExcelService;
import com.shq.oper.service.primarydb.MessageSendDetailService;
import com.shq.oper.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service("messageExcelService")
public class MessageExcelServiceImpl extends BaseServiceImpl<MessageExcel, Long> implements MessageExcelService {

    @Autowired
    private ShqApi shqApi;

    @Autowired
    private ShopperMapper shopperMapper;

    @Autowired
    private MessageExcelMapper messageExcelMapper;

    @Autowired
    private MessageSendDetailMapper messageSendDetailMapper;

    @Autowired
    private MessageSendMapper messageSendMapper;

//    @Autowired
//    private MQSendHandler mqSendHandler;


    /**
     *@author ltg
     *@date 2018/7/10 9:25
     * @params:[file, username, filePath]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @Transactional
    @Override
    public Msg<String> uploadFile(MultipartFile file, String username, String filePath) throws Exception {
        String fileName=file.getOriginalFilename();

        if (!(fileName.matches("^.+\\.(?i)(xls)$")||fileName.matches("^.+\\.(?i)(xlsx)$"))){
            return Msg.error("请导入Excel文件");
        }
        String path = filePath + fileName;
        File tempFile = null;
        tempFile =  new File(path);

        //获取excel实体列表	
        List<MessageExcel> readDateListT = getMessageExcelList(file, fileName, tempFile);

        if (readDateListT==null||readDateListT.size()<=0){
            return Msg.error("没有要导入的信息");
        }

        //验证
        String batch=""; int isapp=0;    int msg=0; int email=0;

        if (StringUtil.isEmpty(readDateListT.get(0).getBatch())){
            return Msg.error("第2行[标题]不能为空");
        }else {
            batch=readDateListT.get(0).getBatch();
        }

        if (StringUtils.isEmpty(readDateListT.get(0).getIsApp()) || readDateListT.get(0).getIsApp()>2){
            return  Msg.error("第2行输入的是否发送推送列不符合要求");
        }else {
            isapp=readDateListT.get(0).getIsApp();
        }

        if (StringUtils.isEmpty(readDateListT.get(0).getIsEmail()) || readDateListT.get(0).getIsEmail()>2){
            return  Msg.error("第2行输入的是否发送邮件列不符合要求");
        }else {
            email=readDateListT.get(0).getIsEmail();
        }

        if (StringUtils.isEmpty(readDateListT.get(0).getIsMsg()) || readDateListT.get(0).getIsMsg()>2){
            return  Msg.error("第2行输入的是否发送短信列不符合要求");
        }else {
            msg=readDateListT.get(0).getIsMsg();
        }

//        if (StringUtils.isEmpty(readDateListT.get(0).getSendingPlatform()) || readDateListT.get(0).getSendingPlatform()<=0){
//            return  Msg.error("第2行输入发送平台不符合要求");
//        }else {
//            SendingPlat=readDateListT.get(0).getIsMsg();
//        }

        if (isapp==0&&msg==0){
            return  Msg.error("输入是否发送App，是否发送短信.必须有一个为1");
        }

        int index=1;
        for (MessageExcel p:readDateListT){
            index++;
            if(StringUtil.isEmpty(p.getBatch())){
                return Msg.error("第"+index+"行[标题]不能为空");
            }
            if (StringUtils.isEmpty(p.getIsEmail()) || p.getIsEmail()!=email){
                return  Msg.error("第"+index+"行输入的是否发送邮件列不一致");
            }
            if (StringUtils.isEmpty(p.getIsApp()) || p.getIsApp()!=isapp){
                return  Msg.error("第"+index+"行输入的是否发送推送列不一致");
            }
            if (StringUtils.isEmpty(p.getIsMsg()) || p.getIsMsg()!=msg){
                return  Msg.error("第"+index+"行输入的是否发送短信列不一致");
            }
            if (StringUtils.isEmpty(p.getBatch()) || !p.getBatch().equals(batch)){
                return  Msg.error("第"+index+"行输入的标题列不一致");
            }


//            if (StringUtils.isEmpty(p.getSendingPlatform()) || p.getSendingPlatform()!=SendingPlat){
//                return  Msg.error("第"+index+"行输入的发送平台列有误");
//            }
        }

        //过滤批次
        MessageExcel message=new MessageExcel();
        message.setBatch(batch);
        int i=messageExcelMapper.selectCount(message);
        if (i>0){
            return Msg.error("该批次已存在");
        }

        Map<String, Shopper> map = getStringShopperMap(readDateListT);

        LocalDateTime dtNow = LocalDateTime.now();

        //过滤库没有的手机号
        List<MessageExcel> detailList=new ArrayList<>();

        readDateListT.forEach(
                p->{
//                    Shopper shopper=new Shopper();
//                    shopper.setMobile(p.getPhone());
//                    int i=shopperMapper.selectCount(shopper);
                    if (map.containsKey(p.getPhone())){
                        p.setCreateAdmin(username);
                        p.setCreateTime(dtNow);
                        p.setExcelName(fileName);

                        //不发邮件
                        p.setIsEmail(0);
                        p.setStatus(0);//未发送状态
                        detailList.add(p);
                    }else {
                        p.setCreateAdmin(username);
                        p.setCreateTime(dtNow);
                        p.setExcelName(fileName);

                        //不发邮件
                        p.setIsEmail(0);
                        p.setStatus(2);//发送失败
                        detailList.add(p);
                    }
                }
        );


        if (detailList.size()<=0){
            return Msg.error("没有符合导入的数据");
        }

        List<MessageExcel> insertList=new ArrayList<>();

        for (MessageExcel tmp:detailList){
            insertList.add(tmp);
            if (insertList.size()==2000){
                messageExcelMapper.insertList(insertList);
                insertList.clear();
            }
        }
        if (insertList.size()>0){
            messageExcelMapper.insertList(insertList);
        }

        return Msg.ok(batch);
    }

    private List<MessageExcel> getMessageExcelList(MultipartFile file, String fileName, File tempFile) {
        List<MessageExcel> readDateListT=new ArrayList<>();
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
            InputStream in = new FileInputStream(tempFile);
            Workbook wb = ImportExeclUtil.chooseWorkbook(fileName, in);
            MessageExcel messageExcel=new MessageExcel();
            readDateListT = ImportExeclUtil.readDateListT(wb,messageExcel,2,0);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            tempFile.delete();
        }
        return readDateListT;
    }


    /**
     *@author ltg
     *@date 2018/7/10 9:26
     * @params:[batch, admin]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @Transactional
    @Override
    public Msg<String> sendMessage(String batch, String admin ) {

        String appStr=HttpClientUtil.doGet(shqApi.getGetAppUrl());
        if(StringUtils.isEmpty(appStr)) {
        	return Msg.error("消息推送接口["+shqApi.getGetAppUrl()+"]异常.");
        }
        appStr=appStr.substring(appStr.indexOf("[")+1,appStr.indexOf("]"));
        AppMessageDto appMessageDto=JsonUtils.parse(appStr,AppMessageDto.class);

        MessageExcel messageExcel1param=new MessageExcel();
        messageExcel1param.setBatch(batch);
        messageExcel1param.setStatus(0);
        List<MessageExcel> list=messageExcelMapper.queryPageSort(messageExcel1param);
        if (list==null||list.size()<=0){
            return Msg.error("没有查到该批次待发的信息");
        }

        LocalDateTime dtNow = LocalDateTime.now();

        Map<String, Shopper> map = getStringShopperMap(list);

        List<MessageSendDetail> MessageSendDetailList=new ArrayList<>();

        //发送信息
        sendMessage(map,admin, appMessageDto, list, dtNow, MessageSendDetailList);

        //
        MessageSend messageSend=new MessageSend();
        saveMessageSend(admin, list, dtNow,messageSend);

        //更新状态
        updateMessageExcelList(list);

        //插入明细
        MessageSendDetailList.forEach(
                p->{
                    p.setMsgSendId(messageSend.getId());
                }
        );
        insertMessageDetail(MessageSendDetailList);

        return Msg.ok("发送成功");
    }

    private Map<String, Shopper> getStringShopperMap(List<MessageExcel> list) {
        List<Shopper> shopperList=new ArrayList<>();
        List<String> phoneList=new ArrayList<>();

        for (MessageExcel tmp: list){
            phoneList.add(tmp.getPhone());
            if (phoneList.size()==1000){
                shopperList.addAll(shopperMapper.queryShopperphones(phoneList));
                phoneList.clear();
            }
        }
        if (phoneList.size()>0){
            shopperList.addAll(shopperMapper.queryShopperphones(phoneList));
        }

        Map<String,Shopper> map=new HashMap<>();
        shopperList.forEach(
                p->{
                    map.put(p.getMobile(),p);
                }
        );
        return map;
    }

    private void sendMessage(Map<String,Shopper> map,String admin, AppMessageDto appMessageDto, List<MessageExcel> list, LocalDateTime dtNow, List<MessageSendDetail> messageSendDetailList) {
        list.forEach(
                p->{
//                    Shopper shopperParam=new Shopper();
//                    shopperParam.setMobile(p.getPhone());
                    Shopper shopper=map.get(p.getPhone());

                    if (p.getIsMsg()==1){
                        long msgId=Long.valueOf(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));
                        SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
                        sendMessageParamDto.setMsgId(msgId);
                        sendMessageParamDto.setChannel("ucpaas");
                        sendMessageParamDto.setAisle("sms");
                        sendMessageParamDto.setAisleAccount(shopper.getMobile());
//        sendMessageParamDto.setAisleAccount("18588829283");
                        sendMessageParamDto.setTitle(p.getBatch());
//                        if (p.getSendingPlatform()==MessagePlamEnum.getValue("爱之家")){
                            sendMessageParamDto.setContent("【520爱之家】"+p.getMsgContent());
//                        }
//                        if (p.getSendingPlatform()==MessagePlamEnum.getValue("生活圈")){
//                            sendMessageParamDto.setContent("【520生活圈】"+p.getMsgContent());
//                        }
//                        sendMessageParamDto.setContent("【520生活圈】"+p.getMsgContent());
                        sendMessageParamDto.setOrigin(appMessageDto.getAppName());
                        sendMessageParamDto.setUserId(shopper.getSeq()+"");
                        sendMessageParamDto.setType("公告");
                        sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));
                        HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);
                        HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));

                        //发送明细
                        MessageSendDetail messageSendDetail=new MessageSendDetail();
                        messageSendDetail.setMsgId(msgId);
                        messageSendDetail.setSendTime(dtNow);
                        messageSendDetail.setUserPhone(p.getPhone());
                        messageSendDetail.setCreateAdmin(admin);
                        messageSendDetail.setCreateTime(dtNow);
//                        messageSendDetail.setFinishTime( LocalDateTime.now());
                        messageSendDetail.setUserSeq(shopper.getSeq()+"");
//        messageSendDetail.setMqQueue();
                        messageSendDetail.setMsgContent("【520爱之家】"+p.getMsgContent());
                        messageSendDetail.setMsgDesc(p.getBatch());
                        messageSendDetail.setSendStatus((short)0);
                        messageSendDetail.setMsgType("ismsg");
                        messageSendDetail.setMessageType((short)2);

                        messageSendDetailList.add(messageSendDetail);
                    }
                    if (p.getIsEmail()==1){
                        if (StringUtil.isEmpty(shopper.getCheckemail())){
                            //发送明细
                            MessageSendDetail messageSendDetail=new MessageSendDetail();
                            messageSendDetail.setSendTime(dtNow);
                            messageSendDetail.setUserPhone(p.getPhone());
                            messageSendDetail.setCreateAdmin(admin);
                            messageSendDetail.setCreateTime(dtNow);
                            messageSendDetail.setFinishTime( LocalDateTime.now());
                            messageSendDetail.setUserSeq(shopper.getSeq()+"");
//        messageSendDetail.setMqQueue();
                            messageSendDetail.setMsgContent("【520爱之家】"+p.getMsgContent());
                            messageSendDetail.setMsgDesc(p.getBatch());
                            messageSendDetail.setSendStatus((short)0);
                            messageSendDetail.setMsgType("isEmail");
                            messageSendDetailList.add(messageSendDetail);
                        }else {

                            long msgId=Long.valueOf(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));
                            SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
                            sendMessageParamDto.setMsgId(msgId);
                            sendMessageParamDto.setAisle("email");
//                        shopper.setCheckemail("491839844@qq.com");
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
                            sendMessageParamDto.setTitle(p.getBatch());
//                            if (p.getSendingPlatform()==MessagePlamEnum.getValue("爱之家")){
                                sendMessageParamDto.setContent("【520爱之家】"+p.getMsgContent());
//                            }
//                            if (p.getSendingPlatform()==MessagePlamEnum.getValue("生活圈")){
//                                sendMessageParamDto.setContent("【520生活圈】"+p.getMsgContent());
//                            }
                            sendMessageParamDto.setOrigin(appMessageDto.getAppName());
                            sendMessageParamDto.setUserId(shopper.getSeq()+"");
                            sendMessageParamDto.setType("公告");
                            sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));
                            HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);

                            HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));

                            //发送明细
                            MessageSendDetail messageSendDetail=new MessageSendDetail();
                            messageSendDetail.setMsgId(msgId);
                            messageSendDetail.setSendTime(dtNow);
                            messageSendDetail.setUserPhone(p.getPhone());
                            messageSendDetail.setCreateAdmin(admin);
                            messageSendDetail.setCreateTime(dtNow);
                            messageSendDetail.setFinishTime( LocalDateTime.now());
                            messageSendDetail.setUserSeq(shopper.getSeq()+"");
//        messageSendDetail.setMqQueue();
                            messageSendDetail.setMsgContent("【520爱之家】"+p.getMsgContent());
                            messageSendDetail.setMsgDesc(p.getBatch());
                            messageSendDetail.setSendStatus((short)0);
                            messageSendDetail.setMsgType("isEmail");
                            messageSendDetailList.add(messageSendDetail);
                        }
                    }
                    if (p.getIsApp()==1){
                        long msgId=Long.valueOf(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));
                        SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
                        sendMessageParamDto.setMsgId(msgId);
//                        sendMessageParamDto.setChannel("aurora");
                        sendMessageParamDto.setAisle("sdk");
                        sendMessageParamDto.setAisleAccount(shopper.getSeq()+"");
//                        sendMessageParamDto.setAisleAccount("4339177");
                        sendMessageParamDto.setTitle(p.getBatch());
//                        if (p.getSendingPlatform()==MessagePlamEnum.getValue("爱之家")){
                            sendMessageParamDto.setChannel("520Love");
                            sendMessageParamDto.setContent("【520爱之家】"+p.getMsgContent());
//                        }
//                        if (p.getSendingPlatform()==MessagePlamEnum.getValue("生活圈")){
//                            sendMessageParamDto.setChannel("aurora");
//                            sendMessageParamDto.setContent("【520生活圈】"+p.getMsgContent());
//                        }
                        sendMessageParamDto.setOrigin(appMessageDto.getAppName());
                        sendMessageParamDto.setUserId(shopper.getSeq()+"");
                        sendMessageParamDto.setType("公告");
                        sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));
                        HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);
                        HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));

                        //发送明细
                        MessageSendDetail messageSendDetail=new MessageSendDetail();
                        messageSendDetail.setMsgId(msgId);
                        messageSendDetail.setSendTime(dtNow);
                        messageSendDetail.setUserPhone(p.getPhone());
                        messageSendDetail.setCreateAdmin(admin);
                        messageSendDetail.setCreateTime(dtNow);
//                        messageSendDetail.setFinishTime( LocalDateTime.now());
                        messageSendDetail.setUserSeq(shopper.getSeq()+"");
//        messageSendDetail.setMqQueue();
                        messageSendDetail.setMsgContent(p.getMsgContent());
                        messageSendDetail.setMsgDesc(p.getBatch());
                        messageSendDetail.setSendStatus((short)0);
                        messageSendDetail.setMsgType("isApp");
                        messageSendDetailList.add(messageSendDetail);
                    }
//                    p.setStatus(1);
                    p.setFinishTime(dtNow);
                    p.setStatus(1);
                }
        );
    }

    private void updateMessageExcelList(List<MessageExcel> list) {
        List<MessageExcel> updateList=new ArrayList<>();

        for (MessageExcel tmp:list){
            updateList.add(tmp);
            if (updateList.size()==2000){
                messageExcelMapper.updateBath(updateList);
                updateList.clear();
            }
        }
        if (updateList.size()>0){
            messageExcelMapper.updateBath(updateList);
        }
    }

    private void insertMessageDetail(List<MessageSendDetail> messageSendDetailList) {
        List<MessageSendDetail> insertList=new ArrayList<>();
        for (MessageSendDetail tmp: messageSendDetailList){
            tmp.setMqQueue("OPERSYS_MSG");
            insertList.add(tmp);
            if (insertList.size()==2000){
                messageSendDetailMapper.insertList(insertList);
                insertList.clear();
            }
        }
        if (insertList.size()>0){
            messageSendDetailMapper.insertList(insertList);
        }
    }

    private void saveMessageSend(String admin, List<MessageExcel> list, LocalDateTime dtNow,MessageSend messageSend) {

        StringBuffer sb=new StringBuffer();
        if (list.get(0).getIsMsg()==1){
            sb.append("{isMsg:true,");
        }else {
            sb.append("{isMsg:false,");
        }
        if (list.get(0).getIsApp()==1){
            sb.append("isApp:true");
        }else {
            sb.append("isApp:false");
        }
        if (list.get(0).getIsEmail()==1){
            sb.append("isEmail:true}");
        }else {
            sb.append("isEmail:false}");
        }

        String typeJson=sb.toString();
        messageSend.setMsgTypeJson(typeJson);
        messageSend.setCreateTime(dtNow);
        messageSend.setCreateAdmin(admin);
        messageSend.setMsgContent(list.get(0).getMsgContent());
        messageSend.setMsgDesc("[导入]"+list.get(0).getBatch());
        messageSend.setReceiveType((short) 1);
        messageSend.setReceiveTypeDetail(list.get(0).getBatch());
        messageSend.setSendingPlatform(1);
        messageSend.setMessageNum(list.size());
        messageSend.setFromSysCode(Constant.COUPONS_FROM_SYS_CODE_DEFAULT);
        messageSend.setFromSys(CouponsFromSysEnums.SYS_OPERATE_CENTER.getCode());
        messageSend.setMessageType((short)2);
        messageSendMapper.insert(messageSend);
    }

//    @Override
//    public Msg<String> sendMessageByMQ(String batch, String admin) {
//
//        String appStr=HttpClientUtil.doGet(shqApi.getGetAppUrl());
//        if(StringUtils.isEmpty(appStr)) {
//            return Msg.error("消息推送接口["+shqApi.getGetAppUrl()+"]异常.");
//        }
//        appStr=appStr.substring(appStr.indexOf("[")+1,appStr.indexOf("]"));
//        AppMessageDto appMessageDto=JsonUtils.parse(appStr,AppMessageDto.class);
//
//        MessageExcel messageExcel1param=new MessageExcel();
//        messageExcel1param.setBatch(batch);
//        messageExcel1param.setStatus(0);
//        List<MessageExcel> list=messageExcelMapper.queryPageSort(messageExcel1param);
//        if (list==null||list.size()<=0){
//            return Msg.error("没有查到该批次待发的信息");
//        }
//        LocalDateTime dtNow = LocalDateTime.now();
//
//        Map<String, Shopper> map = getStringShopperMap(list);
//
//        List<MessageSendDetail> MessageSendDetailList=new ArrayList<>();
//
//        //发送信息
//        sendMessageByMQ(map,admin, appMessageDto, list, dtNow, MessageSendDetailList);
//
//        //保存发送信息
//        saveMessageSend(admin, list, dtNow);
//
//        //更新状态
//        updateMessageExcelList(list);
//
//        //插入明细
//        insertMessageDetail(MessageSendDetailList);
//
//        return Msg.ok("发送成功");
//
//    }

//    private void sendMessageByMQ(Map<String,Shopper> map,String admin, AppMessageDto appMessageDto, List<MessageExcel> list, LocalDateTime dtNow, List<MessageSendDetail> messageSendDetailList) {
//        list.forEach(
//                p->{
////                    Shopper shopperParam=new Shopper();
////                    shopperParam.setMobile(p.getPhone());
//                    Shopper shopper=map.get(p.getPhone());
//
//                    if (p.getIsMsg()==1){
//                        SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
//                        sendMessageParamDto.setChannel("ucpaas");
//                        sendMessageParamDto.setAisle("sms");
////                        sendMessageParamDto.setAisleAccount(shopper.getMobile());
//        sendMessageParamDto.setAisleAccount("18588829283");
//                        sendMessageParamDto.setTitle(p.getBatch());
//                        sendMessageParamDto.setContent("【520生活圈】"+p.getMsgContent());
//                        sendMessageParamDto.setOrigin(appMessageDto.getAppName());
//                        sendMessageParamDto.setUserId(shopper.getSeq()+"");
//                        sendMessageParamDto.setType("公告");
//                        sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));
//
//                        String sendStr=JsonUtils.toDefaultJson(sendMessageParamDto);
//                        mqSendHandler.send(sendStr);
////                        HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);
////                        HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));
//
//                        //发送明细
//                        MessageSendDetail messageSendDetail=new MessageSendDetail();
//                        messageSendDetail.setSendTime(dtNow);
//                        messageSendDetail.setUserPhone(p.getPhone());
//                        messageSendDetail.setCreateAdmin(admin);
//                        messageSendDetail.setCreateTime(dtNow);
//                        messageSendDetail.setFinishTime( LocalDateTime.now());
//                        messageSendDetail.setUserSeq(shopper.getSeq()+"");
////        messageSendDetail.setMqQueue();
//                        messageSendDetail.setMsgContent(p.getMsgContent());
//                        messageSendDetail.setMsgDesc(p.getBatch());
//                        messageSendDetail.setSendStatus((short)1);
//                        messageSendDetail.setMsgType("ismsg");
//                        messageSendDetailList.add(messageSendDetail);
//                    }
//                    if (p.getIsEmail()==1){
//                        if (StringUtil.isEmpty(shopper.getCheckemail())){
//                            //发送明细
//                            MessageSendDetail messageSendDetail=new MessageSendDetail();
//                            messageSendDetail.setSendTime(dtNow);
//                            messageSendDetail.setUserPhone(p.getPhone());
//                            messageSendDetail.setCreateAdmin(admin);
//                            messageSendDetail.setCreateTime(dtNow);
//                            messageSendDetail.setFinishTime( LocalDateTime.now());
//                            messageSendDetail.setUserSeq(shopper.getSeq()+"");
////        messageSendDetail.setMqQueue();
//                            messageSendDetail.setMsgContent(p.getMsgContent());
//                            messageSendDetail.setMsgDesc(p.getBatch());
//                            messageSendDetail.setSendStatus((short)2);
//                            messageSendDetail.setMsgType("isEmail");
//                            messageSendDetailList.add(messageSendDetail);
//                        }else {
//                            SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
//                            sendMessageParamDto.setAisle("email");
////                        shopper.setCheckemail("491839844@qq.com");
//                            if (StringUtil.isNotEmpty(shopper.getCheckemail())){
//                                if (shopper.getCheckemail().contains("@qq.com")){
//                                    sendMessageParamDto.setChannel("qq");
//                                }else if (shopper.getCheckemail().contains("@163.com")){
//                                    sendMessageParamDto.setChannel("netease");
//
//                                }else if(shopper.getCheckemail().contains("@sina.com")||shopper.getCheckemail().contains("@sina.cn")){
//                                    sendMessageParamDto.setChannel("sina");
//                                }
//                            }
//                            sendMessageParamDto.setAisleAccount(shopper.getCheckemail());
//                            sendMessageParamDto.setTitle(p.getBatch());
//                            sendMessageParamDto.setContent("【520生活圈】"+p.getMsgContent());
//                            sendMessageParamDto.setOrigin(appMessageDto.getAppName());
//                            sendMessageParamDto.setUserId(shopper.getSeq()+"");
//                            sendMessageParamDto.setType("公告");
//                            sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));
//                            HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);
//
//                            HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));
//
//                            //发送明细
//                            MessageSendDetail messageSendDetail=new MessageSendDetail();
//                            messageSendDetail.setSendTime(dtNow);
//                            messageSendDetail.setUserPhone(p.getPhone());
//                            messageSendDetail.setCreateAdmin(admin);
//                            messageSendDetail.setCreateTime(dtNow);
//                            messageSendDetail.setFinishTime( LocalDateTime.now());
//                            messageSendDetail.setUserSeq(shopper.getSeq()+"");
////        messageSendDetail.setMqQueue();
//                            messageSendDetail.setMsgContent(p.getMsgContent());
//                            messageSendDetail.setMsgDesc(p.getBatch());
//                            messageSendDetail.setSendStatus((short)1);
//                            messageSendDetail.setMsgType("isEmail");
//                            messageSendDetailList.add(messageSendDetail);
//                        }
//                    }
//                    if (p.getIsApp()==1){
//                        SendMessageParamDto sendMessageParamDto=new SendMessageParamDto();
//                        sendMessageParamDto.setChannel("aurora");
//                        sendMessageParamDto.setAisle("sdk");
//                        sendMessageParamDto.setAisleAccount(shopper.getSeq()+"");
////                        sendMessageParamDto.setAisleAccount("4339351");
//                        sendMessageParamDto.setTitle(p.getBatch());
//                        sendMessageParamDto.setContent("【520生活圈】"+p.getMsgContent());
//                        sendMessageParamDto.setOrigin(appMessageDto.getAppName());
//                        sendMessageParamDto.setUserId(shopper.getSeq()+"");
//                        sendMessageParamDto.setType("公告");
//                        sendMessageParamDto.setAppId(Integer.parseInt(appMessageDto.getId()));
//                        HttpHeaders headers=HttpClientUtil.getSendMessageHeaders(appMessageDto);
//                        HttpClientUtil.doPostHeader(headers,shqApi.getSendMessageUrl(),JsonUtils.toDefaultJson(sendMessageParamDto));
//
//                        //发送明细
//                        MessageSendDetail messageSendDetail=new MessageSendDetail();
//                        messageSendDetail.setSendTime(dtNow);
//                        messageSendDetail.setUserPhone(p.getPhone());
//                        messageSendDetail.setCreateAdmin(admin);
//                        messageSendDetail.setCreateTime(dtNow);
//                        messageSendDetail.setFinishTime( LocalDateTime.now());
//                        messageSendDetail.setUserSeq(shopper.getSeq()+"");
////        messageSendDetail.setMqQueue();
//                        messageSendDetail.setMsgContent(p.getMsgContent());
//                        messageSendDetail.setMsgDesc(p.getBatch());
//                        messageSendDetail.setSendStatus((short)1);
//                        messageSendDetail.setMsgType("isApp");
//                        messageSendDetailList.add(messageSendDetail);
//                    }
////                    p.setStatus(1);
//                    p.setFinishTime(dtNow);
//                    p.setStatus(2);
//                }
//        );
//    }
//


}