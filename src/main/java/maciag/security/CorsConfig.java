package maciag.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/users/**")
                        .allowedOrigins("http://localhost:3000"
                                ,"https://your-planner.herokuapp.com")
                        .allowedMethods("GET","POST", "PUT", "DELETE")
                        .maxAge(3600);
                registry.addMapping("/tasks/**")
                        .allowedOrigins("http://localhost:3000"
                                ,"https://your-planner.herokuapp.com")
                        .allowedMethods("GET","POST", "PUT", "DELETE")
                        .maxAge(3600);
                registry.addMapping("/home")
                        .allowedOrigins("http://localhost:3000"
                                ,"https://your-planner.herokuapp.com")
                        .allowedMethods("GET","POST", "PUT", "DELETE")
                        .maxAge(3600);
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET","POST", "PUT", "DELETE")
                        .maxAge(3600);
            }
        };
    }
}