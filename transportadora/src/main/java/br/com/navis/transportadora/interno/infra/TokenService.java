package br.com.navis.transportadora.interno.infra;

import br.com.navis.transportadora.interno.domain.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Adiciona o usuário no retorno
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", usuario.getId());
            claims.put("login", usuario.getLogin());
            claims.put("role", usuario.getRole().toString()); // Converte o enum para String

            String token = JWT.create()
                    .withIssuer("transportadora")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(genExpirationDate())
                    .withClaim("usuario", claims)
                    .sign(algorithm);
            log.info("Token was generated successfully {}", token);
            return token;
        } catch (JWTCreationException exception) {
            log.error("Error while trying to create token {}", exception.getMessage());
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            log.info("Token was validated successfully {}");
            return JWT.require(algorithm)
                    .withIssuer("transportadora") // verifica se não auth-api
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            log.error("Error while try to validate token {}");
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(48).toInstant(ZoneOffset.of("-03:00"));
    }
}