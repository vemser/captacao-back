package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.formulario.FormularioCreateDto;
import com.br.dbc.captacao.dto.formulario.FormularioDto;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface FormularioControllerInterface {
    @Operation(summary = "Criar um formulário", description = "Criar um formulário para o candidato.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Criou com sucesso o formulário"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<FormularioDto> create(@RequestBody FormularioCreateDto formularioCreateDto) throws RegraDeNegocioException;

    @Operation(summary = "Listar todos formularios", description = "Listar todos formularios")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Formularios listados com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<PageDTO<FormularioDto>> listAll(@RequestParam(defaultValue = "0", required = false) Integer pagina,
                                                   @RequestParam(defaultValue = "10", required = false) Integer tamanho,
                                                   @RequestParam(defaultValue = "idFormulario", required = false) String sort,
                                                   @RequestParam(defaultValue = "0", required = false) int order);

    @Operation(summary = "Atualizar Formulario", description = "Atualizar formulario por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<FormularioDto> updateFormulario(@RequestParam Integer idFormulario,
                                                   @RequestBody @Valid FormularioCreateDto formularioCreateDto) throws RegraDeNegocioException;

    @Operation(summary = "Deletar Formulario", description = "Deletar formulario por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    void deletarFormulario(@RequestParam Integer idFormulario) throws RegraDeNegocioException;
}
