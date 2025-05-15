package com.clinica.aura.modules.school.service;

import com.clinica.aura.exceptions.EmailAlreadyExistsException;
import com.clinica.aura.exceptions.SchoolNotFoundException;
import com.clinica.aura.modules.patient.repository.PatientRepository;
import com.clinica.aura.modules.school.dto.SchoolRequestDto;
import com.clinica.aura.modules.school.dto.SchoolRequestDtoUpdate;
import com.clinica.aura.modules.school.dto.SchoolResponseDto;
import com.clinica.aura.modules.school.model.SchoolModel;
import com.clinica.aura.modules.school.repository.SchoolRepository;
import com.clinica.aura.util.PaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {

    @Autowired
    PatientRepository patientRepository;

    private final SchoolRepository schoolRepository;

    /**
     * Registra una nueva escuela en el sistema.
     *
     * Este método primero valida que el correo electrónico proporcionado en la solicitud
     * no se encuentre ya registrado en la base de datos. Si el email ya existe, lanza una excepción
     * personalizada {@link EmailAlreadyExistsException}.
     *
     * Luego, construye un objeto {@link SchoolModel} con los datos recibidos y lo guarda en la base de datos.
     * Finalmente, retorna un {@link SchoolResponseDto} con la información de la escuela registrada.
     *
     * @param request Objeto {@link SchoolRequestDto} que contiene los datos necesarios para crear la escuela,
     *                incluyendo nombre, correo electrónico y teléfono.
     * @return Un {@link SchoolResponseDto} con los datos de la escuela recién registrada, incluyendo su ID generado.
     * @throws EmailAlreadyExistsException Si el correo electrónico ya está registrado en otra escuela.
     */
    @Transactional
    public SchoolResponseDto createSchool(@Valid SchoolRequestDto request) {

        String emailSchool = request.getEmailSchool();

        if (schoolRepository.findByEmailSchool(emailSchool).isPresent()) {
            throw new EmailAlreadyExistsException("El correo " + emailSchool + " ya existe en la base de datos de escuelas.");
        }

        SchoolModel school = SchoolModel.builder()
                .schoolName(request.getSchoolName())
                .emailSchool(emailSchool)
                .phoneSchool(request.getPhoneSchool())
                .build();

        SchoolModel savedSchool = schoolRepository.save(school);

        return SchoolResponseDto.builder()
                .id(savedSchool.getId())
                .schoolName(savedSchool.getSchoolName())
                .emailSchool(savedSchool.getEmailSchool())
                .phoneSchool(savedSchool.getPhoneSchool())
                .build();
    }


    /**
     * Obtiene una lista paginada de todas las escuelas registradas en el sistema.
     *
     * Este método utiliza los parámetros de paginación proporcionados (número de página y tamaño de página)
     * para recuperar una porción de registros desde la base de datos. Los datos recuperados se transforman
     * en objetos {@link SchoolResponseDto} y se encapsulan en un objeto {@link PaginatedResponse}.
     *
     * @param page Número de página (empezando desde 0) que se desea consultar.
     * @param size Cantidad de registros que se desean obtener por página.
     * @return Un objeto {@link PaginatedResponse} que contiene:
     *         <ul>
     *             <li>Lista de escuelas como {@link SchoolResponseDto}</li>
     *             <li>Número de página actual</li>
     *             <li>Tamaño de página</li>
     *             <li>Total de páginas disponibles</li>
     *             <li>Total de elementos encontrados</li>
     *         </ul>
     */
    public PaginatedResponse<SchoolResponseDto> getAllSchools(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SchoolModel> schoolsPage = schoolRepository.findAll(pageable);

        List<SchoolResponseDto> schoolResponseDtos = schoolsPage.getContent().stream()
                .map(school -> SchoolResponseDto.builder()
                        .id(school.getId())
                        .schoolName(school.getSchoolName())
                        .emailSchool(school.getEmailSchool())
                        .phoneSchool(school.getPhoneSchool())
                        .build())
                .toList();

        return new PaginatedResponse<>(
                schoolResponseDtos,
                schoolsPage.getNumber(),
                schoolsPage.getSize(),
                schoolsPage.getTotalPages(),
                schoolsPage.getTotalElements()
        );
    }


    /**
     * Actualiza los datos de una escuela existente en el sistema.
     *
     * Este método busca una escuela por su ID en la base de datos. Si no se encuentra, lanza una excepción
     * personalizada {@link SchoolNotFoundException}. Si se encuentra, actualiza los campos correspondientes
     * con los valores recibidos en el objeto {@link SchoolRequestDtoUpdate} y guarda los cambios.
     *
     * @param id      ID de la escuela que se desea actualizar.
     * @param request Objeto {@link SchoolRequestDtoUpdate} que contiene los nuevos datos de la escuela:
     *                nombre, correo electrónico y teléfono.
     * @return Un {@link SchoolResponseDto} con la información actualizada de la escuela.
     * @throws SchoolNotFoundException Si no se encuentra una escuela con el ID proporcionado.
     */
    @Transactional
    public SchoolResponseDto updateSchool(Long id, SchoolRequestDtoUpdate request) {
        SchoolModel school = schoolRepository.findById(id)
                .orElseThrow(() -> new SchoolNotFoundException("Escuela no encontrada con ID: " + id));

        school.setSchoolName(request.getSchoolName());
        school.setEmailSchool(request.getEmailSchool());
        school.setPhoneSchool(request.getPhoneSchool());

        SchoolModel updatedSchool = schoolRepository.save(school);

        return SchoolResponseDto.builder()
                .id(updatedSchool.getId())
                .schoolName(updatedSchool.getSchoolName())
                .emailSchool(updatedSchool.getEmailSchool())
                .phoneSchool(updatedSchool.getPhoneSchool())
                .build();
    }

    /**
     * Elimina una escuela del sistema según su ID.
     *
     * Este método primero verifica si existe una escuela con el ID proporcionado.
     * Si no existe, lanza una excepción personalizada {@link SchoolNotFoundException}.
     * Si existe, procede a eliminar el registro correspondiente de la base de datos.
     *
     * @param id ID de la escuela que se desea eliminar.
     * @throws SchoolNotFoundException Si no se encuentra una escuela con el ID especificado.
     */
   @Transactional
   public void deleteSchoolById(Long id) {
       if (!schoolRepository.existsById(id)) {
           throw new SchoolNotFoundException("No se encontró una escuela con el ID: " + id);
       }
       schoolRepository.deleteById(id);
   }


}

