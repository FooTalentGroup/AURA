package com.clinica.aura.models.medical_notes_files.controller;
import com.clinica.aura.models.medical_notes_files.service.MedicalNotesFilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Medical Notes Files", description = "Versión futura esta por fuera del MVP")
@RestController
@RequestMapping("/report")
public class MedicalNotesFilesController {

    private final MedicalNotesFilesService reportService;

    public MedicalNotesFilesController(MedicalNotesFilesService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/{dni}", produces = "application/pdf")
    @Operation(summary = "Descargar pdf por Dni de paciente", description = "A partir de un dni de paciente, " +
            "puede generarse un pdf con sus datos personales, seguimiento y diagnostico si estos están cargados" +
            " en la base de datos. Una vez ingresado el dni del paciente  y ejecutado el método, aparece un botón " +
            "que dice 'Download file' para descargar el pdf generado")
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