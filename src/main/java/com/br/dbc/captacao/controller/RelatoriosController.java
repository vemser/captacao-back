package com.br.dbc.captacao.controller;

import com.br.dbc.captacao.controller.documentationinterface.RelatoriosControllerInterface;
import com.br.dbc.captacao.dto.relatorios.*;
import com.br.dbc.captacao.service.RelatoriosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/relatorios")
@Validated
@RequiredArgsConstructor
public class RelatoriosController implements RelatoriosControllerInterface {
    private final RelatoriosService relatoriosService;

    @Override
    public List<RelatorioQuantidadePessoasInscritasPorEdicaoDTO> recuperarQuantidadeDePessoasInscritasPorEdicao() {
        return relatoriosService.recuperarQuantidadeDePessoasInscritasPorEdicao();
    }

    @Override
    public List<RelatorioQuantidadePessoasInscritasPorEstadoDTO> recuperarQuantidadeDePessoasInscritasPorEstado() {
        return relatoriosService.recuperarQuantidadeDePessoasInscritasPorEstado();
    }

    @Override
    public List<RelatorioQuantidadePessoasInscritasPorPCDDTO> recuperarQuantidadeDePessoasInscritasPorPCD() {
        return relatoriosService.recuperarQuantidadeDePessoasInscritasPorPCD();
    }

    @Override
    public List<RelatorioQuantidadePessoasInscritasPorNeurodiversidadeDTO> recuperarQuantidadeDePessoasInscritasPorNeurodiversidade() {
        return relatoriosService.recuperarQuantidadeDePessoasInscritasPorNeurodiversidade();
    }

    @Override
    public List<RelatorioQuantidadePessoasInscritasPorGeneroDTO> recuperarQuantidadeDePessoasInscritasPorGenero() {
        return relatoriosService.recuperarQuantidadeDePessoasInscritasPorGenero();
    }
}
