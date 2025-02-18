package com.shq.oper.controller.message;

import com.github.pagehelper.Page;
import com.github.pagehelper.util.StringUtil;
import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.primarydb.MessageExcelMapper;
import com.shq.oper.model.domain.primarydb.MessageExcel;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.MessageExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author: ltg
 * @date: Created in 10:34 2018/7/7
 */
@RestController
@Slf4j
@RequestMapping("/message/excel")

public class MessageExcelController extends BaseController {

    @Autowired
    private MessageExcelService messageExcelService;

    @Autowired
    private MessageExcelMapper messageExcelMapper;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        return toPage(request);

    }

    @RequestMapping("/list")
    public Data<MessageExcel> list(MessageExcel entity, PageDto page) {

        com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
        Page<MessageExcel> entitys =messageExcelMapper .queryPageSort(entity);
        return Data.ok(entitys);
    }

    @RequestMapping("/send")
    public Msg<String> index(HttpServletRequest request,String batch) {
        if (StringUtil.isEmpty(batch)){
            return Msg.error("请选择批次");
        }

        String admin=this.getAdmin(request).getName();

        return messageExcelService.sendMessage(batch,admin);
    }


    @RequestMapping("/uploadFile")
    @ResponseBody
    public Msg<String> uploadFile(@RequestParam MultipartFile  file,
    HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 判断文件名是否为空
        if (file == null)
            return Msg.error("输入文件有误");

        String username=this.getAdmin(request).getName();

        String filePath="";
        if(!file.isEmpty()) {
            filePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
        }
        return messageExcelService.uploadFile(file, username, filePath);
    }


}
