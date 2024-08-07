package com.spoonsors.spoonsorsserver.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "더 새로워진, 모두의 한 끼 API 명세서",
                version = "v2",
                description = "모두의 한 끼V2에서 제공하는 API 목록입니다."
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi all() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("전체 API")
                .pathsToMatch(paths)
                .build();
    }

    //TODO: 그룹핑 리팩토링 필요
    @Bean
    public GroupedOpenApi benefitGroup() {
        String[] paths = {"/bMember/**", "/mealplanners/**"};

        return GroupedOpenApi.builder()
                .group("수혜자 API")
                .pathsToMatch(paths)
                .build();
    }
    @Bean
    public GroupedOpenApi sponsorGroup() {
        String[] paths = {"/sMembers/**"};

        return GroupedOpenApi.builder()
                .group("후원자 API")
                .pathsToMatch(paths)
                .build();
    }
    @Bean
    public GroupedOpenApi userGroup() {
        String[] paths = {"/member/**"};

        return GroupedOpenApi.builder()
                .group("전체 사용자 API")
                .pathsToMatch(paths)
                .build();
    }
    @Bean
    public GroupedOpenApi adminGroup() {
        String[] paths = {"/manager/**", "/nutritionist/**"};

        return GroupedOpenApi.builder()
                .group("관리자 & 영양사 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new io.swagger.v3.oas.models.security.SecurityScheme()
                                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .name("Authorization")
                                                .in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
                                )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
