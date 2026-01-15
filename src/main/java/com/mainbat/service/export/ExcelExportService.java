package com.mainbat.service.export;

import com.mainbat.model.batiment.Batiment;
import com.mainbat.model.equipement.Equipement;
import com.mainbat.model.intervention.Intervention;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class ExcelExportService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] exportInterventionsToExcel(List<Intervention> interventions, String titre) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Interventions");

            // Styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            // Title
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(titre);
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            // Generation info
            Row infoRow = sheet.createRow(1);
            infoRow.createCell(0).setCellValue("Généré le " + LocalDateTime.now().format(DATETIME_FORMAT));

            // Headers
            Row headerRow = sheet.createRow(3);
            String[] headers = {"ID", "Titre", "Type", "Statut", "Urgence", "Équipement", "Bâtiment", "Date Planifiée"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 4;
            for (Intervention intervention : interventions) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(intervention.getId());
                row.createCell(1).setCellValue(intervention.getTitre());
                row.createCell(2).setCellValue(intervention.getTypeIntervention().name());
                row.createCell(3).setCellValue(intervention.getStatut().name());
                row.createCell(4).setCellValue(intervention.getUrgence() != null ? intervention.getUrgence().name() : "-");
                row.createCell(5).setCellValue(intervention.getEquipement().getNom());
                row.createCell(6).setCellValue(intervention.getEquipement().getZone().getBatiment().getNom());
                row.createCell(7).setCellValue(intervention.getDatePlanifiee() != null ? 
                        intervention.getDatePlanifiee().format(DATE_FORMAT) : "-");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            log.info("Excel interventions généré: {} entrées", interventions.size());
            return baos.toByteArray();
        }
    }

    public byte[] exportEquipementsToExcel(List<Equipement> equipements, String titre) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Équipements");

            CellStyle headerStyle = createHeaderStyle(workbook);

            // Title
            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue(titre);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            Row infoRow = sheet.createRow(1);
            infoRow.createCell(0).setCellValue("Généré le " + LocalDateTime.now().format(DATETIME_FORMAT));

            // Headers
            Row headerRow = sheet.createRow(3);
            String[] headers = {"ID", "Nom", "Type", "Marque", "Modèle", "État", "Bâtiment", "Zone"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 4;
            for (Equipement equipement : equipements) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(equipement.getId());
                row.createCell(1).setCellValue(equipement.getNom());
                row.createCell(2).setCellValue(equipement.getTypeEquipement() != null ? equipement.getTypeEquipement() : "-");
                row.createCell(3).setCellValue(equipement.getMarque() != null ? equipement.getMarque() : "-");
                row.createCell(4).setCellValue(equipement.getModele() != null ? equipement.getModele() : "-");
                row.createCell(5).setCellValue(equipement.getEtat().name());
                row.createCell(6).setCellValue(equipement.getZone().getBatiment().getNom());
                row.createCell(7).setCellValue(equipement.getZone().getNom());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            log.info("Excel équipements généré: {} entrées", equipements.size());
            return baos.toByteArray();
        }
    }

    public byte[] exportBatimentsToExcel(List<Batiment> batiments, String titre) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Bâtiments");

            CellStyle headerStyle = createHeaderStyle(workbook);

            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue(titre);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

            Row infoRow = sheet.createRow(1);
            infoRow.createCell(0).setCellValue("Généré le " + LocalDateTime.now().format(DATETIME_FORMAT));

            // Headers
            Row headerRow = sheet.createRow(3);
            String[] headers = {"ID", "Nom", "Adresse", "Ville", "Type", "Criticité"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 4;
            for (Batiment batiment : batiments) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(batiment.getId());
                row.createCell(1).setCellValue(batiment.getNom());
                row.createCell(2).setCellValue(batiment.getAdresse());
                row.createCell(3).setCellValue(batiment.getVille() != null ? batiment.getVille() : "-");
                row.createCell(4).setCellValue(batiment.getTypeBatiment() != null ? batiment.getTypeBatiment().name() : "-");
                row.createCell(5).setCellValue(batiment.getCriticite() != null ? batiment.getCriticite().name() : "-");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            log.info("Excel bâtiments généré: {} entrées", batiments.size());
            return baos.toByteArray();
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
        return style;
    }
}
