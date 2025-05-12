package com.clinica.aura.models.medical_notes_files.service;

import com.clinica.aura.models.medical_notes_files.dto.MedicalNotesFilesResponseDTO;
import com.clinica.aura.models.medical_notes_files.model.MedicalNotesFilesModel;
import com.clinica.aura.models.medical_notes_files.repository.MedicalNotesFilesRepository;
import com.clinica.aura.models.patient.dto.PatientResponseDto;
import com.clinica.aura.models.patient.model.PatientModel;
import com.clinica.aura.models.patient.repository.PatientRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

@Service
public class MedicalNotesFilesService {

    private final MedicalNotesFilesRepository repository;


    public MedicalNotesFilesService(MedicalNotesFilesRepository repository) {
        this.repository = repository;
    }

    public byte[] generatePdfReport(String dni, String tituloReporte) {
        List<MedicalNotesFilesResponseDTO.MedicalFilesDTO> records = repository.findReportByDni(dni);

        if (records.isEmpty()) {
            throw new RuntimeException("No se encontraron registros para el DNI: " + dni);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // Usa la clase Document de iTextPDF
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Insertar imagen en la esquina superior derecha
            try {
                InputStream is = getClass().getClassLoader().getResourceAsStream("img/auraLogo.jpg");
                if (is == null) {
                    throw new FileNotFoundException("No se encontró el logo en recursos.");
                }
                Image img = Image.getInstance(IOUtils.toByteArray(is));
                img.scaleToFit(100, 100);
                img.setAlignment(Image.ALIGN_RIGHT);
                document.add(img);
            } catch (Exception e) {
                System.out.println("Error cargando imagen: " + e.getMessage());
            }

            // Usa la clase Paragraph de iTextPDF para agregar texto
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph(tituloReporte, tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph(" "));

            // Agrupamos por followUpEntry (aunque estén repetidos otros datos)
            int i=0;
            for (MedicalNotesFilesResponseDTO.MedicalFilesDTO r : records) {
                if(i>0) break;
                document.add(new Paragraph("Paciente: " + r.getName() + " " + r.getLastName()));
                document.add(new Paragraph("Fecha de nacimiento: " + r.getBirthDate()));
                document.add(new Paragraph("Dni: "+ r.getDni()));
                document.add(new Paragraph("Nombre del tutor: " + r.getTutorName()));
                document.add(new Paragraph("Relación con el paciente: " +r.getRelationToPatient()));
                document.add(new Paragraph("Teléfono: " + (r.getPhoneNumber() != null ? r.getPhoneNumber() : "N/A")));
                document.add(new Paragraph("Email: " + (r.getEmail() != null ? r.getEmail() : "N/A")));

                document.add(new Paragraph(" "));
                document.add(new LineSeparator());
                //
                MedicalNotesFilesModel fileRecord = MedicalNotesFilesModel.builder()
                        .file_name(tituloReporte + ".pdf")
                        .patient_name(r.getName())
                        .patient_last_name(r.getLastName())
                        .uploaded_at(LocalDateTime.now())
                        .build();
                repository.save(fileRecord); // Guarda el registro del archivo
                i++;
            }

            for(MedicalNotesFilesResponseDTO.MedicalFilesDTO r: records){
                document.add(new Paragraph("Fecha de seguimiento: " + (r.getCreateDate() != null ? r.getCreateDate().toString() : "N/A")));
                document.add(new Paragraph("Profesional que hizo el seguimiento: "+ r.getFollowUpProfessionalName()+" "+ r.getFollowUpProfessionalLastName()));
                document.add(new Paragraph("Observación: " + (r.getObservations() != null ? r.getObservations() : "N/A")));
                document.add(new Paragraph("Intervensiones: " + (r.getInterventions() != null ? r.getInterventions() : "N/A")));
                document.add(new Paragraph("Instrucciones de la próxima sesión : " + (r.getNextSessionInstructions() != null ? r.getNextSessionInstructions() : "N/A")));

                document.add(new Paragraph(" "));
                document.add(new LineSeparator());
                document.add(new Paragraph("Diagnóstico: "+ r.getDiagnostico()));
                document.add(new Paragraph("Detalles: "+ r.getDetails()));
                document.add(new Paragraph(" "));
            }

            document.close();
            //aca guarda el registro de la descarga
           // Guardar registro en la tabla medical_notes_files esto me diste vos


        } catch (Exception e) {
            // Es importante manejar las excepciones al trabajar con iTextPDF
            System.err.println("Error al generar el PDF: " + e.getMessage());
            // Considera lanzar una excepción personalizada aquí para informar mejor el error
            throw new RuntimeException("Error al generar el reporte PDF", e);
        }
        return out.toByteArray(); // Envía el PDF como respuesta
   }
}