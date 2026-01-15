package com.mainbat.service.export;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.mainbat.model.batiment.Batiment;
import com.mainbat.model.equipement.Equipement;
import com.mainbat.model.intervention.Intervention;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class PdfExportService {

    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(41, 128, 185);
    private static final DeviceRgb HEADER_BG = new DeviceRgb(52, 73, 94);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] exportInterventionsToPdf(List<Intervention> interventions, String titre) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Title
        addTitle(document, titre, boldFont);
        addGenerationInfo(document, regularFont);

        // Table
        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2, 2, 2}))
                .useAllAvailableWidth();

        // Headers
        addTableHeader(table, boldFont, "Titre", "Type", "Statut", "Urgence", "Équipement", "Date");

        // Data
        for (Intervention intervention : interventions) {
            table.addCell(createCell(intervention.getTitre(), regularFont));
            table.addCell(createCell(intervention.getTypeIntervention().name(), regularFont));
            table.addCell(createCell(intervention.getStatut().name(), regularFont));
            table.addCell(createCell(intervention.getUrgence() != null ? intervention.getUrgence().name() : "-", regularFont));
            table.addCell(createCell(intervention.getEquipement().getNom(), regularFont));
            table.addCell(createCell(intervention.getDatePlanifiee() != null ? 
                    intervention.getDatePlanifiee().format(DATE_FORMAT) : "-", regularFont));
        }

        document.add(table);

        // Summary
        addSummary(document, regularFont, "Total interventions: " + interventions.size());

        document.close();
        log.info("PDF interventions généré: {} entrées", interventions.size());
        return baos.toByteArray();
    }

    public byte[] exportEquipementsToPdf(List<Equipement> equipements, String titre) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        addTitle(document, titre, boldFont);
        addGenerationInfo(document, regularFont);

        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2, 2, 2}))
                .useAllAvailableWidth();

        addTableHeader(table, boldFont, "Nom", "Type", "Marque", "État", "Bâtiment", "Zone");

        for (Equipement equipement : equipements) {
            table.addCell(createCell(equipement.getNom(), regularFont));
            table.addCell(createCell(equipement.getTypeEquipement() != null ? equipement.getTypeEquipement() : "-", regularFont));
            table.addCell(createCell(equipement.getMarque() != null ? equipement.getMarque() : "-", regularFont));
            table.addCell(createCell(equipement.getEtat().name(), regularFont));
            table.addCell(createCell(equipement.getZone().getBatiment().getNom(), regularFont));
            table.addCell(createCell(equipement.getZone().getNom(), regularFont));
        }

        document.add(table);
        addSummary(document, regularFont, "Total équipements: " + equipements.size());

        document.close();
        log.info("PDF équipements généré: {} entrées", equipements.size());
        return baos.toByteArray();
    }

    public byte[] exportBatimentsToPdf(List<Batiment> batiments, String titre) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        addTitle(document, titre, boldFont);
        addGenerationInfo(document, regularFont);

        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 3, 2, 2, 2}))
                .useAllAvailableWidth();

        addTableHeader(table, boldFont, "Nom", "Adresse", "Ville", "Type", "Criticité");

        for (Batiment batiment : batiments) {
            table.addCell(createCell(batiment.getNom(), regularFont));
            table.addCell(createCell(batiment.getAdresse(), regularFont));
            table.addCell(createCell(batiment.getVille() != null ? batiment.getVille() : "-", regularFont));
            table.addCell(createCell(batiment.getTypeBatiment() != null ? batiment.getTypeBatiment().name() : "-", regularFont));
            table.addCell(createCell(batiment.getCriticite() != null ? batiment.getCriticite().name() : "-", regularFont));
        }

        document.add(table);
        addSummary(document, regularFont, "Total bâtiments: " + batiments.size());

        document.close();
        log.info("PDF bâtiments généré: {} entrées", batiments.size());
        return baos.toByteArray();
    }

    private void addTitle(Document document, String titre, PdfFont font) {
        Paragraph title = new Paragraph(titre)
                .setFont(font)
                .setFontSize(18)
                .setFontColor(PRIMARY_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(title);
    }

    private void addGenerationInfo(Document document, PdfFont font) {
        Paragraph info = new Paragraph("Généré le " + LocalDateTime.now().format(DATETIME_FORMAT))
                .setFont(font)
                .setFontSize(10)
                .setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(info);
    }

    private void addTableHeader(Table table, PdfFont font, String... headers) {
        for (String header : headers) {
            Cell cell = new Cell()
                    .add(new Paragraph(header).setFont(font).setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(HEADER_BG)
                    .setPadding(5);
            table.addHeaderCell(cell);
        }
    }

    private Cell createCell(String content, PdfFont font) {
        return new Cell()
                .add(new Paragraph(content).setFont(font).setFontSize(9))
                .setPadding(5);
    }

    private void addSummary(Document document, PdfFont font, String summary) {
        Paragraph summaryP = new Paragraph(summary)
                .setFont(font)
                .setFontSize(10)
                .setMarginTop(20)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(summaryP);
    }
}
