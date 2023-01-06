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

    @GetMapping("/find-by-idInscricao")
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
    public ResponseEntity<PageDTO<InscricaoDTO>> listByTrilha(Integer pagina, Integer tamanho, @RequestParam("trilha")String trilha) throws RegraDeNegocioException {
        PageDTO<InscricaoDTO> listByTrilha = inscricaoService.listInscricoesByTrilha(pagina, tamanho, trilha);

        return new ResponseEntity<>(listByTrilha, HttpStatus.OK);
    }

    @GetMapping("/list-by-edicao")
    public ResponseEntity<List<InscricaoDTO>> listByEdicao(@RequestParam("edicao") String edicao) throws RegraDeNegocioException {
        List<InscricaoDTO> listByEdicao = inscricaoService.listInscricoesByEdicao(edicao);

        return new ResponseEntity<>(listByEdicao, HttpStatus.OK);
    }

    @GetMapping("/find-by-email")
    public ResponseEntity<InscricaoDTO> findInscricaoPorEmail(@RequestParam String email) throws RegraDeNegocioException {
        log.info("Buscando Inscrição por email...");
        InscricaoDTO inscricaoPorEmail = inscricaoService.findInscricaoPorEmail(email);
        log.info("Retornando inscrição encontrada.");
        return new ResponseEntity<>(inscricaoPorEmail, HttpStatus.OK);
    }

    @DeleteMapping("/{idInscricao}")
    public ResponseEntity<Void> delete(@PathVariable("idInscricao") Integer idInscricao) throws RegraDeNegocioException {
        log.info("Deletando inscrição");
        inscricaoService.delete(idInscricao);
        log.info("Inscrição deletada");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exportar-candidatos-para-csv")
    public ResponseEntity<Void> exportarCandidatosParaCsv() throws RegraDeNegocioException {
        inscricaoService.exportarCandidatoCSV();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtro-inscricao")
    public ResponseEntity<PageDTO<InscricaoDTO>> filtroInscricao(@RequestParam Integer pagina,
                                                              @RequestParam Integer tamanho,
                                                              @RequestParam (required = false) String email,
                                                              @RequestParam (required = false) String edicao,
                                                              @RequestParam (required = false) String trilha) throws RegraDeNegocioException {
        PageDTO<InscricaoDTO> filtroInscricaoList = inscricaoService.filtroInscricao(pagina, tamanho, email, edicao, trilha);
        return new ResponseEntity<>(filtroInscricaoList, HttpStatus.OK);
    }
}