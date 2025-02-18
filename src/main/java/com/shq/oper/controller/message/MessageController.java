package com.shq.oper.controller.message;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.service.primarydb.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.util.StringUtil;
import com.shq.oper.controller.BaseController;
import com.shq.oper.enums.MessagePlamEnum;
import com.shq.oper.enums.api.CouponsFromSysEnums;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.MessageSendDetailMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.MessageSend;
import com.shq.oper.model.domain.primarydb.MessageSendDetail;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.MessageSendDetailService;
import com.shq.oper.service.primarydb.MessageSendService;
import com.shq.oper.util.Constant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ltg
 * @date: Created in 14:34 2018/7/4
 */
@RestController
@Slf4j
@RequestMapping("/message")

public class MessageController extends BaseController {

    @Autowired
    private MessageSendDetailService messageSendDetailService;

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private ShopperMapper shopperMapper;

    @Autowired
    private MessageSendDetailMapper messageSendDetailMapper;

    @Autowired
    private DictService dictService;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        Dict dictParam=new Dict();
        dictParam.setCode("messageParam");
        List<Dict> dictList=dictService.select(dictParam);
        Map<String,String> messagePlamList=new HashMap<>();

        if(null!=dictList&&dictList.size()>0){
            dictList.forEach(
                    p->{
                        messagePlamList.put(p.getName(),p.getDictValue());
                    }
            );
        }
//        Map<String, Object> messagePlamList = MessagePlamEnum.getMap();
        request.setAttribute("messagePlamList", messagePlamList);
        return toPage(request);

    }

    @RequestMapping("/phone")
    public ModelAndView phone(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/role")
    public ModelAndView role(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/eare")
    public ModelAndView eare(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/list")
    public Data<MessageSendDetail> list(MessageSendDetail entity, PageDto page) {

        com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
        Page<MessageSendDetail> entitys = messageSendDetailMapper.queryPageSort(entity);
        return Data.ok(entitys);
    }


    @RequestMapping("/save")
    public Msg<String> send(HttpServletRequest request, MessageSend messageSend) {
        if (StringUtil.isEmpty(messageSend.getSeqs())&&messageSend.getReceiveType()!=4){
            return Msg.error("请添加明细");
        }
        LocalDateTime dtNow = LocalDateTime.now();
        messageSend.setCreateAdmin(this.getAdmin(request).getName());
        messageSend.setCreateTime(dtNow);

        String typeJson="";
        if (messageSend.getMsgType()==1){
            typeJson="{isMsg:true,isApp:false,isEmail:false}";
        }
        if (messageSend.getMsgType()==2){
            typeJson="{isMsg:false,isApp:true,isEmail:false}";
        }
        messageSend.setMsgTypeJson(typeJson);
        //默认公告
        if (messageSend.getMsgType()==1){
            messageSend.setMessageType((short) 2);
        }
        messageSend.setFromSys(CouponsFromSysEnums.getByCode("operSys").getCode());
        messageSend.setFromSysCode(Constant.COUPONS_FROM_SYS_CODE_DEFAULT);

        return messageSendService.sendMessage(messageSend);
    }

    @RequestMapping("/shoplist")
    public Data<Shopper> shoplist(Shopper shopper, PageDto page) {
        shopper.setPageNumKey(page.getPage());
        shopper.setPageSizeKey(page.getLimit());
        Page<Shopper> entitys = shopperMapper.queryShopper(shopper);
        return Data.ok(entitys);
    }

    @RequestMapping("/shoprolelist")
    public Data<Shopper> shoprolelist(Shopper shopper, PageDto page) {
        shopper.setPageNumKey(page.getPage());
        shopper.setPageSizeKey(page.getLimit());
        Page<Shopper> entitys = shopperMapper.queryRoleShopper(shopper);
        return Data.ok(entitys);
    }


    @RequestMapping("/shopaerelist")
    public Data<Shopper> shopaerelist(Shopper shopper, PageDto page) {

        Shopper shopperParam=new Shopper();
        //Contact和area用来接受前端传来的值
        if (StringUtil.isNotEmpty(shopper.getContact())){
            if (shopper.getContact().equals("provice")){
                shopperParam.setProvice(shopper.getArea());
            }else if (shopper.getContact().equals("city")){
                shopperParam.setCity(shopper.getArea());
            }else if (shopper.getContact().equals("area")){
                shopperParam.setArea(shopper.getArea());
            }
        }
        shopperParam.setPageNumKey(page.getPage());
        shopperParam.setPageSizeKey(page.getLimit());
        Page<Shopper> entitys = shopperMapper.queryAereShopper(shopperParam);
        return Data.ok(entitys);
    }


    //信息状态同步
    @RequestMapping("/sync")
    public  Msg<String> shoprolelist() {

        try {
            messageSendDetailService.batchUpdateMessageStatus();

        }catch (IllegalArgumentException e){
            return Msg.error(e.getMessage());
        }
        return Msg.ok("同步成功");

    }


    //历史数据信息状态同步
    @RequestMapping("/historysync")
    public  Msg<String> historysync() {

        try {
            messageSendDetailService.batchUpdateHistoryMessageStatus();

        }catch (IllegalArgumentException e){
            return Msg.error(e.getMessage());
        }
        return Msg.ok("同步成功");

    }
    
    //信息状态同步
    @RequestMapping("/sendVerificationCode")
    public  Msg<String> sendVerificationCode(String mobile) {
    	
    	Msg<String> result = messageSendService.sendCheckCode(mobile);
    	return result;
    }

    }
