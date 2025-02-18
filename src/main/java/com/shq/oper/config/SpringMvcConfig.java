package com.shq.oper.config;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.google.common.collect.Maps;
import com.shq.oper.interceptor.AuthInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring MVC 配置
 *
 * @author tjun
 */
@Configuration
@Slf4j
public class SpringMvcConfig extends WebMvcConfigurerAdapter {
    /**
     * 打印堆栈和入参
     */
    private void printStackTrace(HttpServletRequest request, Throwable e) {
        Map<String, String[]> params = request.getParameterMap();
        Map<String, Object> map = Maps.transformValues(
                params, arr -> arr.length == 0 ? null : arr.length == 1 ? arr[0] : arr
        );
        log.error("handle:{}, params:{}", e.getClass().getName(), map, e);
    }
    
	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.add(new HandlerExceptionResolver() {
			@Override
			public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
				// 处理返回值是页面的请求时，返回错误页面，否则返回异常消息
				if(handler instanceof HandlerMethod){
					HandlerMethod handlerMethod = (HandlerMethod) handler;
					if(!handlerMethod.getReturnType().getMethod().getReturnType().isAssignableFrom(ModelAndView.class)){
						printStackTrace(request, ex);
						ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
						mav.addObject("ok", false);
						mav.addObject("msg", "后台操作异常");
						mav.addObject("info", ex.getLocalizedMessage());
						return mav;
					}
				}
				return null;
			}
		});
	}
	
	@Bean  
    public AuthInterceptor pageAuthInterceptor() {  
        return new AuthInterceptor();  
    }  
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
		.addInterceptor(pageAuthInterceptor())
		.addPathPatterns("/**")
		.excludePathPatterns("/navMenus", "/error", "/logout/**", "/token/**", "/login/**","/loginAdmin/**", "/common/**"
				,"/apiDocs/**","/api/**"
				//,"/swagger-resources/**","/api-docs","/v2/api-docs", "/webjars/**"	//Swagger2 Api 配置
				);
	}
	
    /**
     * 设置路径匹配
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                // 是否开启后缀模式匹配，如 /user 是否匹配 /user.*，默认true
                .setUseSuffixPatternMatch(false)
                // 是否开启后缀路径模式匹配，如 /user 是否匹配 /user/，默认true
                .setUseTrailingSlashMatch(true);
    }

    /**
     * 内容判断视图解析器配置
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                //拓展名支持，即/user.json => application/json
                .favorPathExtension(false)
                //url参数支持，即/user?format=json => application/json
                .favorParameter(true)
                //自定义url参数
                .parameterName("data-format")
                //忽略Http Accept Header的支持
                .ignoreAcceptHeader(true)
                //json => application/json
                .mediaType("json", MediaType.APPLICATION_JSON_UTF8)
                //html => text/html
                .mediaType("html", MediaType.TEXT_HTML);
    }
    

    /**
     * 将对于静态资源的请求转发到 Servlet 容器的默认处理静态资源的 Servlet
     * 因为将 Spring 的拦截模式设置为 "/" 时会对静态资源进行拦截
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}