package com.br.dbc.captacao.service;


import com.br.dbc.captacao.dto.candidato.CandidatoCreateDTO;
import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioCreateDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoCreateDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.*;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.factory.*;
import com.br.dbc.captacao.repository.CandidatoRepository;
import com.br.dbc.captacao.repository.FormularioRepository;
import com.br.dbc.captacao.repository.InscricaoRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InscricaoServiceTest {

    @InjectMocks
    private InscricaoService inscricaoService;

    @Mock
    private InscricaoRepository inscricaoRepository;

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private EdicaoService edicaoService;

    @Mock
    private TrilhaService trilhaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(inscricaoService, "objectMapper", objectMapper);
    }


    @Test
    public void deveTestarCreateComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();

        CandidatoDTO candidatoDto = CandidatoFactory.getCandidatoDTO();
        candidatoDto.setEmail("email@email.com");

        FormularioEntity formularioEntity = FormularioFactory.getFormularioEntity();

        InscricaoCreateDTO inscricaoCreateDTO = InscricaoFactory.getInscricaoCreateDto();
        inscricaoCreateDTO.setIdCandidato(CandidatoFactory.getCandidatoDTO().getIdCandidato());


        when(inscricaoRepository.save(any())).thenReturn(InscricaoFactory.getInscricaoEntity());

        when(candidatoService.converterEmDTO(any())).thenReturn(candidatoDto);

        when(inscricaoRepository.findInscricaoEntitiesByCandidato_IdCandidato(anyInt()))
                .thenReturn(Optional.empty());

        when(candidatoService.findDtoById(any()))
                .thenReturn(candidatoDto);

        when(candidatoService.convertToEntity(any(CandidatoCreateDTO.class)))
                .thenReturn(candidatoEntity);

        inscricaoService.create(inscricaoCreateDTO.getIdCandidato());

        verify(inscricaoRepository, times(1)).save(any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarCreateComException() throws RegraDeNegocioException {

        InscricaoEntity inscricaoEntity = new InscricaoEntity();
        inscricaoEntity.setIdInscricao(1);


        InscricaoCreateDTO inscricaoCreateDTO = InscricaoFactory.getInscricaoCreateDto();

        when(inscricaoRepository.findInscricaoEntitiesByCandidato_IdCandidato(anyInt()))
                .thenReturn(Optional.of(inscricaoEntity));

        inscricaoService.create(inscricaoCreateDTO.getIdCandidato());


        verify(inscricaoRepository, times(1)).save(any());
    }

    @Test
    public void deveTestarListarPaginado() throws RegraDeNegocioException {
        Integer pagina = 1;
        Integer tamanho = 5;
        String sort = "idInscricao";
        Integer order = 1;//DESCENDING
        Sort odernacao = Sort.by(sort).descending();
        PageImpl<InscricaoEntity> inscricaoEntities = new PageImpl<>(List.of(InscricaoFactory.getInscricaoEntity()),
                PageRequest.of(pagina, tamanho, odernacao), 0);

        when(inscricaoRepository.findAll(any(Pageable.class))).thenReturn(inscricaoEntities);
        when(candidatoService.converterEmDTO(any())).thenReturn(CandidatoFactory.getCandidatoDTO());

        PageDTO<InscricaoDTO> page = inscricaoService.listar(pagina, tamanho, sort, order);

        assertEquals(page.getTamanho(), tamanho);

    }
    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarListarPaginadoComErro() throws RegraDeNegocioException {
        Integer pagina = 1;
        Integer tamanho = 5;
        String sort = "idInscricao";
        Integer order = 1;//DESCENDING
        Sort odernacao = Sort.by(sort).descending();
        PageImpl<InscricaoEntity> inscricaoEntities = new PageImpl<>(List.of(InscricaoFactory.getInscricaoEntity()),
                PageRequest.of(pagina, tamanho, odernacao), 0);

        when(inscricaoRepository.findAll(any(Pageable.class))).thenReturn(inscricaoEntities);
        when(candidatoService.converterEmDTO(any())).thenReturn(CandidatoFactory.getCandidatoDTO());

        PageDTO<InscricaoDTO> page = inscricaoService.listar(-1, -1, sort, order);


    }

    @Test
    public void deveTestarSetAvaliadoComSucesso() throws RegraDeNegocioException {

        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();

        FormularioDTO formularioDto = FormularioFactory.getFormularioDto();

        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();

        when(inscricaoRepository.save(any()))
                .thenReturn(inscricaoEntity);
        when(inscricaoRepository.findById(anyInt()))
                .thenReturn(Optional.of(inscricaoEntity));

        InscricaoEntity inscricao = inscricaoService.setAvaliado(1);

        Assert.assertNotNull(inscricao);
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {
        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();

        when(inscricaoRepository.findById(anyInt())).thenReturn(Optional.of(inscricaoEntity));

        inscricaoService.delete(1);

        verify(inscricaoRepository, times(1)).deleteById(anyInt());
    }



    @Test
    public void deveTestarFindDtoByIdComSucesso() throws RegraDeNegocioException {

        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();

        CandidatoDTO candidatoDto = CandidatoFactory.getCandidatoDTO();
        FormularioDTO formularioDto = FormularioFactory.getFormularioDto();

        when(inscricaoRepository.findById(anyInt()))
                .thenReturn(Optional.of(inscricaoEntity));
        when(candidatoService.converterEmDTO(any()))
                .thenReturn(candidatoDto);

        InscricaoDTO inscricaoDtoRetorno = inscricaoService.findDtoByid(1);

        Assert.assertNotNull(inscricaoDtoRetorno);
    }
    @Test
    public void deveTestarFindInscricaoPorEmail() throws RegraDeNegocioException {

        String email = "heloise.lopes@dbccompany.com.br";
        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();

        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();

        when(inscricaoRepository.findInscricaoByEmail(any())).thenReturn(inscricaoEntity);

        InscricaoDTO inscricaoRecuperada = inscricaoService.findInscricaoPorEmail(email);

        assertNotNull(inscricaoRecuperada);
        assertEquals(email, inscricaoEntity.getCandidato().getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarFindInscricaoPorEmailComErro() throws RegraDeNegocioException {

        String email = "heloise.lopes@dbccompany.com.br";
        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();

        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
        inscricaoEntity.getCandidato().setEmail("Françisco@dbccompany.com");

        when(inscricaoRepository.findInscricaoByEmail(any())).thenReturn(null);

        InscricaoDTO inscricaoRecuperada = inscricaoService.findInscricaoPorEmail(email);

    }

    @Test
    public void deveTestarExportarCandidatoCSVComSucesso() throws RegraDeNegocioException {
        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
        List<InscricaoEntity> inscricaoEntityList = new ArrayList<>();
        inscricaoEntityList.add(inscricaoEntity);

        BufferedWriter writer = mock(BufferedWriter.class);
        OutputStreamWriter streamWriter = mock(OutputStreamWriter.class);
        FileOutputStream fileOutputStream=mock(FileOutputStream.class);

        when(inscricaoRepository.listarInscricoesAprovadas())
                .thenReturn(inscricaoEntityList);

        inscricaoService.exportarCandidatoCSV();
    }

    @Test
    public void deveTestarConvertToEntity() throws RegraDeNegocioException, RegraDeNegocio404Exception {

        InscricaoDTO inscricaoDTO = InscricaoFactory.getInscricaoDto();
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formularioEntity = FormularioFactory.getFormularioEntity();
        TrilhaEntity trilhaEntity = TrilhaFactory.getTrilhaEntity();
        Set<TrilhaEntity> trilhas = new HashSet<>();
        trilhas.add(trilhaEntity);

        when(candidatoService.convertToEntity(inscricaoDTO.getCandidato())).thenReturn(candidatoEntity);

        InscricaoEntity inscricaoEntityRetorno = inscricaoService.convertToEntity(inscricaoDTO);

        Assert.assertNotNull(inscricaoEntityRetorno);
    }

    @Test
    public void deveTestarConverterParaDTO() throws RegraDeNegocio404Exception, RegraDeNegocioException {
        InscricaoDTO inscricaoDTO = InscricaoFactory.getInscricaoDto();
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        FormularioEntity formularioEntity = FormularioFactory.getFormularioEntity();
        TrilhaEntity trilhaEntity = TrilhaFactory.getTrilhaEntity();
        Set<TrilhaEntity> trilhas = new HashSet<>();
        trilhas.add(trilhaEntity);

        when(candidatoService.convertToEntity(inscricaoDTO.getCandidato())).thenReturn(candidatoEntity);

        InscricaoEntity inscricaoEntityRetorno = inscricaoService.convertToEntity(inscricaoDTO);

        Assert.assertNotNull(inscricaoEntityRetorno);
    }
    @Test
    public void deveTestarListInscricaoByTrilhaComSucesso() throws RegraDeNegocioException {
        TrilhaEntity trilhaEntity = TrilhaFactory.getTrilhaEntity();
        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        formulario.setTrilhaEntitySet(new HashSet<>(List.of(trilhaEntity)));
        inscricaoEntity.getCandidato().setFormularioEntity(formulario);
        List<InscricaoEntity> listInscricao = new ArrayList<>();
        listInscricao.add(inscricaoEntity);

        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();
        List<CandidatoDTO> candidatoDTOList = new ArrayList<>();
        candidatoDTOList.add(candidatoDTO);

        when(trilhaService.findByNome(anyString()))
                .thenReturn(trilhaEntity);

        when(inscricaoRepository.findInscricaoEntitiesByCandidato_FormularioEntity_TrilhaEntitySet(any(TrilhaEntity.class)))
                .thenReturn(listInscricao);

        List<InscricaoDTO> listaRetorno = inscricaoService.listInscricoesByTrilha("frontend");

        assertNotNull(listaRetorno);

    }
    @Test
    public void deveTestarListCandidatosByEdicao() throws RegraDeNegocioException {

        EdicaoEntity edicaoEntity = EdicaoFactory.getEdicaoEntity();
        TrilhaEntity trilhaEntity = TrilhaFactory.getTrilhaEntity();
        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        formulario.setTrilhaEntitySet(new HashSet<>(List.of(trilhaEntity)));
        inscricaoEntity.getCandidato().setFormularioEntity(formulario);
        List<InscricaoEntity> listInscricao = new ArrayList<>();
        listInscricao.add(inscricaoEntity);

        CandidatoDTO candidatoDTO = CandidatoFactory.getCandidatoDTO();
        List<CandidatoDTO> candidatoDTOList = new ArrayList<>();
        candidatoDTOList.add(candidatoDTO);

        when(edicaoService.findByNome(anyString()))
                .thenReturn(edicaoEntity);

        when(inscricaoRepository.findInscricaoEntitiesByCandidato_Edicao(any(EdicaoEntity.class)))
                .thenReturn(listInscricao);

        List<InscricaoDTO> listaRetorno = inscricaoService.listInscricoesByEdicao("VemSer12");

        assertNotNull(listaRetorno);
    }
}
