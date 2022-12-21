package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.candidato.CandidatoCreateDTO;
import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.dto.relatorios.RelatorioCandidatoCadastroDTO;
import com.br.dbc.captacao.dto.relatorios.RelatorioCandidatoPaginaPrincipalDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.LinguagemEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.CandidatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatoService {

//    private static final int DESCENDING = 1;
//    private final CandidatoRepository candidatoRepository;
//    private final FormularioService formularioService;
//    private final ObjectMapper objectMapper;
//
//
//    public CandidatoDTO cadastro(CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException {
//        if (!candidatoRepository.findCandidatoEntitiesByEmail(candidatoCreateDTO.getEmail()).isEmpty()) {
//            throw new RegraDeNegocioException("Email já cadastrado");
//        }
//        if (!candidatoRepository.findCandidatoEntitiesByFormulario_IdFormulario(candidatoCreateDto.getIdFormulario()).isEmpty()) {
//            throw new RegraDeNegocioException("Formulario cadastrado para outro candidato");
//        }
//        CandidatoEntity candidatoEntity = convertToEntity(candidatoCreateDTO);
//        CandidatoDTO candidatoDTO = convertToDto(candidatoRepository.save(candidatoEntity));
////        candidatoDto.setFormulario(formularioService.convertToDto(candidatoEntity.getFormulario()));
//        return candidatoDTO;
//    }
//
//
//    public void deleteById(Integer idCandidato) throws RegraDeNegocioException {
//        findById(idCandidato);
//        candidatoRepository.deleteById(idCandidato);
//    }
//
//    public CandidatoDTO update(Integer idCandidato, CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException {
//        findById(idCandidato);
//        CandidatoEntity candidatoEntity = convertToEntity(candidatoCreateDTO);
//        candidatoEntity.setIdCandidato(idCandidato);
//        CandidatoEntity candidatoEntity1 = candidatoRepository.save(candidatoEntity);
//        return convertToDto(candidatoEntity1);
//    }
//
//    public CandidatoEntity findById(Integer idCandidato) throws RegraDeNegocioException {
//        return candidatoRepository.findById(idCandidato)
//                .orElseThrow(() -> new RegraDeNegocioException("Erro ao buscar candidato!"));
//    }
//
//    public CandidatoDto findDtoById(Integer idCandidato) throws RegraDeNegocioException {
//        return convertToDto(findById(idCandidato));
//    }
//
//    public List<CandidatoDto> findCandidatoDtoByEmail(String email) throws RegraDeNegocioException {
//        List<CandidatoEntity> candidato = candidatoRepository.findCandidatoEntitiesByEmail(email);
//        List<CandidatoDto> candidatoDto = candidato.stream().map(candidatoEntity -> convertToDto(candidatoEntity)).toList();
//        return candidatoDto;
//    }
//
//    public PageDto<CandidatoDto> listaAllPaginado(Integer pagina, Integer tamanho, String sort, int order) {
//        Sort ordenacao = Sort.by(sort).ascending();
//        if (order == DESCENDING) {
//            ordenacao = Sort.by(sort).descending();
//        }
//        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
//        Page<CandidatoEntity> paginaCandidatoEntity = candidatoRepository.findAll(pageRequest);
//
//        List<CandidatoDto> candidatoDtos = paginaCandidatoEntity.getContent().stream()
//                .map(candidatoEntity -> {
//                    CandidatoDto candidatoDto = convertToDto(candidatoEntity);
//                    return candidatoDto;
//                }).toList();
//
//        return new PageDto<>(paginaCandidatoEntity.getTotalElements(),
//                paginaCandidatoEntity.getTotalPages(),
//                pagina,
//                tamanho,
//                candidatoDtos);
//    }
//
//
//    public CandidatoDTO convertToDTO(CandidatoEntity candidatoEntity) {
//        CandidatoDTO candidatoDTO = objectMapper.convertValue(candidatoEntity, CandidatoDTO.class);
//        candidatoDTO.setFormulario(formularioService.convertToDTO(candidatoEntity.getFormulario()));
//        return candidatoDTO;
//    }
//
    public CandidatoEntity convertToEntity(CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoCreateDTO, CandidatoEntity.class);
        candidatoEntity.setPcd(candidatoCreateDTO.isPcdboolean() ? TipoMarcacao.T : TipoMarcacao.F);
        candidatoEntity.setFormularioEntity(formularioService.convertToEntity(formularioService.findDtoById(candidatoCreateDTO.getFormulario().getIdFormulario())));
        return candidatoEntity;
    }
////
//    public CandidatoEntity convertToEntity(CandidatoDTO candidatoDto) {
//        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoDto, CandidatoEntity.class);
//        candidatoEntity.setFormularioEntity(formularioService.convertToEntity(candidatoDto.getFormulario()));
//        return candidatoEntity;
//    }

    private final CandidatoRepository candidatoRepository;
    private final ObjectMapper objectMapper;
    private final LinguagemService linguagemService;
    private final EdicaoService edicaoService;
    private final TrilhaService trilhaService;

    public CandidatoDTO create(CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException {
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        Optional<CandidatoEntity> candidatoEntityOptional = candidatoRepository.findByEmail(candidatoCreateDTO.getEmail());
        if (candidatoEntityOptional.isPresent()) {
            throw new RegraDeNegocioException("Candidato com este e-mail já existe no sistema.");
        }
        if (candidatoCreateDTO.getEmail().isEmpty() || candidatoCreateDTO.getEmail().isBlank()) {
            throw new RegraDeNegocioException("E-mail inválido! Deve ser inserido um endereço de email válido!");
        }

        linguagemList = getLinguagensCandidato(candidatoCreateDTO, linguagemList);
        CandidatoEntity candidatoEntity = converterEntity(candidatoCreateDTO);
        candidatoEntity.setNome(candidatoEntity.getNome().trim());
        candidatoEntity.setEdicao(edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome()));
        candidatoEntity.setLinguagens(new HashSet<>(linguagemList));
        return converterEmDTO(candidatoRepository.save(candidatoEntity));
    }

    public PageDTO<CandidatoDTO> list(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<CandidatoEntity> candidatoEntityPage = candidatoRepository.findAll(pageRequest);
        List<CandidatoDTO> candidatoDTOList = candidatoEntityPage.stream()
                .map(this::converterEmDTO)
                .toList();
        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                candidatoDTOList);
    }

    public void delete(Integer id) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = findById(id);
        candidatoEntity.setAtivo(TipoMarcacao.F);
        candidatoRepository.save(candidatoEntity);
    }

    public CandidatoDTO update(Integer id, CandidatoCreateDTO candidatoCreateDTO) throws RegraDeNegocioException {
        List<LinguagemEntity> linguagemList = new ArrayList<>();
        findById(id);
        if (candidatoCreateDTO.getEmail().isEmpty() || candidatoCreateDTO.getEmail().isBlank()) {
            throw new RegraDeNegocioException("E-mail inválido! Deve ser inserido um endereço de email válido!");
        }
        CandidatoEntity candidatoEntity = converterEntity(candidatoCreateDTO);
        linguagemList = getLinguagensCandidato(candidatoCreateDTO, linguagemList);
        candidatoEntity.setIdCandidato(id);
        candidatoEntity.setEmail(candidatoCreateDTO.getEmail());
        candidatoEntity.setNome(candidatoEntity.getNome().trim());
        candidatoEntity.setEdicao(edicaoService.findByNome(candidatoCreateDTO.getEdicao().getNome()));
        candidatoEntity.setLinguagens(new HashSet<>(linguagemList));
        return converterEmDTO(candidatoRepository.save(candidatoEntity));
    }

    private List<LinguagemEntity> getLinguagensCandidato(CandidatoCreateDTO candidatoCreateDTO, List<LinguagemEntity> linguagemList) {
        for (LinguagemDTO linguagem : candidatoCreateDTO.getLinguagens()) {
            LinguagemEntity byNome = linguagemService.findByNome(linguagem.getNome());
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

    public CandidatoEntity findByEmailEntity(String email) throws RegraDeNegocioException {
        Optional<CandidatoEntity> candidatoEntity = candidatoRepository.findByEmail(email);
        if (candidatoEntity.isEmpty()) {
            throw new RegraDeNegocioException("Candidato com o e-mail especificado não existe");
        }
        return candidatoEntity.get();
    }

    public PageDTO<RelatorioCandidatoCadastroDTO> listRelatorioCandidatoCadastroDTO(String nomeCompleto, Integer pagina, Integer tamanho, String nomeTrilha, String nomeEdicao) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by("notaProva");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<RelatorioCandidatoPaginaPrincipalDTO> candidatoEntityPage =
                candidatoRepository.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(
                        nomeCompleto,
                        nomeTrilha,
                        nomeEdicao,
                        pageRequest);
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
//            candidato.setGenero(candidatoEntity.getGenero());
            candidato.setCidade(candidatoEntity.getCidade());
            candidato.setEstado(candidatoEntity.getEstado());
            candidato.setObservacoes(candidatoEntity.getObservacoes());
//            if (candidatoEntity.getCurriculoEntity() == null) {
//                throw new RegraDeNegocioException("O candidato com o email " + candidatoEntity.getEmail() + " não possui currículo cadastrado!");
//            }
//            candidato.setDado(candidatoEntity.getCurriculoEntity().getDado());
        }
        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                relatorioCandidatoCadastroDTOPage);
    }

    public PageDTO<RelatorioCandidatoPaginaPrincipalDTO> listRelatorioRelatorioCandidatoPaginaPrincipalDTO(String nomeCompleto, Integer pagina, Integer tamanho, String nomeTrilha, String nomeEdicao) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by("notaProva");
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<RelatorioCandidatoPaginaPrincipalDTO> candidatoEntityPage = candidatoRepository.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(nomeCompleto, nomeTrilha, nomeEdicao, pageRequest);
        if (candidatoEntityPage.isEmpty()) {
            throw new RegraDeNegocioException("Candidato com dados especificados não existe");
        }
        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                candidatoEntityPage.toList());
    }

    public CandidatoEntity converterEntity(CandidatoCreateDTO candidatoCreateDTO) {
        return objectMapper.convertValue(candidatoCreateDTO, CandidatoEntity.class);
    }

    public CandidatoDTO converterEmDTO(CandidatoEntity candidatoEntity) {
        CandidatoDTO candidatoDTO = objectMapper.convertValue(candidatoEntity, CandidatoDTO.class);
        candidatoDTO.setEdicao(objectMapper.convertValue(candidatoEntity.getEdicao(), EdicaoDTO.class));
        candidatoDTO.setLinguagens(candidatoEntity.getLinguagens()
                .stream()
                .map(linguagem -> objectMapper.convertValue(linguagem, LinguagemDTO.class))
                .collect(Collectors.toSet()));
        return candidatoDTO;
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        candidatoRepository.deleteById(id);
    }
}