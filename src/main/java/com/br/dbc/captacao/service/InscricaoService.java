package com.br.dbc.captacao.service;


import com.br.dbc.captacao.dto.SendEmailDTO;
import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoCreateDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.*;
import com.br.dbc.captacao.enums.TipoEmail;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.InscricaoRepository;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InscricaoService {

    private static final int DESCENDING = 1;
    private final InscricaoRepository inscricaoRepository;
    private final CandidatoService candidatoService;

    private final EmailService emailService;

    private final EdicaoService edicaoService;
    private final TrilhaService trilhaService;
    private final ObjectMapper objectMapper;

    public InscricaoDTO create(Integer idCandidato) throws RegraDeNegocioException {
//        if (!inscricaoRepository.findInscricaoEntitiesByCandidato_IdCandidato(inscricaoCreateDTO.getIdCandidato()).isEmpty()) {
//            throw new RegraDeNegocioException("Inscrição já realizada");
//        }

        InscricaoEntity inscricaoEntity = new InscricaoEntity();
        inscricaoEntity.setCandidato(candidatoService.findById(idCandidato));
        inscricaoEntity.setDataInscricao(LocalDate.now());
        inscricaoEntity.setAvaliado(TipoMarcacao.F);
        InscricaoEntity inscricaoRetorno = inscricaoRepository.save(inscricaoEntity);
        InscricaoDTO inscricaoDto = converterParaDTO(inscricaoRetorno);
//        SendEmailDTO sendEmailDTO = new SendEmailDTO();
//        sendEmailDTO.setNome(inscricaoEntity.getCandidato().getNome());
//        sendEmailDTO.setEmail(inscricaoDto.getCandidato().getEmail());
//        emailService.sendEmail(sendEmailDTO, TipoEmail.INSCRICAO);

        return inscricaoDto;
    }

    public InscricaoEntity setAvaliado(Integer idInscricao) throws RegraDeNegocioException {
        InscricaoEntity inscricaoEntity = findById(idInscricao);
        inscricaoEntity.setAvaliado(TipoMarcacao.T);
        InscricaoEntity inscricao = inscricaoRepository.save(inscricaoEntity);
        return inscricao;
    }

    public InscricaoDTO findInscricaoPorEmail(String email) throws RegraDeNegocioException {
        InscricaoEntity inscricaoEntity = inscricaoRepository.findInscricaoEntitiesByCandidato_Email(email);
        if(inscricaoEntity == null){
            throw new RegraDeNegocioException("Candidato com o e-mail especificado não existe");
        }
        return objectMapper.convertValue(inscricaoEntity,InscricaoDTO.class);
    }

    public PageDTO<InscricaoDTO> listar(Integer pagina, Integer tamanho, String sort, int order) throws RegraDeNegocioException {
        Sort ordenacao = Sort.by(sort).ascending();
        if (order == DESCENDING) {
            ordenacao = Sort.by(sort).descending();
        }
        if(tamanho < 0 || pagina < 0) {
            throw new RegraDeNegocioException("Page ou Size não pode ser menor que zero.");
        }
        if(tamanho > 0) {
            PageRequest pageRequest = PageRequest.of(pagina, tamanho, ordenacao);
            Page<InscricaoEntity> paginaInscricaoEntities = inscricaoRepository.findAll(pageRequest);

            List<InscricaoDTO> inscricaoDtos = paginaInscricaoEntities.getContent().stream()
                    .map(inscricaoEntity -> converterParaDTO(inscricaoEntity)).toList();

            return new PageDTO<>(paginaInscricaoEntities.getTotalElements(),
                    paginaInscricaoEntities.getTotalPages(),
                    pagina,
                    tamanho,
                    inscricaoDtos);
        }
        List<InscricaoDTO> listaVazia = new ArrayList<>();
        return new PageDTO<>(0L, 0, 0, tamanho, listaVazia);
    }

    public InscricaoDTO findDtoByid(Integer idInscricao) throws RegraDeNegocioException {
        InscricaoDTO inscricaoDto = converterParaDTO(findById(idInscricao));
        return inscricaoDto;
    }

    public void exportarCandidatoCSV() throws RegraDeNegocioException {
        List<InscricaoEntity> inscricaoEntityList = inscricaoRepository.listarInscricoesAprovadas();
        try {
            BufferedWriter bw = new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream("candidatos.csv", false), "UTF-8"));
            for (InscricaoEntity inscricao : inscricaoEntityList) {
                StringBuilder oneLine = new StringBuilder();
                oneLine.append(inscricao.getCandidato().getIdCandidato());
                oneLine.append(",");
                oneLine.append(inscricao.getCandidato().getNome());
                oneLine.append(",");
                oneLine.append(inscricao.getCandidato().getEmail());
                oneLine.append(",");
                oneLine.append(inscricao.getCandidato().getMedia());
                oneLine.append(",");
                oneLine.append(inscricao.getCandidato().getParecerTecnico());
                oneLine.append(",");
                oneLine.append(inscricao.getCandidato().getParecerComportamental());
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new RegraDeNegocioException("Erro ao exportar dados para arquivo.");
        }
    }

    public void delete(Integer id) throws RegraDeNegocioException {
        findById(id);
        inscricaoRepository.deleteById(id);
    }


    public InscricaoEntity findById(Integer idInscricao) throws RegraDeNegocioException {
        return inscricaoRepository.findById(idInscricao)
                .orElseThrow(() -> new RegraDeNegocioException("ID_Inscrição inválido"));

    }

    public InscricaoDTO converterParaDTO(InscricaoEntity inscricaoEntity) {
        InscricaoDTO inscricaoDto = objectMapper.convertValue(inscricaoEntity, InscricaoDTO.class);
        inscricaoDto.setCandidato(candidatoService.converterEmDTO(inscricaoEntity.getCandidato()));
        inscricaoDto.getCandidato().setFormulario(objectMapper.convertValue(inscricaoEntity.getCandidato().getFormularioEntity(), FormularioDTO.class));
        return inscricaoDto;
    }


    public InscricaoEntity convertToEntity(InscricaoDTO inscricaoDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        InscricaoEntity inscricaoEntity = objectMapper.convertValue(inscricaoDTO, InscricaoEntity.class);
        inscricaoEntity.setCandidato(candidatoService.convertToEntity(inscricaoDTO.getCandidato()));
        return inscricaoEntity;
    }

    public List<InscricaoDTO> listInscricoesByTrilha(String trilha) throws RegraDeNegocioException {

        TrilhaEntity trilhaEntity = trilhaService.findByNome(trilha);

        List<InscricaoDTO> inscricaoDTOListByTrilha = inscricaoRepository.findInscricaoEntitiesByCandidato_FormularioEntity_TrilhaEntitySet(trilhaEntity).stream()
                .map(inscricaoEntity -> {
                    InscricaoDTO inscricaoDTO = objectMapper.convertValue(inscricaoEntity, InscricaoDTO.class);

                    CandidatoDTO candidatoDTO = objectMapper.convertValue(inscricaoEntity.getCandidato(), CandidatoDTO.class);
                    candidatoDTO.setEdicao(objectMapper.convertValue(inscricaoEntity.getCandidato().getEdicao(), EdicaoDTO.class));

                    Set<TrilhaDTO> trilhaDTOList = new HashSet<>();
                    for (TrilhaEntity trilhaTemp : inscricaoEntity.getCandidato().getFormularioEntity().getTrilhaEntitySet()) {
                        trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
                    }
                    FormularioDTO formularioDTO = objectMapper.convertValue(inscricaoEntity.getCandidato().getFormularioEntity(), FormularioDTO.class);
                    formularioDTO.setTrilhas(trilhaDTOList);
                    candidatoDTO.setFormulario(formularioDTO);

                    List<LinguagemDTO> linguagemDTOArrayList = new ArrayList<>();
                    for (LinguagemEntity linguagem : inscricaoEntity.getCandidato().getLinguagens()) {
                        linguagemDTOArrayList.add(objectMapper.convertValue(linguagem, LinguagemDTO.class));
                    }
                    candidatoDTO.setLinguagens(linguagemDTOArrayList);

                    inscricaoDTO.setCandidato(candidatoDTO);
                    return inscricaoDTO;
                })
                .toList();

        return inscricaoDTOListByTrilha;
    }

    public List<InscricaoDTO> listInscricoesByEdicao(String edicao) throws RegraDeNegocioException {

        EdicaoEntity edicaoEntity = edicaoService.findByNome(edicao);

        List<InscricaoDTO> inscricaoDTOListByEdicao = inscricaoRepository.findInscricaoEntitiesByCandidato_Edicao(edicaoEntity).stream()
                .map(inscricaoEntity -> {
                    InscricaoDTO inscricaoDTO = objectMapper.convertValue(inscricaoEntity, InscricaoDTO.class);

                    CandidatoDTO candidatoDTO = objectMapper.convertValue(inscricaoEntity.getCandidato(), CandidatoDTO.class);
                    candidatoDTO.setEdicao(objectMapper.convertValue(inscricaoEntity.getCandidato().getEdicao(), EdicaoDTO.class));

                    Set<TrilhaDTO> trilhaDTOList = new HashSet<>();
                    for (TrilhaEntity trilhaTemp : inscricaoEntity.getCandidato().getFormularioEntity().getTrilhaEntitySet()) {
                        trilhaDTOList.add(objectMapper.convertValue(trilhaTemp, TrilhaDTO.class));
                    }
                    FormularioDTO formularioDTO = objectMapper.convertValue(inscricaoEntity.getCandidato().getFormularioEntity(), FormularioDTO.class);
                    formularioDTO.setTrilhas(trilhaDTOList);
                    candidatoDTO.setFormulario(formularioDTO);

                    List<LinguagemDTO> linguagemDTOArrayList = new ArrayList<>();
                    for (LinguagemEntity linguagem : inscricaoEntity.getCandidato().getLinguagens()) {
                        linguagemDTOArrayList.add(objectMapper.convertValue(linguagem, LinguagemDTO.class));
                    }
                    candidatoDTO.setLinguagens(linguagemDTOArrayList);

                    inscricaoDTO.setCandidato(candidatoDTO);
                    inscricaoDTO.setAvaliacao(inscricaoEntity.getAvaliado());
                    return inscricaoDTO;
                })
                .toList();


        return inscricaoDTOListByEdicao;
    }

}
