//package com.pzy.codegenerator.common.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//import java.util.ArrayList;
//
//
///**
// * swagger配置
// *
// * @author pzy
// */
//@EnableOpenApi
//@Configuration
//public class SwaggerConfig {
//
//    @Value("${config.swagger3.flag}")
//    private boolean flag;
//
//    @Bean
//    public Docket docket(Environment environment) {
//        // 指定3.0版本
//        return new Docket(DocumentationType.OAS_30)
//                .apiInfo(apiInfo())
//                .enable(flag)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.pzy"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//
//        return new ApiInfo("pzy",
//                "code",
//                "v1.0",
//                "",
//                new Contact("pzy", "pzy", "pzy1013@163.com"),
//                "Apache 2.0",
//                "http://www.apache.org/licenses/LICENSE-2.0",
//                new ArrayList());
//    }
//}
