package com.mainbat.controller;

import com.mainbat.model.batiment.Batiment;
import com.mainbat.model.equipement.Equipement;
import com.mainbat.model.intervention.Intervention;
import com.mainbat.repository.BatimentRepository;
import com.mainbat.repository.EquipementRepository;
import com.mainbat.repository.InterventionRepository;
import com.mainbat.service.export.ExcelExportService;
import com.mainbat.service.export.PdfExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
@Tag(name = "Export", description = "Export de rapports PDF et Excel")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'GESTIONNAIRE', 'SUPERVISEUR')")
public class ExportController {

    private final PdfExportService pdfExportService;
    private final ExcelExportService excelExportService;
    private final InterventionRepository interventionRepository;
    private final EquipementRepository equipementRepository;
    private final BatimentRepository batimentRepository;

    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");

    // ============ INTERVENTIONS ============

    @GetMapping("/interventions/pdf")
    @Operation(summary = "Export PDF des interventions")
    public ResponseEntity<byte[]> exportInterventionsPdf() throws IOException {
        List<Intervention> interventions = interventionRepository.findAll();
        byte[] pdf = pdfExportService.exportInterventionsToPdf(interventions, "Rapport des Interventions");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=interventions_" + 
                        LocalDateTime.now().format(FILE_DATE_FORMAT) + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/interventions/excel")
    @Operation(summary = "Export Excel des interventions")
    public ResponseEntity<byte[]> exportInterventionsExcel() throws IOException {
        List<Intervention> interventions = interventionRepository.findAll();
        byte[] excel = excelExportService.exportInterventionsToExcel(interventions, "Rapport des Interventions");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=interventions_" + 
                        LocalDateTime.now().format(FILE_DATE_FORMAT) + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel);
    }

    // ============ EQUIPEMENTS ============

    @GetMapping("/equipements/pdf")
    @Operation(summary = "Export PDF des équipements")
    public ResponseEntity<byte[]> exportEquipementsPdf() throws IOException {
        List<Equipement> equipements = equipementRepository.findAll();
        byte[] pdf = pdfExportService.exportEquipementsToPdf(equipements, "Inventaire des Équipements");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=equipements_" + 
                        LocalDateTime.now().format(FILE_DATE_FORMAT) + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/equipements/excel")
    @Operation(summary = "Export Excel des équipements")
    public ResponseEntity<byte[]> exportEquipementsExcel() throws IOException {
        List<Equipement> equipements = equipementRepository.findAll();
        byte[] excel = excelExportService.exportEquipementsToExcel(equipements, "Inventaire des Équipements");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=equipements_" + 
                        LocalDateTime.now().format(FILE_DATE_FORMAT) + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel);
    }

    // ============ BATIMENTS ============

    @GetMapping("/batiments/pdf")
    @Operation(summary = "Export PDF des bâtiments")
    public ResponseEntity<byte[]> exportBatimentsPdf() throws IOException {
        List<Batiment> batiments = batimentRepository.findAll();
        byte[] pdf = pdfExportService.exportBatimentsToPdf(batiments, "Liste des Bâtiments");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=batiments_" + 
                        LocalDateTime.now().format(FILE_DATE_FORMAT) + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/batiments/excel")
    @Operation(summary = "Export Excel des bâtiments")
    public ResponseEntity<byte[]> exportBatimentsExcel() throws IOException {
        List<Batiment> batiments = batimentRepository.findAll();
        byte[] excel = excelExportService.exportBatimentsToExcel(batiments, "Liste des Bâtiments");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=batiments_" + 
                        LocalDateTime.now().format(FILE_DATE_FORMAT) + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel);
    }

    // ============ EQUIPEMENTS EN PANNE ============

    @GetMapping("/equipements-en-panne/pdf")
    @Operation(summary = "Export PDF des équipements en panne")
    public ResponseEntity<byte[]> exportEquipementsEnPannePdf() throws IOException {
        List<Equipement> equipements = equipementRepository.findEquipementsEnPanne();
        byte[] pdf = pdfExportService.exportEquipementsToPdf(equipements, "Équipements en Panne");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=equipements_panne_" + 
                        LocalDateTime.now().format(FILE_DATE_FORMAT) + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/equipements-en-panne/excel")
    @Operation(summary = "Export Excel des équipements en panne")
    public ResponseEntity<byte[]> exportEquipementsEnPanneExcel() throws IOException {
        List<Equipement> equipements = equipementRepository.findEquipementsEnPanne();
        byte[] excel = excelExportService.exportEquipementsToExcel(equipements, "Équipements en Panne");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=equipements_panne_" + 
                        LocalDateTime.now().format(FILE_DATE_FORMAT) + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel);
    }
}
