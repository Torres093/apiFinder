package grupoExpo.API.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Filtro se ejecuta una vez por cada solicitud HTTP
 * Componente gestionado por Spring
 */
@Component
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtCookieAuthFilter.class);

    private static final String auth_cookie_name = "authToken";

    private final JWTUtils jwtUtils;

    @Autowired
    public JwtCookieAuthFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        //Corregido: Mejor lógica para los endpoints públicos
        if (isPublicEndpoint(request)){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractTokenFromCookies(request);

            if (token == null || token.isBlank()){
                //Para endpoints no públicos, requerimos token
                if (isPublicEndpoint(request)){
                    sendError(response, "Token no encontrado", HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                filterChain.doFilter(request, response);
                return;
            }
            Claims claims = jwtUtils.parseToken(token);

            //Extraer el rol real del token
            String rol = jwtUtils.extractRol(token);

            //Crear authorities basado en el rol real
            Collection<? extends GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol));

            //Crear autenticación con authorities correctos
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(), //Username
                            null, // credentials
                            authorities // <- Roles reales
                    );

            //Establecer autenticación en contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        }catch (ExpiredJwtException e){
            log.warn("Token expirado: {}", e.getMessage());
            sendError(response, "Token expirado", HttpServletResponse.SC_UNAUTHORIZED);
        }catch (MalformedJwtException e){
            log.warn("Token malformado: {}", e.getMessage());
            sendError(response, "Token invalido", HttpServletResponse.SC_FORBIDDEN);
        }catch (Exception e){
            log.error("Error de autenticacion", e);
            sendError(response, "Error de autenticacion", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String extractTokenFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> auth_cookie_name.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void sendError(HttpServletResponse response, String message, int status) throws IOException{
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(String.format(
                "{\"error\": \"%s\", \"status\": %d}", message, status
        ));
    }

    //Mejorada: lógica para endpoints públicos
    private boolean isPublicEndpoint(HttpServletRequest request){
        String path = request.getRequestURI();
        String method = request.getMethod();

        //Endpoints públicos
        return (path.equals("/api/authLogin") && "POST".equals(method) ||
                (path.equals("/api/authRegister") && "POST".equals(method)) ||
                (path.equals("/api/public/") && "GET".equals(method)));
    }
}
