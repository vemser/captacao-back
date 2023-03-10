package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.candidato.*;
import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.*;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.CandidatoRepository;
import com.br.dbc.captacao.repository.InscricaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidatoService {

    private static final int DESCENDING = 1;
    private final CandidatoRepository candidatoRepository;
    private final FormularioService formularioService;
    private final ObjectMapper objectMapper;
    private final LinguagemService linguagemService;
    private final TrilhaService trilhaService;
    private final EdicaoService edicaoService;
    private final InscricaoRepository inscricaoRepository;
    private final ExcelExporterCandidatos excelExporterCandidatos;
    private final VwCandidatosUltimaEdicaoService vwCandidatosUltimaEdicaoService;

    public CandidatoDTO create(CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        Optional<CandidatoEntity> candidatoEntityOptional = candidatoRepository.findByEmail(candidatoCreateDTO.getEmail());
        Optional<CandidatoEntity> candidatoEntityCPF = candidatoRepository.findByCpf(candidatoCreateDTO.getCpf());

        CandidatoEntity candidatoEntity = convertToEntity(candidatoCreateDTO);
        verificarEmailCandidato(candidatoCreateDTO, candidatoEntityOptional, candidatoEntity);
        verificarCpfCandidato(candidatoCreateDTO, candidatoEntityCPF);
        if (candidatoCreateDTO.getEmail().isEmpty() || candidatoCreateDTO.getEmail().isBlank()) {
            throw new RegraDeNegocioException("E-mail inv??lido! Deve ser inserido um endere??o de email v??lido!");
        }
        linguagemList = getLinguagensCandidato(candidatoCreateDTO, linguagemList);
        candidatoEntity.setNome(candidatoEntity.getNome().trim().toUpperCase());
        candidatoEntity.setEdicao(edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome()));
        candidatoEntity.setLinguagens(new HashSet<>(linguagemList));
        FormularioEntity formulario = formularioService.findById(candidatoCreateDTO.getFormulario());
        candidatoEntity.setFormularioEntity(formulario);

        EdicaoEntity edicao = edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome());

        candidatoEntity.setEdicao(edicao);
        candidatoEntity.setNotaProva(0.00);
        candidatoEntity.setNotaEntrevistaComportamental(0.00);
        candidatoEntity.setNotaEntrevistaTecnica(0.00);

        CandidatoDTO candidatoDTO = converterEmDTO(candidatoRepository.save(candidatoEntity));
        candidatoDTO.setFormulario(formularioService.convertToDto(formulario));
        candidatoDTO.setIdCandidato(candidatoEntity.getIdCandidato());

        return candidatoDTO;
    }

    private void verificarEmailCandidato(CandidatoCreateDTO candidatoCreateDTO,
                                         Optional<CandidatoEntity> candidatoEntityOptional,
                                         CandidatoEntity candidatoEntity) throws RegraDeNegocioException {
        if (candidatoEntityOptional.isPresent()) {
            if (candidatoEntityOptional.get().getEdicao().getNome().equals(candidatoCreateDTO.getEdicao().getNome())) {
                throw new RegraDeNegocioException("Candidato com este e-mail j?? cadastrado para essa edi????o.");
            }
            candidatoEntity.setIdCandidato(candidatoEntityOptional.get().getIdCandidato());
        }
    }

    private static void verificarCpfCandidato(CandidatoCreateDTO candidatoCreateDTO,
                                              Optional<CandidatoEntity> candidatoEntityCPF) throws RegraDeNegocioException {
        if (candidatoEntityCPF.isPresent()) {
            if (candidatoEntityCPF.get().getEdicao().getNome().equals(candidatoCreateDTO.getEdicao().getNome())) {
                throw new RegraDeNegocioException("Candidato com esse cpf j?? existe!");
            }
        }
    }

    public PageDTO<CandidatoDTO> listaAllPaginado(Integer pagina,
                                                  Integer tamanho,
                                                  String sort,
                                                  int order) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by(sort).ascending();
        if (order == DESCENDING) {
            ordenacao = Sort.by(sort).descending();
        }
        if (tamanho <= 0) {
            throw new RegraDeNegocioException("O tamanho n??o pode ser menor do que 1.");
        }
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<CandidatoEntity> paginaCandidatoEntity = candidatoRepository.findAll(pageRequest);

        List<CandidatoDTO> candidatoDtos = paginaCandidatoEntity.getContent().stream()
                .map(candidatoEntity -> converterEmDTO(candidatoEntity))
                .toList();

        return new PageDTO<>(paginaCandidatoEntity.getTotalElements(),
                paginaCandidatoEntity.getTotalPages(),
                pagina,
                tamanho,
                candidatoDtos);
    }

    public void deleteLogicoById(Integer id) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = findById(id);
        candidatoEntity.setAtivo(TipoMarcacao.F);
        candidatoRepository.save(candidatoEntity);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        if (findByIdCandidato(id).isEmpty()) {
            candidatoRepository.deleteById(id);
        } else {
            throw new RegraDeNegocioException("Candidato n??o pode ser deletado, pois est?? em uma inscri????o");
        }
    }

    public Optional<InscricaoEntity> findByIdCandidato(Integer idCandidato) {
        return inscricaoRepository.findInscricaoEntitiesByCandidato_IdCandidato(idCandidato);
    }

    public CandidatoDTO update(Integer id, CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        findById(id);
        if (candidatoCreateDTO.getEmail().isEmpty() || candidatoCreateDTO.getEmail().isBlank()) {
            throw new RegraDeNegocioException("E-mail inv??lido! Deve ser inserido um endere??o de email v??lido!");
        }
        CandidatoEntity candidatoEntity = convertToEntity(candidatoCreateDTO);
        linguagemList = getLinguagensCandidato(candidatoCreateDTO, linguagemList);
        EdicaoEntity edicao = edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome());
        candidatoEntity.setEdicao(edicao);
        candidatoEntity.setIdCandidato(id);
        candidatoEntity.setNome(candidatoCreateDTO.getNome().trim());
        candidatoEntity.setDataNascimento(candidatoCreateDTO.getDataNascimento());
        candidatoEntity.setEmail(candidatoCreateDTO.getEmail());
        candidatoEntity.setTelefone(candidatoCreateDTO.getTelefone());
        candidatoEntity.setRg(candidatoCreateDTO.getRg());
        candidatoEntity.setCpf(candidatoCreateDTO.getCpf());
        candidatoEntity.setEstado(candidatoCreateDTO.getEstado());
        candidatoEntity.setCidade(candidatoCreateDTO.getCidade());
        candidatoEntity.setLinguagens(new HashSet<>(linguagemList));

        return converterEmDTO(candidatoRepository.save(candidatoEntity));
    }

    public CandidatoDTO updateTecnico(Integer id, CandidatoTecnicoNotaDTO candidatoTecnicoNotaDTO) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = findById(id);

        candidatoEntity.setNotaEntrevistaTecnica(candidatoTecnicoNotaDTO.getNotaTecnica());
        candidatoEntity.setParecerTecnico(candidatoTecnicoNotaDTO.getParecerTecnico());

        CandidatoDTO candidatoDTO = converterEmDTO(candidatoRepository.save(candidatoEntity));
        return calcularMediaNotas(candidatoDTO.getIdCandidato());
    }

    public CandidatoDTO calcularMediaNotas(Integer id) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = findById(id);
        Double nota1 = candidatoEntity.getNotaProva() * 0.3;
        Double nota2 = candidatoEntity.getNotaEntrevistaComportamental() * 0.35;
        Double nota3 = candidatoEntity.getNotaEntrevistaTecnica() * 0.35;
        candidatoEntity.setMedia(nota1 + nota2 + nota3);
        return converterEmDTO(candidatoRepository.save(candidatoEntity));
    }

    public CandidatoDTO updateNota(Integer id, CandidatoNotaDTO candidatoNotaDTO) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = findById(id);
        candidatoEntity.setNotaProva(candidatoNotaDTO.getNotaProva());

        return converterEmDTO(candidatoRepository.save(candidatoEntity));
    }

    public CandidatoDTO updateComportamental(Integer id, CandidatoNotaComportamentalDTO candidatoNotaComportamentalDTO) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = findById(id);

        candidatoEntity.setNotaEntrevistaComportamental(candidatoNotaComportamentalDTO.getNotaComportamental());
        candidatoEntity.setParecerComportamental(candidatoNotaComportamentalDTO.getParecerComportamental());

        CandidatoDTO candidatoDTO = converterEmDTO(candidatoRepository.save(candidatoEntity));
        return calcularMediaNotas(candidatoDTO.getIdCandidato());
    }

    private List<LinguagemEntity> getLinguagensCandidato(CandidatoCreateDTO candidatoCreateDTO, List<LinguagemEntity> linguagemList) {
        for (String linguagem : candidatoCreateDTO.getLinguagens()) {
            LinguagemEntity byNome = linguagemService.findByNome(linguagem);
            if (byNome == null) {
                LinguagemDTO linguagemDTO = new LinguagemDTO(linguagem);
                linguagemService.create(linguagemDTO);
            }
            linguagemList.add(byNome);
        }
        return linguagemList;
    }

    public CandidatoEntity findById(Integer id) throws RegraDeNegocioException {
        return candidatoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Candidato n??o encontrado."));
    }

    public CandidatoDTO findDtoById(Integer idCandidato) throws RegraDeNegocioException {
        return converterEmDTO(findById(idCandidato));
    }

    public CandidatoDTO findByEmail(String email) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = findByEmailEntity(email);
        return converterEmDTO(candidatoEntity);
    }

    public CandidatoEntity findByEmailEntity(String email) throws RegraDeNegocioException {
        Optional<CandidatoEntity> candidatoEntity = candidatoRepository.findByEmail(email);
        if (candidatoEntity.isEmpty()) {
            throw new RegraDeNegocioException("Candidato com o e-mail especificado n??o existe");
        }
        return candidatoEntity.get();
    }


    public CandidatoEntity convertToEntity(CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocio404Exception {
        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoCreateDTO, CandidatoEntity.class);
        candidatoEntity.setPcd(candidatoCreateDTO.getPcd());
        candidatoEntity.setFormularioEntity(formularioService.convertToEntity(formularioService.findDtoById(candidatoCreateDTO.getFormulario())));
        return candidatoEntity;
    }

    public CandidatoEntity convertToEntity(CandidatoDTO candidatoDto) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoDto, CandidatoEntity.class);
        candidatoEntity.setFormularioEntity(objectMapper.convertValue(formularioService.findById(candidatoDto
                .getFormulario().getIdFormulario()), FormularioEntity.class));
        candidatoEntity.setEdicao(objectMapper.convertValue(candidatoDto.getEdicao(), EdicaoEntity.class));
        candidatoEntity.setLinguagens(candidatoDto.getLinguagens().stream()
                .map(linguagemDTO -> objectMapper.convertValue(linguagemDTO, LinguagemEntity.class))
                .collect(Collectors.toSet()));
        return candidatoEntity;
    }


    public CandidatoDTO converterEmDTO(CandidatoEntity candidato) {
        CandidatoDTO candidatoDTO = objectMapper.convertValue(candidato, CandidatoDTO.class);
        FormularioDTO formularioDTO = objectMapper.convertValue(candidato.getFormularioEntity(), FormularioDTO.class);
        EdicaoDTO edicaoDTO = objectMapper.convertValue(candidato.getEdicao(), EdicaoDTO.class);

        formularioDTO.setTrilhas(trilhaService.convertToDTO(candidato.getFormularioEntity().getTrilhaEntitySet()));

        if (candidato.getImageEntity() != null) {
            candidatoDTO.setImagem(candidato.getImageEntity().getIdImagem());
        }
        if (candidato.getFormularioEntity().getCurriculoEntity() != null) {
            formularioDTO.setCurriculo(candidato.getFormularioEntity().getCurriculoEntity().getIdCurriculo());
        }

        candidatoDTO.setFormulario(formularioDTO);
        candidatoDTO.setLinguagens(linguagemService.convertToDTO(candidato.getLinguagens()));
        candidatoDTO.setEdicao(edicaoDTO);

        return candidatoDTO;
    }

    public PageDTO<CandidatoDTO> findByNota(Integer pagina, Integer tamanho) {
        Sort orderBy = Sort.by("notaProva");

        PageRequest pageRequest = PageRequest.of(pagina, tamanho, orderBy);

        Page<CandidatoEntity> paginaDoRepositorio = candidatoRepository.findByNota(pageRequest, edicaoService.retornarEdicaoAtual());

        List<CandidatoDTO> candidatoDTOList = paginaDoRepositorio.getContent().stream()
                .map(this::converterEmDTO).toList();

        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                candidatoDTOList);
    }

    public PageDTO<CandidatoDTO> filtrarCandidatosAptosEntrevista(Integer pagina,
                                                                  Integer tamanho,
                                                                  String email,
                                                                  String edicao,
                                                                  String trilha) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);

        Page<CandidatoEntity> candidatoEntityPage = candidatoRepository.filtrarCandidatosAptosEntrevista(pageRequest, email, edicao, trilha);

        List<CandidatoDTO> candidatosDTO = candidatoEntityPage.stream()
                .map(this::converterEmDTO).toList();

        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                candidatosDTO);
    }

    public PageDTO<CandidatoDTO> filtrarCandidatosAprovados(Integer pagina,
                                                            Integer tamanho,
                                                            String email,
                                                            String edicao,
                                                            String trilha) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);

        Page<CandidatoEntity> candidatoEntityPage = candidatoRepository.filtrarCandidatosAprovados(pageRequest, email, edicao, trilha);

        List<CandidatoDTO> candidatosDTO = candidatoEntityPage.stream()
                .map(this::converterEmDTO).toList();

        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                candidatosDTO);
    }

    public void exportarCandidatosCsv(HttpServletResponse response) throws IOException {
        List<CandidatoEntity> listCandidato = candidatoRepository.findByMedia(edicaoService.retornarEdicaoAtual());
        List<CandidatoDTO> candidatoDTOS = listCandidato.stream()
                .map(this::converterEmDTO).toList();

        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=candidatos_aprovados_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        ExcelExporter excelExporter = new ExcelExporter(candidatoDTOS, new XSSFWorkbook());
        excelExporter.exportCandidato(response);
    }

    public void exportarCsvCanditatosEdicaoAtual(HttpServletResponse response) throws IOException {
        log.info("gerando relatorio de candidatos");
        List<VwCandidatosUltimaEdicao> listaVwCandidatosUltimaEdicoes = vwCandidatosUltimaEdicaoService.findAll();
        log.info("quantidade de candidatos: {}", listaVwCandidatosUltimaEdicoes.size());

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=candidatos_edicao_atual" + currentDateTime + ".csv";
//        response.setContentType("text/csv");
        response.setHeader(headerKey, headerValue);
        excelExporterCandidatos.writeHeaderLineCandidato(listaVwCandidatosUltimaEdicoes, response.getWriter());
        log.info("relatorio gerado");
    }
}