package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.entity.LinguagemEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.LinguagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinguagemService {
    private final LinguagemRepository linguagemRepository;
    private final ObjectMapper objectMapper;

    public LinguagemEntity create(LinguagemDTO linguagemDTO) {
        linguagemDTO.setNome(linguagemDTO.getNome().trim().toUpperCase());
        return linguagemRepository.save(converterEntity(linguagemDTO));
    }

    public LinguagemEntity findByNome(String nome) {
        nome = nome.trim().toUpperCase();
        Optional<LinguagemEntity> linguagemEntity = linguagemRepository.findByNome(nome);

        if (linguagemEntity.isEmpty()) {
            return create(new LinguagemDTO(nome));
        }

        return linguagemEntity.get();
    }

    public LinguagemEntity findById(Integer idLinguagem) throws RegraDeNegocioException {
        return linguagemRepository.findById(idLinguagem)
                .orElseThrow(() -> new RegraDeNegocioException("Linguagem não encontrada!"));
    }

    public LinguagemEntity converterEntity(LinguagemDTO linguagemDTO) {
        return objectMapper.convertValue(linguagemDTO, LinguagemEntity.class);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        linguagemRepository.deleteById(id);
    }
}
