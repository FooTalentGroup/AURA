package com.clinica.aura.modules.medical_notes_files.controller;
import com.clinica.aura.modules.medical_notes_files.service.MedicalNotesFilesService;
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
/**
 * Controlador encargado de exponer el endpoint para la generación y descarga
 * de reportes médicos en formato PDF, basados en el DNI del paciente.
 */

@Tag(name = "Medical Notes Files", description = "Versión futura esta por fuera del MVP")
@RestController
@RequestMapping("/report")
public class MedicalNotesFilesController {

    private final MedicalNotesFilesService reportService;

    public MedicalNotesFilesController(MedicalNotesFilesService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/{dni}", produces = "application/pdf")
    @Operation(
            summary = "Descargar PDF por DNI del paciente",
            description = "A partir del DNI de un paciente, se genera un archivo PDF con sus datos personales, " +
                    "información de seguimiento y diagnóstico (si están disponibles en la base de datos). " +
                    "Una vez ejecutado el método, se mostrará un botón con la leyenda 'Download file' para descargar el PDF generado."
    )
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