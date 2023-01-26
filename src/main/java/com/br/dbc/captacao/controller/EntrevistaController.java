package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.EntrevistaControllerInterface;
import com.br.dbc.captacao.dto.entrevista.EntrevistaAtualizacaoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaCreateDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.repository.enums.Legenda;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.EntrevistaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.io.IOException;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/entrevista")
public class EntrevistaController implements EntrevistaControllerInterface {

    private final EntrevistaService entrevistaService;

    @PostMapping("/marcar-entrevista")
    public ResponseEntity<EntrevistaDTO> cadastrarEntrevista(@Valid @RequestBody EntrevistaCreateDTO entrevistaCreateDTO, @RequestParam String token) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.createEntrevista(entrevistaCreateDTO, token), HttpStatus.CREATED);
    }

    @PutMapping("/atualizar-observacao-entrevista/{idEntrevista}")
    public ResponseEntity<Void> atualizarEntrevista(@PathVariable ("idEntrevista") Integer id,
                                                    String observacao) throws RegraDeNegocioException {
        entrevistaService.atualizarObservacaoEntrevista(id, observacao);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/confirmar-entrevista")
    public void confirmarEntrevista(@RequestParam @Valid String tokenEntrevista) throws RegraDeNegocioException {
        entrevistaService.confirmarEntrevista(tokenEntrevista);
        new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/atualizar-entrevista/{idEntrevista}")
    public ResponseEntity<EntrevistaDTO> updateEntrevista(@Valid @RequestBody EntrevistaAtualizacaoDTO entrevistaCreateDTO,
                                                          @PathVariable("idEntrevista") Integer id,
                                                          Legenda legenda) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.atualizarEntrevista(id, entrevistaCreateDTO, legenda), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EntrevistaDTO>> list() throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.list(), HttpStatus.OK);
    }

    @GetMapping("/por-trilha")
    public ResponseEntity<List<EntrevistaDTO>> listPorTrilha(@RequestParam(name = "trilha") String trilha) {
        return new ResponseEntity<>(entrevistaService.listPorTrilha(trilha), HttpStatus.OK);
    }

    @GetMapping("/listar-por-mes")
    public ResponseEntity<PageDTO<EntrevistaDTO>> listarMesAno(@RequestParam(defaultValue = "0") Integer pagina,
                                                               @RequestParam(defaultValue = "20") Integer tamanho,
                                                               @RequestParam Integer mes,
                                                               @RequestParam Integer ano) {
        return new ResponseEntity<>(entrevistaService.listMes(pagina, tamanho, mes, ano), HttpStatus.OK);
    }

    @GetMapping("/buscar-entrevista-email-candidato/{email}")
    public ResponseEntity<EntrevistaDTO> buscarEntrevistaPorEmailCandidato(@PathVariable ("email") String email) throws RegraDeNegocioException {
        return new ResponseEntity<>(entrevistaService.buscarPorEmailCandidato(email), HttpStatus.OK);
    }

    @GetMapping("/export-xlsx")
    public ResponseEntity<Void> exportarEntrevistasParaCsv(HttpServletResponse response) throws IOException {
        entrevistaService.exportarEntrevistasCsv(response);
        return ResponseEntity.noContent().build();
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
