package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.dto.avaliacao.AvaliacaoCreateDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoDTO;
import com.br.dbc.captacao.entity.AvaliacaoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.InscricaoEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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
        avaliacaoEntity.setAprovado(TipoMarcacao.T);
        avaliacaoEntity.setIdAvaliacao(1);
        avaliacaoEntity.setAvaliador(GestorFactory.getGestorEntity());
        return avaliacaoEntity;
    }

    public static AvaliacaoEntity getAvaliacaoEntityReprovado() {

        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity();
        avaliacaoEntity.setAprovado(TipoMarcacao.F);
        avaliacaoEntity.setIdAvaliacao(1);
        avaliacaoEntity.setAvaliador(GestorFactory.getGestorEntity());
        return avaliacaoEntity;
    }


    public static AvaliacaoCreateDTO getAvaliacaoCreateDto() {
        AvaliacaoCreateDTO avaliacaoCreateavaliacaoCreateDTO = new AvaliacaoCreateDTO();
        avaliacaoCreateavaliacaoCreateDTO.setAprovadoBoolean(true);
        avaliacaoCreateavaliacaoCreateDTO.setIdInscricao(InscricaoFactory.getInscricaoDto().getIdInscricao());

        return avaliacaoCreateavaliacaoCreateDTO;
    }

}
