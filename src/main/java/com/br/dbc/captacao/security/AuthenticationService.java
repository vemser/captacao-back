package com.br.dbc.captacao.security;

import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.service.GestorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final GestorService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<GestorEntity> usuarioEntityOptional = usuarioService.findByEmail(username);
        return usuarioEntityOptional
                .orElseThrow(() -> new UsernameNotFoundException("Usuário inválido"));
    }
}
