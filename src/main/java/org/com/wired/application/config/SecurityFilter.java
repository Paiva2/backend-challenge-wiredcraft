package org.com.wired.application.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.com.wired.adapters.infra.config.SecurityUserImpl;
import org.com.wired.adapters.strategy.infra.auth.AuthenticationStrategyConcrete;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private static final String[] AUTH_WHITELIST = {
        // Swagger
        "/v3/api-docs/**",
        "/swagger-ui/**",
        // Application
        "/api/user/register",
        "/api/user/auth"
    };

    private final AuthenticationStrategyConcrete authenticationStrategyConcrete;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getBearerToken(request);

        if (token == null || token.isEmpty()) {
            response.sendError(403, "Authorization Token missing!");
            return;
        }

        try {
            String subjectToken = authenticationStrategyConcrete.verifyAuthentication(token);
            Long subjectId = Long.parseLong(subjectToken);
            User user = userRepositoryPort.findById(subjectId).orElseThrow(UserNotFoundException::new);

            List<String> roles = authenticationStrategyConcrete.getTokenClaims(token);

            if (roles == null || roles.isEmpty()) {
                response.sendError(403, "Not allowed!");
                return;
            }

            UserDetails userDetails = new SecurityUserImpl(subjectId.toString(), user.getDisabledAt(), roles, user.getPassword());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            response.sendError(403, exception.getMessage());
        }
    }

    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        return List.of(AUTH_WHITELIST).contains(request.getServletPath());
    }

    private String getBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.replace("Bearer ", "");
        }

        return null;
    }
}
