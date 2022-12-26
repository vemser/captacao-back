package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.CurriculoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.CurriculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CurriculoService {

    private final CurriculoRepository curriculoRepository;
    private final FormularioService formularioService;

    public CurriculoEntity findById(Integer idCurriculo) throws RegraDeNegocioException {
        return curriculoRepository.findById(idCurriculo)
                .orElseThrow(() -> new RegraDeNegocioException("Currículo não encontrado!"));
    }

    public FormularioEntity arquivarCurriculo(MultipartFile file, Integer idFormulario) throws IOException, RegraDeNegocioException {
        formularioService.findById(idFormulario);
        String nomeArquivo = StringUtils.cleanPath(file.getOriginalFilename());
        if (!nomeArquivo.endsWith(".pdf") && !nomeArquivo.endsWith(".docx")) {
            throw new RegraDeNegocioException("Formato de arquivo inválido! Inserir .pdf ou .docx");
        }
        CurriculoEntity curriculoEntity = new CurriculoEntity();
        curriculoEntity.setData(file.getBytes());
        curriculoEntity.setNome(file.getName());
        curriculoEntity.setTipo(file.getContentType());
        CurriculoEntity curriculoRetorno = curriculoRepository.save(curriculoEntity);
        FormularioEntity formulario = formularioService.findById(idFormulario);
        formulario.setCurriculoEntity(curriculoRetorno);
        formularioService.updateCurriculo(formulario, curriculoEntity);

        return formulario;
    }

    public String pegarCurriculoCandidato(Integer idFormulario) throws RegraDeNegocioException {

        FormularioEntity formularioEntity = formularioService.findById(idFormulario);

        if (formularioEntity.getCurriculoEntity() == null) {
            throw new RegraDeNegocioException("Usuário não possui currículo cadastrado.");
        }

        return Base64Utils.encodeToString(formularioEntity.getCurriculoEntity().getData());
    }


    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        CurriculoEntity curriculo = findById(id);
        curriculoRepository.deleteById(curriculo.getIdCurriculo());
    }
}
