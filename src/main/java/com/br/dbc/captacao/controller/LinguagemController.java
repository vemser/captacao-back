package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.LinguagemControllerInterface;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.LinguagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/linguagem")
public class LinguagemController implements LinguagemControllerInterface {
    private final LinguagemService linguagemService;

    @DeleteMapping("/delete-fisico/{idLinguagem}")
    public ResponseEntity<GestorDTO> deleteFisico(@PathVariable("idLinguagem") Integer id) throws RegraDeNegocioException {
        linguagemService.deleteFisico(id);
        return ResponseEntity.noContent().build();
    }
}
