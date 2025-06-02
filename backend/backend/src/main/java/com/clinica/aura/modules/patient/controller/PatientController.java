package com.clinica.aura.modules.patient.controller;



import com.clinica.aura.exceptions.InvalidDniFormatException;
import com.clinica.aura.exceptions.InvalidNameFormatException;
import com.clinica.aura.modules.patient.dto.PatientRequestDto;
import com.clinica.aura.modules.patient.dto.PatientResponseDto;
import com.clinica.aura.modules.patient.service.PatientService;
import com.clinica.aura.util.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST que gestiona las operaciones relacionadas con los pacientes.
 * Proporciona endpoints para registrar, buscar, actualizar y asignar escuelas a pacientes.
 */

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "Endpoints para manejar pacientes")
@PreAuthorize("hasRole('ADMIN') or hasRole('RECEPTIONIST') or hasRole('PROFESSIONAL')")
public class PatientController {

    private final PatientService patientService;

    /**
     * Registra un nuevo paciente en el sistema.
     * @param authCreateUserDto DTO con los datos del paciente a registrar
     * @return DTO con los datos del paciente creado
     */
    @Operation(summary = "Registrar nuevo paciente", description = """
            Registra un nuevo paciente. El campo professionalIds es opcional y puede dejarse en blanco, pero,
            si se completa, se debe asegurar que contenga IDs de profesionales existentes en la base de datos.
            "Si el paciente tiene obra social, establecer 'hasInsurance' en true y completar 'insuranceName', 'insurancePlan' y 'memberShipNumber'. " +
            "Si el paciente no tiene obra social (es particular), establecer 'hasInsurance' en false, 'insuranceName' como 'Particular', y dejar vacíos " +
            "'insurancePlan' y 'memberShipNumber'.")
            """)
    @PostMapping(value = "/register")
    @Tag(name = "Patient")
    public ResponseEntity<PatientResponseDto> registerPatient(@RequestBody @Valid PatientRequestDto authCreateUserDto) {
        PatientResponseDto response = patientService.createUser(authCreateUserDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * Recupera una lista paginada de pacientes.
     * @param page número de página (comienza en 0)
     * @param size cantidad de registros por página
     * @return respuesta con la lista de pacientes paginados
     */
    @GetMapping
    @Operation(summary = "Filtrar pacientes por paginación", description = "Recupera pacientes con paginación. Recordar" +
            " que la paginación comienza desde la página 0. Ejemplo: page=0 y size=1 trae el primer registro.")
    public ResponseEntity<PaginatedResponse<PatientResponseDto>> getAllPatients
    (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(patientService.getAllPatients(page, size));
    }


    /**
     * Busca un paciente por su ID.
     * @param id identificador único del paciente
     * @return DTO con los datos del paciente
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Devuelve los datos de un paciente específico según su ID.")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }


    /**
     * Actualiza los datos de un paciente existente.
     * @param id      ID del paciente a actualizar
     * @param request DTO con los nuevos datos del paciente
     * @return DTO con los datos actualizados
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente específico. No olvidar " +
            "cambiar los campos de ejemplo; de lo contrario, se guardarán como están en la base de datos al ejecutar la prueba.")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable("id") Long id, @RequestBody @Valid PatientRequestDto request) {
        PatientResponseDto patientResponseDto =  patientService.updatePatient(id, request);
        return new ResponseEntity<>(patientResponseDto, HttpStatus.CREATED);
    }


    /**
     * Busca un paciente por su número de DNI.
     * @param dni número de documento (8 dígitos)
     * @return DTO con los datos del paciente encontrado
     * @throws InvalidDniFormatException si el DNI no tiene el formato esperado
     */
    @GetMapping("/search/dni")
    @Operation(
            summary = "Buscar paciente por DNI",
            description = "Permite buscar un paciente a partir de su DNI. Se requiere ingresar exactamente 8 dígitos numéricos. " +
                    "Si se ingresan letras, guiones u otros caracteres no válidos, el sistema devolverá un error indicando " +
                    "que el formato del DNI es inválido."
    )
    public ResponseEntity<PatientResponseDto> getPatientByDni(@RequestParam String dni) {
        if (!dni.matches("\\d{8}")) {
            throw new InvalidDniFormatException("DNI inválido. Se esperan exactamente 8 dígitos numéricos.");
        }

        PatientResponseDto dto = patientService.getPatientByDni(dni);
        return ResponseEntity.ok(dto);
    }


    /**
     * Busca pacientes por nombre, apellido o ambos.
     * Solo se permiten letras, tildes y espacios.
     * @param name     nombre del paciente (opcional)
     * @param lastName apellido del paciente (opcional)
     * @return lista de pacientes coincidentes
     * @throws InvalidNameFormatException si alguno de los campos contiene números o caracteres especiales
     */
    @GetMapping("/search/name")
    @Operation(
            summary = "Buscar paciente por nombre o apellido",
            description = "Busca un paciente por nombre, apellido o ambos (coincidencia parcial o total). " +
                    "Se debe completar al menos uno de los dos campos. Si se ingresan caracteres especiales o números, " +
                    "el sistema devolverá un error indicando que solo se permiten letras."
    )
    public ResponseEntity<List<PatientResponseDto>> getPatientsByName(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "lastName", required = false) String lastName) {

        name = (name == null || name.trim().isEmpty()) ? null : name.trim();
        lastName = (lastName == null || lastName.trim().isEmpty()) ? null : lastName.trim();

        if (name == null && lastName == null) {
            return ResponseEntity.badRequest().body(null);
        }

        String regex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";

        if ((name != null && !name.matches(regex)) || (lastName != null && !lastName.matches(regex))) {
            throw new InvalidNameFormatException("Los campos 'name' y 'lastName' deben contener solo letras.");
        }

        return ResponseEntity.ok(patientService.getPatientsByName(name,lastName));
    }


    /**
     * Asigna una escuela a un paciente.
     * @param patientId ID del paciente
     * @param schoolId  ID de la escuela a asignar
     * @return mensaje de confirmación
     */
    @PutMapping("/{patient_id}/{school_id}")
    @Operation(summary = "Asignar escuela a paciente", description = "Asigna una escuela a un paciente. Se debe ingresar" +
            " el ID del paciente en el path y, en el cuerpo del request, el ID de la escuela.")
    public ResponseEntity<String> assignSchoolToPatient(@Schema(description = "ID del paciente", example = "1") @PathVariable("patient_id") Long patientId,
                                                        @Schema(description = "ID de la escuela", example = "1", title = "ID de la escuela" ) @RequestBody Long schoolId) {
        patientService.assignSchoolToPatient(patientId, schoolId);
        return ResponseEntity.ok("Escuela asignada al paciente correctamente");
    }


    /**
     * Elimina un paciente como soft delete, pero no lo elimina físicamente de la base de datos.
     * @param id ID del paciente a eliminar
     * @return mensaje de confirmación
     */
    @Operation(
            summary = "Eliminar paciente",
            description = "Marca un paciente como eliminado (soft delete). Se debe ingresar el ID del paciente en el path. El paciente no se elimina físicamente de la base de datos."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePatient(@PathVariable("id") Long id) {
        patientService.deletePatient(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Paciente eliminado correctamente");
        return ResponseEntity.ok(response);
    }

}
