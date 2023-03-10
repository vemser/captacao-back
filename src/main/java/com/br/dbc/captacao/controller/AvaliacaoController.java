package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.AvaliacaoControllerInterface;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoCreateDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/avaliacao")
public class AvaliacaoController implements AvaliacaoControllerInterface {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<AvaliacaoDTO> create(@RequestBody AvaliacaoCreateDTO avaliacaoCreateDto, @RequestParam String token) throws RegraDeNegocioException {
        AvaliacaoDTO avaliacaoDto = avaliacaoService.create(avaliacaoCreateDto, token);
        return new ResponseEntity<>(avaliacaoDto, HttpStatus.OK);
    }

    @PutMapping("/update/{idAvaliacao}")
    public ResponseEntity<AvaliacaoDTO> update(@PathVariable("idAvaliacao") Integer idAvaliacao,
                                               @RequestBody AvaliacaoCreateDTO avaliacaoCreateDto) throws RegraDeNegocioException {
        AvaliacaoDTO avaliacaoDtoRetorno = avaliacaoService.update(idAvaliacao, avaliacaoCreateDto);
        return new ResponseEntity<>(avaliacaoDtoRetorno, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageDTO<AvaliacaoDTO>> listAll(@RequestParam(defaultValue = "0", required = false) Integer pagina,
                                                         @RequestParam(defaultValue = "10", required = false) Integer tamanho,
                                                         @RequestParam(defaultValue = "idAvaliacao", required = false) String sort,
                                                         @RequestParam(defaultValue = "0", required = false) int order) {
        return new ResponseEntity<>(avaliacaoService.list(pagina, tamanho, sort, order), HttpStatus.OK);
    }

    @GetMapping("/filtro-avaliacao")
    public ResponseEntity<PageDTO<AvaliacaoDTO>> filtrarAvaliacoes(@RequestParam Integer pagina,
                                                                   @RequestParam Integer tamanho,
                                                                   @RequestParam (required = false) String email,
                                                                   @RequestParam (required = false) String edicao,
                                                                   @RequestParam (required = false) String trilha) throws RegraDeNegocioException {
        PageDTO<AvaliacaoDTO> filtroAvaliacaoList = avaliacaoService.filtrarAvaliacoes(pagina, tamanho, email, edicao, trilha);
        return new ResponseEntity<>(filtroAvaliacaoList, HttpStatus.OK);
    }

    @DeleteMapping("/{idAvaliacao}")
    public ResponseEntity<Void> delete(@PathVariable("idAvaliacao") Integer idAvaliacao) throws RegraDeNegocioException {
        avaliacaoService.deleteById(idAvaliacao);
        return ResponseEntity.noContent().build();
    }
}