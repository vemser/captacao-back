package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.dto.inscricao.InscricaoCreateDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.entity.InscricaoEntity;

import java.time.LocalDate;

public class InscricaoFactory {

    public static InscricaoDTO getInscricaoDto() {

        InscricaoDTO inscricaoDTO = new InscricaoDTO();
        inscricaoDTO.setIdInscricao(1);
        inscricaoDTO.setDataInscricao(LocalDate.now());
        inscricaoDTO.setAvaliacao(AvaliacaoFactory.getAvaliacaoEntityAprovado().getAprovado());
        inscricaoDTO.setCandidato(CandidatoFactory.getCandidatoDTO());
        return inscricaoDTO;
    }

    public static InscricaoEntity getInscricaoEntity() {

        InscricaoEntity inscricaoEntity = new InscricaoEntity();
        inscricaoEntity.setIdInscricao(1);
//        inscricaoEntity.setDataInscricao(LocalDate.now());
        inscricaoEntity.setAvaliado(AvaliacaoFactory.getAvaliacaoEntityAprovado().getAprovado());
        inscricaoEntity.setCandidato(CandidatoFactory.getCandidatoEntity());
        inscricaoEntity.setAvaliacaoEntity(AvaliacaoFactory.getAvaliacaoEntityAprovado());
        return inscricaoEntity;
    }

    public static InscricaoCreateDTO getInscricaoCreateDto() {
        InscricaoCreateDTO inscricao = new InscricaoCreateDTO();
        inscricao.setIdCandidato(CandidatoFactory.getCandidatoEntity().getIdCandidato());
        return inscricao;
    }
}
