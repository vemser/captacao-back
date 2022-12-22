package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.CurriculoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.CurriculoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurriculoService {

    private final CurriculoRepository curriculoRepository;

    private final CandidatoService candidatoService;

    private final FormularioService formularioService;

    private final ObjectMapper objectMapper;

    public CurriculoEntity findById(Integer idCurriculo) throws RegraDeNegocioException {
        return curriculoRepository.findById(idCurriculo)
                .orElseThrow(() -> new RegraDeNegocioException("Currículo não encontrado!"));
    }

    public void arquivarCurriculo(MultipartFile file, Integer idFormulario) throws IOException, RegraDeNegocioException {
        FormularioEntity formularioEntity = formularioService.findById(idFormulario);

        CurriculoEntity curriculoEntity = findById(formularioEntity.getCurriculoEntity().getIdCurriculo());

        String nomeArquivo = StringUtils.cleanPath(file.getOriginalFilename());
        if (!nomeArquivo.endsWith(".pdf") && !nomeArquivo.endsWith(".docx")) {
            throw new RegraDeNegocioException("Formato de arquivo inválido! Inserir .pdf ou .docx");
        }
        curriculoRepository.save(curriculoEntity);
    }

    public String pegarCurriculoCandidato(String email) throws RegraDeNegocioException {
        FormularioEntity formularioEntity = formularioService.findByEmail(email);
        Optional<CurriculoEntity> curriculo = objectMapper.convertValue(formularioEntity.getCurriculoEntity(), Optional.class);
        if (curriculo.isEmpty()) {
            throw new RegraDeNegocioException("Usuário não possui currículo cadastrado.");
        }
        return Base64Utils.encodeToString(curriculo.get().getData());
    }

    private Optional<CurriculoEntity> findByCandidato(CandidatoEntity candidatoEntity) throws RegraDeNegocioException {
        FormularioEntity formularioEntity = formularioService.findByEmail(candidatoEntity.getEmail());
        return objectMapper.convertValue(formularioEntity.getCurriculoEntity(), Optional.class);
    }


    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        CandidatoEntity candidatoEntity = candidatoService.findById(id);
        Optional<CurriculoEntity> curriculo = findByCandidato(candidatoEntity);
        Integer idCurriculo = curriculo.get().getIdCurriculo();
        ;
        curriculoRepository.deleteById(idCurriculo);
    }
}
