package com.br.dbc.captacao.security;

import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.GestorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final GestorService gestorService;

    @Override
    public UserDetails loadUserByUsername(String loginUsername) throws UsernameNotFoundException {
        try {
            GestorEntity usuarioOptional = gestorService.findByEmail(loginUsername);
            return (UserDetails) usuarioOptional;
        } catch (RegraDeNegocioException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
