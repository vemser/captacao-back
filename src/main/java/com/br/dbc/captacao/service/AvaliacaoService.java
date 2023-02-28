package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.CargoDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoCreateDTO;
import com.br.dbc.captacao.dto.avaliacao.AvaliacaoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.*;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.AvaliacaoRepository;
import com.br.dbc.captacao.enums.TipoMarcacao;
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
    private final GestorService gestorService;

    public AvaliacaoDTO create(AvaliacaoCreateDTO avaliacaoCreateDTO, String token) throws RegraDeNegocioException {
        if (avaliacaoRepository.findAvaliacaoEntitiesByInscricao_IdInscricao(avaliacaoCreateDTO.getIdInscricao()) != null) {
            throw new RegraDeNegocioException("Candidato já avaliado!");
        }

        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity();
        InscricaoEntity inscricao = inscricaoService.findById(avaliacaoCreateDTO.getIdInscricao());
        avaliacaoEntity.setAprovado(avaliacaoCreateDTO.isAprovadoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        avaliacaoEntity.setInscricao(inscricao);

        GestorEntity gestor = gestorService.getUser(token);
        avaliacaoEntity.setAvaliador(gestor);
        AvaliacaoEntity avaliacaoRetorno = avaliacaoRepository.save(avaliacaoEntity);
        AvaliacaoDTO avaliacaoDto = convertToDTO(avaliacaoRetorno);
        GestorDTO gestorDTO = objectMapper.convertValue(gestor, GestorDTO.class);
        avaliacaoDto.setAvaliador(gestorDTO);

        List<CargoDTO> cargoDTOList = new ArrayList<>();
        for (CargoEntity cargo : gestor.getCargoEntity()) {
            CargoDTO cargoDTO = objectMapper.convertValue(cargo, CargoDTO.class);
            cargoDTO.setIdCargo(cargo.getIdCargo());
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

        inscricaoService.setAvaliado(avaliacaoCreateDTO.getIdInscricao());
        return avaliacaoDto;
    }

    public PageDTO<AvaliacaoDTO> list(Integer pagina, Integer tamanho, String sort, int order) {
        Sort ordenacao = Sort.by(sort).ascending();
        if (order == DESCENDING) {
            ordenacao = Sort.by(sort).descending();
        }
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<AvaliacaoEntity> paginaAvaliacaoEntities = avaliacaoRepository.findByAprovado(pageRequest, TipoMarcacao.T);

        List<AvaliacaoDTO> avaliacaoDtos = paginaAvaliacaoEntities.getContent().stream()
                .map(this::convertToDTO).toList();

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

    public void deleteById(Integer idAvaliacao) throws RegraDeNegocioException {
        findById(idAvaliacao);
        avaliacaoRepository.deleteById(idAvaliacao);
    }

    public PageDTO<AvaliacaoDTO> filtrarAvaliacoes(Integer pagina,
                                                   Integer tamanho,
                                                   String email,
                                                   String edicao,
                                                   String trilha) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);

        Page<AvaliacaoEntity> avaliacaoEntityPage = avaliacaoRepository.filtrarAvaliacoes(pageRequest,
                email,
                edicao,
                trilha);

        List<AvaliacaoDTO> avaliacaoDTOS = avaliacaoEntityPage.stream()
                .map(this::convertToDTO).toList();

        return new PageDTO<>(avaliacaoEntityPage.getTotalElements(),
                avaliacaoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                avaliacaoDTOS);
    }

    private AvaliacaoEntity findById(Integer idAvaliacao) throws RegraDeNegocioException {
        return avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new RegraDeNegocioException("Avaliação não encontrada!"));
    }

    public AvaliacaoDTO findDtoById(Integer idAvaliacao) throws RegraDeNegocioException {
        return convertToDTO(findById(idAvaliacao));
    }

    public AvaliacaoDTO convertToDTO(AvaliacaoEntity avaliacaoEntity) {
        AvaliacaoDTO avaliacaoDTO = objectMapper.convertValue(avaliacaoEntity, AvaliacaoDTO.class);
        avaliacaoDTO.setAprovado(avaliacaoEntity.getAprovado());
        avaliacaoDTO.setAvaliador(gestorService.getGestorDTO(avaliacaoEntity.getAvaliador()));
        avaliacaoDTO.setInscricao(inscricaoService.converterParaDTO(avaliacaoEntity.getInscricao()));
        return avaliacaoDTO;
    }

    public AvaliacaoEntity convertToEntity(AvaliacaoCreateDTO avaliacaoCreateDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        AvaliacaoEntity avaliacaoEntity = objectMapper.convertValue(avaliacaoCreateDTO, AvaliacaoEntity.class);
        InscricaoEntity inscricaoEntity = inscricaoService.convertToEntity(inscricaoService.findDtoById(avaliacaoCreateDTO.getIdInscricao()));
        avaliacaoEntity.setInscricao(inscricaoEntity);
        avaliacaoEntity.setAprovado(avaliacaoCreateDTO.isAprovadoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        avaliacaoEntity.setAvaliador(gestorService.convertToEntity(gestorService.findDtoById(1)));
        return avaliacaoEntity;
    }
}