package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.relatorios.*;
import com.br.dbc.captacao.repository.CandidatoRepository;
import com.br.dbc.captacao.repository.FormularioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatoriosService {

    private final CandidatoRepository candidatoRepository;
    private final FormularioRepository formularioRepository;

    public List<RelatorioQuantidadePessoasInscritasPorEdicaoDTO> recuperarQuantidadeDePessoasInscritasPorEdicao(){
        return candidatoRepository.recuperarQuantidadeDePessoasInscritasPorEdicao();
    }

    public List<RelatorioQuantidadePessoasInscritasPorEstadoDTO> recuperarQuantidadeDePessoasInscritasPorEstado(){
        return candidatoRepository.recuperarQuantidadeDePessoasInscritasPorEstado();
    }

    public List<RelatorioQuantidadePessoasInscritasPorPCDDTO> recuperarQuantidadeDePessoasInscritasPorPCD(){
        return candidatoRepository.recuperarQuantidadeDePessoasInscritasPorPCD();
    }

    public List<RelatorioQuantidadePessoasInscritasPorNeurodiversidadeDTO> recuperarQuantidadeDePessoasInscritasPorNeurodiversidade(){
        return formularioRepository.recuperarQuantidadeDePessoasInscritasPorNeurodiversidade();
    }

    public List<RelatorioQuantidadePessoasInscritasPorGeneroDTO> recuperarQuantidadeDePessoasInscritasPorGenero(){
        return formularioRepository.recuperarQuantidadeDePessoasInscritasPorGenero();
    }
}
