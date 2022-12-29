package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.CargoDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoCreateDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoDTO;
import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.*;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.AvaliacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private static final int DESCENDING = 1;
    private final ObjectMapper objectMapper;
    private final AvaliacaoRepository avaliacaoRepository;
    private final InscricaoService inscricaoService;

    private final TrilhaService trilhaService;

    private final EdicaoService edicaoService;

    private final GestorService gestorService;
    private final EmailService emailService;


    public AvaliacaoDTO create(AvaliacaoCreateDTO avaliacaoCreateDTO) throws RegraDeNegocioException {
//        if (!avaliacaoRepository.findAvaliacaoEntitiesByInscricao_IdInscricao(avaliacaoCreateDTO.getIdInscricao()).isEmpty()) {
//            throw new RegraDeNegocioException("Formulario cadastrado para outro candidato");
//        }

        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity();
        InscricaoEntity inscricao = inscricaoService.findById(avaliacaoCreateDTO.getIdInscricao());
        avaliacaoEntity.setAprovado(avaliacaoCreateDTO.isAprovadoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        avaliacaoEntity.setInscricao(inscricao);
        GestorEntity gestor = gestorService.findByEmail(avaliacaoCreateDTO.getEmailGestor());
        avaliacaoEntity.setAvaliador(gestor);
        AvaliacaoEntity avaliacaoRetorno = avaliacaoRepository.save(avaliacaoEntity);
        AvaliacaoDTO avaliacaoDto = convertToDTO(avaliacaoRetorno);
        GestorDTO gestorDTO = objectMapper.convertValue(gestor, GestorDTO.class);
        avaliacaoDto.setAvaliador(gestorDTO);
        List<CargoDTO> cargoDTOList = new ArrayList<>();
        for (CargoEntity cargo : gestor.getCargoEntity()) {
            CargoDTO cargoDTO = objectMapper.convertValue(cargo, CargoDTO.class);
            cargoDTO.setId(cargo.getIdCargo());
            cargoDTOList.add(cargoDTO);
        }
        gestorDTO.setCargosDto(cargoDTOList);

        FormularioDTO formularioDTO = objectMapper.convertValue(inscricao.getCandidato().getFormularioEntity(), FormularioDTO.class);

        List<TrilhaDTO> trilhaDTOList = new ArrayList<>();
        for (TrilhaEntity trilhaTemp : avaliacaoEntity.getInscricao().getCandidato().getFormularioEntity().getTrilhaEntitySet()) {
            trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
        }
        formularioDTO.setTrilhas(new HashSet<>(trilhaDTOList));

        avaliacaoDto.getInscricao().getCandidato().setFormulario(formularioDTO);

//        SendEmailDTO sendEmailDTO = new SendEmailDTO();
//        sendEmailDTO.setNome(avaliacaoDto.getInscricao().getCandidato().getNome());
//        sendEmailDTO.setEmail(avaliacaoDto.getInscricao().getCandidato().getEmail());
//        if (avaliacaoDto.getAprovado() == TipoMarcacao.T) {
//            emailService.sendEmail(sendEmailDTO, TipoEmail.APROVADO);
//        } else {
//            emailService.sendEmail(sendEmailDTO, TipoEmail.REPROVADO);
//        }
        inscricaoService.setAvaliado(avaliacaoCreateDTO.getIdInscricao());
        return avaliacaoDto;
    }


    public PageDTO<AvaliacaoDTO> list(Integer pagina, Integer tamanho, String sort, int order) {
        Sort ordenacao = Sort.by(sort).ascending();
        if (order == DESCENDING) {
            ordenacao = Sort.by(sort).descending();
        }
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<AvaliacaoEntity> paginaAvaliacaoEntities = avaliacaoRepository.findAll(pageRequest);

        List<AvaliacaoDTO> avaliacaoDtos = paginaAvaliacaoEntities.getContent().stream()
                .map(avaliacaoEntity -> convertToDTO(avaliacaoEntity)).toList();

        return new PageDTO<>(paginaAvaliacaoEntities.getTotalElements(),
                paginaAvaliacaoEntities.getTotalPages(),
                pagina,
                tamanho,
                avaliacaoDtos);
    }


    public AvaliacaoDTO update(Integer idAvaliacao, AvaliacaoCreateDTO avaliacaoCreateDto) throws RegraDeNegocioException {
        AvaliacaoEntity avaliacaoEntity = findById(idAvaliacao);
        avaliacaoEntity.setAprovado(avaliacaoCreateDto.isAprovadoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        AvaliacaoDTO avaliacaoRetorno = convertToDTO(avaliacaoRepository.save(avaliacaoEntity));
        return avaliacaoRetorno;
    }

    public AvaliacaoDTO findDtoById(Integer idAvaliacao) throws RegraDeNegocioException {
        return convertToDTO(findById(idAvaliacao));
    }

    public void deleteById(Integer idAvaliacao) throws RegraDeNegocioException {
        findById(idAvaliacao);
        avaliacaoRepository.deleteById(idAvaliacao);
    }

    private AvaliacaoEntity findById(Integer idAvaliacao) throws RegraDeNegocioException {
        return avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new RegraDeNegocioException("Avaliação não encontrada!"));
    }

    public List<AvaliacaoDTO> findAvaliacaoByCanditadoEmail(String email) {
        List<AvaliacaoEntity> lista = avaliacaoRepository.findAvaliacaoEntitiesByInscricao_Candidato_Email(email);
        return lista.stream().map(avaliacaoEntity -> convertToDTO(avaliacaoEntity))
                .toList();
    }


    public AvaliacaoDTO convertToDTO(AvaliacaoEntity avaliacaoEntity) {
        AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setIdAvaliacao(avaliacaoEntity.getIdAvaliacao());
        avaliacaoDTO.setAprovado(avaliacaoEntity.getAprovado());
        avaliacaoDTO.setAvaliador(objectMapper.convertValue(avaliacaoEntity.getAvaliador(), GestorDTO.class));
        avaliacaoDTO.setInscricao(inscricaoService.converterParaDTO(avaliacaoEntity.getInscricao()));
        return avaliacaoDTO;
    }

    public AvaliacaoEntity convertToEntity(AvaliacaoCreateDTO avaliacaoCreateDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        AvaliacaoEntity avaliacaoEntity = objectMapper.convertValue(avaliacaoCreateDTO, AvaliacaoEntity.class);
        InscricaoEntity inscricaoEntity = inscricaoService.convertToEntity(inscricaoService.findDtoByid(avaliacaoCreateDTO.getIdInscricao()));
        avaliacaoEntity.setInscricao(inscricaoEntity);
        avaliacaoEntity.setAprovado(avaliacaoCreateDTO.isAprovadoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        avaliacaoEntity.setAvaliador(gestorService.convertToEntity(gestorService.findDtoById(1)));
        return avaliacaoEntity;
    }

    public List<AvaliacaoDTO> listByTrilha(String trilha) throws RegraDeNegocioException {

        TrilhaEntity trilhaEntity = trilhaService.findByNome(trilha);

        List<AvaliacaoDTO> avaliacaoDTOListByTrilha = avaliacaoRepository.findAvaliacaoEntitiesByInscricao_Candidato_FormularioEntity_TrilhaEntitySet(trilhaEntity).stream()
                .map(avaliacaoEntity -> {

                    AvaliacaoDTO avaliacaoDTO = objectMapper.convertValue(avaliacaoEntity, AvaliacaoDTO.class);
                    InscricaoDTO inscricaoDTO = objectMapper.convertValue(avaliacaoEntity.getInscricao(), InscricaoDTO.class);
                    CandidatoDTO candidatoDTO = objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato(), CandidatoDTO.class);

                    List<CargoDTO> cargoDTOList = new ArrayList<>();
                    for (CargoEntity cargo : avaliacaoEntity.getAvaliador().getCargoEntity()) {
                        CargoDTO cargoDTO = objectMapper.convertValue(cargo, CargoDTO.class);
                        cargoDTO.setId(cargo.getIdCargo());
                        cargoDTOList.add(cargoDTO);
                    }
                    avaliacaoDTO.getAvaliador().setCargosDto(cargoDTOList);

                    List<LinguagemDTO> linguagemDTOList = new ArrayList<>();
                    for (LinguagemEntity linguagem : avaliacaoEntity.getInscricao().getCandidato().getLinguagens()) {
                        linguagemDTOList.add(objectMapper.convertValue(linguagem, LinguagemDTO.class));
                    }
                    candidatoDTO.setLinguagens(linguagemDTOList);

                    candidatoDTO.setFormulario(objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato().getFormularioEntity(), FormularioDTO.class));

                    List<TrilhaDTO> trilhaDTOList = new ArrayList<>();
                    for (TrilhaEntity trilhaTemp : avaliacaoEntity.getInscricao().getCandidato().getFormularioEntity().getTrilhaEntitySet()) {
                        trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
                    }
                    candidatoDTO.getFormulario().setTrilhas(new HashSet<>(trilhaDTOList));

                    candidatoDTO.setEdicao(objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato().getEdicao(), EdicaoDTO.class));
                    inscricaoDTO.setCandidato(candidatoDTO);

                    avaliacaoDTO.setInscricao(inscricaoDTO);

                    return avaliacaoDTO;
                })
                .toList();

        return avaliacaoDTOListByTrilha;
    }


    public List<AvaliacaoDTO> listByEdicao(String edicao) throws RegraDeNegocioException {

        EdicaoEntity edicaoEntity = edicaoService.findByNome(edicao);

        List<AvaliacaoDTO> avaliacaoDTOListByEdicao = avaliacaoRepository.findAvaliacaoEntitiesByInscricao_Candidato_Edicao(edicaoEntity).stream()
                .map(avaliacaoEntity -> {

                    AvaliacaoDTO avaliacaoDTO = objectMapper.convertValue(avaliacaoEntity, AvaliacaoDTO.class);
                    InscricaoDTO inscricaoDTO = objectMapper.convertValue(avaliacaoEntity.getInscricao(), InscricaoDTO.class);
                    CandidatoDTO candidatoDTO = objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato(), CandidatoDTO.class);

                    List<CargoDTO> cargoDTOList = new ArrayList<>();
                    for (CargoEntity cargo : avaliacaoEntity.getAvaliador().getCargoEntity()) {
                        CargoDTO cargoDTO = objectMapper.convertValue(cargo, CargoDTO.class);
                        cargoDTO.setId(cargo.getIdCargo());
                        cargoDTOList.add(cargoDTO);
                    }
                    avaliacaoDTO.getAvaliador().setCargosDto(cargoDTOList);

                    List<LinguagemDTO> linguagemDTOList = new ArrayList<>();
                    for (LinguagemEntity linguagem : avaliacaoEntity.getInscricao().getCandidato().getLinguagens()) {
                        linguagemDTOList.add(objectMapper.convertValue(linguagem, LinguagemDTO.class));
                    }
                    candidatoDTO.setLinguagens(linguagemDTOList);

                    candidatoDTO.setFormulario(objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato().getFormularioEntity(), FormularioDTO.class));

                    List<TrilhaDTO> trilhaDTOList = new ArrayList<>();
                    for (TrilhaEntity trilhaTemp : avaliacaoEntity.getInscricao().getCandidato().getFormularioEntity().getTrilhaEntitySet()) {
                        trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
                    }
                    candidatoDTO.getFormulario().setTrilhas(new HashSet<>(trilhaDTOList));

                    candidatoDTO.setEdicao(objectMapper.convertValue(avaliacaoEntity.getInscricao().getCandidato().getEdicao(), EdicaoDTO.class));
                    inscricaoDTO.setCandidato(candidatoDTO);

                    avaliacaoDTO.setInscricao(inscricaoDTO);

                    return avaliacaoDTO;
                })
                .toList();

        return avaliacaoDTOListByEdicao;
    }


}