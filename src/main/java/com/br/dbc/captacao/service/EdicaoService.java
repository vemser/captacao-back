package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.entity.EdicaoEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.EdicaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EdicaoService {
    private final EdicaoRepository edicaoRepository;
    private final ObjectMapper objectMapper;

    public EdicaoEntity findById(Integer idEdicao) throws RegraDeNegocioException {
        return edicaoRepository.findById(idEdicao)
                .orElseThrow(() -> new RegraDeNegocioException("Edição não encontrada!"));
    }

    public EdicaoDTO createAndReturnDTO(EdicaoDTO edicaoDTO) {
        edicaoDTO.setNome(edicaoDTO.getNome().trim());
        EdicaoEntity edicaoEntity = edicaoRepository.save(converterEntity(edicaoDTO));
        return objectMapper.convertValue(edicaoEntity, EdicaoDTO.class);
    }

    public EdicaoEntity findByNome(String nome) throws RegraDeNegocioException {
        return edicaoRepository.findByNome(nome)
                .orElseThrow(() -> new RegraDeNegocioException("Edição não encontrada!"));
    }

    public EdicaoEntity converterEntity(EdicaoDTO edicaoDTO) {
        return objectMapper.convertValue(edicaoDTO, EdicaoEntity.class);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        edicaoRepository.deleteById(id);
    }
}
