package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaAtualizacaoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaCreateDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.EntrevistaEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.enums.Legenda;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.EntrevistaRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.br.dbc.captacao.factory.CandidatoFactory.getCandidatoDTO;
import static com.br.dbc.captacao.factory.CandidatoFactory.getCandidatoEntity;
import static com.br.dbc.captacao.factory.EntrevistaFactory.*;
import static com.br.dbc.captacao.factory.GestorFactory.getGestorEntity;
import static com.br.dbc.captacao.factory.GestorFactory.getGestorDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EntrevistaServiceTest {
    @InjectMocks
    private EntrevistaService entrevistaService;
    @Mock
    private EntrevistaRepository entrevistaRepository;
    @Mock
    private CandidatoService candidatoService;
    @Mock
    private GestorService gestorService;
    @Mock
    private EmailService emailService;
    //    @Mock
//    private TokenService tokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(entrevistaService, "objectMapper", objectMapper);
    }

    @Test
    public void deveRetornarEntrevistaProcuradaPeloIdCorretamente() throws RegraDeNegocioException {
        final int idEntrevista = 1;
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();

        when(entrevistaRepository.findById(1)).thenReturn(Optional.of(entrevistaEntity));
        EntrevistaEntity entrevistaRetornada = entrevistaService.findById(1);

        assertEquals(idEntrevista, entrevistaRetornada.getIdEntrevista());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoIdNaoCadastradoNoBanco() throws RegraDeNegocioException {
        when(entrevistaRepository.findById(1)).thenReturn(Optional.empty());
        entrevistaService.findById(1);
    }

//    @Test
//    public void deveRetornarUmaListaDeEntrevistaDTO() {
//        final int tamanhoEsperado = 1;
//        final int pagina = 0;
//        final int tamanho = 5;
//
//        EntrevistaEntity entrevista = getEntrevistaEntity();
//        PageImpl<EntrevistaEntity> page =
//                new PageImpl<>(List.of(entrevista), PageRequest.of(pagina, tamanho), 0);
//
//        when(entrevistaRepository.findAll(any(PageRequest.class))).thenReturn(page);
//
//        PageDTO<EntrevistaDTO> entrevistaDTOS = entrevistaService.list(pagina, tamanho);
//
//        assertEquals(pagina, entrevistaDTOS.getPagina());
//        assertEquals(tamanho, entrevistaDTOS.getTamanho());
//        assertEquals(tamanhoEsperado, entrevistaDTOS.getElementos().size());
//    }

//    @Test
//    public void deveRetornarUmaListaPorMes() {
//        final int tamanhoEsperado = 1;
//        final int pagina = 0;
//        final int tamanho = 5;
//
//        EntrevistaEntity entrevista = getEntrevistaEntity();
//        PageImpl<EntrevistaEntity> page =
//                new PageImpl<>(List.of(entrevista), PageRequest.of(pagina, tamanho), 0);
//
//        when(entrevistaRepository.findAllByMes(anyInt(), anyInt(), any())).thenReturn(page);
//
//        PageDTO<EntrevistaDTO> entrevistaDTOS = entrevistaService.listMes(pagina, tamanho, 11, 2022);
//
//        assertEquals(pagina, entrevistaDTOS.getPagina());
//        assertEquals(tamanho, entrevistaDTOS.getTamanho());
//        assertEquals(tamanhoEsperado, entrevistaDTOS.getElementos().size());
//    }

    @Test
    public void deveConverterCorretamenteEntrevistaEntityParaEntrevistaDTO() throws RegraDeNegocioException {
        final int idEsperado = 1;
        final String nomeCandidatoEsperado = "Heloise Isabela Lopes";
        final String nomeUsuarioesperado = "Débora Sophia da Silva";

        GestorEntity usuarioEntity = getGestorEntity();
        CandidatoEntity candidato = getCandidatoEntity();

        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        entrevistaEntity.setGestorEntity(usuarioEntity);
        entrevistaEntity.setCandidatoEntity(candidato);

        GestorDTO usuarioDTO = getGestorDTO();
        CandidatoDTO candidatoDTO = getCandidatoDTO();

        when(gestorService.convertoToDTO(any())).thenReturn(usuarioDTO);
        when(candidatoService.converterEmDTO(any())).thenReturn(candidatoDTO);

        EntrevistaDTO entrevistaDTO = entrevistaService.converterParaEntrevistaDTO(entrevistaEntity);

        assertEquals(idEsperado, entrevistaDTO.getIdEntrevista());
        assertEquals(nomeCandidatoEsperado, entrevistaDTO.getCandidatoDTO().getNome());
        assertEquals(nomeUsuarioesperado, entrevistaDTO.getGestorDTO().getNome());
    }

//    @Test
//    public void deveCadastrarUmaEntrevistaCorretamente() throws RegraDeNegocioException {
//        final int idEsperado = 1;
//        final String token = "token";
//
//        EntrevistaCreateDTO entrevistaCreateDTO = getEntrevistaDTO();
//        GestorEntity gestorEntity = getGestorEntity();
//
//        CandidatoEntity candidato = getCandidatoEntity();
//        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
//        entrevistaEntity.setCandidatoEntity(candidato);
//        entrevistaEntity.setGestorEntity(gestorEntity);
//
//        LocalDateTime localDateTime =
//                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(8, 0));
//        EntrevistaEntity entrevistaCadastrada = getEntrevistaEntity();
//        entrevistaCadastrada.setGestorEntity(gestorEntity);
//        entrevistaCadastrada.setDataEntrevista(localDateTime);
//
//        when(gestorService.findByEmail(anyString())).thenReturn(gestorEntity);
//        when(candidatoService.findByEmailEntity(anyString())).thenReturn(candidato);
//        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.empty());
//        when(entrevistaRepository.findByDataEntrevista(any())).thenReturn(List.of(entrevistaCadastrada));
//        when(entrevistaRepository.save(any())).thenReturn(entrevistaEntity);
////        when(tokenService.getTokenConfirmacao(any())).thenReturn(token);
//
//        EntrevistaDTO entrevistaDTO = entrevistaService.createEntrevista(entrevistaCreateDTO, token);
//
//        assertEquals(idEsperado, entrevistaDTO.getIdEntrevista());
//    }

//    @Test(expected = RegraDeNegocioException.class)
//    public void deveVerificarListaCreate() throws RegraDeNegocioException {
//        LocalDateTime localDateTime =
//                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0));
//
//        EntrevistaCreateDTO entrevistaCreateDTO = getEntrevistaDTO();
//        entrevistaCreateDTO.setDataEntrevista(localDateTime);
//        GestorEntity usuarioEntity = getGestorEntity();
//        String token = "abc";
//
//        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
//        entrevistaEntity.setGestorEntity(usuarioEntity);
//
//        CandidatoEntity candidato = getCandidatoEntity();
//        entrevistaEntity.setCandidatoEntity(candidato);
//        entrevistaEntity.setGestorEntity(usuarioEntity);
//
//        EntrevistaEntity entrevistaCadastrada = getEntrevistaEntity();
//        entrevistaCadastrada.setGestorEntity(usuarioEntity);
//        entrevistaCadastrada.setDataEntrevista(localDateTime);
//
//        when(gestorService.findByEmail(anyString())).thenReturn(usuarioEntity);
//        when(candidatoService.findByEmailEntity(anyString())).thenReturn(candidato);
//        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.empty());
//        when(entrevistaRepository.findByDataEntrevista(any())).thenReturn(List.of(entrevistaCadastrada));
//
//        entrevistaService.createEntrevista(entrevistaCreateDTO, token);
//    }

    @Test
    public void deveBuscarEntrevistaPeloCandidatoCorretamente() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        entrevistaEntity.setCandidatoEntity(candidatoEntity);

        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.of(entrevistaEntity));

        EntrevistaEntity entrevistaRetornada = entrevistaService.findByCandidatoEntity(candidatoEntity);

        assertEquals(1, entrevistaRetornada.getIdEntrevista());
        assertEquals(candidatoEntity.getNome(), entrevistaEntity.getCandidatoEntity().getNome());
        assertEquals(candidatoEntity.getEmail(), entrevistaEntity.getCandidatoEntity().getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoCandidatoNaoTiverEntrevistas() throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = getCandidatoEntity();

        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.empty());

        entrevistaService.findByCandidatoEntity(candidatoEntity);
    }

    @Test
    public void deveDeletarUmaEntrevistaCorretamente() throws RegraDeNegocioException {
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();

        when(entrevistaRepository.findById(anyInt())).thenReturn(Optional.of(entrevistaEntity));

        entrevistaService.deletarEntrevista(1);
        verify(entrevistaRepository).delete(any());
    }

//    @Test
//    public void deveAtualizarEntrevistaCorretamente() throws RegraDeNegocioException {
//        LocalDateTime localDateTime =
//                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(8, 0));
//
//        EntrevistaAtualizacaoDTO entrevistaAtualizacaoDTO = getEntrevistaAtualizacaoDTO();
//        GestorEntity usuarioEntity = getGestorEntity();
//
//        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
//        entrevistaEntity.setGestorEntity(usuarioEntity);
//        entrevistaEntity.setDataEntrevista(localDateTime);
//
//        when(gestorService.findByEmail(anyString())).thenReturn(usuarioEntity);
//        when(entrevistaRepository.findById(anyInt())).thenReturn(Optional.of(entrevistaEntity));
//        when(entrevistaRepository.findByDataEntrevista(any())).thenReturn(List.of(entrevistaEntity));
//        when(entrevistaRepository.save(any())).thenReturn(entrevistaEntity);
//
//        EntrevistaDTO entrevistaDTO =
//                entrevistaService.atualizarEntrevista(1, entrevistaAtualizacaoDTO, Legenda.CONFIRMADA);
//
//        assertEquals(1, entrevistaDTO.getIdEntrevista());
//    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoAtualizarEntrevistaEmUmHorarioOcupado() throws RegraDeNegocioException {
        LocalDateTime localDateTime =
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(15, 0));

        EntrevistaAtualizacaoDTO entrevistaAtualizacaoDTO = getEntrevistaAtualizacaoDTO();
        GestorEntity gestorEntity = getGestorEntity();

        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        entrevistaEntity.setGestorEntity(gestorEntity);
        entrevistaEntity.setDataEntrevista(localDateTime);

        when(gestorService.findByEmail(anyString())).thenReturn(gestorEntity);
        when(entrevistaRepository.findById(anyInt())).thenReturn(Optional.of(entrevistaEntity));
        when(entrevistaRepository.findByDataEntrevista(any())).thenReturn(List.of(entrevistaEntity));

        entrevistaService.atualizarEntrevista(1, entrevistaAtualizacaoDTO, Legenda.CONFIRMADA);
    }

//    @Test
//    public void deveEnviarOTokenDeConfirmacaoQuandoAtualizarEntrevistaPendenteCorretamente() throws RegraDeNegocioException {
//        EntrevistaAtualizacaoDTO entrevistaAtualizacaoDTO = getEntrevistaAtualizacaoDTO();
//        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
//
//        when(entrevistaRepository.findById(anyInt())).thenReturn(Optional.of(entrevistaEntity));
//        when(entrevistaRepository.findByDataEntrevista(any())).thenReturn(List.of());
//        when(entrevistaRepository.save(any())).thenReturn(entrevistaEntity);
//
//        EntrevistaDTO entrevistaDTO =
//                entrevistaService.atualizarEntrevista(1, entrevistaAtualizacaoDTO, Legenda.PENDENTE);
//
//        assertEquals(1, entrevistaDTO.getIdEntrevista());
//    }

    @Test
    public void deveAtualizarObservacaoEntrevistaCorretamente() throws RegraDeNegocioException {
        EntrevistaAtualizacaoDTO entrevistaAtualizacaoDTO = getEntrevistaAtualizacaoDTO();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        String observacao = "obs";
        entrevistaEntity.setObservacoes(observacao);

        when(entrevistaRepository.findById(anyInt())).thenReturn(Optional.of(entrevistaEntity));
        when(entrevistaRepository.save(any())).thenReturn(entrevistaEntity);

        entrevistaService.atualizarObservacaoEntrevista(1, "obs");

        assertEquals(1, entrevistaEntity.getIdEntrevista());
        assertEquals("obs", entrevistaEntity.getObservacoes());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoUsuarioNaoForCadastrado() throws RegraDeNegocioException {
        EntrevistaAtualizacaoDTO entrevistaAtualizacaoDTO = getEntrevistaAtualizacaoDTO();

        entrevistaService.atualizarEntrevista(1, entrevistaAtualizacaoDTO, Legenda.CANCELADA);
    }


    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoCandidatoJaTiverUmaEntrevistaMarcada() throws RegraDeNegocioException {
        EntrevistaCreateDTO entrevistaCreateDTO = getEntrevistaDTO();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        CandidatoEntity candidato = getCandidatoEntity();
        String token = "abc";

        when(candidatoService.findByEmailEntity(anyString())).thenReturn(candidato);
        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.of(entrevistaEntity));

        entrevistaService.createEntrevista(entrevistaCreateDTO, token);
    }

    @Test
    public void deveBuscarEntrevistaPeloEmailDoCandidatoCorretamente() throws RegraDeNegocioException {
        final String email = "heloise.lopes@dbccompany.com.br";

        CandidatoDTO candidatoDTO = getCandidatoDTO();

        CandidatoEntity candidato = getCandidatoEntity();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        entrevistaEntity.setCandidatoEntity(candidato);

        when(candidatoService.findByEmail(anyString())).thenReturn(candidatoDTO);
        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.of(entrevistaEntity));

        EntrevistaDTO entrevistaDTO = entrevistaService.buscarPorEmailCandidato(email);

        assertEquals(1, entrevistaDTO.getIdEntrevista());
        assertEquals(Legenda.PENDENTE, entrevistaDTO.getLegenda());
    }

    @Test
    public void deveDeletarEntrevistaPeloEmailDoCandidatoCorretamente() throws RegraDeNegocioException {
        final String email = "heloise.lopes@dbccompany.com.br";

        CandidatoDTO candidatoDTO = getCandidatoDTO();
        CandidatoEntity candidato = getCandidatoEntity();
        EntrevistaEntity entrevistaEntity = getEntrevistaEntity();
        entrevistaEntity.setCandidatoEntity(candidato);

        when(candidatoService.findByEmail(anyString())).thenReturn(candidatoDTO);
        when(entrevistaRepository.findByCandidatoEntity(any())).thenReturn(Optional.of(entrevistaEntity));

        entrevistaService.deletarEntrevistaEmail(email);

        verify(entrevistaRepository).deleteById(anyInt());
    }
}
