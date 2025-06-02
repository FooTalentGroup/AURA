package com.clinica.aura.modules.medical_notes_files.service;

import com.clinica.aura.modules.medical_notes_files.dto.MedicalNotesFilesResponseDTO;
import com.clinica.aura.modules.medical_notes_files.model.MedicalNotesFilesModel;
import com.clinica.aura.modules.medical_notes_files.repository.MedicalNotesFilesRepository;
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

    /**
     * Constructor de la clase {@link MedicalNotesFilesService}.
     *
     * Inicializa el servicio con una instancia del repositorio {@link MedicalNotesFilesRepository},
     * que será utilizado para acceder y manipular los archivos de notas médicas en la base de datos.
     *
     * @param repository Repositorio de archivos de notas médicas inyectado mediante constructor.
     */
    public MedicalNotesFilesService(MedicalNotesFilesRepository repository) {
        this.repository = repository;
    }

    /**
     * Genera un reporte PDF personalizado con los datos médicos de un paciente, identificado por su DNI.
     * El reporte incluye información básica del paciente (nombre, fecha de nacimiento, tutor, etc.) y un
     * listado de seguimientos médicos obtenidos desde la base de datos. También se incluye el logotipo institucional
     * al inicio del documento, si está disponible en los recursos.
     * Además, se guarda un registro del archivo generado en la base de datos mediante un objeto {@link MedicalNotesFilesModel}.
     * @param dni   del paciente cuyos registros médicos se desean incluir en el reporte.
     * @param tituloReporte  Título que se colocará en el encabezado del documento PDF y como nombre del archivo registrado.
     * @return Un arreglo de bytes que representa el contenido binario del archivo PDF generado.
     * @throws RuntimeException Si no se encuentran registros para el DNI proporcionado o si ocurre un error
     * durante la generación del PDF (por ejemplo, al cargar el logo o escribir el documento).
     */
    public byte[] generatePdfReport(String dni, String tituloReporte) {
        List<MedicalNotesFilesResponseDTO.MedicalFilesDTO> records = repository.findReportByDni(dni);

        if (records.isEmpty()) {
            throw new RuntimeException("No se encontraron registros para el DNI: " + dni);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();


            try {
                InputStream is = getClass().getClassLoader().getResourceAsStream("img/auraLogo.jpg");
                if (is == null) {
                    throw new FileNotFoundException("No se encontró el logotipo en los recursos.");
                }
                Image img = Image.getInstance(IOUtils.toByteArray(is));
                img.scaleToFit(100, 100);
                img.setAlignment(Image.ALIGN_RIGHT);
                document.add(img);
            } catch (Exception e) {
                System.out.println("Error cargando imagen: " + e.getMessage());
            }


            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph(tituloReporte, tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph(" "));


            int i=0;
            for (MedicalNotesFilesResponseDTO.MedicalFilesDTO r : records) {
                if(i>0) break;
                document.add(new Paragraph("Paciente: " + r.getName() + " " + r.getLastName()));
                document.add(new Paragraph("Fecha de nacimiento: " + r.getBirthDate()));
                document.add(new Paragraph("DNI: "+ r.getDni()));
                document.add(new Paragraph("Nombre del tutor: " + r.getTutorName()));
                document.add(new Paragraph("Relación con el paciente: " +r.getRelationToPatient()));
                document.add(new Paragraph("Teléfono: " + (r.getPhoneNumber() != null ? r.getPhoneNumber() : "N/A")));
                document.add(new Paragraph("Email: " + (r.getEmail() != null ? r.getEmail() : "N/A")));

                document.add(new Paragraph(" "));
                document.add(new LineSeparator());

                MedicalNotesFilesModel fileRecord = MedicalNotesFilesModel.builder()
                        .file_name(tituloReporte + ".pdf")
                        .patient_name(r.getName())
                        .patient_last_name(r.getLastName())
                        .uploaded_at(LocalDateTime.now())
                        .build();
                repository.save(fileRecord);
                i++;
            }

            for(MedicalNotesFilesResponseDTO.MedicalFilesDTO r: records){
                document.add(new Paragraph("Fecha de seguimiento: " + (r.getCreateDate() != null ? r.getCreateDate().toString() : "N/A")));
                document.add(new Paragraph("Profesional que hizo el seguimiento: "+ r.getFollowUpProfessionalName()+" "+ r.getFollowUpProfessionalLastName()));
                document.add(new Paragraph("Observación: " + (r.getObservations() != null ? r.getObservations() : "N/A")));
                document.add(new Paragraph("Intervenciones: " + (r.getInterventions() != null ? r.getInterventions() : "N/A")));
                document.add(new Paragraph("Instrucciones de la próxima sesión: " + (r.getNextSessionInstructions() != null ? r.getNextSessionInstructions() : "N/A")));

                document.add(new Paragraph(" "));
                document.add(new LineSeparator());
                document.add(new Paragraph("Diagnóstico: " + r.getDiagnostico()));
                document.add(new Paragraph("Detalles: " + r.getDetails()));
                document.add(new Paragraph(" "));
            }

            document.close();

        } catch (Exception e) {

            System.err.println("Error al generar el PDF: " + e.getMessage());

            throw new RuntimeException("Error al generar el reporte PDF", e);
        }
        return out.toByteArray();
    }

}