package com.shq.oper.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.shq.oper.controller.BaseController;
import com.shq.oper.model.dto.yml.SystemProperties;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/comm/open")
public class CommOpenController extends BaseController{
    
	@Autowired
	private SystemProperties systemProperties;
    
	@RequestMapping(value = {"/{url}"})
	public ModelAndView url(HttpServletRequest request, @PathVariable(value="url") String url) {
		log.debug("====={url}==>"+url);
		url = "comm/open/comm-open-".concat(url);
		return toPage(url, request);
	}
	
	
}