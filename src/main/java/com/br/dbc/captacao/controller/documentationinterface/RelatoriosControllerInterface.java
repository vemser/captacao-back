package com.br.dbc.captacao.controller.documentationinterface;

import com.br.dbc.captacao.dto.relatorios.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface RelatoriosControllerInterface {

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/quantidade-de-pessoas-inscritas-por-edicao")
    List<RelatorioQuantidadePessoasInscritasPorEdicaoDTO> recuperarQuantidadeDePessoasInscritasPorEdicao();

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/quantidade-de-pessoas-inscritas-por-estado")
    List<RelatorioQuantidadePessoasInscritasPorEstadoDTO> recuperarQuantidadeDePessoasInscritasPorEstado();

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/quantidade-de-pessoas-inscritas-por-pcd")
    List<RelatorioQuantidadePessoasInscritasPorPCDDTO> recuperarQuantidadeDePessoasInscritasPorPCD();

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/quantidade-de-pessoas-inscritas-por-neurodiversidade")
    List<RelatorioQuantidadePessoasInscritasPorNeurodiversidadeDTO> recuperarQuantidadeDePessoasInscritasPorNeurodiversidade();


    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/quantidade-de-pessoas-inscritas-por-genero")
    List<RelatorioQuantidadePessoasInscritasPorGeneroDTO> recuperarQuantidadeDePessoasInscritasPorGenero();
}
