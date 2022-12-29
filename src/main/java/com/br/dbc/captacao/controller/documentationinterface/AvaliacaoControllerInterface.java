package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.avaliacao.AvaliacaoCreateDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AvaliacaoControllerInterface {
    @Operation(summary = "Cadastrar Avaliacao", description = "Cadastro de avaliacao")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Avaliacao cadastrada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    public ResponseEntity<AvaliacaoDTO> create(@RequestBody AvaliacaoCreateDTO avaliacaoCreateDto) throws RegraDeNegocioException;


    @Operation(summary = "Listar todas Avaliações", description = "Retorna uma lista com todas avaliações")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retornou lista de avaliações com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    public ResponseEntity<PageDTO<AvaliacaoDTO>> listAll
            (@RequestParam(defaultValue = "0", required = false) Integer pagina,
             @RequestParam(defaultValue = "10", required = false) Integer tamanho,
             @RequestParam(defaultValue = "idAvaliacao", required = false) String sort,
             @RequestParam(defaultValue = "0", required = false) int order);


    @Operation(summary = "Atualizar Avaliação", description = "Atualizar avaliação por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Avaliacao atualiazada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/update/{idAvaliacao}")
    public ResponseEntity<AvaliacaoDTO> update(@PathVariable("idAvaliacao") Integer idAvaliacao,
                                               @RequestBody AvaliacaoCreateDTO avaliacaoCreateDto) throws RegraDeNegocioException;

    @Operation(summary = "Deletar Avaliacao", description = "Deletar uma avaliacao por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Avaliacao excluída com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idAvaliacao}")
    public ResponseEntity<Void> delete(@PathVariable("idAvaliacao") Integer idAvaliacao) throws RegraDeNegocioException;

    @Operation(summary = "Busca avaliacao por EMAIL", description = "Busca avaliação por email")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna uma avaliacao."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/buscar-by-email")
    public ResponseEntity<List<AvaliacaoDTO>> findInscricaoPorEmail(@RequestParam String email);

    @Operation(summary = "Busca lista de avaliações por TRILHA", description = "Busca lista de avaliações por TRILHA. Ex: BACKEND")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna uma lista de avaliações."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    public ResponseEntity<List<AvaliacaoDTO>> listByTrilha(@RequestParam("trilha") String trilha) throws RegraDeNegocioException;

    @Operation(summary = "Busca lista de avaliações por EDICAO", description = "Busca lista de avaliações por EDICAO.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna uma lista de avaliações."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    public ResponseEntity<List<AvaliacaoDTO>> listByEdicao(@RequestParam("edicao") String edicao) throws RegraDeNegocioException;

    }