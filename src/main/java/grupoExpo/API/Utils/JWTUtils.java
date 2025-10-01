package grupoExpo.API.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtils {

    @Value("${security.jwt.secret}")
    private String jwtsecreto;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.expiration}")
    private long expirationMS;

    private final Logger log = LoggerFactory.getLogger(JWTUtils.class);

    public String create(String id, String correo, String rol) {
        //Decodificar el secreto Base64 y crea una clave HMAC-SHA segura
        SecretKey signingkey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtsecreto));

        //Obtener la fecha actual y calcular la fecha de expiración
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMS);

        //Construir el token con sus componentes
        return Jwts.builder()
                .setId(id) //Id del token
                .setIssuedAt(now) //Fecha de emisión
                .setSubject(correo) //Sujeto
                .claim("id", id)
                .claim("rol", rol)
                .setIssuer(issuer) //Emisor del token
                .setExpiration(expirationMS >= 0 ? expiration : null) //Tiempo de expiración
                .signWith(signingkey, SignatureAlgorithm.HS256) //Firma del algoritmo HS256
                .compact(); //Convierte a String compacto (optimización)
    }

    public String extractRol(String token){
        Claims claims = parseToken(token);
        return claims.get("rol", String.class);
    }

    /**
     * Obtiene el subject (nombre) del JWT
     * @param jwt Token JWT como String
     * @return String con el subject del token
     */
    public String getValue(String jwt){
        Claims claims = parseClaims(jwt);
        return  claims.getSubject();
    }

    /**
     * Obtiene el id del JWT
     * @param jwt
     * @return
     */
    public String getKey(String jwt){
        Claims claims = parseClaims(jwt);
        return claims.getId();
    }

    /**
     * Parsea y valida el token
     * @param jwt
     * @return
     * @throws ExpiredJwtException
     * @throws MalformedJwtException
     */
    public Claims parseToken(String jwt) throws ExpiredJwtException, MalformedJwtException {
        return parseClaims(jwt);
    }

    public String extractTokenFromRequest(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("authToken")){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Validacion del token
     * @param token
     * @return
     */
    public boolean validate(String token){
        try {
            parseClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            log.warn("Token invalido: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Metodo privado para parsear los claims de un JWT
     * @param jwt
     * @return
     */
    private Claims parseClaims(String jwt){
        //Configurar el parse con la clave de firma y parsea el token
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtsecreto)))
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
