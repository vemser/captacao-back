package com.br.dbc.captacao.service;


import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.edicao.EdicaoDTO;
import com.br.dbc.captacao.dto.formulario.FormularioDTO;
import com.br.dbc.captacao.dto.inscricao.InscricaoDTO;
import com.br.dbc.captacao.dto.linguagem.LinguagemDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.dto.trilha.TrilhaDTO;
import com.br.dbc.captacao.entity.InscricaoEntity;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.InscricaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscricaoService {

    private static final int DESCENDING = 1;
    private final InscricaoRepository inscricaoRepository;
    private final CandidatoService candidatoService;
    private final ObjectMapper objectMapper;

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
//        SendEmailDTO sendEmailDTO = new SendEmailDTO();
//        sendEmailDTO.setNome(inscricaoEntity.getCandidato().getNome());
//        sendEmailDTO.setEmail(inscricaoDto.getCandidato().getEmail());
//        emailService.sendEmail(sendEmailDTO, TipoEmail.INSCRICAO);

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

            List<InscricaoDTO> inscricoesDTO = getInscricoesDTO(paginaInscricaoEntities);

            return new PageDTO<>(paginaInscricaoEntities.getTotalElements(),
                    paginaInscricaoEntities.getTotalPages(),
                    pagina,
                    tamanho,
                    inscricoesDTO);
        }
        List<InscricaoDTO> listaVazia = new ArrayList<>();
        return new PageDTO<>(0L, 0, 0, tamanho, listaVazia);
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

    public PageDTO<InscricaoDTO> filtrarInscricoes(Integer pagina, Integer tamanho, String email, String edicao, String trilha) throws RegraDeNegocioException {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);

        Page<InscricaoEntity> inscricaoEntityPage = inscricaoRepository.filtrarInscricoes(pageRequest, email, edicao, trilha);

        List<InscricaoDTO> inscricaoDTOS = getInscricoesDTO(inscricaoEntityPage);

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

    private List<InscricaoDTO> getInscricoesDTO(Page<InscricaoEntity> inscricaoEntityPage) {
        List<InscricaoDTO> inscricaoDTOS = inscricaoEntityPage.stream()
                .map(inscricao -> {
                    InscricaoDTO inscricaoDTO = objectMapper.convertValue(inscricao, InscricaoDTO.class);
                    CandidatoDTO candidatoDTO = objectMapper.convertValue(inscricao.getCandidato(), CandidatoDTO.class);
                    FormularioDTO formularioDTO = objectMapper.convertValue(inscricao.getCandidato().getFormularioEntity(), FormularioDTO.class);
                    List<LinguagemDTO> linguagensDTO = inscricao.getCandidato().getLinguagens().stream()
                            .map(linguagem -> objectMapper.convertValue(linguagem, LinguagemDTO.class)).toList();
                    EdicaoDTO edicaoDTO = objectMapper.convertValue(inscricao.getCandidato().getEdicao(), EdicaoDTO.class);
                    Set<TrilhaDTO> trilhasDTO = inscricao.getCandidato().getFormularioEntity().getTrilhaEntitySet().stream()
                            .map(trilhaEntity -> objectMapper.convertValue(trilhaEntity, TrilhaDTO.class)).collect(Collectors.toSet());

                    formularioDTO.setTrilhas(trilhasDTO);

                    candidatoDTO.setFormulario(formularioDTO);
                    candidatoDTO.setLinguagens(linguagensDTO);
                    candidatoDTO.setEdicao(edicaoDTO);

                    inscricaoDTO.setCandidato(candidatoDTO);
                    inscricaoDTO.setAvaliado(inscricao.getAvaliado());

                    return inscricaoDTO;
                }).toList();
        return inscricaoDTOS;
    }

    public InscricaoEntity findById(Integer idInscricao) throws RegraDeNegocioException {
        return inscricaoRepository.findById(idInscricao)
                .orElseThrow(() -> new RegraDeNegocioException("ID_Inscrição inválido"));

    }

    public InscricaoDTO findDtoByid(Integer idInscricao) throws RegraDeNegocioException {
        InscricaoDTO inscricaoDto = converterParaDTO(findById(idInscricao));
        return inscricaoDto;
    }

    public InscricaoDTO converterParaDTO(InscricaoEntity inscricaoEntity) {
        Page<InscricaoEntity> inscricaoPage = new PageImpl<>(List.of(inscricaoEntity));
        List<InscricaoDTO> inscricoesDTO = getInscricoesDTO(inscricaoPage);
        return inscricoesDTO.get(0);
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
