package com.clinica.aura.models.medical_notes_files.service;

import com.clinica.aura.models.medical_notes_files.dto.MedicalRecordReporteDTO;
import com.clinica.aura.models.medical_notes_files.repository.MedicalRecordRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

@Service
public class MedicalReportService {

    private final MedicalRecordRepository repository;

    public MedicalReportService(MedicalRecordRepository repository) {
        this.repository = repository;
    }

    public byte[] generatePdfReport(String dni, String tituloReporte) {
        List<MedicalRecordReporteDTO.MedicalRecordReportDTO> records = repository.findReportByDni(dni);

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
            for (MedicalRecordReporteDTO.MedicalRecordReportDTO r : records) {
                if(i>0) break;
                document.add(new Paragraph("Paciente: " + r.getName() + " " + r.getLastName()));
                document.add(new Paragraph("Fecha de nacimiento: " + r.getBirthDate()));
                document.add(new Paragraph("Dni: "+ r.getDni()));
                document.add(new Paragraph("Nombre del tutor: " + r.getTutorName()));
                document.add(new Paragraph("Relación con el paciente: " +r.getRelationToPatient()));
                document.add(new Paragraph("Teléfono: " + (r.getPhoneNumber() != null ? r.getPhoneNumber() : "N/A")));
                document.add(new Paragraph("Email: " + (r.getEmail() != null ? r.getEmail() : "N/A")));

                document.add(new Paragraph("Profesional: " + r.getProfessionalName() + " " + r.getProfessionalLastName() + " (" + r.getSpecialty() + ")"));
                document.add(new Paragraph(" "));
                document.add(new LineSeparator());
                i++;
            }

            for(MedicalRecordReporteDTO.MedicalRecordReportDTO r: records){
                document.add(new Paragraph("Fecha de seguimiento: " + (r.getFollowUpDate() != null ? r.getFollowUpDate().toString() : "N/A")));
                document.add(new Paragraph("Nota: " + (r.getFollowUpNotes() != null ? r.getFollowUpNotes() : "N/A")));
                document.add(new Paragraph(" "));
            }

            document.close();
        } catch (Exception e) {
            // Es importante manejar las excepciones al trabajar con iTextPDF
            System.err.println("Error al generar el PDF: " + e.getMessage());
            // Considera lanzar una excepción personalizada aquí para informar mejor el error
            throw new RuntimeException("Error al generar el reporte PDF", e);
        }
        return out.toByteArray();
   }
}