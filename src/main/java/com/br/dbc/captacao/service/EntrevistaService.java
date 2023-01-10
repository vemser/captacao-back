package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaAtualizacaoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaCreateDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaDTO;
import com.br.dbc.captacao.dto.gestor.GestorDTO;
import com.br.dbc.captacao.dto.paginacao.PageDTO;
import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.EntrevistaEntity;
import com.br.dbc.captacao.entity.GestorEntity;
import com.br.dbc.captacao.entity.TrilhaEntity;
import com.br.dbc.captacao.enums.Legenda;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.EntrevistaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntrevistaService {
    private final EntrevistaRepository entrevistaRepository;
    private final CandidatoService candidatoService;
    private final GestorService gestorService;
    private final ObjectMapper objectMapper;

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

    public PageDTO<EntrevistaDTO> list(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<EntrevistaEntity> entrevistaEntityPage = entrevistaRepository.findAll(pageRequest);
        List<EntrevistaDTO> entrevistaDTOList = entrevistaEntityPage.stream()
                .map(this::converterParaEntrevistaDTO)
                .toList();
        return new PageDTO<>(entrevistaEntityPage.getTotalElements(),
                entrevistaEntityPage.getTotalPages(),
                pagina,
                tamanho,
                entrevistaDTOList);
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
        return converterParaEntrevistaDTO(entrevistaSalva);
    }

    public EntrevistaEntity findByCandidatoEntity(CandidatoEntity candidatoEntity) throws RegraDeNegocioException {
        Optional<EntrevistaEntity> entrevistaEntityOptional = entrevistaRepository.findByCandidatoEntity(candidatoEntity);
        if (entrevistaEntityOptional.isEmpty()) {
            throw new RegraDeNegocioException("Entrevista com o candidato não encontrada!");
        }
        return entrevistaEntityOptional.get();
    }

    public void deletarEntrevista(Integer idEntrevista) throws RegraDeNegocioException {
        EntrevistaEntity entrevistaRecuperada = findById(idEntrevista);
        entrevistaRepository.delete(entrevistaRecuperada);
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

    public void exportarEntrevistaCSV() throws RegraDeNegocioException {
        List<EntrevistaEntity> entrevistaEntityList = entrevistaRepository.findAll();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("entrevistas.csv", false));
            for (EntrevistaEntity entrevista : entrevistaEntityList) {
                StringBuilder oneLine = new StringBuilder();
                oneLine.append(entrevista.getIdEntrevista());
                oneLine.append(",");
                oneLine.append(entrevista.getDataEntrevista());
                oneLine.append(",");
                oneLine.append(entrevista.getCandidatoEntity().getIdCandidato());
                oneLine.append(",");
                oneLine.append(entrevista.getCandidatoEntity().getNome());
                oneLine.append(",");
                oneLine.append(entrevista.getCandidatoEntity().getEmail());
                oneLine.append(",");
                oneLine.append(entrevista.getCandidatoEntity().getFormularioEntity().getTrilhaEntitySet().stream().map(TrilhaEntity::getNome).toList());
                oneLine.append(",");
                oneLine.append(entrevista.getAvaliado() == null ? "F" : entrevista.getAvaliado());
                oneLine.append(",");
                oneLine.append(entrevista.getLegenda().toString());
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new RegraDeNegocioException("Erro ao exportar dados para arquivo.");
        }
    }

    public EntrevistaDTO buscarPorEmailCandidato(String email) throws RegraDeNegocioException {
        CandidatoDTO candidatoDTO = candidatoService.findByEmail(email);
        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoDTO, CandidatoEntity.class);
        EntrevistaEntity entrevista = findByCandidatoEntity(candidatoEntity);

        EntrevistaDTO entrevistaDTO = objectMapper.convertValue(entrevista, EntrevistaDTO.class);
        entrevistaDTO.setCandidatoDTO(candidatoDTO);
        entrevistaDTO.setCandidatoEmail(candidatoDTO.getEmail());
        entrevistaDTO.setAvaliado(entrevista.getAvaliado().toString());
        entrevistaDTO.setGestorDTO(objectMapper.convertValue(entrevista.getGestorEntity(), GestorDTO.class));

        return entrevistaDTO;
    }

    public void deletarEntrevistaEmail(String email) throws RegraDeNegocioException {
        CandidatoDTO candidatoDTO = candidatoService.findByEmail(email);
        CandidatoEntity candidatoEntity = objectMapper.convertValue(candidatoDTO, CandidatoEntity.class);
        EntrevistaEntity entrevista = findByCandidatoEntity(candidatoEntity);
        entrevistaRepository.deleteById(entrevista.getIdEntrevista());
    }

    public void atualizarObservacaoEntrevista(Integer id, String observacao) throws RegraDeNegocioException {
        EntrevistaEntity entrevista = findById(id);
        entrevista.setObservacoes(observacao);
        entrevistaRepository.save(entrevista);
    }
}
