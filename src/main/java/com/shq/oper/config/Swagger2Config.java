package com.shq.oper.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Predicate;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.SwaggerDefinition.Scheme;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 配置
 * 文档首页：http://localhost:8080/swagger-ui.html
 *
 * @author tjun
 */
@Configuration
@EnableSwagger2
@SwaggerDefinition(schemes = Scheme.HTTP, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        Predicate<RequestHandler> predicate = new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
            	// 只有标注了注解ApiOperation，并且返回的类型不是ModelAndView
            	if(input.isAnnotatedWith(ApiOperation.class) && !input.getReturnType().isInstanceOf(ModelAndView.class)){
            		return true;
            	}
                return false;
            }
        };
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(predicate)
                .apis(RequestHandlerSelectors.basePackage("com.shq.oper.controller.api"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口文档")
                .contact(new Contact("添军", "http://www.520shq.com/", "871827928@qq.com"))
                .version("1.0")
                .build();
    }

}