package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.dto.candidato.CandidatoCreateDTO;
import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.LinguagemEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.br.dbc.captacao.factory.EdicaoFactory.getEdicaoDTO;

public class CandidatoFactory {
    public static CandidatoEntity getCandidatoEntity() {
        LinguagemEntity linguagemEntity = LinguagemFactory.getLinguagemEntity();
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        linguagemList.add(linguagemEntity);

        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(1);
        candidatoEntity.setNotaProva(8.00);
        candidatoEntity.setNome("Heloise Isabela Lopes");
        candidatoEntity.setCidade("Santana");
        candidatoEntity.setEstado("AP");
        candidatoEntity.setEmail("heloise.lopes@dbccompany.com.br");
        candidatoEntity.setLinguagens(new HashSet<>(linguagemList));
        candidatoEntity.setEdicao(EdicaoFactory.getEdicaoEntity());
        candidatoEntity.setAtivo(TipoMarcacao.T);
        candidatoEntity.setPcd(TipoMarcacao.T);
        candidatoEntity.setFormularioEntity(FormularioFactory.getFormularioEntity());

        return candidatoEntity;
    }

    public static CandidatoCreateDTO getCandidatoCreateDTO() {
        LinguagemDTO linguagemDTO = new LinguagemDTO("Java");
        List<String> linguagemDTOList = new ArrayList<>();
        linguagemDTOList.add(linguagemDTO.getNome());

        CandidatoCreateDTO candidatoCreateDTO = new CandidatoCreateDTO();
        candidatoCreateDTO.setNome("Heloise Isabela Lopes");
        candidatoCreateDTO.setCidade("Santana");
        candidatoCreateDTO.setEstado("AP");
        candidatoCreateDTO.setEmail("heloise.lopes@dbccompany.com.br");
        candidatoCreateDTO.setLinguagens(linguagemDTOList);
        candidatoCreateDTO.setEdicao(getEdicaoDTO());
        candidatoCreateDTO.setAtivo(TipoMarcacao.T);
        candidatoCreateDTO.setPcdboolean(true);

        return candidatoCreateDTO;
    }

    public static CandidatoDTO getCandidatoDTO() {
        LinguagemDTO linguagemDTO = new LinguagemDTO("Java");
        List<LinguagemDTO> linguagemDTOList = new ArrayList<>();
        linguagemDTOList.add(linguagemDTO);

        CandidatoDTO candidatoDTO = new CandidatoDTO();
        candidatoDTO.setNome("Heloise Isabela Lopes");
        candidatoDTO.setCidade("Santana");
        candidatoDTO.setEstado("AP");
        candidatoDTO.setEmail("heloise.lopes@dbccompany.com.br");
        candidatoDTO.setLinguagens(linguagemDTOList);
        candidatoDTO.setEdicao(getEdicaoDTO());
        candidatoDTO.setAtivo(TipoMarcacao.T);

        return candidatoDTO;
    }
}
