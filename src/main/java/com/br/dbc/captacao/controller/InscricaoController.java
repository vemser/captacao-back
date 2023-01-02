package com.br.dbc.captacao.controller;


import com.br.dbc.captacao.controller.documentationinterface.InscricaoControllerInterface;
import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.InscricaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/inscricao")
public class InscricaoController implements InscricaoControllerInterface {

    private final InscricaoService inscricaoService;


    @PostMapping("/cadastro")
    public ResponseEntity<InscricaoDTO> create(@RequestParam Integer idCandidato) throws RegraDeNegocioException {
        log.info("Criando inscrição");
        InscricaoDTO inscricaoDTO = inscricaoService.create(idCandidato);
        log.info("Inscrição criada");
        return new ResponseEntity<>(inscricaoDTO, HttpStatus.OK);
    }

    @GetMapping("/by-id")
    public ResponseEntity<InscricaoDTO> findById(@RequestParam("id") Integer id) throws RegraDeNegocioException {
        log.info("Buscando inscrição por id...");
        InscricaoDTO inscricaoDTO = inscricaoService.findDtoByid(id);
        log.info("Inscrição encontrada");
        return new ResponseEntity<>(inscricaoDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageDTO<InscricaoDTO>> listar(@RequestParam(defaultValue = "0", required = false) Integer pagina,
                                                        @RequestParam(defaultValue = "10", required = false) Integer tamanho,
                                                        @RequestParam(defaultValue = "idInscricao", required = false) String sort,
                                                        @RequestParam(defaultValue = "0", required = false) int order) throws RegraDeNegocioException {
        log.info("Listando inscrições");
        return new ResponseEntity<>(inscricaoService.listar(pagina, tamanho, sort, order), HttpStatus.OK);
    }

    @GetMapping("/list-by-trilha")
    public ResponseEntity<List<InscricaoDTO>> listByTrilha(@RequestParam("trilha") String trilha) throws RegraDeNegocioException {
        List<InscricaoDTO> listByTrilha = inscricaoService.listInscricoesByTrilha(trilha);

        return new ResponseEntity<>(listByTrilha, HttpStatus.OK);
    }

    @GetMapping("/list-by-edicao")
    public ResponseEntity<List<InscricaoDTO>> listByEdicao(@RequestParam("edicao") String edicao) throws RegraDeNegocioException {
        List<InscricaoDTO> listByEdicao = inscricaoService.listInscricoesByEdicao(edicao);

        return new ResponseEntity<>(listByEdicao, HttpStatus.OK);
    }

    @GetMapping("/buscar-by-email")
    public ResponseEntity<List<InscricaoDTO>> findInscricaoPorEmail(@RequestParam String email) {
        log.info("Buscando Inscrição por email...");
        List<InscricaoDTO> list = inscricaoService.findInscricaoPorEmail(email);
        log.info("Retornando inscrição encontrada.");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/{idInscricao}")
    public ResponseEntity<Void> delete(@PathVariable("idInscricao") Integer idInscricao) throws RegraDeNegocioException {
        log.info("Deletando inscrição");
        inscricaoService.delete(idInscricao);
        log.info("Inscrição deletada");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exportar-candidatos-para-csv")
    public ResponseEntity<Void> exportarEntrevistaParaCsv() throws RegraDeNegocioException {
        inscricaoService.exportarCandidatoCSV();
        return ResponseEntity.noContent().build();
    }

}