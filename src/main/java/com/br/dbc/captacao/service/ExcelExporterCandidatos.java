package com.br.dbc.captacao.service;

import com.br.dbc.captacao.entity.CandidatoEntity;
import com.br.dbc.captacao.entity.VwCandidatosUltimaEdicao;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcelExporterCandidatos {

    private static final String NOME_PLANILHA = "Candidatos";

    public XSSFWorkbook getPlanilhaCandidatos(List<VwCandidatosUltimaEdicao> listaCandidatos) {
        XSSFWorkbook sheet = writeHeaderLineCandidato();
        writeDataLinesCandidatos(sheet, listaCandidatos);
        return sheet;
    }


    private XSSFWorkbook writeHeaderLineCandidato() {
        XSSFWorkbook sheets = new XSSFWorkbook();
        XSSFSheet sheet = sheets.createSheet(NOME_PLANILHA);
        Row row = sheet.createRow(0);

        Workbook workbook = sheet.getWorkbook();
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = sheet.getWorkbook().createFont();
        font.setBold(true);
        font.setFontHeight(15);
        style.setFont(font);

        int i = 0;

        createCell(row, i++, "Cód.Formulário", style); // createCell(row, columnCount++, candidato.getIdFormulario(), style);
        createCell(row, i++, "Cód.Candidato", style);//        createCell(row, columnCount++, candidato.getIdCandidato(), style);
        createCell(row, i++, "Data", style);//        createCell(row, columnCount++, candidato.getDataInscricao().toString(), style);
        createCell(row, i++, "E-mail", style);//        createCell(row, columnCount++, candidato.getEmail(), style);
        createCell(row, i++, "Telefone ", style);//        createCell(row, columnCount++, candidato.getTelefone(), style);
        createCell(row, i++, "RG", style);//        createCell(row, columnCount++, candidato.getRg(), style);
        createCell(row, i++, "Estado ", style);//        createCell(row, columnCount++, candidato.getEstado(), style);
        createCell(row, i++, "Matriculado em curso de TI? ", style);//        createCell(row, columnCount++, candidato.getMatricula(), style);
        createCell(row, i++, "Instituição de ensino", style); //        createCell(row, columnCount++, candidato.getInstituicao(), style);
        createCell(row, i++, "Curso", style); //        createCell(row, columnCount++, candidato.getCurso(), style);
        createCell(row, i++, "Turno", style); //        createCell(row, columnCount++, candidato.getTurno(), style);
        createCell(row, i++, "Por qual motivo você se interessou pela área de tecnologia?", style); //        createCell(row, columnCount++, candidato.getMotivosAreaTech(), style);
        createCell(row, i++, "Uma das nossas etapas eliminatórias de seleção será a realização de uma prova técnica. Será necessário conhecimento de lógica de programação e uso básico em algumas dessas tecnologias (Javascript, Java, Python, C e C++), mas será avaliado principalmente raciocínio para solução de problemas. Tens conhecimento necessário para realizar esta prova específica?", style); //        createCell(row, columnCount++, candidato.getProva(), style);
        createCell(row, i++, "Trilha(s) Selecionadas", style); //        createCell(row, columnCount++, candidato.getTrilhas(), style);
        createCell(row, i++, "O interesse da DBC é efetivar os participantes que se desenvolverem bem ao longo do período de formação. Tens interesse e disponibilidade para trabalhar em turno integral, (manhã e tarde, até 44h semanais), caso aprovado? (Disponibilidade de no mínimo 1 ano para ficar na DBC).", style); //        createCell(row, columnCount++, candidato.getEfetivacao(), style);
        createCell(row, i++, "O estágio/formação acontecerá de maneira virtual, no turno da tarde, das 13h30min às 17h30min, de segunda a sexta-feira, e será necessária muita dedicação extra para as atividades. Tens disponibilidade para estudar em outros turnos?", style); //        createCell(row, columnCount++, candidato.getDisponibilidade(), style);
        createCell(row, i++, "Alguém te ensinou algo importante para a vida e que você nunca esqueceu? Quem foi e o que você aprendeu?", style); //        createCell(row, columnCount++, candidato.getAlguemEnsinouImportante(), style);
        createCell(row, i++, "Inglês", style); //        createCell(row, columnCount++, candidato.getIngles(), style);
        createCell(row, i++, "Espanhol", style); //        createCell(row, columnCount++, candidato.getEspanhol(), style);
        createCell(row, i++, "Gênero", style); //        createCell(row, columnCount++, candidato.getGenero(), style);
        createCell(row, i++, "Orientação", style); //        createCell(row, columnCount++, candidato.getOrientacao(), style);
        createCell(row, i++, "Neurodiversidade", style); //        createCell(row, columnCount++, candidato.getNeurodiversidade(), style);
        createCell(row, i++, "PCD", style); //        createCell(row, columnCount++, candidato.getPcd(), style);
        createCell(row, i++, "LinkedIn", style); //        createCell(row, columnCount++, candidato.getLinkedin(), style);
        createCell(row, i++, "GitHub", style); //        createCell(row, columnCount++, candidato.getGithub(), style);
        createCell(row, i++, "Aprovado(a) p/ realização da prova técnica?", style);
        createCell(row, i++, "Motivo?", style);

        return sheets;
    }

    private void writeDataLinesCandidatos(XSSFWorkbook workbook, List<VwCandidatosUltimaEdicao> listaCandidatos) {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (VwCandidatosUltimaEdicao candidato : listaCandidatos) {

            Row row = workbook.getSheet(NOME_PLANILHA).createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, candidato.getIdFormulario(), style);
            createCell(row, columnCount++, candidato.getIdCandidato(), style);
            createCell(row, columnCount++, candidato.getDataInscricao().toString(), style);
            createCell(row, columnCount++, candidato.getEmail(), style);
            createCell(row, columnCount++, candidato.getTelefone(), style);
            createCell(row, columnCount++, candidato.getRg(), style);
            createCell(row, columnCount++, candidato.getEstado(), style);
            createCell(row, columnCount++, candidato.getMatricula(), style);
            createCell(row, columnCount++, candidato.getInstituicao(), style);
            createCell(row, columnCount++, candidato.getCurso(), style);
            createCell(row, columnCount++, candidato.getTurno(), style);
            createCell(row, columnCount++, candidato.getMotivosAreaTech(), style);
            createCell(row, columnCount++, candidato.getProva(), style);
            createCell(row, columnCount++, candidato.getTrilhas(), style);
            createCell(row, columnCount++, candidato.getEfetivacao(), style);
            createCell(row, columnCount++, candidato.getDisponibilidade(), style);
            createCell(row, columnCount++, candidato.getAlguemEnsinouImportante(), style);
            createCell(row, columnCount++, candidato.getIngles(), style);
            createCell(row, columnCount++, candidato.getEspanhol(), style);
            createCell(row, columnCount++, candidato.getGenero(), style);
            createCell(row, columnCount++, candidato.getOrientacao(), style);
            createCell(row, columnCount++, candidato.getNeurodiversidade(), style);
            createCell(row, columnCount++, candidato.getPcd(), style);
            createCell(row, columnCount++, candidato.getLinkedin(), style);
            createCell(row, columnCount++, candidato.getGithub(), style);

        }
    }


    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        row.getSheet().autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Number) {
            cell.setCellValue(value.toString());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }


}

