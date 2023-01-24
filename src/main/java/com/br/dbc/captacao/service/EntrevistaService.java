package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.SendEmailDTO;
import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaAtualizacaoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaCreateDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.EntrevistaEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.EntrevistaRepository;
import com.br.dbc.captacao.repository.enums.Legenda;
import com.br.dbc.captacao.repository.enums.TipoEmail;
import com.br.dbc.captacao.repository.enums.TipoMarcacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntrevistaService {
    private final EntrevistaRepository entrevistaRepository;
    private final CandidatoService candidatoService;
    private final GestorService gestorService;
    private final ObjectMapper objectMapper;

    private final EmailService emailService;

    public EntrevistaDTO createEntrevista(EntrevistaCreateDTO entrevistaCreateDTO, String token) throws RegraDeNegocioException {
        GestorEntity gestor = gestorService.getUser(token);

        CandidatoEntity candidato = candidatoService.findByEmailEntity(entrevistaCreateDTO.getCandidatoEmail());
        if (entrevistaRepository.findByCandidatoEntity(candidato).isPresent()) {
            throw new RegraDeNegocioException("Entrevista para o Candidato já agendada!");
        }

        String observacoes = entrevistaCreateDTO.getObservacoes();
        LocalDateTime dia = entrevistaCreateDTO.getDataEntrevista();
        List<EntrevistaEntity> entrevistaEntityList = entrevistaRepository.findByDataEntrevista(dia);
        verificarListaEntrevistas(entrevistaCreateDTO, gestor, entrevistaEntityList);
        EntrevistaEntity entrevistaEntity = new EntrevistaEntity();
        entrevistaEntity.setDataEntrevista(dia);
        entrevistaEntity.setCandidatoEntity(candidato);
        entrevistaEntity.setGestorEntity(gestor);
        entrevistaEntity.setObservacoes(observacoes);
        entrevistaEntity.setLegenda(Legenda.PENDENTE);
        entrevistaEntity.setAvaliado(entrevistaCreateDTO.getAvaliado().equals("T") ? TipoMarcacao.T : TipoMarcacao.F);
        EntrevistaEntity entrevistaSalva = entrevistaRepository.save(entrevistaEntity);

        SendEmailDTO sendEmailDTO = new SendEmailDTO();
        sendEmailDTO.setEmail(candidato.getEmail());
        sendEmailDTO.setNome(candidato.getNome());
        sendEmailDTO.setData(entrevistaEntity.getDataEntrevista().getDayOfMonth() + " de "
                + entrevistaEntity.getDataEntrevista().getMonth() + " de " + entrevistaEntity.getDataEntrevista().getYear() +
                " às " + entrevistaEntity.getDataEntrevista().getHour() + " Horas");

        emailService.sendEmail(sendEmailDTO, TipoEmail.CONFIRMAR_ENTREVISTA);
        return converterParaEntrevistaDTO(entrevistaSalva);
    }

    public void atualizarObservacaoEntrevista(Integer id, String observacao) throws RegraDeNegocioException {
        EntrevistaEntity entrevista = findById(id);
        entrevista.setObservacoes(observacao);
        entrevistaRepository.save(entrevista);
    }

    public EntrevistaDTO atualizarEntrevista(Integer idEntrevista,
                                             EntrevistaAtualizacaoDTO entrevistaCreateDTO,
                                             Legenda legenda) throws RegraDeNegocioException {
        GestorEntity gestor = gestorService.findByEmail(entrevistaCreateDTO.getEmail());
        EntrevistaEntity entrevista = findById(idEntrevista);
        List<EntrevistaEntity> entrevistaEntityList = entrevistaRepository.findByDataEntrevista(entrevista.getDataEntrevista());
        verificarListaEntrevistas(entrevistaCreateDTO, gestor, entrevistaEntityList);
        entrevista.setObservacoes(entrevistaCreateDTO.getObservacoes());
        entrevista.setDataEntrevista(entrevistaCreateDTO.getDataEntrevista());
        entrevista.setLegenda(legenda);
//        entrevista.setUsuarioEntity(usuario);
        EntrevistaEntity entrevistaSalva = entrevistaRepository.save(entrevista);
//        if(entrevista.getDataEntrevista().isAfter(LocalDateTime.now()) && legenda.equals(Legenda.PENDENTE)){
//            tokenConfirmacao(entrevista);
//        }
        return converterParaEntrevistaDTO(entrevistaSalva);
    }

    public List<EntrevistaDTO> list() {
        List<EntrevistaEntity> entrevistaEntityPage = entrevistaRepository.findAll();

        return entrevistaEntityPage.stream()
                .map(this::converterParaEntrevistaDTO)
                .toList();
    }

    public List<EntrevistaDTO> listPorTrilha(String trilha) {
        List<EntrevistaEntity> list = entrevistaRepository.findAllByTrilha(trilha);
        return list.stream().map(this::converterParaEntrevistaDTO).toList();
    }

    public PageDTO<EntrevistaDTO> listMes(Integer pagina, Integer tamanho, Integer mes, Integer ano) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<EntrevistaEntity> entrevistaEntityPage = entrevistaRepository.findAllByMes(mes, ano, pageRequest);

        List<EntrevistaDTO> entrevistaDTOList = entrevistaEntityPage
                .stream()
                .map(this::converterParaEntrevistaDTO)
                .toList();
        return new PageDTO<>(entrevistaEntityPage.getTotalElements(),
                entrevistaEntityPage.getTotalPages(),
                pagina,
                tamanho,
                entrevistaDTOList);
    }

    public EntrevistaDTO buscarPorEmailCandidato(String email) throws RegraDeNegocioException {
        CandidatoDTO candidatoDTO = candidatoService.findByEmail(email);
        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoDTO, CandidatoEntity.class);
        EntrevistaEntity entrevista = findByCandidatoEntity(candidatoEntity);

        EntrevistaDTO entrevistaDTO = objectMapper.convertValue(entrevista, EntrevistaDTO.class);
        entrevistaDTO.setCandidatoDTO(candidatoDTO);
        entrevistaDTO.setCandidatoEmail(candidatoDTO.getEmail());
        entrevistaDTO.setAvaliado(entrevista.getAvaliado() == null ? null : entrevista.getAvaliado().toString());
        entrevistaDTO.setGestorDTO(objectMapper.convertValue(entrevista.getGestorEntity(), GestorDTO.class));

        return entrevistaDTO;
    }

    public void exportarEntrevistasCsv(HttpServletResponse response) throws IOException {
        List<EntrevistaEntity> entrevistaEntityList = entrevistaRepository.findEntrevista();
        List<EntrevistaDTO> entrevistaDTOS = entrevistaEntityList.stream()
                .map(this::converterParaEntrevistaDTO).toList();

        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=entrevistas_agendadas_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        ExcelExporter excelExporter = new ExcelExporter(entrevistaDTOS);
        excelExporter.exportEntrevista(response);
    }

    public void deletarEntrevista(Integer idEntrevista) throws RegraDeNegocioException {
        EntrevistaEntity entrevistaRecuperada = findById(idEntrevista);
        entrevistaRepository.delete(entrevistaRecuperada);
    }

    public void deletarEntrevistaEmail(String email) throws RegraDeNegocioException {
        CandidatoDTO candidatoDTO = candidatoService.findByEmail(email);
        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoDTO, CandidatoEntity.class);
        EntrevistaEntity entrevista = findByCandidatoEntity(candidatoEntity);
        entrevistaRepository.deleteById(entrevista.getIdEntrevista());
    }

    public EntrevistaEntity findById(Integer id) throws RegraDeNegocioException {
        return entrevistaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Entrevista não encontrada!"));
    }

    public EntrevistaDTO converterParaEntrevistaDTO(EntrevistaEntity entrevistaEntity){
        EntrevistaDTO entrevistaDTO = objectMapper.convertValue(entrevistaEntity, EntrevistaDTO.class);
        entrevistaDTO.setGestorDTO(gestorService.convertoToDTO(entrevistaEntity.getGestorEntity()));
        entrevistaDTO.setCandidatoDTO(candidatoService.converterEmDTO(entrevistaEntity.getCandidatoEntity()));
        entrevistaDTO.setCandidatoEmail(entrevistaDTO.getCandidatoDTO().getEmail());
        entrevistaDTO.setAvaliado(entrevistaDTO.getAvaliado());
        return entrevistaDTO;
    }

    public EntrevistaEntity findByCandidatoEntity(CandidatoEntity candidatoEntity) throws RegraDeNegocioException {
        Optional<EntrevistaEntity> entrevistaEntityOptional = entrevistaRepository.findByCandidatoEntity(candidatoEntity);
        if (entrevistaEntityOptional.isEmpty()) {
            throw new RegraDeNegocioException("Entrevista com o candidato não encontrada!");
        }
        return entrevistaEntityOptional.get();
    }

    private void verificarListaEntrevistas(EntrevistaAtualizacaoDTO entrevistaCreateDTO,
                                           GestorEntity gestor,
                                           List<EntrevistaEntity> entrevistaEntityList) throws RegraDeNegocioException {
        if (!entrevistaEntityList.isEmpty()) {
            entrevistaEntityList = entrevistaEntityList.stream()
                    .filter(entrevista -> entrevista.getGestorEntity().getEmail().equals(gestor.getEmail()))
                    .toList();
            for (EntrevistaEntity entrevistaEntity : entrevistaEntityList) {
                if (entrevistaEntity.getDataEntrevista().equals(entrevistaCreateDTO.getDataEntrevista())) {
                    throw new RegraDeNegocioException("Horário já ocupado para entrevista! Agendar outro horário!");
                }
            }
        }
    }

    private void verificarListaEntrevistas(EntrevistaCreateDTO entrevistaCreateDTO,
                                           GestorEntity gestor,
                                           List<EntrevistaEntity> entrevistaEntityList) throws RegraDeNegocioException {
        if (!entrevistaEntityList.isEmpty()) {
            entrevistaEntityList = entrevistaEntityList.stream()
                    .filter(x -> x.getGestorEntity().getEmail().equals(gestor.getEmail()))
                    .toList();
            for (EntrevistaEntity entrevistaEntity : entrevistaEntityList) {
                if (entrevistaEntity.getDataEntrevista().equals(entrevistaCreateDTO.getDataEntrevista())) {
                    throw new RegraDeNegocioException("Horário já ocupado para entrevista! Agendar outro horário!");
                }
            }
        }
    }
}
