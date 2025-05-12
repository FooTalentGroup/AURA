package com.clinica.aura.models.medical_notes_files.controller;
import com.clinica.aura.models.medical_notes_files.service.MedicalNotesFilesService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class MedicalNotesFilesController {

    private final MedicalNotesFilesService reportService;

    public MedicalNotesFilesController(MedicalNotesFilesService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/{dni}", produces = "application/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String dni) {
        String tituloReporte = "Reporte Médico";
        byte[] pdf = reportService.generatePdfReport(dni, tituloReporte);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(tituloReporte + ".pdf").build());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}