package gdsc.speacher.login;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }/*

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns ("/", "/api/members", "/api/members/duplicate/loginId", "/api/members/duplicate/nickname",
                        "/mailConfirm", "/api/members/duplicate/phoneNumber", "/api/members/login",
                        "/api/acceptStatusCategories", "/api/bloodDonationCategories", "/api/bloodTypeCategories",
                        "/api/questionTypeCategories", "/api/requestStatusCategories",
                        "/css/**", "/*.ico", "/error", "/session-info");


    }*/
}
