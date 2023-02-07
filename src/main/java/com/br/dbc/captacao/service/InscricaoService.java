package com.br.dbc.captacao.service;


import com.br.dbc.captacao.dto.SendEmailDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.InscricaoEntity;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.InscricaoRepository;
import com.br.dbc.captacao.repository.enums.TipoEmail;
import com.br.dbc.captacao.repository.enums.TipoMarcacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.dynalink.beans.StaticClass;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InscricaoService {

    private static final int DESCENDING = 1;
    private final InscricaoRepository inscricaoRepository;
    private final CandidatoService candidatoService;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    public InscricaoDTO create(Integer idCandidato) throws RegraDeNegocioException {
        if (!inscricaoRepository.findInscricaoEntitiesByCandidato_IdCandidato(idCandidato).isEmpty()) {
            throw new RegraDeNegocioException("Inscrição já realizada");
        }

        InscricaoEntity inscricaoEntity = new InscricaoEntity();
        inscricaoEntity.setCandidato(candidatoService.findById(idCandidato));
        inscricaoEntity.setDataInscricao(LocalDate.now());
        inscricaoEntity.setAvaliado(TipoMarcacao.F);
        InscricaoEntity inscricaoRetorno = inscricaoRepository.save(inscricaoEntity);
        InscricaoDTO inscricaoDto = converterParaDTO(inscricaoRetorno);
        SendEmailDTO sendEmailDTO = new SendEmailDTO();
        sendEmailDTO.setNome(inscricaoEntity.getCandidato().getNome());
        sendEmailDTO.setEmail(inscricaoDto.getCandidato().getEmail());
        sendEmailDTO.setData(inscricaoDto.getDataInscricao().toString());

        emailService.sendEmail(sendEmailDTO, TipoEmail.INSCRICAO);
        return inscricaoDto;
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

            List<InscricaoDTO> inscricoesDTO = paginaInscricaoEntities.stream()
                    .map(this::converterParaDTO).toList();

            return new PageDTO<>(paginaInscricaoEntities.getTotalElements(),
                    paginaInscricaoEntities.getTotalPages(),
                    pagina,
                    tamanho,
                    inscricoesDTO);
        }
        List<InscricaoDTO> listaVazia = new ArrayList<>();
        return new PageDTO<>(0L, 0, 0, tamanho, listaVazia);
    }

    public PageDTO<InscricaoDTO> filtrarInscricoes(Integer pagina, Integer tamanho, String email, String edicao, String trilha) throws RegraDeNegocioException {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);

        Page<InscricaoEntity> inscricaoEntityPage = inscricaoRepository.filtrarInscricoes(pageRequest, email, edicao, trilha);

        List<InscricaoDTO> inscricaoDTOS = inscricaoEntityPage.stream()
                .map(this::converterParaDTO).toList();

        return new PageDTO<>(inscricaoEntityPage.getTotalElements(),
                inscricaoEntityPage.getTotalPages(),
                pagina,
                tamanho,
                inscricaoDTOS);
    }

    public void delete(Integer id) throws RegraDeNegocioException {
        findById(id);
        inscricaoRepository.deleteById(id);
    }

    public InscricaoEntity findById(Integer idInscricao) throws RegraDeNegocioException {
        return inscricaoRepository.findById(idInscricao)
                .orElseThrow(() -> new RegraDeNegocioException("ID_Inscrição inválido"));

    }

    public InscricaoDTO findDtoById(Integer idInscricao) throws RegraDeNegocioException {
        InscricaoDTO inscricaoDto = converterParaDTO(findById(idInscricao));
        return inscricaoDto;
    }

    public InscricaoDTO converterParaDTO(InscricaoEntity inscricao) {
        InscricaoDTO inscricaoDTO = objectMapper.convertValue(inscricao, InscricaoDTO.class);
        inscricaoDTO.setCandidato(candidatoService.converterEmDTO(inscricao.getCandidato()));
        inscricaoDTO.setAvaliado(inscricao.getAvaliado());

        return inscricaoDTO;
    }

    public InscricaoEntity convertToEntity(InscricaoDTO inscricaoDTO) throws RegraDeNegocioException, RegraDeNegocio404Exception {
        InscricaoEntity inscricaoEntity = objectMapper.convertValue(inscricaoDTO, InscricaoEntity.class);
        inscricaoEntity.setCandidato(candidatoService.convertToEntity(inscricaoDTO.getCandidato()));
        return inscricaoEntity;
    }

    public InscricaoEntity setAvaliado(Integer idInscricao) throws RegraDeNegocioException {
        InscricaoEntity inscricaoEntity = findById(idInscricao);
        inscricaoEntity.setAvaliado(TipoMarcacao.T);
        InscricaoEntity inscricao = inscricaoRepository.save(inscricaoEntity);
        return inscricao;
    }
}
