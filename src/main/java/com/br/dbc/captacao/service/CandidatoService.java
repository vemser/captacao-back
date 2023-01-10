package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.candidato.*;
import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.EdicaoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.LinguagemEntity;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatoService {

    private static final int DESCENDING = 1;
    private final CandidatoRepository candidatoRepository;
    private final FormularioService formularioService;
    private final ObjectMapper objectMapper;
    private final LinguagemService linguagemService;
    private final TrilhaService trilhaService;
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

    public CandidatoDTO findCandidatoDtoByEmail(String email) {
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
        try {
        List<CandidatoEntity> candidatoEntityList = candidatoRepository.findAll();
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

    public CandidatoDTO converterEmDTO(CandidatoEntity candidato) {
//        CandidatoDTO candidatoDTO = objectMapper.convertValue(candidatoEntity, CandidatoDTO.class);
//
//        if (candidatoEntity.getImageEntity() != null) {
//            candidatoDTO.setImagem(candidatoEntity.getImageEntity().getIdImagem());
//        }
//        candidatoDTO.setFormulario(objectMapper.convertValue(candidatoEntity.getFormularioEntity(), FormularioDTO.class));
//        if (candidatoEntity.getFormularioEntity().getCurriculoEntity() != null) {
//            candidatoDTO.getFormulario().setCurriculo(candidatoEntity.getFormularioEntity().getCurriculoEntity().getIdCurriculo());
//        }
//        List<TrilhaDTO> trilhaDTOList = new ArrayList<>();
//        for (TrilhaEntity trilha : candidatoEntity.getFormularioEntity().getTrilhaEntitySet()) {
//            trilhaDTOList.add(objectMapper.convertValue(trilha, TrilhaDTO.class));
//        }
//        candidatoDTO.getFormulario().setTrilhas(new HashSet<>(trilhaDTOList));
//        if (candidatoEntity.getFormularioEntity().getImagemConfigPc() != null) {
//            candidatoDTO.getFormulario().setImagemConfigPc(candidatoEntity.getFormularioEntity().getImagemConfigPc().getIdImagem());
//        }
//        if (candidatoEntity.getObservacoes() != null) {
//            candidatoDTO.setObservacoes(candidatoEntity.getObservacoes());
//        }
//        if (candidatoEntity.getParecerComportamental() != null) {
//            candidatoDTO.setParecerComportamental(candidatoEntity.getParecerComportamental());
//        }
//        if (candidatoEntity.getParecerTecnico() != null) {
//            candidatoDTO.setParecerTecnico(candidatoEntity.getParecerTecnico());
//        }
//        if (candidatoEntity.getMedia() != null) {
//            candidatoDTO.setMedia(candidatoEntity.getMedia());
//        }
//        candidatoDTO.setIdCandidato(candidatoEntity.getIdCandidato());
//        candidatoDTO.setEdicao(objectMapper.convertValue(candidatoEntity.getEdicao(), EdicaoDTO.class));
//        candidatoDTO.setLinguagens(candidatoEntity.getLinguagens()
//                .stream()
//                .map(linguagem -> objectMapper.convertValue(linguagem, LinguagemDTO.class))
//                .toList());
//        return candidatoDTO;

                    CandidatoDTO candidatoDTO = objectMapper.convertValue(candidato, CandidatoDTO.class);
                    FormularioDTO formularioDTO = objectMapper.convertValue(candidato.getFormularioEntity(), FormularioDTO.class);
                    EdicaoDTO edicaoDTO = objectMapper.convertValue(candidato.getEdicao(), EdicaoDTO.class);

                    formularioDTO.setTrilhas(trilhaService.convertToDTO(candidato.getFormularioEntity().getTrilhaEntitySet()));
                    if(candidato.getImageEntity() != null) {
                        candidatoDTO.setImagem(candidato.getImageEntity().getIdImagem());
                    }
                    if(candidato.getFormularioEntity().getCurriculoEntity() != null) {
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

        Page<CandidatoEntity> paginaDoRepositorio = candidatoRepository.findByNota(pageRequest);

        List<CandidatoDTO> candidatoDTOList = paginaDoRepositorio.getContent().stream()
                .map(this::converterEmDTO).toList();

        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                candidatoDTOList);
    }

    public PageDTO<CandidatoDTO> filtrarCandidatos(Integer pagina, Integer tamanho, String nome, String email, String edicao, String trilha) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);

        Page<CandidatoEntity> candidatoEntityPage = candidatoRepository.filtrarCandidatos(pageRequest, nome, email, edicao, trilha);

        List<CandidatoDTO> candidatosDTO = candidatoEntityPage.stream()
                .map(this::converterEmDTO).toList();

        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
                candidatoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                candidatosDTO);
    }

    public PageDTO<CandidatoDTO> findByMedia(Integer pagina, Integer tamanho) {
        Sort orderBy = Sort.by("media");

        PageRequest pageRequest = PageRequest.of(pagina, tamanho, orderBy);

        Page<CandidatoEntity> paginaDoRepositorio = candidatoRepository.findByMedia(pageRequest);

        List<CandidatoDTO> candidatoDTOList = paginaDoRepositorio.getContent().stream()
                .map(this::converterEmDTO).toList();

        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                candidatoDTOList);
    }

    //    public PageDTO<RelatorioCandidatoCadastroDTO> listRelatorioCandidatoCadastroDTO(String nomeCompleto, Integer pagina, Integer tamanho, String nomeTrilha, String nomeEdicao, String emailCandidato) throws RegraDeNegocioException {
//        Sort ordenacao = Sort.by("notaProva");
//        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
//        Page<RelatorioCandidatoPaginaPrincipalDTO> candidatoEntityPage =
//                candidatoRepository.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(
//                        nomeCompleto,
//                        nomeTrilha,
//                        nomeEdicao,
//                        pageRequest,
//                        emailCandidato);
//        if (candidatoEntityPage.isEmpty()) {
//            throw new RegraDeNegocioException("Candidato com dados especificados não existe");
//        }
//        List<RelatorioCandidatoCadastroDTO> relatorioCandidatoCadastroDTOPage = candidatoEntityPage
//                .stream()
//                .map(relatorioCandidatoPaginaPrincipalDTO ->
//                        objectMapper.convertValue(relatorioCandidatoPaginaPrincipalDTO, RelatorioCandidatoCadastroDTO.class))
//                .toList();
//        for (RelatorioCandidatoCadastroDTO candidato : relatorioCandidatoCadastroDTOPage) {
//            CandidatoEntity candidatoEntity = findByEmailEntity(candidato.getEmail());
//            List<String> linguagemList = candidatoEntity.getLinguagens()
//                    .stream()
//                    .map(LinguagemEntity::getNome)
//                    .toList();
//            candidato.setLinguagemList(linguagemList);
//            candidato.setEdicao(candidatoEntity.getEdicao().getNome());
//            candidato.setCidade(candidatoEntity.getCidade());
//            candidato.setEstado(candidatoEntity.getEstado());
//            candidato.setObservacoes(candidatoEntity.getObservacoes());
//            if (candidatoEntity.getFormularioEntity().getCurriculoEntity() == null) {
//                throw new RegraDeNegocioException("O candidato com o email " + candidatoEntity.getEmail() + " não possui currículo cadastrado!");
//            }
//            candidato.setDado(candidatoEntity.getFormularioEntity().getCurriculoEntity().getData());
//        }
//        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
//                candidatoEntityPage.getTotalPages(),
//                pagina,
//                tamanho,
//                relatorioCandidatoCadastroDTOPage);
//    }

//    public PageDTO<RelatorioCandidatoPaginaPrincipalDTO> listRelatorioRelatorioCandidatoPaginaPrincipalDTO(String nomeCompleto, Integer pagina, Integer tamanho, String nomeTrilha, String nomeEdicao, String emailCandidato) throws RegraDeNegocioException {
//        Sort ordenacao = Sort.by("notaProva");
//        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
//        Page<RelatorioCandidatoPaginaPrincipalDTO> candidatoEntityPage = candidatoRepository.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(nomeCompleto, nomeTrilha, nomeEdicao, pageRequest, emailCandidato);
//        if (candidatoEntityPage.isEmpty()) {
//            throw new RegraDeNegocioException("Candidato com dados especificados não existe");
//        }
//        return new PageDTO<>(candidatoEntityPage.getTotalElements(),
//                candidatoEntityPage.getTotalPages(),
//                pagina,
//                tamanho,
//                candidatoEntityPage.toList());
//    }

//    public List<CandidatoDTO> listCandidatosByTrilha(String trilha) throws RegraDeNegocioException {
//
//        TrilhaEntity trilhaEntity = trilhaService.findByNome(trilha);
//
//        List<CandidatoDTO> candidatoDTOListByTrilha = candidatoRepository.findCandidatoEntitiesByFormularioEntity_TrilhaEntitySet(trilhaEntity).stream()
//                .map(candidatoEntity -> {
//                    CandidatoDTO candidatoDTO = objectMapper.convertValue(candidatoEntity, CandidatoDTO.class);
//                    candidatoDTO.setFormulario(objectMapper.convertValue(candidatoEntity.getFormularioEntity(), FormularioDTO.class));
//
//                    Set<TrilhaDTO> trilhaDTOList = new HashSet<>();
//                    for (TrilhaEntity trilhaTemp : candidatoEntity.getFormularioEntity().getTrilhaEntitySet()) {
//                        trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
//                    }
//                    candidatoDTO.getFormulario().setTrilhas(trilhaDTOList);
//
//                    candidatoDTO.setEdicao(objectMapper.convertValue(candidatoEntity.getEdicao(), EdicaoDTO.class));
//
//                    List<LinguagemDTO> linguagemDTOArrayList = new ArrayList<>();
//                    for (LinguagemEntity linguagem : candidatoEntity.getLinguagens()) {
//                        linguagemDTOArrayList.add(objectMapper.convertValue(linguagem, LinguagemDTO.class));
//                    }
//                    candidatoDTO.setLinguagens(linguagemDTOArrayList);
//                    return candidatoDTO;
//                })
//                .toList();
//
//        return candidatoDTOListByTrilha;
//    }

//    public List<CandidatoDTO> listCandidatosByEdicao(String edicao) throws RegraDeNegocioException {
//
//        EdicaoEntity edicaoRetorno = edicaoService.findByNome(edicao);
//
//        List<CandidatoDTO> candidatoDTOListByEdicao = candidatoRepository.findCandidatoEntitiesByEdicao(edicaoRetorno).stream()
//                .map(candidatoEntity -> {
//                    CandidatoDTO candidatoDTO = objectMapper.convertValue(candidatoEntity, CandidatoDTO.class);
//                    candidatoDTO.setFormulario(objectMapper.convertValue(candidatoEntity.getFormularioEntity(), FormularioDTO.class));
//
//                    Set<TrilhaDTO> trilhaDTOList = new HashSet<>();
//                    for (TrilhaEntity trilhaTemp : candidatoEntity.getFormularioEntity().getTrilhaEntitySet()) {
//                        trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
//                    }
//                    candidatoDTO.getFormulario().setTrilhas(trilhaDTOList);
//
//                    candidatoDTO.setEdicao(objectMapper.convertValue(candidatoEntity.getEdicao(), EdicaoDTO.class));
//
//                    List<LinguagemDTO> linguagemDTOArrayList = new ArrayList<>();
//                    for (LinguagemEntity linguagem : candidatoEntity.getLinguagens()) {
//                        linguagemDTOArrayList.add(objectMapper.convertValue(linguagem, LinguagemDTO.class));
//                    }
//                    candidatoDTO.setLinguagens(linguagemDTOArrayList);
//                    return candidatoDTO;
//                })
//                .toList();
//
//        return candidatoDTOListByEdicao;
//    }
}