package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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


    @Operation(summary = "Retorna a edição atual", description = "Retorna a ultima edição criada no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retornado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    ResponseEntity<String> retornarEdicaoAtual() throws RegraDeNegocioException;

    @Operation(summary = "Listar edições", description = "Lista todas as edições cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edições listadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    ResponseEntity<List<EdicaoDTO>> list();
}
