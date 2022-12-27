package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.formulario.FormularioCreateDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    ResponseEntity<FormularioDTO> create(@RequestBody @Valid FormularioCreateDTO formularioCreateDto) throws RegraDeNegocioException;

    @Operation(summary = "Listar todos formularios", description = "Listar todos formularios")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Formularios listados com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<PageDTO<FormularioDTO>> listAll(@RequestParam(defaultValue = "0", required = false) Integer pagina,
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
    ResponseEntity<FormularioDTO> updateFormulario(@RequestParam Integer idFormulario,
                                                   @RequestBody @Valid FormularioCreateDTO formularioCreateDto) throws RegraDeNegocioException;

    @Operation(summary = "Deletar Formulario", description = "Deletar formulario por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/delete-fisico/{idFormulario}")
    public ResponseEntity<Void> deletarFormulario(@PathVariable("idFormulario") Integer idFormulario) throws RegraDeNegocioException ;
}
