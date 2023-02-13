package com.br.dbc.captacao.service;

import com.br.dbc.captacao.dto.candidato.CandidatoDTO;
import com.br.dbc.captacao.dto.entrevista.EntrevistaDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExporter {

    private static final String NOTA_PADRAO = "0";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<EntrevistaDTO> entrevistasDTO;
    private List<CandidatoDTO> candidatosDTO;

    public ExcelExporter() {
    }

    public ExcelExporter(List<CandidatoDTO> candidatosDTO , XSSFWorkbook workbook) {
        this.candidatosDTO = candidatosDTO;
        this.workbook = workbook;
    }

    public ExcelExporter(List<EntrevistaDTO> entrevistasDTO) {
        this.entrevistasDTO = entrevistasDTO;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLineCandidato() {
        sheet = workbook.createSheet("Candidatos");
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(15);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "Nome do candidato", style);
        createCell(row, 2, "E-mail do candidato", style);
        createCell(row, 3, "Trilhas", style);
        createCell(row, 4, "Nota da prova", style);
        createCell(row, 5, "Nota da Entrevista tecnica", style);
        createCell(row, 6, "Nota da Comportamental", style);
        createCell(row, 7, "Média das notas", style);
        createCell(row, 8, "Telefone do candidato", style);
        createCell(row, 9, "Parecer tecnico", style);
        createCell(row, 10, "Parecer Comportamental", style);
    }

    private void writeHeaderLineEntrevista() {
        sheet = workbook.createSheet("Entrevistas");
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(15);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "Nome do candidato", style);
        createCell(row, 2, "E-mail do candidato", style);
        createCell(row, 3, "Trilhas", style);
        createCell(row, 4, "Nota da prova", style);
        createCell(row, 5, "Data da entrevista", style);
        createCell(row, 6, "Hora da entrevista", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLinesCandidato(List<CandidatoDTO> candidatosDTO) {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (CandidatoDTO candidatoDTO : candidatosDTO) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, candidatoDTO.getIdCandidato().toString(), style);
            createCell(row, columnCount++, candidatoDTO.getNome(), style);
            createCell(row, columnCount++, candidatoDTO.getEmail(), style);

            String trilha = "";
            List<String> trilhas = candidatoDTO.getFormulario().getTrilhas().stream().map(x -> x.getNome()).toList();

            for (String x : trilhas){
                trilha += x + " ";
            }

            createCell(row, columnCount++, trilha, style);
            createCell(row, columnCount++, candidatoDTO.getNotaProva().toString(), style);
            createCell(row, columnCount++, candidatoDTO.getNotaEntrevistaTecnica().toString(), style);
            createCell(row, columnCount++, candidatoDTO.getNotaEntrevistaComportamental().toString(), style);
            createCell(row, columnCount++, candidatoDTO.getMedia().toString(), style);
            createCell(row, columnCount, candidatoDTO.getTelefone(), style);
            createCell(row, columnCount++, candidatoDTO.getParecerTecnico(), style);
            createCell(row, columnCount++, candidatoDTO.getParecerComportamental(), style);
        }
    }
    private void writeDataLinesCandidatos(List<CandidatoDTO> candidatosDTO) {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (CandidatoDTO candidatoDTO : candidatosDTO) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, candidatoDTO.getIdCandidato().toString(), style);
            createCell(row, columnCount++, candidatoDTO.getNome(), style);
            createCell(row, columnCount++, candidatoDTO.getEmail(), style);

            String trilha = "";
            List<String> trilhas = candidatoDTO.getFormulario().getTrilhas().stream().map(x -> x.getNome()).toList();

            for (String x : trilhas){
                trilha += x + " ";
            }

            createCell(row, columnCount++, trilha, style);
            createCell(row, columnCount++, candidatoDTO.getNotaProva().toString(), style);
            createCell(row, columnCount++, candidatoDTO.getNotaEntrevistaTecnica().toString(), style);
            createCell(row, columnCount++, candidatoDTO.getNotaEntrevistaComportamental().toString(), style);

            if(candidatoDTO.getMedia() == null){
                createCell(row, columnCount++, NOTA_PADRAO, style);
            }else {
                createCell(row, columnCount++, candidatoDTO.getMedia().toString(), style);
            }

            createCell(row, columnCount++, candidatoDTO.getTelefone(), style);
            createCell(row, columnCount++, candidatoDTO.getParecerTecnico(), style);
            createCell(row, columnCount++, candidatoDTO.getParecerComportamental(), style);
        }
    }

    private void writeDataLinesEntrevista(List<EntrevistaDTO> entrevistasDTO) {
        int rowCount = 1;
        DateTimeFormatter data = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter hora = DateTimeFormatter.ofPattern("HH:mm");

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (EntrevistaDTO entrevista : entrevistasDTO) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, entrevista.getCandidatoDTO().getIdCandidato().toString(), style);
            createCell(row, columnCount++, entrevista.getCandidatoDTO().getNome(), style);
            createCell(row, columnCount++, entrevista.getCandidatoDTO().getEmail(), style);

            String trilha = "";
            List<String> trilhas = entrevista.getCandidatoDTO().getFormulario().getTrilhas().stream().map(x -> x.getNome()).toList();

            for (String x : trilhas){
                trilha += x + " ";
            }

            createCell(row, columnCount++, trilha, style);
            createCell(row, columnCount++, entrevista.getCandidatoDTO().getNotaProva().toString(), style);
            createCell(row, columnCount++, data.format(entrevista.getDataEntrevista()), style);
            createCell(row, columnCount, hora.format(entrevista.getDataEntrevista()), style);
        }
    }

    public void exportCandidato(HttpServletResponse response) throws IOException {
        writeHeaderLineCandidato();
        writeDataLinesCandidato(candidatosDTO);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
    public void exportCandidatos(HttpServletResponse response) throws IOException {
        writeHeaderLineCandidato();
        writeDataLinesCandidatos(candidatosDTO);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }

    public void exportEntrevista(HttpServletResponse response) throws IOException {
        writeHeaderLineEntrevista();
        writeDataLinesEntrevista(entrevistasDTO);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}

