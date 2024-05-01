package br.com.navis.transportadora.interno.infra;

import br.com.navis.transportadora.config.hibernate.TenantContext;
import br.com.navis.transportadora.config.hibernate.TenantInterceptor;
import br.com.navis.transportadora.interno.domain.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final TenantInterceptor tenantInterceptor;

    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository, TenantInterceptor tenantInterceptor) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.tenantInterceptor = tenantInterceptor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Chamando TenantInterceptor para definir o contexto do tenant
            // tenantInterceptor.preHandle(request, response, null);

            String requestURI = request.getRequestURI();

            Optional.ofNullable(request.getHeader("tenant"))
                .map(String::toUpperCase)
                .ifPresent(TenantContext::setCurrentTenant);

            var token = this.recoverToken(request);
            if (token != null) {
                var login = tokenService.validateToken(token);
                UserDetails user = usuarioRepository.findByLogin(login);

                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // Tratamento de erro
            log.error("Erro ao processar a solicitação: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
