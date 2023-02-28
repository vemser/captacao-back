package com.br.dbc.captacao.service;


import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoCreateDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.InscricaoEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.factory.*;
import com.br.dbc.captacao.repository.AvaliacaoRepository;
import com.br.dbc.captacao.repository.InscricaoRepository;
import com.br.dbc.captacao.enums.TipoMarcacao;
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
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InscricaoServiceTest {

    @InjectMocks
    private InscricaoService inscricaoService;
    @Mock
    private InscricaoRepository inscricaoRepository;
    @Mock
    private CandidatoService candidatoService;
    @Mock
    private EdicaoService edicaoService;
    @Mock
    private AvaliacaoRepository avaliacaoRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(inscricaoService, "objectMapper", objectMapper);
    }

//    @Test
//    public void deveTestarCreateComSucesso() throws RegraDeNegocioException, RegraDeNegocio404Exception {
//        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();
//
//        Set<TrilhaEntity> listTrilha = new HashSet<>();
//        listTrilha.add(trilha);
//        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
//        formulario.setTrilhaEntitySet(listTrilha);
//        candidatoEntity.setFormularioEntity(formulario);
//        inscricaoEntity.setCandidato(candidatoEntity);
//        inscricaoEntity.setDataInscricao(LocalDate.now());
//        inscricaoEntity.setAvaliado(TipoMarcacao.F);
//        inscricaoEntity.setIdCandidato(candidatoEntity.getIdCandidato());
//        inscricaoEntity.setAvaliacaoEntity(AvaliacaoFactory.getAvaliacaoEntityAprovado());
//        when(inscricaoRepository.findInscricaoEntitiesByCandidato_IdCandidato(anyInt())).thenReturn(Optional.empty());
//        when(inscricaoRepository.save(any())).thenReturn(inscricaoEntity);
//
//        InscricaoDTO inscricaoDTO = inscricaoService.create(inscricaoEntity.getIdCandidato());
//
//        assertEquals(inscricaoDTO.getIdInscricao(), 1);
//    }

    @Test
    public void deveTestarListarAllPaginadoComSucesso() throws RegraDeNegocioException{
        Integer pagina = 1;
        Integer tamanho = 5;
        String sort = "idCandidato";
        Integer orderDescendente = 1;
        Sort ordenacao = Sort.by(sort).descending();
        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();

        Set<TrilhaEntity> listTrilha = new HashSet<>();
        listTrilha.add(trilha);
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        formulario.setTrilhaEntitySet(listTrilha);
        candidatoEntity.setFormularioEntity(formulario);
        inscricaoEntity.setCandidato(candidatoEntity);
        inscricaoEntity.setDataInscricao(LocalDate.now());
        inscricaoEntity.setAvaliado(TipoMarcacao.F);
        PageImpl<InscricaoEntity> inscricaoEntities = new PageImpl<>(List.of(inscricaoEntity), PageRequest.of(pagina, tamanho, ordenacao), 0);

        when(inscricaoRepository.findAll(any(Pageable.class)))
                .thenReturn(inscricaoEntities);

        PageDTO<InscricaoDTO> pages = inscricaoService.listar(pagina, tamanho, sort, orderDescendente);

        assertNotNull(pages);

    }

    @Test
    public void deveTestarListarAllPaginadoVazioComSucesso() throws RegraDeNegocioException{
        Integer pagina = 1;
        Integer tamanho = 0;
        String sort = "idCandidato";
        Integer orderDescendente = 1;
        Sort ordenacao = Sort.by(sort).descending();
        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();

        Set<TrilhaEntity> listTrilha = new HashSet<>();
        listTrilha.add(trilha);
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        formulario.setTrilhaEntitySet(listTrilha);
        candidatoEntity.setFormularioEntity(formulario);
        inscricaoEntity.setCandidato(candidatoEntity);
        inscricaoEntity.setDataInscricao(LocalDate.now());
        inscricaoEntity.setAvaliado(TipoMarcacao.F);
        List<InscricaoDTO> listaVazia = new ArrayList<>();
//        PageDTO<InscricaoDTO> inscricaoDTOPageDTO = new PageDTO<>(0L, 0, 0, tamanho, listaVazia);
        Page<InscricaoDTO> inscricaoDTOS = new PageImpl<>(List.copyOf(listaVazia));

//        when(inscricaoRepository.findAll(any(Pageable.class)))
//                .thenReturn(inscricaoEntities);

        PageDTO<InscricaoDTO> pages = inscricaoService.listar(pagina, tamanho, sort, orderDescendente);

        assertNotNull(pages);

    }

    @Test
    public void deveFiltrarInscricoesComSucesso() throws RegraDeNegocioException{
        Integer pagina = 1;
        Integer tamanho = 5;
        String sort = "idCandidato";
        Integer orderDescendente = 1;
        Sort ordenacao = Sort.by(sort).descending();
        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();
        final String trilhaNome = "Frontend";
        final String edicaoNome = "Edição 10";
        Set<TrilhaEntity> listTrilha = new HashSet<>();
        listTrilha.add(trilha);
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        formulario.setTrilhaEntitySet(listTrilha);
        candidatoEntity.setFormularioEntity(formulario);
        inscricaoEntity.setCandidato(candidatoEntity);
        inscricaoEntity.setDataInscricao(LocalDate.now());
        inscricaoEntity.setAvaliado(TipoMarcacao.F);
        PageImpl<InscricaoEntity> inscricaoEntities = new PageImpl<>(List.of(inscricaoEntity), PageRequest.of(pagina, tamanho, ordenacao), 0);

        when(inscricaoRepository.filtrarInscricoes(any(Pageable.class), anyString(), anyString(), anyString()))
                .thenReturn(inscricaoEntities);

        PageDTO<InscricaoDTO> pages = inscricaoService.filtrarInscricoes(pagina, tamanho, inscricaoEntity.getCandidato().getEmail(), edicaoNome, trilhaNome);

        assertEquals(pages.getTamanho(), tamanho);


    }

//    @Test(expected = RegraDeNegocioException.class)
//    public void deveFiltrarInscricoesComErro() throws RegraDeNegocioException{
//        Integer pagina = 1;
//        Integer tamanho = 5;
//        String sort = "idCandidato";
//        Integer orderDescendente = 1;
//        Sort ordenacao = Sort.by(sort).descending();
//        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
//        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
//        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();
//        final String trilhaNome = "Frontend";
//        final String edicaoNome = "Edição 10";
//        Set<TrilhaEntity> listTrilha = new HashSet<>();
//        listTrilha.add(trilha);
//        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
//        formulario.setTrilhaEntitySet(listTrilha);
//        candidatoEntity.setFormularioEntity(formulario);
//        inscricaoEntity.setCandidato(candidatoEntity);
//        inscricaoEntity.setDataInscricao(LocalDate.now());
//        inscricaoEntity.setAvaliado(TipoMarcacao.F);
//        PageImpl<InscricaoEntity> inscricaoEntities = new PageImpl<>(List.of(inscricaoEntity), PageRequest.of(pagina, tamanho, ordenacao), 0);
//
//        when(inscricaoRepository.filtrarInscricoes(any(Pageable.class), anyString(), anyString(), anyString()))
//                .thenReturn(inscricaoEntities);
//
//        inscricaoService.filtrarInscricoes(-1, -1, "sdafsa", edicaoNome, trilhaNome);
//
//    }
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

//    @Test
//    public void deveTestarListarPaginado() throws RegraDeNegocioException {
//        Integer pagina = 1;
//        Integer tamanho = 5;
//        String sort = "idInscricao";
//        Integer order = 1;//DESCENDING
//        Sort odernacao = Sort.by(sort).descending();
//        PageImpl<InscricaoEntity> inscricaoEntities = new PageImpl<>(List.of(InscricaoFactory.getInscricaoEntity()),
//                PageRequest.of(pagina, tamanho, odernacao), 0);
//
//        when(inscricaoRepository.findAll(any(Pageable.class))).thenReturn(inscricaoEntities);
//        when(candidatoService.converterEmDTO(any())).thenReturn(CandidatoFactory.getCandidatoDTO());
//
//        PageDTO<InscricaoDTO> page = inscricaoService.listar(pagina, tamanho, sort, order);
//
//        assertEquals(page.getTamanho(), tamanho);
//
//    }

    @Test
    public void deveProcurarDTOPeloIdComSucesso() throws RegraDeNegocioException{
        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();

        Set<TrilhaEntity> listTrilha = new HashSet<>();
        listTrilha.add(trilha);
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        formulario.setTrilhaEntitySet(listTrilha);
        candidatoEntity.setFormularioEntity(formulario);
        inscricaoEntity.setCandidato(candidatoEntity);
        inscricaoEntity.setDataInscricao(LocalDate.now());
        inscricaoEntity.setAvaliado(TipoMarcacao.F);
        inscricaoEntity.setIdCandidato(candidatoEntity.getIdCandidato());
        inscricaoEntity.setAvaliacaoEntity(AvaliacaoFactory.getAvaliacaoEntityAprovado());

        when(inscricaoRepository.findById(anyInt())).thenReturn(Optional.of(inscricaoEntity));

        InscricaoDTO inscricaoDTO = inscricaoService.findDtoById(inscricaoEntity.getIdInscricao());

        assertEquals(inscricaoDTO.getIdInscricao(), inscricaoEntity.getIdInscricao());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveProcurarDTOPeloIdComErro() throws RegraDeNegocioException{
        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
        CandidatoEntity candidatoEntity = CandidatoFactory.getCandidatoEntity();
        TrilhaEntity trilha = TrilhaFactory.getTrilhaEntity();

        Set<TrilhaEntity> listTrilha = new HashSet<>();
        listTrilha.add(trilha);
        FormularioEntity formulario = FormularioFactory.getFormularioEntity();
        formulario.setTrilhaEntitySet(listTrilha);
        candidatoEntity.setFormularioEntity(formulario);
        inscricaoEntity.setCandidato(candidatoEntity);
        inscricaoEntity.setDataInscricao(LocalDate.now());
        inscricaoEntity.setAvaliado(TipoMarcacao.F);
        inscricaoEntity.setIdCandidato(candidatoEntity.getIdCandidato());
        inscricaoEntity.setAvaliacaoEntity(AvaliacaoFactory.getAvaliacaoEntityAprovado());

        when(inscricaoRepository.findById(anyInt())).thenReturn(Optional.empty());

        inscricaoService.findDtoById(inscricaoEntity.getIdInscricao());
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
        when(avaliacaoRepository.findAvaliacaoEntitiesByInscricao_IdInscricao(anyInt())).thenReturn(null);

        inscricaoService.delete(1);

        verify(inscricaoRepository, times(1)).deleteById(anyInt());
    }

//    @Test
//    public void deveTestarFindDtoByIdComSucesso() throws RegraDeNegocioException {
//
//        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
//
//        CandidatoDTO candidatoDto = CandidatoFactory.getCandidatoDTO();
//        FormularioDTO formularioDto = FormularioFactory.getFormularioDto();
//
//        when(inscricaoRepository.findById(anyInt()))
//                .thenReturn(Optional.of(inscricaoEntity));
//        when(candidatoService.converterEmDTO(any()))
//                .thenReturn(candidatoDto);
//
//        InscricaoDTO inscricaoDtoRetorno = inscricaoService.findDtoByid(1);
//
//        Assert.assertNotNull(inscricaoDtoRetorno);
//    }


//    @Test(expected = IOException.class)
//    public void deveTestarExportarCSVComIOException() throws RegraDeNegocioException, IOException {
//        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
//        List<InscricaoEntity> inscricaoEntityList = new ArrayList<>();
//        inscricaoEntityList.add(inscricaoEntity);
//
//        final BufferedWriter writer = Mockito.mock(BufferedWriter.class, RETURNS_SMART_NULLS);
//        OutputStreamWriter streamWriter = mock(OutputStreamWriter.class, RETURNS_DEEP_STUBS);
//        FileOutputStream fileOutputStream=mock(FileOutputStream.class, RETURNS_DEEP_STUBS);
//
//
//        when(inscricaoRepository.listarInscricoesAprovadas())
//                .thenReturn(inscricaoEntityList);
//        when(writer.getClass()).thenThrow(new IOException("Teste"));
//        when(streamWriter.getEncoding()).thenThrow(new IOException("Teste"));
//        when(fileOutputStream.getClass()).thenThrow(new IOException("Teste"));
////        doThrow(new IOException()).when(writer).getClass();
////        when(writer.isOk()).thenReturn(true);
////        when(writer.isOk()).thenThrow(new IOException("Teste"));
////        doThrow(new IOException("Teste")).when(writer).
//
//        inscricaoService.exportarCandidatoCSV();
//    }

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
    public void deveBuscarPorIdComSucesso() throws RegraDeNegocioException{
        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
        inscricaoEntity.setIdInscricao(1);

        when(inscricaoRepository.findById(anyInt())).thenReturn(Optional.of(inscricaoEntity));

        InscricaoEntity inscricao = inscricaoService.findById(inscricaoEntity.getIdInscricao());

        assertEquals(inscricao, inscricaoEntity);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveBuscarOPorIdComErro() throws RegraDeNegocioException{
        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
        inscricaoEntity.setIdInscricao(1);

        when(inscricaoRepository.findById(anyInt())).thenReturn(Optional.empty());

        inscricaoService.findById(inscricaoEntity.getIdInscricao());
    }
//    @Test
//    public void deveBuscarDTOPorIdComSucesso() throws RegraDeNegocioException{
//        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
//
//        when(inscricaoRepository.findById(anyInt())).thenReturn(Optional.of(inscricaoEntity));
//
//        InscricaoDTO inscricaoDTO = inscricaoService.findDtoById(1);
//
//        assertEquals(inscricaoDTO.getIdInscricao(), inscricaoEntity.getIdInscricao());
//    }

//    @Test(expected = RegraDeNegocioException.class)
//    public void deveBuscarDTOPorIdComErro() throws RegraDeNegocioException{
//        InscricaoEntity inscricaoEntity = InscricaoFactory.getInscricaoEntity();
//
//        when(inscricaoRepository.findById(anyInt())).thenReturn(Optional.of(inscricaoEntity));
//
//        inscricaoService.findDtoById(1);
//    }
}
