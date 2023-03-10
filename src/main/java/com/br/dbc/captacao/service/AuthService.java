package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.login.LoginDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.EntrevistaEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.EntrevistaRepository;
import com.br.dbc.captacao.enums.Legenda;
import com.br.dbc.captacao.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GestorService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final EntrevistaService entrevistaService;
    private final CandidatoService candidatoService;
    private final EntrevistaRepository entrevistaRepository;

    public GestorEntity auth(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getSenha()
                );
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        Object principal = authentication.getPrincipal();
        GestorEntity usuarioEntity = (GestorEntity) principal;
        return usuarioEntity;
    }

    public void confirmarEntrevista(String token) throws RegraDeNegocioException {
        EntrevistaEntity entrevista = procurarCandidato(token);
        entrevista.setLegenda(Legenda.CONFIRMADA);
        entrevistaRepository.save(entrevista);
    }

    public String procurarUsuario(String token) throws RegraDeNegocioException {
        String cpfByToken = tokenService.getEmailByToken(token);
        return usuarioService.findByEmail(cpfByToken).getEmail();
    }

    public EntrevistaEntity procurarCandidato(String token) throws RegraDeNegocioException {
        String emailCandidatoByToken = tokenService.getEmailByToken(token);
        CandidatoEntity candidatoEntity = candidatoService.findByEmailEntity(emailCandidatoByToken);
        return entrevistaService.findByCandidatoEntity(candidatoEntity);
    }
}
