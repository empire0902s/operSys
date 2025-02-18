package com.shq.oper.config;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import com.shq.oper.model.dto.yml.SystemProperties;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.util.CommRequestUtil;

import lombok.extern.slf4j.Slf4j;

@Order(99)
// 重点
@WebFilter(filterName = "webLogFilter", urlPatterns = "/api/wlog.gif")
@Slf4j
public class TestFilterFirst implements Filter {
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	
	@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String referer = request.getHeader("Referer");
		String remortIP = CommRequestUtil.getRemortIP(request);
		SystemProperties systemProperties = SpringContextHolder.getBean(SystemProperties.class);
        String remoteUrl = request.getHeader("Origin");
		if(systemProperties != null && systemProperties.getWeblogTest() != null) {
			log.debug("-----"+remortIP+"------"+referer+"--------Origin==="+ request.getHeader("Origin")+"=====");
			if(systemProperties.getWeblogTest()) {//测试环境。通用访问
				//通用跨域
				response.setHeader("Access-Control-Allow-Origin", remoteUrl);  
				response.setHeader("Access-Control-Allow-Credentials", "true");  
				
		        response.setHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");  
		        response.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST");  
		        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type"); 
		        
			}else {
				//允许跨域请求的 域名
				List<String> originList = systemProperties.getOriginList();
				String origin = request.getHeader("Origin");
				if(!StringUtils.isEmpty(origin) && originList.contains(origin)) {
					//通用跨域
					response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
					response.setHeader("Access-Control-Allow-Credentials", "true");  
					
			        response.setHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");  
			        response.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST");  
			        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type"); 
				}
			}
		}else {
			log.debug("null=systemProperties-----"+remortIP+"------"+referer+"--------Origin==="+ request.getHeader("Origin")+"=====");
		}
		 chain.doFilter(req, res);
	}

	@Override
	public void destroy() {

	}
}