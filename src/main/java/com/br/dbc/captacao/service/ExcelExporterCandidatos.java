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
            writer.write(candidato.getNome().toString() + SEPARADOR);
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
                        "C??d.Formul??rio", // createCell(row, columnCount++, candidato.getIdFormulario(),
                        "C??d.Candidato",//        createCell(row, columnCount++, candidato.getIdCandidato(),
                        "Data",//        createCell(row, columnCount++, candidato.getDataInscricao().toString(),
                        "Nome",//        createCell(row, columnCount++, candidato.getNome().toString(),
                        "E-mail",//        createCell(row, columnCount++, candidato.getEmail(),
                        "Telefone ",//        createCell(row, columnCount++, candidato.getTelefone(),
                        "RG",//        createCell(row, columnCount++, candidato.getRg(),
                        "Estado ",//        createCell(row, columnCount++, candidato.getEstado(),
                        "Matriculado em curso de TI? ",//        createCell(row, columnCount++, candidato.getMatricula(),
                        "Institui????o de ensino", //        createCell(row, columnCount++, candidato.getInstituicao(),
                        "Curso", //        createCell(row, columnCount++, candidato.getCurso(),
                        "Turno", //        createCell(row, columnCount++, candidato.getTurno(),
                        "Por qual motivo voc?? se interessou pela ??rea de tecnologia?", //        createCell(row, columnCount++, candidato.getMotivosAreaTech(),
                        "Uma das nossas etapas eliminat??rias de sele????o ser?? a realiza????o de uma prova t??cnica. Ser?? necess??rio conhecimento de l??gica de programa????o e uso b??sico em algumas dessas tecnologias (Javascript, Java, Python, C e C++), mas ser?? avaliado principalmente racioc??nio para solu????o de problemas. Tens conhecimento necess??rio para realizar esta prova espec??fica?", //        createCell(row, columnCount++, candidato.getProva(),
                        "Trilha(s) Selecionadas", //        createCell(row, columnCount++, candidato.getTrilhas(),
                        "O interesse da DBC ?? efetivar os participantes que se desenvolverem bem ao longo do per??odo de forma????o. Tens interesse e disponibilidade para trabalhar em turno integral, (manh?? e tarde, at?? 44h semanais), caso aprovado? (Disponibilidade de no m??nimo 1 ano para ficar na DBC).", //        createCell(row, columnCount++, candidato.getEfetivacao(),
                        "O est??gio/forma????o acontecer?? de maneira virtual, no turno da tarde, das 13h30min ??s 17h30min, de segunda a sexta-feira, e ser?? necess??ria muita dedica????o extra para as atividades. Tens disponibilidade para estudar em outros turnos?", //        createCell(row, columnCount++, candidato.getDisponibilidade(),
                        "Algu??m te ensinou algo importante para a vida e que voc?? nunca esqueceu? Quem foi e o que voc?? aprendeu?", //        createCell(row, columnCount++, candidato.getAlguemEnsinouImportante(),
                        "Ingl??s", //        createCell(row, columnCount++, candidato.getIngles(),
                        "Espanhol", //        createCell(row, columnCount++, candidato.getEspanhol(),
                        "G??nero", //        createCell(row, columnCount++, candidato.getGenero(),
                        "Orienta????o", //        createCell(row, columnCount++, candidato.getOrientacao(),
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

