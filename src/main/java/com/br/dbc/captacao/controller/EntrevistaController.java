package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.EntrevistaControllerInterface;
import com.br.dbc.captacao.dto.entrevista.EntrevistaAtualizacaoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaCreateDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.enums.Legenda;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.EntrevistaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/entrevista")
public class EntrevistaController implements EntrevistaControllerInterface {

    private final EntrevistaService entrevistaService;

    @PutMapping("/atualizar-observacao-entrevista/{idEntrevista}")
    public ResponseEntity<Void> atualizarEntrevista(@PathVariable ("idEntrevista") Integer id,
                                                    String observacao) throws RegraDeNegocioException {
        entrevistaService.atualizarObservacaoEntrevista(id, observacao);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar-entrevista/{idEntrevista}")
    public ResponseEntity<EntrevistaDTO> updateEntrevista(@Valid @RequestBody EntrevistaAtualizacaoDTO entrevistaCreateDTO,
                                                          @PathVariable("idEntrevista") Integer id,
                                                          Legenda legenda) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.atualizarEntrevista(id, entrevistaCreateDTO, legenda), HttpStatus.OK);
    }

    @PostMapping("/marcar-entrevista")
    public ResponseEntity<EntrevistaDTO> cadastrarEntrevista(@Valid @RequestBody EntrevistaCreateDTO entrevistaCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.createEntrevista(entrevistaCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageDTO<EntrevistaDTO>> list(@RequestParam(defaultValue = "0") Integer pagina,
                                                       @RequestParam(defaultValue = "20") Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.list(pagina, tamanho), HttpStatus.OK);
    }

    @GetMapping("/listar-por-mes")
    public ResponseEntity<PageDTO<EntrevistaDTO>> listarMesAno(@RequestParam(defaultValue = "0") Integer pagina,
                                                               @RequestParam(defaultValue = "20") Integer tamanho,
                                                               @RequestParam Integer mes,
                                                               @RequestParam Integer ano) {
        return new ResponseEntity<>(entrevistaService.listMes(pagina, tamanho, mes, ano), HttpStatus.OK);
    }

    @GetMapping("/exportar-entrevista-para-csv")
    public ResponseEntity<Void> exportarEntrevistaParaCsv() throws RegraDeNegocioException {
        entrevistaService.exportarEntrevistaCSV();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar-entrevista-email-candidato/{email}")
    public ResponseEntity<EntrevistaDTO> buscarEntrevistaPorEmailCandidato(@PathVariable ("email") String email) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.buscarPorEmailCandidato(email), HttpStatus.OK);
    }

    @DeleteMapping("/{idEntrevista}")
    public ResponseEntity<Void> deletarEntrevista(@PathVariable("idEntrevista") Integer id) throws RegraDeNegocioException {
        entrevistaService.deletarEntrevista(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletar-entrevista-email-candidato/{email}")
    public ResponseEntity<Void> deletarEntrevistaEmailCandidato(@PathVariable ("email") String email) throws RegraDeNegocioException {
        entrevistaService.deletarEntrevistaEmail(email);
        return ResponseEntity.noContent().build();
    }
}
