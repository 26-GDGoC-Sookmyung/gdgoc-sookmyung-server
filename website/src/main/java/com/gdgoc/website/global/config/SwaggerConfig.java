package com.gdgoc.website.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI gdgocOpenAPI() {
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
                                """));
    }

}
