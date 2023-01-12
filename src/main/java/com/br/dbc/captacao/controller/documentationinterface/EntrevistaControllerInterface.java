package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.entrevista.EntrevistaAtualizacaoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaCreateDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.repository.enums.Legenda;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

public interface EntrevistaControllerInterface {

    @Operation(summary = "Criar entrevista para candidatos", description = "Criar entrevistas no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro de entrevista realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @PostMapping
    ResponseEntity<EntrevistaDTO> cadastrarEntrevista(@Valid @RequestBody EntrevistaCreateDTO entrevistaCreateDTO, @RequestParam String token) throws RegraDeNegocioException;

    @Operation(summary = "Atualizar entrevista por id", description = "Atualiza a observacao da entrevista de acordo com o id presente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização  realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @PutMapping
    ResponseEntity<Void> atualizarEntrevista(@PathVariable("idEntrevista") Integer id,
                                             String observacao) throws RegraDeNegocioException;

    @Operation(summary = "Atualizar entrevista de candidato", description = "Atualizar entrevista no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização de entrevista de candidato realizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @PutMapping
    ResponseEntity<EntrevistaDTO> updateEntrevista(@Valid @RequestBody EntrevistaAtualizacaoDTO entrevistaCreateDTO,
                                                   @PathVariable("idEntrevista") Integer id,
                                                   Legenda legenda) throws RegraDeNegocioException;

    @Operation(summary = "Listagem de entrevistas no sistema", description = "Listagem das entrevistas presentes no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem de entrevistas realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    ResponseEntity<PageDTO<EntrevistaDTO>> list(@RequestParam(defaultValue = "0") Integer pagina,
                                                @RequestParam(defaultValue = "20") Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Listagem de entrevistas por trilha", description = "Listagem de entrevistas por trilha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem  realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    ResponseEntity<List<EntrevistaDTO>> listPorTrilha(@RequestParam(name = "trilha") String trilha);

    @Operation(summary = "Listagem de entrevistas pelo mês e ano", description = "Listagem de entrevistas pelo mês e ano")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem  realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    ResponseEntity<PageDTO<EntrevistaDTO>> listarMesAno(@RequestParam(defaultValue = "0") Integer pagina,
                                                        @RequestParam(defaultValue = "20") Integer tamanho,
                                                        @RequestParam Integer mes,
                                                        @RequestParam Integer ano);

    @Operation(summary = "Buscar entrevista pelo e-mail do candidato", description = "Busca uma entrevista a partir do e-mail do candidato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrevista encontrada!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    ResponseEntity<EntrevistaDTO> buscarEntrevistaPorEmailCandidato(@PathVariable ("email") String email) throws RegraDeNegocioException;

    @Operation(summary = "Exporta para xlsx lista de entrevistas agendadas.", description = "Exporta para csv lista de entrevistas agendadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "xlsx exportado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @GetMapping
    ResponseEntity<Void> exportarEntrevistasParaCsv(HttpServletResponse response) throws IOException;
    @Operation(summary = "Deletar entrevista pelo id", description = "Deleta uma entrevista no sistema a partir do id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrevista removida do sistema com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @DeleteMapping
    ResponseEntity<Void> deletarEntrevista(@PathVariable("idEntrevista") Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Deletar entrevista pelo e-mail do candidato", description = "Deleta uma entrevista a partir do e-mail do candidato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrevista removida do sistema com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    @DeleteMapping
    ResponseEntity<Void> deletarEntrevistaEmailCandidato(@PathVariable ("email") String email) throws RegraDeNegocioException;
}
