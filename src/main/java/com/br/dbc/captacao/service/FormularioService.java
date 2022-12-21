package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.formulario.FormularioCreateDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.FormularioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormularioService {
    private static final int DESCENDING = 1;
    private final FormularioRepository formularioRepository;
    private final TrilhaService trilhaService;
    private final ObjectMapper objectMapper;

    public FormularioDTO create(FormularioCreateDTO formularioCreateDto) throws RegraDeNegocioException {
        if (!formularioCreateDto.isMatriculadoBoolean()) {
            throw new RegraDeNegocioException("Precisa estar matriculado!");
        }
        FormularioEntity formulario = convertToEntity(formularioCreateDto);
        FormularioEntity formularioRetornoBanco = formularioRepository.save(formulario);
        return convertToDto(formularioRetornoBanco);
    }

    public PageDTO<FormularioDTO> listAllPaginado(Integer pagina, Integer tamanho, String sort, int order) {
        Sort ordenacao = Sort.by(sort).ascending();
        if (order == DESCENDING) {
            ordenacao = Sort.by(sort).descending();
        }
        PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
        Page<FormularioEntity> paginaFormularioEntity = formularioRepository.findAll(pageRequest);
        List<FormularioDTO> formularioDTOS = paginaFormularioEntity.getContent().stream()
                .map(this::convertToDto)
                .toList();
        return new PageDTO<>(paginaFormularioEntity.getTotalElements(),
                paginaFormularioEntity.getTotalPages(),
                pagina,
                tamanho,
                formularioDTOS);
    }

    public FormularioEntity findById(Integer idFormulario) throws RegraDeNegocioException {
        return formularioRepository.findById(idFormulario)
                .orElseThrow(() -> new RegraDeNegocioException("Erro ao buscar Formulario"));
    }

    public FormularioDTO findDtoById(Integer idFormulario) throws RegraDeNegocioException {
        FormularioEntity formulario = findById(idFormulario);
        return convertToDto(formulario);
    }

    public void deleteById(Integer idFormulario) throws RegraDeNegocioException {
        findById(idFormulario);
        formularioRepository.deleteById(idFormulario);
    }

    public FormularioDTO update(Integer idFormulario, FormularioCreateDTO formularioCreateDto) throws RegraDeNegocioException {
        findById(idFormulario);
        FormularioEntity formulario1 = convertToEntity(formularioCreateDto);
        formulario1.setIdFormulario(idFormulario);
        FormularioEntity formularioEntity = formularioRepository.save(formulario1);
        return convertToDto(formularioEntity);
    }

    public FormularioDTO convertToDto(FormularioEntity formulario) {
        FormularioDTO formularioDto = objectMapper.convertValue(formulario, FormularioDTO.class);
        formularioDto.setTrilhas(formulario.getTrilhaEntitySet().stream()
                .map(trilhaEntity -> objectMapper.convertValue(trilhaEntity, TrilhaDTO.class))
                .collect(Collectors.toSet()));
        return formularioDto;
    }

    private FormularioEntity convertToEntity(FormularioCreateDTO formularioCreateDto) throws RegraDeNegocioException {
        FormularioEntity formularioEntity = objectMapper.convertValue(formularioCreateDto, FormularioEntity.class);
        formularioEntity.setMatriculado(formularioCreateDto.isMatriculadoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formularioEntity.setDesafios(formularioCreateDto.isDesafiosBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formularioEntity.setProblema(formularioCreateDto.isProblemaBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formularioEntity.setReconhecimento(formularioCreateDto.isReconhecimentoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formularioEntity.setAltruismo(formularioCreateDto.isAltruismoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formularioEntity.setLgpd(formularioCreateDto.isLgpdBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formularioEntity.setProva(formularioCreateDto.isProvaBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formularioEntity.setEfetivacao(formularioCreateDto.isEfetivacaoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formularioEntity.setDisponibilidade(formularioCreateDto.isDisponibilidadeBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        Set<TrilhaEntity> trilhas = trilhaService.findListaTrilhas(formularioCreateDto.getTrilhas());
        formularioEntity.setTrilhaEntitySet(trilhas);
        return formularioEntity;
    }

    public FormularioEntity convertToEntity(FormularioDTO formularioDto) {
        FormularioEntity formulario = objectMapper.convertValue(formularioDto, FormularioEntity.class);
        formulario.setTrilhaEntitySet(trilhaService.convertToEntity(formularioDto.getTrilhas()));
        return formulario;
    }
}