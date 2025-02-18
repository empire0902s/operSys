package com.shq.oper.controller;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shq.oper.model.dto.AdminDto;
import com.shq.oper.util.Constant;

import lombok.extern.slf4j.Slf4j;

/**
 * controller基类
 * @author tjun
 */
@Slf4j
public class BaseController {
	
	protected AdminDto getAdmin(HttpServletRequest request) {
		return (AdminDto)request.getSession().getAttribute(Constant.OPER_USER);
	}
	
	protected String getCreateOrUpdateAdminName(HttpServletRequest request) {
		return getAdmin(request).getName()+"-"+getAdmin(request).getId();
	}
	
	/**
	 * 根据url规则生成的页面视图</br>
	 * /user/index 对应 front/user/user-index.jsp </br>
	 * /m/user/index 对应 front/m/user/user-index.jsp </br>
	 * /admin/user/index 对应 admin/user/admin-user-index.jsp </br>
	 * @param request
	 * @return
	 */
	protected ModelAndView toPage(HttpServletRequest request) {
		String contextPath = request.getContextPath(), uri = (String)request.getAttribute(org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		if(!StringUtils.isEmpty(contextPath) && uri.indexOf(contextPath)>=0){
			uri = uri.substring(contextPath.length());
		}
		int index = uri.indexOf("/{");
		if(index > 0){
			uri = uri.substring(0, index);
		}
		RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);
		String mappingUrl = "", pageUrl = "";
		if(requestMapping == null){
			pageUrl = uri;
		}else{
			String[] mappingUrls = requestMapping.value();
			for (String  url: mappingUrls) {
				if(uri.startsWith(url)){
					mappingUrl = url;
					pageUrl = uri.substring(url.length());
					break;
				}
			}
		}
		
		if(pageUrl.isEmpty()){
			return toPage(mappingUrl, request);
		}
		if(mappingUrl.isEmpty()){
			return toPage(pageUrl, request);
		}
		
		String[] pages = mappingUrl.concat(pageUrl).split("/");
		String page = Stream.of(pages).filter(t -> !t.isEmpty()).collect(Collectors.joining("-"));
		return toPage(String.format("%s/%s", mappingUrl, page), request);
	}
	/**
	 * 跳转页面，根据访问url地址，跳转到对应端</br>
	 * 默认跳转到 front 目录下</br>
	 * 以/m/开头的，统一跳转到front/m/ 目录</br>
	 * 以/admin/开头的，统一跳转到admin/ 目录</br>
	 * </br>
	 * 
	 * 如：</br>
	 * /user/index 对应目录文件 front/user/index.jsp</br>
	 * /m/user/index 对应目录文件 front/m/user/index.jsp</br>
	 * /admin/user/index 对应目录文件 admin/user/index.jsp</br>
	 * 
	 */
	protected ModelAndView toPage(String page, HttpServletRequest request) {
		String viewName = getViewName(page, request.getRequestURI());
		request.setAttribute("viewName", viewName);
		return new ModelAndView(viewName);
	}
	
	private String getViewName(String page, String uri) {
		if(page.startsWith("/")){
			page = page.substring(1);
		}
		return page;
	}
}