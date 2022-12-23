package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface EdicaoControllerInterface {
    @Operation(summary = "Deleta a edição no sistema", description = "Deleta a edição no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @DeleteMapping
    ResponseEntity<Void> deleteFisico(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Criar cadastro de edição", description = "Cria edição no Sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro de edição realizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @PostMapping
    ResponseEntity<EdicaoDTO> cadastrarEdicao(@Valid @RequestBody EdicaoDTO edicaoDTO);
}
