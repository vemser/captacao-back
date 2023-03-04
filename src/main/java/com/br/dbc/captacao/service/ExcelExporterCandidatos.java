package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.VwCandidatosUltimaEdicao;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;

@Service
public class ExcelExporterCandidatos {

    private static final String SEPARADOR = ";";

    public void writeHeaderLineCandidato(List<VwCandidatosUltimaEdicao> listaCandidatos, PrintWriter writer) {
        imprimirCabecalho(writer);
        for (VwCandidatosUltimaEdicao candidato : listaCandidatos) {
            writer.write(candidato.getIdFormulario().toString() + SEPARADOR);
            writer.write(candidato.getIdCandidato().toString() + SEPARADOR);
            writer.write(candidato.getDataInscricao().toString() + SEPARADOR);
            writer.write(candidato.getEmail().toString() + SEPARADOR);
            writer.write(candidato.getTelefone().toString() + SEPARADOR);
            writer.write(candidato.getRg().toString() + SEPARADOR);
            writer.write(candidato.getEstado().toString() + SEPARADOR);
            writer.write(candidato.getMatricula().toString() + SEPARADOR);
            writer.write(candidato.getInstituicao().toString() + SEPARADOR);
            writer.write(candidato.getCurso().toString() + SEPARADOR);
            writer.write(candidato.getTurno().toString() + SEPARADOR);
            writer.write(candidato.getMotivosAreaTech().toString() + SEPARADOR);
            writer.write(candidato.getProva().toString() + SEPARADOR);
            writer.write(candidato.getTrilhas().toString() + SEPARADOR);
            writer.write(candidato.getEfetivacao().toString() + SEPARADOR);
            writer.write(candidato.getDisponibilidade().toString() + SEPARADOR);
            writer.write(candidato.getAlguemEnsinouImportante().toString() + SEPARADOR);
            writer.write(candidato.getIngles().toString() + SEPARADOR);
            writer.write(candidato.getEspanhol().toString() + SEPARADOR);
            writer.write(candidato.getGenero().toString() + SEPARADOR);
            writer.write(candidato.getOrientacao().toString() + SEPARADOR);
            writer.write(candidato.getNeurodiversidade().toString() + SEPARADOR);
            writer.write(candidato.getPcd().toString() + SEPARADOR);
            writer.write(candidato.getLinkedin().toString() + SEPARADOR);
            writer.write(candidato.getGithub().toString());
            writer.write(System.getProperty("line.separator"));
        }
    }

    private static void imprimirCabecalho(PrintWriter writer) {
        String[] columns = new String[]
                {
                        "Cód.Formulário", // createCell(row, columnCount++, candidato.getIdFormulario(),
                        "Cód.Candidato",//        createCell(row, columnCount++, candidato.getIdCandidato(),
                        "Data",//        createCell(row, columnCount++, candidato.getDataInscricao().toString(),
                        "E-mail",//        createCell(row, columnCount++, candidato.getEmail(),
                        "Telefone ",//        createCell(row, columnCount++, candidato.getTelefone(),
                        "RG",//        createCell(row, columnCount++, candidato.getRg(),
                        "Estado ",//        createCell(row, columnCount++, candidato.getEstado(),
                        "Matriculado em curso de TI? ",//        createCell(row, columnCount++, candidato.getMatricula(),
                        "Instituição de ensino", //        createCell(row, columnCount++, candidato.getInstituicao(),
                        "Curso", //        createCell(row, columnCount++, candidato.getCurso(),
                        "Turno", //        createCell(row, columnCount++, candidato.getTurno(),
                        "Por qual motivo você se interessou pela área de tecnologia?", //        createCell(row, columnCount++, candidato.getMotivosAreaTech(),
                        "Uma das nossas etapas eliminatórias de seleção será a realização de uma prova técnica. Será necessário conhecimento de lógica de programação e uso básico em algumas dessas tecnologias (Javascript, Java, Python, C e C++), mas será avaliado principalmente raciocínio para solução de problemas. Tens conhecimento necessário para realizar esta prova específica?", //        createCell(row, columnCount++, candidato.getProva(),
                        "Trilha(s) Selecionadas", //        createCell(row, columnCount++, candidato.getTrilhas(),
                        "O interesse da DBC é efetivar os participantes que se desenvolverem bem ao longo do período de formação. Tens interesse e disponibilidade para trabalhar em turno integral, (manhã e tarde, até 44h semanais), caso aprovado? (Disponibilidade de no mínimo 1 ano para ficar na DBC).", //        createCell(row, columnCount++, candidato.getEfetivacao(),
                        "O estágio/formação acontecerá de maneira virtual, no turno da tarde, das 13h30min às 17h30min, de segunda a sexta-feira, e será necessária muita dedicação extra para as atividades. Tens disponibilidade para estudar em outros turnos?", //        createCell(row, columnCount++, candidato.getDisponibilidade(),
                        "Alguém te ensinou algo importante para a vida e que você nunca esqueceu? Quem foi e o que você aprendeu?", //        createCell(row, columnCount++, candidato.getAlguemEnsinouImportante(),
                        "Inglês", //        createCell(row, columnCount++, candidato.getIngles(),
                        "Espanhol", //        createCell(row, columnCount++, candidato.getEspanhol(),
                        "Gênero", //        createCell(row, columnCount++, candidato.getGenero(),
                        "Orientação", //        createCell(row, columnCount++, candidato.getOrientacao(),
                        "Neurodiversidade", //        createCell(row, columnCount++, candidato.getNeurodiversidade(),
                        "PCD", //        createCell(row, columnCount++, candidato.getPcd(),
                        "LinkedIn", //        createCell(row, columnCount++, candidato.getLinkedin(),
                        "GitHub" //        createCell(row, columnCount++, candidato.getGithub(),
                };

        for (int i = 0; i < columns.length; i++) {
            writer.write(columns[i]);
            if (i != columns.length - 1) {
                writer.write(SEPARADOR);
            } else {
                writer.write(System.getProperty("line.separator"));
            }
        }
    }

}

