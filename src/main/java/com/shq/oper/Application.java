package com.shq.oper;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import lombok.extern.slf4j.Slf4j;

/**
 * SpringBoot 启动类
 *
 * @author tjun
 */
//启用缓存
@EnableCaching	
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})  
@ServletComponentScan
@Slf4j
public class Application  extends SpringBootServletInitializer{

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
		    
    public static void main(String[] args) {
        System.setProperty("druid.logType", "slf4j");
        SpringApplication.run(Application.class, args);
        log.info("this project is running...");
    }

    @Controller
    public static class WelcomeController {
    	@RequestMapping("/")
        public ModelAndView index(HttpServletRequest request) {
            return new ModelAndView(new RedirectView(request.getContextPath()+"/index"));
        }
    	
		@GetMapping("/apiDocs")
		public ModelAndView apidocs(HttpServletRequest request){
			return new ModelAndView(new RedirectView(request.getContextPath()+"/swagger-ui.html"));
		}
    }


}