package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface InscricaoControllerInterface {

    @Operation(summary = "Criar uma inscrição", description = "Criar uma inscrição com o ID do candidate e ID do formulario.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Criou uma inscrição com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    ResponseEntity<InscricaoDTO> create(@RequestParam Integer idCandidato) throws RegraDeNegocioException;

    @Operation(summary = "Procura uma inscrição por ID", description = "Procura uma inscrição por ID da inscrição.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Encontrou uma inscrição com sucesso"),
                    @ApiResponse(responseCode = "400", description = "ID _Inscrição inválido"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<InscricaoDTO> findById(@RequestParam("id") Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Busca toda lista de inscrições", description = "Retonar uma lista com todas inscrições do Banco de dados.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retornou uma lista de Inscrições com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<PageDTO<InscricaoDTO>> listar(@RequestParam(defaultValue = "0", required = false) Integer pagina,
                                                        @RequestParam(defaultValue = "10", required = false) Integer tamanho,
                                                        @RequestParam(defaultValue = "idInscricao", required = false) String sort,
                                                        @RequestParam(defaultValue = "0", required = false) int order) throws RegraDeNegocioException;

    @Operation(summary = "Busca lista de inscrições por TRILHA", description = "Busca lista de inscrições por TRILHA")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retornou lista de inscrições com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<PageDTO<InscricaoDTO>> listByTrilha(Integer pagina, Integer tamanho, @RequestParam("trilha") String trilha) throws RegraDeNegocioException;

    @Operation(summary = "Busca lista de inscrições por EDIÇAO", description = "Busca lista de inscrições por EDIÇAO")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retornou lista de inscrições com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<List<InscricaoDTO>> listByEdicao(@RequestParam("edicao") String edicao) throws RegraDeNegocioException;

    @Operation(summary = "Busca inscricao por EMAIL", description = "Busca inscrição por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna uma inscrição."),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<InscricaoDTO> findInscricaoPorEmail(@RequestParam String email) throws RegraDeNegocioException;

    @Operation(summary = "Deleta inscrição por ID", description = "Deleta inscrição por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Deletou inscrição com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping
    ResponseEntity<Void> delete(@PathVariable("idInscricao") Integer idInscricao) throws RegraDeNegocioException;

    @Operation(summary = "Exportar candidatos para csv", description = "Exporta lista de candidatos para um arquivo csv")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Candidatos exportados com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<Void> exportarCandidatosParaCsv() throws RegraDeNegocioException;

    @Operation(summary = "Busca inscricao por filtro", description = "Busca inscrição por filtro")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna lista de inscrições"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<PageDTO<InscricaoDTO>> filtroInscricao(@RequestParam Integer pagina,
                                                       @RequestParam Integer tamanho,
                                                       @RequestParam String email,
                                                       @RequestParam String edicao,
                                                       @RequestParam (required = false) String trilha) throws RegraDeNegocioException;
}
