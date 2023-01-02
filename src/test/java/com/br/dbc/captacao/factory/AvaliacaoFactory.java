package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.dto.avaliacao.AvaliacaoCreateDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoDTO;
import com.br.dbc.captacao.entity.AvaliacaoEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;

public class AvaliacaoFactory {


    public static AvaliacaoDTO getAvaliacaoDto() {

        AvaliacaoDTO avaliacaoDto = new AvaliacaoDTO();
        avaliacaoDto.setAprovado(TipoMarcacao.T);
        avaliacaoDto.setIdAvaliacao(1);
        avaliacaoDto.setInscricao(InscricaoFactory.getInscricaoDto());;
        return avaliacaoDto;
    }

    public static AvaliacaoEntity getAvaliacaoEntityAprovado() {

        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity();
        avaliacaoEntity.setInscricao(InscricaoFactory.getInscricaoEntity());
        avaliacaoEntity.setAprovado(TipoMarcacao.T);
        avaliacaoEntity.setIdAvaliacao(1);
        avaliacaoEntity.setAvaliador(GestorFactory.getGestorEntity());
        avaliacaoEntity.setInscricao(InscricaoFactory.getInscricaoEntity());
        return avaliacaoEntity;
    }

    public static AvaliacaoEntity getAvaliacaoEntityReprovado() {

        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity();
        avaliacaoEntity.setInscricao(InscricaoFactory.getInscricaoEntity());
        avaliacaoEntity.setAprovado(TipoMarcacao.F);
        avaliacaoEntity.setIdAvaliacao(1);
        avaliacaoEntity.setAvaliador(GestorFactory.getGestorEntity());
        avaliacaoEntity.setInscricao(InscricaoFactory.getInscricaoEntity());
        return avaliacaoEntity;
    }


    public static AvaliacaoCreateDTO getAvaliacaoCreateDto() {
        AvaliacaoCreateDTO avaliacaoCreateavaliacaoCreateDTO = new AvaliacaoCreateDTO();
        avaliacaoCreateavaliacaoCreateDTO.setAprovadoBoolean(true);
        avaliacaoCreateavaliacaoCreateDTO.setIdInscricao(InscricaoFactory.getInscricaoDto().getIdInscricao());

        return avaliacaoCreateavaliacaoCreateDTO;
    }


}
