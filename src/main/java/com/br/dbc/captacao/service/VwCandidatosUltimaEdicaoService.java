package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.VwCandidatosUltimaEdicao;
import com.br.dbc.captacao.repository.VwCandidatosUltimaEdicaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VwCandidatosUltimaEdicaoService {
    private final VwCandidatosUltimaEdicaoRepository repository;

    public List<VwCandidatosUltimaEdicao> findAll(){
        return repository.findAll();
    }
}
