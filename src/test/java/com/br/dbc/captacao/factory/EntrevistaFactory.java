package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaAtualizacaoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.entity.EntrevistaEntity;
import com.br.dbc.captacao.enums.Legenda;
import com.br.dbc.captacao.enums.TipoMarcacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.br.dbc.captacao.factory.CandidatoFactory.getCandidatoDTO;
import static com.br.dbc.captacao.factory.CandidatoFactory.getCandidatoEntity;
import static com.br.dbc.captacao.factory.GestorFactory.getGestorDTO;
import static com.br.dbc.captacao.factory.GestorFactory.getGestorEntity;

public class EntrevistaFactory {
    public static EntrevistaEntity getEntrevistaEntity() {
        EntrevistaEntity entrevistaEntity = new EntrevistaEntity();
        entrevistaEntity.setIdEntrevista(1);
        entrevistaEntity.setDataEntrevista(LocalDateTime.of(LocalDate.now().plusDays(1),
                LocalTime.of(15, 0)));
        entrevistaEntity.setObservacoes("Sem observações.");
        entrevistaEntity.setLegenda(Legenda.PENDENTE);
        entrevistaEntity.setCandidatoEntity(getCandidatoEntity());
        entrevistaEntity.setGestorEntity(getGestorEntity());
        entrevistaEntity.setAvaliado(TipoMarcacao.T);

        return entrevistaEntity;
    }

    public static EntrevistaDTO getEntrevistaDTO() {
        CandidatoDTO candidatoDTO = getCandidatoDTO();
        GestorDTO gestorDTO = getGestorDTO();

        EntrevistaDTO entrevistaDTO = new EntrevistaDTO();
        entrevistaDTO.setIdEntrevista(1);
        entrevistaDTO.setCandidatoDTO(candidatoDTO);
        entrevistaDTO.setGestorDTO(gestorDTO);
        entrevistaDTO.setCandidatoEmail(candidatoDTO.getEmail());

        return entrevistaDTO;
    }

    public static EntrevistaAtualizacaoDTO getEntrevistaAtualizacaoDTO() {
        EntrevistaAtualizacaoDTO entrevistaAtualizacaoDTO =
                new EntrevistaAtualizacaoDTO(
                        LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0)),
                        "Santana",
                        "AP",
                        "Sem observações",
                        "T"
                );

        return entrevistaAtualizacaoDTO;
    }
}
