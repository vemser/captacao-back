package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.avaliacao.AvaliacaoCreateDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.*;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.factory.AvaliacaoFactory;
import com.br.dbc.captacao.factory.InscricaoFactory;
import com.br.dbc.captacao.repository.AvaliacaoRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.br.dbc.captacao.factory.AvaliacaoFactory.getAvaliacaoCreateDto;
import static com.br.dbc.captacao.factory.AvaliacaoFactory.getAvaliacaoEntityAprovado;
import static com.br.dbc.captacao.factory.CandidatoFactory.getCandidatoEntity;
import static com.br.dbc.captacao.factory.CargoFactory.getCargoEntity;
import static com.br.dbc.captacao.factory.EdicaoFactory.getEdicaoEntity;
import static com.br.dbc.captacao.factory.FormularioFactory.getFormularioEntity;
import static com.br.dbc.captacao.factory.GestorFactory.getGestorEntity;
import static com.br.dbc.captacao.factory.InscricaoFactory.getInscricaoEntity;
import static com.br.dbc.captacao.factory.TrilhaFactory.getTrilhaEntity;
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
    private AvaliacaoRepository avaliacaoRepository;

    @Mock
    private TrilhaService trilhaService;

    @Mock
    private EdicaoService edicaoService;

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

        AvaliacaoEntity avaliacaoEntity = getAvaliacaoEntityAprovado();
        avaliacaoEntity.setInscricao(getInscricaoEntity());

        FormularioEntity formularioEntity = getFormularioEntity();
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        candidatoEntity.setFormularioEntity(formularioEntity);

        InscricaoEntity inscricaoEntity = getInscricaoEntity();
        inscricaoEntity.setCandidato(candidatoEntity);

        when(inscricaoService.findById(anyInt())).thenReturn(inscricaoEntity);
        when(gestorService.findByEmail(any())).thenReturn(getAvaliacaoEntityAprovado().getAvaliador());
        when(avaliacaoRepository.findAvaliacaoEntitiesByInscricao_IdInscricao(anyInt())).thenReturn(null);
        when(inscricaoService.converterParaDTO((any()))).thenReturn(InscricaoFactory.getInscricaoDto());
        when(avaliacaoRepository.save(any())).thenReturn(getAvaliacaoEntityAprovado());

        AvaliacaoDTO avaliacaoDtoRetorno = avaliacaoService.create(avaliacaoCreateDTO);

        Assert.notNull(avaliacaoDtoRetorno);
        Assertions.assertEquals(TipoMarcacao.T, avaliacaoDtoRetorno.getAprovado());
//        verify(emailService, times(1)).sendEmail(any(), any());
    }

    @Test
    public void deveTestarFindByDtoComSucesso() throws RegraDeNegocioException {
        int id = 1;
        AvaliacaoEntity avaliacaoEntity = getAvaliacaoEntityAprovado();

        when(avaliacaoRepository.findById(anyInt()))
                .thenReturn(Optional.of(avaliacaoEntity));

        AvaliacaoDTO avaliacaoDto = avaliacaoService.findDtoById(id);

        assertEquals(id, avaliacaoDto.getIdAvaliacao());
    }

    @Test
    public void deveTestarCreateComSucessoReprovado() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        AvaliacaoCreateDTO avaliacaoCreateDto = AvaliacaoFactory.getAvaliacaoCreateDto();

        FormularioEntity formularioEntity = getFormularioEntity();
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        candidatoEntity.setFormularioEntity(formularioEntity);

        InscricaoEntity inscricaoEntity = getInscricaoEntity();
        inscricaoEntity.setCandidato(candidatoEntity);

        CargoEntity cargo = getCargoEntity();
        GestorEntity gestor = getGestorEntity();
        gestor.setCargoEntity(Set.of(cargo));

        when(avaliacaoRepository.findAvaliacaoEntitiesByInscricao_IdInscricao(anyInt())).thenReturn(null);
        when(inscricaoService.findById(anyInt())).thenReturn(inscricaoEntity);
        when(inscricaoService.converterParaDTO((any()))).thenReturn(InscricaoFactory.getInscricaoDto());
        when(gestorService.findByEmail(any())).thenReturn(gestor);
        when(avaliacaoRepository.save(any())).thenReturn(AvaliacaoFactory.getAvaliacaoEntityReprovado());

        AvaliacaoDTO avaliacaoDtoRetorno = avaliacaoService.create(avaliacaoCreateDto);

        Assert.notNull(avaliacaoDtoRetorno);
        Assertions.assertEquals(TipoMarcacao.F, avaliacaoDtoRetorno.getAprovado());
//        verify(emailService, times(1)).sendEmail(any(), any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarCreateComException() throws RegraDeNegocioException {
        AvaliacaoCreateDTO avaliacaoCreateDto = AvaliacaoFactory.getAvaliacaoCreateDto();
        AvaliacaoEntity avaliacaoEntity = getAvaliacaoEntityAprovado();

        when(avaliacaoRepository.findAvaliacaoEntitiesByInscricao_IdInscricao(anyInt())).thenReturn(avaliacaoEntity);

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
        PageImpl<AvaliacaoEntity> pageImpl = new PageImpl<>(List.of(getAvaliacaoEntityAprovado()),
                PageRequest.of(pagina, tamanho, odernacao), 0);

        when(avaliacaoRepository.findAll(any(Pageable.class))).thenReturn(pageImpl);

        PageDTO<AvaliacaoDTO> page = avaliacaoService.list(pagina, tamanho, sort, order);

        assertEquals(page.getTamanho(), tamanho);

    }

    @Test
    public void deveTestarFindAvaliacaoPorEmail() throws RegraDeNegocioException {
        // Criar variaveis (SETUP)
        String email = "heloise.lopes@dbccompany.com.br";
        List<AvaliacaoDTO> avaliacaoDtos = List.of(AvaliacaoFactory.getAvaliacaoDto());
        List<AvaliacaoEntity> avaliacaoEntities = List.of(AvaliacaoFactory.getAvaliacaoEntityReprovado());

        when(avaliacaoRepository.findAvaliacaoEntitiesByInscricao_Candidato_Email(any())).thenReturn(avaliacaoEntities);
        when(inscricaoService.converterParaDTO(any())).thenReturn(InscricaoFactory.getInscricaoDto());
        // Ação (ACT)
        List<AvaliacaoDTO> avaliacaoDtos1 = avaliacaoService.findAvaliacaoByCanditadoEmail(email);
        // Verificação (ASSERT)
        assertNotNull(avaliacaoDtos1);
        assertEquals(email, avaliacaoDtos1.get(0).getInscricao().getCandidato().getEmail());
    }

    @Test
    public void deveTestarUpdateComSucesso() throws RegraDeNegocioException {
        when(avaliacaoRepository.findById(anyInt())).thenReturn(Optional.of(getAvaliacaoEntityAprovado()));
        when(avaliacaoRepository.save(any())).thenReturn(getAvaliacaoEntityAprovado());
        when(inscricaoService.converterParaDTO(any())).thenReturn(InscricaoFactory.getInscricaoDto());
        AvaliacaoDTO avaliacaoRetorno = avaliacaoService.update(1, AvaliacaoFactory.getAvaliacaoCreateDto());

        Assert.notNull(avaliacaoRetorno);
    }

    @Test
    public void deveTestarDeleteByIdComSucesso() throws RegraDeNegocioException {
        AvaliacaoEntity avaliacaoEntity = getAvaliacaoEntityAprovado();

        when(avaliacaoRepository.findById(anyInt())).thenReturn(Optional.of(avaliacaoEntity));

        avaliacaoService.deleteById(10);
    }

    @Test
    public void deveListarAvaliacaoPorTrilha() throws RegraDeNegocioException {
        final String trilhaNome = "BACKEND";
        TrilhaEntity trilha = getTrilhaEntity();

        FormularioEntity formularioEntity = getFormularioEntity();
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        candidatoEntity.setFormularioEntity(formularioEntity);
        InscricaoEntity inscricaoEntity = getInscricaoEntity();
        inscricaoEntity.setCandidato(candidatoEntity);
        AvaliacaoEntity avaliacaoEntity = getAvaliacaoEntityAprovado();
        avaliacaoEntity.setInscricao(inscricaoEntity);

        when(trilhaService.findByNome(anyString())).thenReturn(trilha);
        when(avaliacaoRepository.findAvaliacaoEntitiesByInscricao_Candidato_FormularioEntity_TrilhaEntitySet(trilha))
                .thenReturn(List.of(avaliacaoEntity));
        List<AvaliacaoDTO> list = avaliacaoService.listByTrilha(trilhaNome);

        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void deveListarAvaliacaoPorEdicao() throws RegraDeNegocioException {
        final String edicaoNome = "Edição 10";
        EdicaoEntity edicaoEntity = getEdicaoEntity();

        FormularioEntity formularioEntity = getFormularioEntity();
        CandidatoEntity candidatoEntity = getCandidatoEntity();
        candidatoEntity.setFormularioEntity(formularioEntity);
        InscricaoEntity inscricaoEntity = getInscricaoEntity();
        inscricaoEntity.setCandidato(candidatoEntity);
        AvaliacaoEntity avaliacaoEntity = getAvaliacaoEntityAprovado();
        avaliacaoEntity.setInscricao(inscricaoEntity);

        when(edicaoService.findByNome(anyString())).thenReturn(edicaoEntity);
        when(avaliacaoRepository.findAvaliacaoEntitiesByInscricao_Candidato_Edicao(edicaoEntity))
                .thenReturn(List.of(avaliacaoEntity));
        List<AvaliacaoDTO> list = avaliacaoService.listByEdicao(edicaoNome);

        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void deveConverterAvaliacaoDTOParaEntity() throws RegraDeNegocio404Exception, RegraDeNegocioException {
        AvaliacaoCreateDTO avaliacaoCreateDTO = getAvaliacaoCreateDto();

        AvaliacaoEntity avaliacaoEntity = avaliacaoService.convertToEntity(avaliacaoCreateDTO);

        assertEquals(TipoMarcacao.T, avaliacaoEntity.getAprovado());
    }
}
