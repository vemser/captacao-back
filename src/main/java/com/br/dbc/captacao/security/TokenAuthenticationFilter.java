package com.br.dbc.captacao.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private static String originToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String authorization = request.getHeader("Authorization");

            UsernamePasswordAuthenticationToken dtoToken = tokenService.isValid(authorization);
            originToken = authorization;
            SecurityContextHolder.getContext().setAuthentication(dtoToken);

            filterChain.doFilter(request, response);
        }catch(ExpiredJwtException e) {
            response.setStatus(403);
        }
    }

    public static String getOriginToken() {
        return originToken;
    }
}
