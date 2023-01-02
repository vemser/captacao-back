package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.avaliacao.AvaliacaoCreateDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.AvaliacaoEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.factory.AvaliacaoFactory;
import com.br.dbc.captacao.factory.GestorFactory;
import com.br.dbc.captacao.factory.InscricaoFactory;
import com.br.dbc.captacao.repository.AvaliacaoRepository;
import com.br.dbc.captacao.repository.InscricaoRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AvaliacaoServiceTest {

    @InjectMocks
    private AvaliacaoService avaliacaoService;

    @Mock
    private InscricaoService inscricaoService;
    @Mock
    private EmailService emailService;
    @Mock
    private GestorService gestorService;
    @Mock
    private FormularioService formularioService;

    @Mock
    private InscricaoRepository inscricaoRepository;
    @Mock
    private CargoService cargoService;

    @Mock
    private CandidatoService candidatoService;
    @Mock
    private AvaliacaoRepository avaliacaoRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(avaliacaoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarCreateComSucessoAprovado() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        AvaliacaoCreateDTO avaliacaoCreateDTO = AvaliacaoFactory.getAvaliacaoCreateDto();
        when(avaliacaoRepository.findAvaliacaoEntitiesByInscricao_IdInscricao(anyInt())).thenReturn(Optional.empty());
        when(inscricaoService.findDtoByid(anyInt())).thenReturn(InscricaoFactory.getInscricaoDto());
        when(inscricaoService.convertToEntity(any())).thenReturn(InscricaoFactory.getInscricaoEntity());
        when(inscricaoService.converterParaDTO((any()))).thenReturn(InscricaoFactory.getInscricaoDto());
        when(gestorService.findDtoById(anyInt())).thenReturn(GestorFactory.getGestorDTO());
        when(gestorService.convertToEntity(any())).thenReturn(GestorFactory.getGestorEntity());
        when(gestorService.convertoToDTO(any())).thenReturn(GestorFactory.getGestorDTO());
        when(avaliacaoRepository.save(any())).thenReturn(AvaliacaoFactory.getAvaliacaoEntityAprovado());

        AvaliacaoDTO avaliacaoDtoRetorno = avaliacaoService.create(avaliacaoCreateDTO);

        Assert.notNull(avaliacaoDtoRetorno);
        Assertions.assertEquals(avaliacaoDtoRetorno.getAprovado(), TipoMarcacao.T);
        verify(emailService, times(1)).sendEmail(any(), any());


    }

    @Test
    public void deveTestarFindByDtoComSucesso() throws RegraDeNegocioException{
        int id = 1;
        AvaliacaoEntity avaliacaoEntity = AvaliacaoFactory.getAvaliacaoEntityAprovado();

        when(avaliacaoRepository.findById(anyInt()))
                .thenReturn(Optional.of(avaliacaoEntity));

        AvaliacaoDTO avaliacaoDto = avaliacaoService.findDtoById(id);

        assertEquals(id,avaliacaoDto.getIdAvaliacao());
    }

    @Test
    public void deveTestarCreateComSucessoReprovado() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        AvaliacaoCreateDTO avaliacaoCreateDto = AvaliacaoFactory.getAvaliacaoCreateDto();

        when(avaliacaoRepository.findAvaliacaoEntitiesByInscricao_IdInscricao(anyInt())).thenReturn(Optional.empty());
        when(inscricaoService.findDtoByid(anyInt())).thenReturn(InscricaoFactory.getInscricaoDto());
        when(inscricaoService.convertToEntity(any())).thenReturn(InscricaoFactory.getInscricaoEntity());
        when(inscricaoService.converterParaDTO((any()))).thenReturn(InscricaoFactory.getInscricaoDto());
        when(gestorService.findDtoById(anyInt())).thenReturn(GestorFactory.getGestorDTO());
        when(gestorService.convertToEntity(any())).thenReturn(GestorFactory.getGestorEntity());
        when(gestorService.convertoToDTO(any())).thenReturn(GestorFactory.getGestorDTO());
        when(avaliacaoRepository.save(any())).thenReturn(AvaliacaoFactory.getAvaliacaoEntityReprovado());

        AvaliacaoDTO avaliacaoDtoRetorno = avaliacaoService.create(avaliacaoCreateDto);

        Assert.notNull(avaliacaoDtoRetorno);
        Assertions.assertEquals(avaliacaoDtoRetorno.getAprovado(), TipoMarcacao.F);
        verify(emailService, times(1)).sendEmail(any(), any());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarCreateComException() throws RegraDeNegocioException {
        AvaliacaoCreateDTO avaliacaoCreateDto = AvaliacaoFactory.getAvaliacaoCreateDto();
        AvaliacaoEntity avaliacaoEntity = AvaliacaoFactory.getAvaliacaoEntityAprovado();

        when(avaliacaoRepository.findAvaliacaoEntitiesByInscricao_IdInscricao(anyInt())).thenReturn(Optional.of(avaliacaoEntity));

        avaliacaoService.create(avaliacaoCreateDto);

        verify(avaliacaoRepository, times(1)).save(any());
    }

    @Test
    public void deveTestarListarPaginado() throws RegraDeNegocioException {
        Integer pagina = 1;
        Integer tamanho = 5;
        String sort = "idAvaliacao";
        Integer order = 1;//DESCENDING
        Sort odernacao = Sort.by(sort).descending();
        PageImpl<AvaliacaoEntity> pageImpl = new PageImpl<>(List.of(AvaliacaoFactory.getAvaliacaoEntityAprovado()),
                PageRequest.of(pagina, tamanho, odernacao), 0);

        when(avaliacaoRepository.findAll(any(Pageable.class))).thenReturn(pageImpl);

        PageDTO<AvaliacaoDTO> page = avaliacaoService.list(pagina, tamanho, sort, order);

        assertEquals(page.getTamanho(), tamanho);

    }

    @Test
    public void deveTestarFindAvaliacaoPorEmail() throws RegraDeNegocioException {
        // Criar variaveis (SETUP)
        String email = "eduardosedrez@gmail.com";
        List<AvaliacaoDTO> avaliacaoDtos = List.of(AvaliacaoFactory.getAvaliacaoDto());
        List<AvaliacaoEntity> avaliacaoEntities = List.of(AvaliacaoFactory.getAvaliacaoEntityReprovado());

        when(avaliacaoRepository.findAvaliacaoEntitiesByInscricao_Candidato_Email(any())).thenReturn(avaliacaoEntities);
        when(gestorService.convertoToDTO(any())).thenReturn(GestorFactory.getGestorDTO());
        when(inscricaoService.converterParaDTO(any())).thenReturn(InscricaoFactory.getInscricaoDto());
        // Ação (ACT)
        List<AvaliacaoDTO> avaliacaoDtos1 = avaliacaoService.findAvaliacaoByCanditadoEmail(email);
        // Verificação (ASSERT)
        assertNotNull(avaliacaoDtos1);
        assertEquals(email, avaliacaoDtos1.get(0).getInscricao().getCandidato().getEmail());
    }

    @Test
    public void deveTestarUpdateComSucesso() throws RegraDeNegocioException {
        when(avaliacaoRepository.findById(anyInt())).thenReturn(Optional.of(AvaliacaoFactory.getAvaliacaoEntityAprovado()));
        when(avaliacaoRepository.save(any())).thenReturn(AvaliacaoFactory.getAvaliacaoEntityAprovado());
        when(gestorService.convertoToDTO(any())).thenReturn(GestorFactory.getGestorDTO());
        when(inscricaoService.converterParaDTO(any())).thenReturn(InscricaoFactory.getInscricaoDto());
        AvaliacaoDTO avaliacaoRetorno = avaliacaoService.update(1, AvaliacaoFactory.getAvaliacaoCreateDto());

        Assert.notNull(avaliacaoRetorno);
    }


    @Test
    public void deveTestarDeleteByIdComSucesso() throws RegraDeNegocioException {

        AvaliacaoEntity avaliacaoEntity = AvaliacaoFactory.getAvaliacaoEntityAprovado();

        when(avaliacaoRepository.findById(anyInt())).thenReturn(Optional.of(avaliacaoEntity));

        avaliacaoService.deleteById(10);
    }


}