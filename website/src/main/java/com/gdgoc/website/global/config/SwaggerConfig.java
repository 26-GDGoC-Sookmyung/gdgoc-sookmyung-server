package com.gdgoc.website.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI gdgocOpenAPI() {

        String jwtSchemeName = "BearerAuth";
        SecurityScheme securityScheme = new SecurityScheme()
            .name(jwtSchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList(jwtSchemeName);

        return new OpenAPI()
                .info(new Info()
                        .title("GDGoC Website API")
                        .version("v1.0.0")
                        .description("""
                                ### 공통 응답 형식
                                
                                #### 성공 응답 예시
                                ```json
                                {
                                    "success": true,
                                    "code": "OK",
                                    "message": "요청이 성공적으로 처리되었습니다.",
                                    "data": { ... }
                                }
                                ```
                                
                                #### 실패 응답 예시
                                ```json
                                {
                                    "success": false,
                                    "code": "INVALID_INPUT_VALUE",
                                    "message": "필수 입력 항목이 누락되었거나 잘못된 형식입니다.",
                                    "data": null
                                }
                                ```

                                ### 자주 사용되는 에러 코드
                                - INVALID_INPUT_VALUE (400)
                                    - 요청 값이 유효하지 않은 경우
                                    - message에는 실제 검증 실패 사유가 포함됩니다.
                                - *_NOT_FOUND (404)
                                    - e.g. USER_NOT_FOUND
                                    - 요청한 데이터를 찾을 수 없는 경우
                                - INTERNAL_SERVER_ERROR (500)
                                    - 서버에서 예상치 못한 에러가 발생한 경우
                                
                                API별로 발생 가능한 추가 에러는 각 API 문서를 참고해주세요.
                                
                                ### 참고사항 ⭐
                                - 일부 필드(예: Enum 값, 전화번호 형식)의 상세 설명은 각 API의 Request Body → Schema 탭에서 확인할 수 있습니다.
                                - API별 예외 상황, 에러 코드 및 상세 설명은 노션 'Back-End > API별 예외 상황' 페이지를 참고해주세요.
                                """))
                .addSecurityItem(securityRequirement)
                        .components(new Components()
                        .addSecuritySchemes(jwtSchemeName, securityScheme));
    }

}
