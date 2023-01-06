package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.candidato.*;
import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.dto.relatorios.RelatorioCandidatoCadastroDTO;
import com.br.dbc.captacao.dto.relatorios.RelatorioCandidatoPaginaPrincipalDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.*;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.CandidatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatoService {

    private static final int DESCENDING = 1;
    private final CandidatoRepository candidatoRepository;
    private final FormularioService formularioService;

    private final TrilhaService trilhaService;

    private final ObjectMapper objectMapper;
    private final LinguagemService linguagemService;
    private final EdicaoService edicaoService;

    public CandidatoDTO create(CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        Optional<CandidatoEntity> candidatoEntityOptional = candidatoRepository.findByEmail(candidatoCreateDTO.getEmail());
        if (candidatoEntityOptional.isPresent()) {
            throw new RegraDeNegocioException("Candidato com este e-mail já existe no sistema.");
        }
        if (candidatoCreateDTO.getEmail().isEmpty() || candidatoCreateDTO.getEmail().isBlank()) {
            throw new RegraDeNegocioException("E-mail inválido! Deve ser inserido um endereço de email válido!");
        }

        linguagemList = getLinguagensCandidato(candidatoCreateDTO, linguagemList);
        CandidatoEntity candidatoEntity = convertToEntity(candidatoCreateDTO);
        candidatoEntity.setNome(candidatoEntity.getNome().trim());
        candidatoEntity.setEdicao(edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome()));
        candidatoEntity.setLinguagens(new HashSet<>(linguagemList));
        FormularioEntity formulario = formularioService.findById(candidatoCreateDTO.getFormulario());
        candidatoEntity.setFormularioEntity(formulario);

        EdicaoEntity edicao = edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome());

        candidatoEntity.setEdicao(edicao);

        CandidatoDTO candidatoDTO = converterEmDTO(candidatoRepository.save(candidatoEntity));
        candidatoDTO.setFormulario(formularioService.convertToDto(formulario));
        candidatoDTO.setIdCandidato(candidatoEntity.getIdCandidato());

        return candidatoDTO;
    }


    public PageDTO<CandidatoDTO> listaAllPaginado(Integer pagina, Integer tamanho, String sort, int order) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by(sort).ascending();
        if (order == DESCENDING) {
            ordenacao = Sort.by(sort).descending();
        }
        if (tamanho <= 0) {
            throw new RegraDeNegocioException("O tamanho não pode ser menor do que 1.");
        }
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<CandidatoEntity> paginaCandidatoEntity = candidatoRepository.findAll(pageRequest);

        List<CandidatoDTO> candidatoDtos = paginaCandidatoEntity.getContent().stream()
                .map(candidatoEntity -> {
                    CandidatoDTO candidatoDto = converterEmDTO(candidatoEntity);
                    if (candidatoEntity.getImageEntity() != null) {
                        candidatoDto.setImagem(candidatoEntity.getImageEntity().getIdImagem());
                    }
                    candidatoDto.setFormulario(objectMapper.convertValue(candidatoEntity.getFormularioEntity(), FormularioDTO.class));
                    candidatoDto.setIdCandidato(candidatoEntity.getIdCandidato());
                    return candidatoDto;
                }).toList();

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
        candidatoRepository.deleteById(id);
    }

    public CandidatoDTO update(Integer id, CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        findById(id);
        if (candidatoCreateDTO.getEmail().isEmpty() || candidatoCreateDTO.getEmail().isBlank()) {
            throw new RegraDeNegocioException("E-mail inválido! Deve ser inserido um endereço de email válido!");
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
        candidatoEntity.setNotaEntrevistaTecnica(candidatoTecnicoNotaDTO.getNotaTecnico());
        candidatoEntity.setParecerTecnico(candidatoTecnicoNotaDTO.getParecerTecnico());
        return converterEmDTO(candidatoRepository.save(candidatoEntity));
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
        return converterEmDTO(candidatoRepository.save(candidatoEntity));
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
                .orElseThrow(() -> new RegraDeNegocioException("Candidato não encontrado."));
    }


    public CandidatoDTO findDtoById(Integer idCandidato) throws RegraDeNegocioException {
        return converterEmDTO(findById(idCandidato));
    }

    public CandidatoDTO findByEmail(String email) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = findByEmailEntity(email);
        return converterEmDTO(candidatoEntity);
    }

    public CandidatoDTO findCandidatoDtoByEmail(String email) throws RegraDeNegocioException {
        CandidatoEntity candidato = candidatoRepository.findCandidatoEntitiesByEmail(email);
        CandidatoDTO candidatoDto = converterEmDTO(candidato);
        return candidatoDto;
    }

    public CandidatoEntity findByEmailEntity(String email) throws RegraDeNegocioException {
        Optional<CandidatoEntity> candidatoEntity = candidatoRepository.findByEmail(email);
        if (candidatoEntity.isEmpty()) {
            throw new RegraDeNegocioException("Candidato com o e-mail especificado não existe");
        }
        return candidatoEntity.get();
    }

    public void exportarCandidatoCSV() throws RegraDeNegocioException {
        List<CandidatoEntity> candidatoEntityList = candidatoRepository.findAll();
        try {
            BufferedWriter bw = new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream("candidatos.csv", false), "UTF-8"));
            for (CandidatoEntity candidato : candidatoEntityList) {
                StringBuilder oneLine = new StringBuilder();
                oneLine.append(candidato.getIdCandidato());
                oneLine.append(",");
                oneLine.append(candidato.getNome());
                oneLine.append(",");
                oneLine.append(candidato.getEmail());
                oneLine.append(",");
                oneLine.append(candidato.getMedia());
                oneLine.append(",");
                oneLine.append(candidato.getParecerTecnico());
                oneLine.append(",");
                oneLine.append(candidato.getParecerComportamental());
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new RegraDeNegocioException("Erro ao exportar dados para arquivo.");
        }
    }

    public PageDTO<RelatorioCandidatoCadastroDTO> listRelatorioCandidatoCadastroDTO(String nomeCompleto, Integer pagina, Integer tamanho, String nomeTrilha, String nomeEdicao, String emailCandidato) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by("notaProva");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<RelatorioCandidatoPaginaPrincipalDTO> candidatoEntityPage =
                candidatoRepository.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(
                        nomeCompleto,
                        nomeTrilha,
                        nomeEdicao,
                        pageRequest,
                        emailCandidato);
        if (candidatoEntityPage.isEmpty()) {
            throw new RegraDeNegocioException("Candidato com dados especificados não existe");
        }
        List<RelatorioCandidatoCadastroDTO> relatorioCandidatoCadastroDTOPage = candidatoEntityPage
                .stream()
                .map(relatorioCandidatoPaginaPrincipalDTO ->
                        objectMapper.convertValue(relatorioCandidatoPaginaPrincipalDTO, RelatorioCandidatoCadastroDTO.class))
                .toList();
        for (RelatorioCandidatoCadastroDTO candidato : relatorioCandidatoCadastroDTOPage) {
            CandidatoEntity candidatoEntity = findByEmailEntity(candidato.getEmail());
            List<String> linguagemList = candidatoEntity.getLinguagens()
                    .stream()
                    .map(LinguagemEntity::getNome)
                    .toList();
            candidato.setLinguagemList(linguagemList);
            candidato.setEdicao(candidatoEntity.getEdicao().getNome());
            candidato.setCidade(candidatoEntity.getCidade());
            candidato.setEstado(candidatoEntity.getEstado());
            candidato.setObservacoes(candidatoEntity.getObservacoes());
            if (candidatoEntity.getFormularioEntity().getCurriculoEntity() == null) {
                throw new RegraDeNegocioException("O candidato com o email " + candidatoEntity.getEmail() + " não possui currículo cadastrado!");
            }
            candidato.setDado(candidatoEntity.getFormularioEntity().getCurriculoEntity().getData());
        }
        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                relatorioCandidatoCadastroDTOPage);
    }

    public PageDTO<RelatorioCandidatoPaginaPrincipalDTO> listRelatorioRelatorioCandidatoPaginaPrincipalDTO(String nomeCompleto, Integer pagina, Integer tamanho, String nomeTrilha, String nomeEdicao, String emailCandidato) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by("notaProva");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<RelatorioCandidatoPaginaPrincipalDTO> candidatoEntityPage = candidatoRepository.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(nomeCompleto, nomeTrilha, nomeEdicao, pageRequest, emailCandidato);
        if (candidatoEntityPage.isEmpty()) {
            throw new RegraDeNegocioException("Candidato com dados especificados não existe");
        }
        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                candidatoEntityPage.toList());
    }

    public CandidatoEntity convertToEntity(CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocio404Exception {
        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoCreateDTO, CandidatoEntity.class);
        candidatoEntity.setPcd(candidatoCreateDTO.isPcdboolean() ? TipoMarcacao.T : TipoMarcacao.F);
        candidatoEntity.setFormularioEntity(formularioService.convertToEntity(formularioService.findDtoById(candidatoCreateDTO.getFormulario())));
        return candidatoEntity;
    }


    public CandidatoEntity convertToEntity(CandidatoDTO candidatoDto) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoDto, CandidatoEntity.class);
        candidatoEntity.setFormularioEntity(objectMapper.convertValue(formularioService.findById(candidatoDto.getFormulario().getIdFormulario()), FormularioEntity.class));
        candidatoEntity.setEdicao(objectMapper.convertValue(candidatoDto.getEdicao(), EdicaoEntity.class));
        candidatoEntity.setLinguagens(candidatoDto.getLinguagens().stream()
                .map(linguagemDTO -> objectMapper.convertValue(linguagemDTO, LinguagemEntity.class))
                .collect(Collectors.toSet()));
        return candidatoEntity;
    }

    public CandidatoDTO converterEmDTO(CandidatoEntity candidatoEntity) {
        CandidatoDTO candidatoDTO = objectMapper.convertValue(candidatoEntity, CandidatoDTO.class);
        candidatoDTO.setEdicao(objectMapper.convertValue(candidatoEntity.getEdicao(), EdicaoDTO.class));
        candidatoDTO.setLinguagens(candidatoEntity.getLinguagens()
                .stream()
                .map(linguagem -> objectMapper.convertValue(linguagem, LinguagemDTO.class))
                .toList());
        return candidatoDTO;
    }

    public List<CandidatoDTO> listCandidatosByTrilha(String trilha) throws RegraDeNegocioException {

        TrilhaEntity trilhaEntity = trilhaService.findByNome(trilha);

        List<CandidatoDTO> candidatoDTOListByTrilha = candidatoRepository.findCandidatoEntitiesByFormularioEntity_TrilhaEntitySet(trilhaEntity).stream()
                .map(candidatoEntity -> {
                    CandidatoDTO candidatoDTO = objectMapper.convertValue(candidatoEntity, CandidatoDTO.class);
                    candidatoDTO.setFormulario(objectMapper.convertValue(candidatoEntity.getFormularioEntity(), FormularioDTO.class));

                    Set<TrilhaDTO> trilhaDTOList = new HashSet<>();
                    for (TrilhaEntity trilhaTemp : candidatoEntity.getFormularioEntity().getTrilhaEntitySet()) {
                        trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
                    }
                    candidatoDTO.getFormulario().setTrilhas(trilhaDTOList);

                    candidatoDTO.setEdicao(objectMapper.convertValue(candidatoEntity.getEdicao(), EdicaoDTO.class));

                    List<LinguagemDTO> linguagemDTOArrayList = new ArrayList<>();
                    for (LinguagemEntity linguagem : candidatoEntity.getLinguagens()) {
                        linguagemDTOArrayList.add(objectMapper.convertValue(linguagem, LinguagemDTO.class));
                    }
                    candidatoDTO.setLinguagens(linguagemDTOArrayList);
                    return candidatoDTO;
                })
                .toList();

        return candidatoDTOListByTrilha;
    }

    public List<CandidatoDTO> listCandidatosByEdicao(String edicao) throws RegraDeNegocioException {

        EdicaoEntity edicaoRetorno = edicaoService.findByNome(edicao);

        List<CandidatoDTO> candidatoDTOListByEdicao = candidatoRepository.findCandidatoEntitiesByEdicao(edicaoRetorno).stream()
                .map(candidatoEntity -> {
                    CandidatoDTO candidatoDTO = objectMapper.convertValue(candidatoEntity, CandidatoDTO.class);
                    candidatoDTO.setFormulario(objectMapper.convertValue(candidatoEntity.getFormularioEntity(), FormularioDTO.class));

                    Set<TrilhaDTO> trilhaDTOList = new HashSet<>();
                    for (TrilhaEntity trilhaTemp : candidatoEntity.getFormularioEntity().getTrilhaEntitySet()) {
                        trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
                    }
                    candidatoDTO.getFormulario().setTrilhas(trilhaDTOList);

                    candidatoDTO.setEdicao(objectMapper.convertValue(candidatoEntity.getEdicao(), EdicaoDTO.class));

                    List<LinguagemDTO> linguagemDTOArrayList = new ArrayList<>();
                    for (LinguagemEntity linguagem : candidatoEntity.getLinguagens()) {
                        linguagemDTOArrayList.add(objectMapper.convertValue(linguagem, LinguagemDTO.class));
                    }
                    candidatoDTO.setLinguagens(linguagemDTOArrayList);
                    return candidatoDTO;
                })
                .toList();

        return candidatoDTOListByEdicao;
    }

    public PageDTO<CandidatoDTO> listCandidatosByNota(Integer pagina, Integer tamanho) {

        Sort orderBy = Sort.by("notaProva");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, orderBy);
        Page<CandidatoEntity> paginaDoRepositorio = candidatoRepository.findByNota(pageRequest);
        List<CandidatoDTO> candidatoDTOList = paginaDoRepositorio.getContent().stream()
                .map(candidato -> objectMapper.convertValue(candidato, CandidatoDTO.class))
                .toList();
        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                candidatoDTOList);
    }
}