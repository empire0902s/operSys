package com.shq.oper.interceptor;

import java.time.LocalDateTime;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shq.oper.dao.mongo.AdminActionLogMongo;
import com.shq.oper.model.domain.mongo.AdminActionLog;
import com.shq.oper.model.dto.AdminDto;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.util.CommRequestUtil;
import com.shq.oper.util.Constant;
import com.shq.oper.util.JsonUtils;
import com.shq.oper.util.MemoryViewUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 权限拦截器
 * @author tjun
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor{
	
	@Autowired
	private AdminActionLogMongo adminActionLogMongo;
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
		HttpSession session = req.getSession();
		String action = req.getRequestURI();
		String referer = req.getHeader("Referer");
		String ip = CommRequestUtil.getRemortIP(req);
		
		log.debug(MemoryViewUtil.showMemoryStr());
		
		
		if(action.contains("/message/sendVerificationCode")) {
			return true;
		}
		// 未登录，跳转到登录
		AdminDto admin = (AdminDto)session.getAttribute(Constant.OPER_USER);
		log.info("------enter---[{}]{}------{}",ip,action,admin==null?"未登录":admin.getName());
		if(admin == null){
			if(isAjaxRequest(req)){
				// ajax请求不自动跳转，返回未登录信息
				resp.setCharacterEncoding("UTF-8");
				resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
				Msg<String> msg = new Msg<String>(false, "会话超时,请刷新页面重新登录!", "", 1000);
			    resp.getWriter().print(new ObjectMapper().writeValueAsString(msg));
			    resp.getWriter().flush();
				return false;
			}
			
			resp.sendRedirect(req.getContextPath()+"/login");
			return false;
		}else{
			
			Map<String,String> parMap = CommRequestUtil.getRequestMap(req);
			AdminActionLog adminActionLog = new AdminActionLog();
			adminActionLog.setAction(action);
			adminActionLog.setRefAction(referer);
			adminActionLog.setAdminId(admin.getId());
			adminActionLog.setAdminName(admin.getName());
			adminActionLog.setCreateTime(LocalDateTime.now());
			adminActionLog.setIp(ip);
			adminActionLog.setActionPara(JsonUtils.toDefaultJson(parMap));
			if(action.contains("mongo/adminactionlog")) {
				log.debug("adminactionlog-----------"+JsonUtils.toDefaultJson(adminActionLog));
			}else {
				adminActionLogMongo.insert(adminActionLog);
			}
			
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		
	}

	private boolean isAjaxRequest(HttpServletRequest request){
		String header = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(header);  
	}
}
