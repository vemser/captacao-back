package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.trilha.TrilhaCreateDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface TrilhaControllerInterface {

    @Operation(summary = "Cadastrar nova Trilha", description = "Cadastro de nova Trilha")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Trilha cadastrada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            })
    @PostMapping
    ResponseEntity<TrilhaDTO> create(@Valid @RequestBody TrilhaCreateDTO trilhaCreateDTO) throws RegraDeNegocioException;

    @Operation(summary = "Listar Trilhas", description = "Lista todas Trilhas do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Trilha cadastrada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/listar")
    ResponseEntity<List<TrilhaDTO>> listarTodos();

    @Operation(summary = "Deletar Trilha", description = "Deletar Trilha por ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Trilha excluída com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping
   ResponseEntity<Void> delete(@PathVariable Integer idTrilha) throws RegraDeNegocioException;
}
