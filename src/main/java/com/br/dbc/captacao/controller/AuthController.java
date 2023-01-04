package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.AuthControllerInterface;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.security.TokenService;
import com.br.dbc.captacao.service.AuthService;
import com.br.dbc.captacao.service.GestorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController implements AuthControllerInterface {
    private final GestorService gestorService;
    private final TokenService tokenService;
    private final AuthService authService;


    @PutMapping("/confirmar-entrevista")
    public void confirmarEntrevista(@RequestParam @Valid String tokenEntrevista) throws RegraDeNegocioException {
        authService.confirmarEntrevista(tokenEntrevista);
        new ResponseEntity<>(null, HttpStatus.OK);
    }
}
