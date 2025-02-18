package com.shq.oper.controller.common;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import  org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.Uploader;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

/**
 * @author: ltg
 * @date: Created in 16:53 2018/5/22
 */
@Slf4j
@RestController
@RequestMapping(value = {"/ueditor"})
public class UeditorController {

	@Autowired
	private ShqApi ShqApi;
	
    @RequestMapping(value = "/uploadimage", method =  { RequestMethod.POST,RequestMethod.GET })
    @ResponseBody
    public Map<String, String> uploadimage(@RequestParam(value = "upfile") MultipartFile upfile) {
        Map<String, String> map = new HashMap<>();

        try {
            byte[] bytes=upfile.getBytes();
            BASE64Encoder encoder = new BASE64Encoder();
            String base64Str=encoder.encode(bytes);//返回Base64编码过的字节数组字符串
            String body=String.format("{'imgBase64':'%s'}",base64Str);
            String urlresult=HttpClientUtil.doPostJson(ShqApi.getImageUploadUrl(),body);
            Assert.notNull(urlresult,"请求发送图片接口异常");
            String url=ShqApi.getImageAddrUrl();
            if (urlresult!=null && !urlresult.equals("")){
                urlresult= urlresult.substring(1,urlresult.length()-1);
                String[] strs=urlresult.split(",~");
                url+=strs[1];
                url+=strs[0];
            }

            map.put("url",url);//这个url是前台回显路径（回显路径为config.json中的imageUrlPrefix+此处的url）
            map.put("state", "SUCCESS");
            map.put("original", "");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping(value = "/upload", method = { RequestMethod.POST,RequestMethod.GET })
    public void upload(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        Uploader up = new Uploader(request);
        up.setSavePath("upload");
        String[] fileType = { ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
        up.setAllowFiles(fileType);
        up.setMaxSize(10000); // 单位KB
        up.upload();

        String callback = request.getParameter("callback");

        String result = "{\"name\":\"" + up.getFileName()
                + "\", \"originalName\": \"" + up.getOriginalName()
                + "\", \"size\": " + up.getSize() + ", \"state\": \""
                + up.getState() + "\", \"type\": \"" + up.getType()
                + "\", \"url\": \"" + up.getUrl() + "\"}";

        result = result.replaceAll("\\\\", "\\\\");

        if (callback == null) {
            response.getWriter().print(result);
        } else {
            response.getWriter().print(
                    "<script>" + callback + "(" + result + ")</script>");
        }
    }
}
