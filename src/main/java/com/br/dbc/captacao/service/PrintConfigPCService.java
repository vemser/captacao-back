package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.FormularioEntity;
import com.br.dbc.captacao.entity.PrintConfigPCEntity;
import com.br.dbc.captacao.exception.RegraDeNegocio404Exception;
import com.br.dbc.captacao.exception.RegraDeNegocioException;
import com.br.dbc.captacao.repository.PrintConfigPCRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PrintConfigPCService {

    private final PrintConfigPCRepository printConfigPCRepository;
    private final FormularioService formularioService;

    public PrintConfigPCEntity findById(Integer id) throws RegraDeNegocioException {
        return printConfigPCRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Configurações do computador não encontrada!"));
    }

    public void arquivarPrintConfigPc(MultipartFile file, Integer idFormulario) throws RegraDeNegocioException, IOException, RegraDeNegocio404Exception {
        FormularioEntity formulario = formularioService.findById(idFormulario);

        String nomeArquivo = StringUtils.cleanPath((file.getOriginalFilename()));
        if(!nomeArquivo.endsWith(".png") && !nomeArquivo.endsWith(".jpeg") && !nomeArquivo.endsWith(".jpg")){
            throw new RegraDeNegocioException("Formato de arquivo inválido! Inserir .png, .jpg ou .jpeg");
        }

        PrintConfigPCEntity novaImagemBD = new PrintConfigPCEntity();

        novaImagemBD.setNome(nomeArquivo);
        novaImagemBD.setTipo(file.getContentType());
        novaImagemBD.setData(file.getBytes());

        PrintConfigPCEntity printSalvo = printConfigPCRepository.save(novaImagemBD);
        formulario.setImagemConfigPc(printSalvo);
        formularioService.save(formulario);
    }

    public String recuperarPrint(Integer idFormulario) throws RegraDeNegocioException, RegraDeNegocio404Exception {

        FormularioEntity formularioEntity = formularioService.findById(idFormulario);

        if (formularioEntity.getImagemConfigPc() == null) {
            throw new RegraDeNegocioException("Usuário não possui print das configurações do pc cadastrado.");
        }

        return Base64Utils.encodeToString(formularioEntity.getImagemConfigPc().getData());
    }

    public void deleteFisico(Integer id) throws RegraDeNegocioException {
        findById(id);
        printConfigPCRepository.deleteById(id);
    }
}
