package grupoExpo.API.Config.Security;

import grupoExpo.API.Utils.JwtCookieAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtCookieAuthFilter jwtCookieAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtCookieAuthFilter jwtCookieAuthFilter, CorsConfigurationSource corsConfigurationSource) {
        this.jwtCookieAuthFilter = jwtCookieAuthFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }


    //Configuración de seguridad HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //Aqui van todos los endpoints públicos que no requieren de un JWT
        http
                //Csfr -> Son llamadas o donde se pueden hacer peticiones de origen desconocido
                .csrf(csrf -> csrf.disable()) //Nuevo estilo lambda, deshabilita que la Api pueda ser llamada desde cualquier lugar
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // <- Configura CORS aqui
                .authorizeHttpRequests(auth -> auth // Cambia authorizeRequests por authorizeHttpRequests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // <- Permite prefLight requests
                        .requestMatchers(HttpMethod.POST,
                                "/api/authLogin",
                                "/api/authRegister",
                                "/api/authLogout")
                        .permitAll()
                        .requestMatchers("/api/authMe").authenticated()

                        //Endpoints específicos
                        .requestMatchers("/api/testAdminOnly").hasRole("Administrador")
                        .requestMatchers("/api/testEmpleadoOnly").hasRole("Empleado")
                        .requestMatchers("/api/testClienteOnly").hasRole("Cliente")
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //Exponer el AuthenticationManager como bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
