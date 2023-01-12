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
import java.util.List;

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
    public ResponseEntity<EdicaoDTO> cadastrarEdicao(@Valid @RequestBody EdicaoDTO edicaoDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(edicaoService.createAndReturnDTO(edicaoDTO), HttpStatus.CREATED);
    }

    @GetMapping("/edicao-atual")
    public ResponseEntity<String> retornarEdicaoAtual() throws RegraDeNegocioException {
        String nomeEdicao = edicaoService.retornarEdicaoAtual();
        return new ResponseEntity<>(nomeEdicao, HttpStatus.OK);
    }

    @GetMapping("/listar-todas")
    public ResponseEntity<List<EdicaoDTO>> list() {
        return new ResponseEntity<>(edicaoService.list(), HttpStatus.OK);
    }
}
