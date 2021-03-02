package kr.co.emotion.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 정의
 *
 * @since 2021-02-19 @author 류성재
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Cross Domain 설정
     *
     * 현재 전체 허용한 상태
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

}
