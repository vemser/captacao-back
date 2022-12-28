package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.EdicaoControllerInterface;
import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.EdicaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/edicao")
public class EdicaoController implements EdicaoControllerInterface {
    private final EdicaoService edicaoService;

    @DeleteMapping("/delete-fisico/{idEdicao}")
    public ResponseEntity<Void> deleteFisico(@PathVariable("idEdicao") Integer id) throws RegraDeNegocioException {
        edicaoService.deleteFisico(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/criar-edicao")
    public ResponseEntity<EdicaoDTO> cadastrarEdicao(@Valid @RequestBody EdicaoDTO edicaoDTO) {
        return new ResponseEntity<>(edicaoService.createAndReturnDTO(edicaoDTO), HttpStatus.CREATED);
    }

    @GetMapping("/edicao-atual")
    public ResponseEntity<String> retornarEdicaoAtual(){
       String nomeEdicao= edicaoService.RetornarEdicaoAtual();
        return new ResponseEntity<>(nomeEdicao,HttpStatus.OK);
    }
}
