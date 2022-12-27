package com.br.dbc.captacao.controller;


import com.br.dbc.captacao.dto.inscricao.InscricaoCreateDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.service.InscricaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
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
public class InscricaoController {

    private final InscricaoService inscricaoService;

    @Operation(summary = "Criar uma inscrição", description = "Criar uma inscrição com o ID do candidate e ID do formulario.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Criou uma inscrição com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/cadastro")
    public ResponseEntity<InscricaoDTO> create(@RequestParam Integer idCandidato) throws RegraDeNegocioException {
        log.info("Criando inscrição");
        InscricaoDTO inscricaoDTO = inscricaoService.create(idCandidato);
        log.info("Inscrição criada");
        return new ResponseEntity<>(inscricaoDTO, HttpStatus.OK);
    }

    @Operation(summary = "Procura uma inscrição", description = "Procura uma inscrição por ID da inscrição.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Encontrou uma inscrição com sucesso"),
                    @ApiResponse(responseCode = "400", description = "ID _Inscrição inválido"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/by-id")
    public ResponseEntity<InscricaoDTO> findById(@RequestParam("id") Integer id) throws RegraDeNegocioException {
        log.info("Buscando inscrição por id...");
        InscricaoDTO inscricaoDTO = inscricaoService.findDtoByid(id);
        log.info("Inscrição encontrada");
        return new ResponseEntity<>(inscricaoDTO, HttpStatus.OK);
    }

    @Operation(summary = "Busca toda lista de inscrições", description = "Retonar uma lista com todas inscrições do Banco de dados.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retornou uma lista de Inscrições com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    public ResponseEntity<PageDTO<InscricaoDTO>> listar(@RequestParam(defaultValue = "0", required = false) Integer pagina,
                                                        @RequestParam(defaultValue = "10", required = false) Integer tamanho,
                                                        @RequestParam(defaultValue = "idInscricao", required = false) String sort,
                                                        @RequestParam(defaultValue = "0", required = false) int order) {
        log.info("Listando inscrições");
        return new ResponseEntity<>(inscricaoService.listar(pagina, tamanho, sort, order), HttpStatus.OK);
    }


    @Operation(summary = "Busca inscricao por EMAIL", description = "Busca inscrição por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna uma inscrição."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/buscar-by-email")
    public ResponseEntity<List<InscricaoDTO>> findInscricaoPorEmail(@RequestParam String email) {
        log.info("Buscando Inscrição por email...");
        List<InscricaoDTO> list = inscricaoService.findInscricaoPorEmail(email);
        log.info("Retornando inscrição encontrada.");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Operation(summary = "Deleta inscrição por ID", description = "Deleta inscrição por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Deletou inscrição com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idInscricao}")
    public void delete(@PathVariable("idInscricao") Integer idInscricao) throws RegraDeNegocioException {
        log.info("Deletando inscrição");
        inscricaoService.delete(idInscricao);
        log.info("Inscrição deletada");
        new ResponseEntity<>(null, HttpStatus.OK);
    }

}