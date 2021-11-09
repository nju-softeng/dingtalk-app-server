package com.softeng.dingtalk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2 // 我下的swagger 3.0.0
public class SwaggerConfig {
    @Bean
    public Docket docket(Environment environment) {
        // 生产环境禁用
        var enable = !environment.acceptsProfiles(Profiles.of("prod"));
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.softeng.dingtalk.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Dingtalk Api Documentation",
                "钉钉绩效系统后端接口文档",
                "1.0",
                "urn:tos",
                new Contact("", "https://github.com/zhanyeye/dingtalk-springboot", ""),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
