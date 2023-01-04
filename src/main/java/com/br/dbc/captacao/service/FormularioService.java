package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.formulario.FormularioCreateDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.CurriculoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.PrintConfigPCEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.FormularioRepository;
import com.br.dbc.captacao.repository.PrintConfigPCRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormularioService {
    private static final int DESCENDING = 1;
    private final FormularioRepository formularioRepository;
    private final TrilhaService trilhaService;
    private final PrintConfigPCRepository printConfigPCRepository;
    private final ObjectMapper objectMapper;

    public FormularioDTO create(FormularioCreateDTO formularioCreateDto) throws RegraDeNegocioException {
        if (!formularioCreateDto.isMatriculadoBoolean()) {
            throw new RegraDeNegocioException("Precisa estar matriculado!");
        }
        FormularioEntity formulario = convertToEntity(formularioCreateDto);

        List<TrilhaEntity> trilhas = new ArrayList<>();
        trilhas = getTrilhasFormulario(formularioCreateDto, trilhas);
        formulario.setTrilhaEntitySet(new HashSet<>(trilhas));

        PrintConfigPCEntity printConfigPCEntity = new PrintConfigPCEntity();
        printConfigPCEntity.setTipo(" ");
        printConfigPCEntity.setNome(" ");
        printConfigPCEntity.setData("dados".getBytes());
        PrintConfigPCEntity print = printConfigPCRepository.save(printConfigPCEntity);
        CurriculoEntity curriculo = new CurriculoEntity();

        formulario.setImagemConfigPc(print);
        FormularioEntity formularioRetornoBanco = formularioRepository.save(formulario);
        FormularioDTO formularioRetorno = convertToDto(formularioRetornoBanco);
        Set<TrilhaDTO> trilhaDTOSet = formularioRetornoBanco.getTrilhaEntitySet().stream()
                .map(trilhaEntity -> objectMapper.convertValue(trilhaEntity, TrilhaDTO.class))
                .collect(Collectors.toSet());
        formularioRetorno.setTrilhas(trilhaDTOSet);

        return formularioRetorno;
    }

    private List<TrilhaEntity> getTrilhasFormulario(FormularioCreateDTO formularioCreateDTO, List<TrilhaEntity> trilhas) throws RegraDeNegocioException {
        for (String nomeTrilha : formularioCreateDTO.getTrilhas()) {
            TrilhaEntity trilhaEntity = trilhaService.findByNome(nomeTrilha);
            trilhas.add(trilhaEntity);
        }
        return trilhas;
    }

    public PageDTO<FormularioDTO> listAllPaginado(Integer pagina, Integer tamanho, String sort, int order) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by(sort).ascending();
        if (order == DESCENDING) {
            ordenacao = Sort.by(sort).descending();
        }
        if (tamanho < 0 || pagina < 0) {
            throw new RegraDeNegocioException("Page ou Size não pode ser menor que zero.");
        }
        if (tamanho > 0) {
            PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
            Page<FormularioEntity> paginaFormularioEntity = formularioRepository.listarFormulariosSemVazios(pageRequest);
            List<FormularioDTO> formularioDtos = paginaFormularioEntity.getContent().stream()
                    .map(formularioEntity -> {
                        FormularioDTO formularioDTO = convertToDto(formularioEntity);
                        Set<TrilhaDTO> trilhaDTOSet = formularioEntity.getTrilhaEntitySet().stream()
                                .map(trilhaEntity -> objectMapper.convertValue(trilhaEntity, TrilhaDTO.class))
                                .collect(Collectors.toSet());
                        formularioDTO.setTrilhas(trilhaDTOSet);
                        if (formularioEntity.getCurriculoEntity() != null) {
                            formularioDTO.setCurriculo(formularioEntity.getCurriculoEntity().getIdCurriculo());
                        }
                        if (formularioEntity.getImagemConfigPc() != null) {
                            formularioDTO.setImagemConfigPc(formularioEntity.getImagemConfigPc().getIdImagem());
                        }
                        return formularioDTO;
                    })
                    .toList();
            return new PageDTO<>(paginaFormularioEntity.getTotalElements(),
                    paginaFormularioEntity.getTotalPages(),
                    pagina,
                    tamanho,
                    formularioDtos);
        }
        List<FormularioDTO> listaVazia = new ArrayList<>();
        return new PageDTO<>(0L, 0, 0, tamanho, listaVazia);
    }

    public FormularioEntity findById(Integer idFormulario) throws RegraDeNegocio404Exception {
        return formularioRepository.findById(idFormulario)
                .orElseThrow(() -> new RegraDeNegocio404Exception("Erro ao buscar o formulário."));
    }

    public FormularioDTO findDtoById(Integer idFormulario) throws RegraDeNegocio404Exception {
        FormularioEntity formulario = findById(idFormulario);
        return convertToDto(formulario);
    }

    public void deleteById(Integer idFormulario) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        findById(idFormulario);
        formularioRepository.deleteById(idFormulario);
    }

    public FormularioDTO update(Integer idFormulario, FormularioCreateDTO formularioCreateDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        FormularioEntity formulario = findById(idFormulario);

        formulario.setMatriculado(formularioCreateDTO.isMatriculadoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formulario.setCurso(formularioCreateDTO.getCurso());
        formulario.setTurno(formularioCreateDTO.getTurno());
        formulario.setInstituicao(formularioCreateDTO.getInstituicao());
        formulario.setGithub(formularioCreateDTO.getGithub());
        formulario.setLinkedin(formularioCreateDTO.getLinkedin());
        formulario.setDesafios(formularioCreateDTO.isDesafiosBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formulario.setProblema(formularioCreateDTO.isProblemaBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formulario.setReconhecimento(formularioCreateDTO.isReconhecimentoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formulario.setAltruismo(formularioCreateDTO.isAltruismoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formulario.setResposta(formularioCreateDTO.getResposta());
        formulario.setLgpd(formularioCreateDTO.isLgpdBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formulario.setProva(formularioCreateDTO.isProvaBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formulario.setIngles(formularioCreateDTO.getIngles());
        formulario.setEspanhol(formularioCreateDTO.getEspanhol());
        formulario.setNeurodiversidade(formularioCreateDTO.getNeurodiversidade());
        formulario.setEfetivacao(formularioCreateDTO.isEfetivacaoBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formulario.setDisponibilidade(formularioCreateDTO.isDisponibilidadeBoolean() ? TipoMarcacao.T : TipoMarcacao.F);
        formulario.setGenero(formularioCreateDTO.getGenero());
        formulario.setOrientacao(formularioCreateDTO.getOrientacao());
        formulario.setImportancia(formularioCreateDTO.getImportancia());

        formulario = formularioRepository.save(formulario);

        return convertToDto(formulario);
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
        List<Integer> trilhasFormulario = new ArrayList<>();
        for(String nomeTrilha: formularioCreateDto.getTrilhas()){
            TrilhaEntity trilha = trilhaService.findByNome(nomeTrilha);
            trilhasFormulario.add(trilha.getIdTrilha());
        }
        Set<TrilhaEntity> trilhas = trilhaService.findListaTrilhas(trilhasFormulario);
        formularioEntity.setTrilhaEntitySet(trilhas);
        return formularioEntity;
    }

    public FormularioEntity convertToEntity(FormularioDTO formularioDto) {
        FormularioEntity formulario = objectMapper.convertValue(formularioDto, FormularioEntity.class);
        formulario.setTrilhaEntitySet(trilhaService.convertToEntity(formularioDto.getTrilhas()));
        return formulario;
    }

    public void updateCurriculo(FormularioEntity formularioEntity, CurriculoEntity curriculoEntity) {
        formularioEntity.setCurriculoEntity(curriculoEntity);
        formularioRepository.save(formularioEntity);
    }
}