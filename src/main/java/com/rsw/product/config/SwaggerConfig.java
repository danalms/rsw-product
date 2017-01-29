package com.rsw.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by DAlms on 10/2/16.
 * Reference: http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        return docket.select()
                .apis(RequestHandlerSelectors.basePackage("com.rsw.product.controller"))
                .paths(PathSelectors.ant("/api/**"))
                .build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Demo REST API",
                "Demonstration of cloud-based API",
                "API v1",
                "Terms of service",
                new Contact("Dan Alms", "moreInfo", "dan@rosssoftwareworks.com"),
                "Licensing Here",
                "API license URL");
        return apiInfo;
    }
}
