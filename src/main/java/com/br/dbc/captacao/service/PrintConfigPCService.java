package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.PrintConfigPCEntity;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.PrintConfigPCRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrintConfigPCService {

    private final PrintConfigPCRepository printConfigPCRepository;

    private final ObjectMapper objectMapper;

    private final FormularioService formularioService;

    public PrintConfigPCEntity findById(Integer id) throws RegraDeNegocioException {
        return printConfigPCRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Configurações do computador não encontrada!"));
    }

    public void arquivarPrintConfigPc(MultipartFile file, Integer idFormulario) throws RegraDeNegocioException, IOException {
        FormularioEntity formulario = formularioService.findById(idFormulario);

        Optional<PrintConfigPCEntity> printConfigPCEntity = findByFormulario(formulario);
        String nomeArquivo = StringUtils.cleanPath((file.getOriginalFilename()));
        if (printConfigPCEntity.isPresent()) {
            printConfigPCEntity.get().setNome(nomeArquivo);
            printConfigPCEntity.get().setTipo(file.getContentType());
            printConfigPCEntity.get().setData(file.getBytes());
            printConfigPCEntity.get().setFormulario(formulario);
            printConfigPCRepository.save(printConfigPCEntity.get());
        } else {
            PrintConfigPCEntity novaImagemBD = new PrintConfigPCEntity();
            novaImagemBD.setNome(nomeArquivo);
            novaImagemBD.setTipo(file.getContentType());
            novaImagemBD.setData(file.getBytes());
            novaImagemBD.setFormulario(formulario);
            printConfigPCRepository.save(novaImagemBD);
        }
    }

    private Optional<PrintConfigPCEntity> findByCandidato(CandidatoEntity candidatoEntity) throws RegraDeNegocioException {
        FormularioEntity formularioEntity = formularioService.findById(candidatoEntity.getFormularioEntity().getIdFormulario());
        return objectMapper.convertValue(formularioEntity.getCurriculoEntity(),Optional.class);
    }

    private Optional<PrintConfigPCEntity> findByFormulario(FormularioEntity formulario) {
        return printConfigPCRepository.findByFormulario(formulario);
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        printConfigPCRepository.deleteById(id);
    }
}
