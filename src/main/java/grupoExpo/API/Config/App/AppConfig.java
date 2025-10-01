package grupoExpo.API.Config.App;

import grupoExpo.API.Utils.JWTUtils;
import grupoExpo.API.Utils.JwtCookieAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public JwtCookieAuthFilter jwtCookieAuthFilter(JWTUtils jwtUtils){
        return new JwtCookieAuthFilter(jwtUtils);
    }
}
