package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Buscar inscrições por filtro", description = "Busca inscrições por filtro")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna lista de inscrições"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<PageDTO<InscricaoDTO>> filtrarInscricoes(@RequestParam Integer pagina,
                                                            @RequestParam Integer tamanho,
                                                            @RequestParam (required = false) String email,
                                                            @RequestParam (required = false) String edicao,
                                                            @RequestParam (required = false) String trilha) throws RegraDeNegocioException;

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
}
