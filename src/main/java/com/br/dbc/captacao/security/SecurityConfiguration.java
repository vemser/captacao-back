package com.br.dbc.captacao.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final TokenService tokenService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.headers()
                .frameOptions().disable().and()
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests((auth) -> auth.antMatchers("/", "/auth/**").permitAll()

                        .antMatchers(HttpMethod.POST, "/formulario/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/formulario/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/fomulario/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/fomulario/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")

                        .antMatchers(HttpMethod.POST, "/inscricao/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/inscricao/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/incricao/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/inscricao").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")

                        .antMatchers(HttpMethod.POST, "/candidato/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/candidato/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/candidato/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/candidato/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")

                        .antMatchers("/usuario/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")

                        .antMatchers(HttpMethod.GET, "/entrevista/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")
                        .antMatchers(HttpMethod.POST, "/entrevista/**").hasAnyRole( "GESTAO_DE_PESSOAS", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/entrevista/**").hasAnyRole( "GESTAO_DE_PESSOAS", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/entrevista/**").hasAnyRole( "GESTAO_DE_PESSOAS", "ADMIN")

                        .antMatchers("/avaliacao/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")

                        .antMatchers("/trilha/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")

                        .antMatchers("/edicao/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")

                        .antMatchers("/linguagem/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")

                        .antMatchers("/imagem/**").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")

                        .anyRequest().authenticated()

                );

        http.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> web.ignoring().antMatchers("/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .exposedHeaders("Authorization");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
