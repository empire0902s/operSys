package com.shq.oper.controller.message;

import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.enums.MessagePlamEnum;
import com.shq.oper.mapper.primarydb.MessageSendDetailMapper;
import com.shq.oper.mapper.primarydb.MessageSendMapper;
import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.model.domain.primarydb.MessageSend;
import com.shq.oper.model.domain.primarydb.MessageSendDetail;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ltg
 * @date: Created in 11:36 2018/7/10
 */
@RestController
@Slf4j
@RequestMapping("/message/setting")
public class MessageSettingController  extends BaseController {
    @Autowired
    private MessageSendMapper messageSendMapper;

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

    @RequestMapping("/list")
    public Data<MessageSend> list(MessageSend entity, PageDto page) {

        com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
        Page<MessageSend> entitys = messageSendMapper.queryPageSort(entity);
        return Data.ok(entitys);
    }
}
