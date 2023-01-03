package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.AuthControllerInterface;
import com.br.dbc.captacao.dto.login.LoginDTO;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.security.TokenService;
import com.br.dbc.captacao.service.AuthService;
import com.br.dbc.captacao.service.GestorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController implements AuthControllerInterface {
    private final GestorService gestorService;
    private final TokenService tokenService;
    private final AuthService authService;

    @PostMapping("/fazer-login")
    public ResponseEntity<String> auth(@RequestBody @Valid LoginDTO loginDTO) {
        GestorEntity gestorEntity = authService.auth(loginDTO);
        return new ResponseEntity<>(tokenService.getToken(gestorEntity, null), HttpStatus.CREATED);
    }

//    @PostMapping("/solicitar-troca-senha")
//    public void trocarSenha(@RequestParam @Valid String email) throws RegraDeNegocioException {
//        authService.trocarSenha(email);
//        new ResponseEntity<>(null, HttpStatus.CREATED);
//    }
//
//    @PostMapping("/trocar-senha")
//    public void trocarSenhaAuntenticado(@RequestParam String token) throws RegraDeNegocioException {
//        String email = authService.procurarUsuario(token);
//        gestorService.atualizarSenhaUsuario(email);
//        new ResponseEntity<>(null, HttpStatus.CREATED);
//    }

    @PutMapping("/confirmar-entrevista")
    public void confirmarEntrevista(@RequestParam @Valid String tokenEntrevista) throws RegraDeNegocioException {
        authService.confirmarEntrevista(tokenEntrevista);
        new ResponseEntity<>(null, HttpStatus.OK);
    }
}
