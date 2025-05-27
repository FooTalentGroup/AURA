package com.clinica.aura.modules.patient.service;

import com.clinica.aura.config.jwt.JwtUtils;

import com.clinica.aura.exceptions.DniAlreadyExistsException;
import com.clinica.aura.exceptions.EmailAlreadyExistsException;
import com.clinica.aura.exceptions.PatientNotFoundException;
import com.clinica.aura.exceptions.SchoolNotFoundException;

import com.clinica.aura.exceptions.*;

import com.clinica.aura.modules.medical_records.repository.MedicalRecordsRepository;
import com.clinica.aura.modules.patient.dto.PatientRequestDto;
import com.clinica.aura.modules.patient.dto.PatientResponseDto;
import com.clinica.aura.modules.patient.model.PatientModel;
import com.clinica.aura.modules.patient.repository.PatientRepository;
import com.clinica.aura.modules.person.model.PersonModel;
import com.clinica.aura.modules.person.repository.PersonRepository;
import com.clinica.aura.modules.professional.model.ProfessionalModel;
import com.clinica.aura.modules.professional.repository.ProfessionalRepository;
import com.clinica.aura.modules.school.model.SchoolModel;
import com.clinica.aura.modules.school.repository.SchoolRepository;
import com.clinica.aura.modules.user_account.Enum.EnumRole;
import com.clinica.aura.modules.user_account.models.RoleModel;
import com.clinica.aura.modules.user_account.models.UserModel;
import com.clinica.aura.modules.user_account.repository.RoleRepository;
import com.clinica.aura.modules.user_account.repository.UserRepository;
import com.clinica.aura.util.PaginatedResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;
import java.util.Optional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordsRepository medicalRecordsRepository;
    private final ProfessionalRepository professionalRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SchoolRepository schoolRepository;

    /**
     * Crea un nuevo paciente en el sistema junto con su usuario asociado.
     * Este método realiza múltiples validaciones previas:
     * <ul>
     *     <li>Verifica que el correo electrónico no esté ya registrado.</li>
     *     <li>Verifica que el DNI no esté ya registrado.</li>
     *     <li>Valida la existencia del rol "PATIENT".</li>
     *     <li>Valida la existencia de los profesionales referenciados (si los hay).</li>
     *     <li>Valida la existencia de la escuela referenciada (si hay).</li>
     * </ul>
     * Luego crea las entidades {@code PersonModel}, {@code PatientModel} y {@code UserModel},
     * las asocia entre sí, y las persiste en la base de datos.
     *
     * @param authCreateUserDto DTO con los datos del nuevo paciente.
     * @return {@link PatientResponseDto} con los datos del paciente creado.
     * @throws EmailAlreadyExistsException Si el correo electrónico ya está registrado.
     * @throws DniAlreadyExistsException Si el DNI ya existe en el sistema.
     * @throws IllegalArgumentException Si no se encuentra el rol "PATIENT" en la base de datos.
     * @throws ProfessionalNotFoundException Si uno o más IDs de profesionales no existen.
     * @throws SchoolNotFoundException Si el ID de la escuela no existe en la base de datos.
     * Calcula la edad actual del paciente usando la fecha de nacimiento.
     */
    @Transactional
    public PatientResponseDto createUser(@Valid PatientRequestDto authCreateUserDto) {


        String email = authCreateUserDto.getEmail();
        String username = authCreateUserDto.getName();
        String lastName = authCreateUserDto.getLastName();
        String dni = authCreateUserDto.getDni();
        String phoneNumber = authCreateUserDto.getPhoneNumber();
        LocalDate birthDate = authCreateUserDto.getBirthDate();
        String address = authCreateUserDto.getAddress();
        String tutorName = authCreateUserDto.getTutorName();
        String relationToPatient = authCreateUserDto.getRelationToPatient();
        String genre = authCreateUserDto.getGenre();
        String insurancePlan = authCreateUserDto.getInsurancePlan();
        String memberShipNumber = authCreateUserDto.getMemberShipNumber();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("El correo " + email + " ya existe en la base de datos.");
        }

        if (personRepository.findByDni(dni).isPresent()) {
            throw new DniAlreadyExistsException("El DNI " + dni + " ya está registrado en la base de datos.");
        }

        Optional<RoleModel> professionalRole = roleRepository.findByEnumRole(EnumRole.PATIENT);
        if (professionalRole.isEmpty()) {
            throw new IllegalArgumentException("El rol especificado no está configurado en la base de datos.");
        }
        Set<RoleModel> roleEntities = Set.of(professionalRole.get());

        PersonModel personEntity = PersonModel.builder()
                .dni(dni)
                .name(username)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .birthDate(birthDate)
                .build();

        PatientModel patientModel = PatientModel.builder()
                .person(personEntity)
                .hasInsurance(authCreateUserDto.isHasInsurance())
                .insuranceName(authCreateUserDto.getInsuranceName())
                .insurancePlan(insurancePlan)
                .address(address)
                .tutorName(tutorName)
                .relationToPatient(relationToPatient)
                .genre(genre)
                .memberShipNumber(memberShipNumber)
                .build();


        List<Long> profIds = authCreateUserDto.getProfessionalIds();
        if (profIds != null && !profIds.isEmpty()) {
            List<ProfessionalModel> professionals = professionalRepository.findAllById(profIds);
            List<Long> existingProfIds = professionals.stream().map(ProfessionalModel::getId).toList();
            List<Long> nonExistingProfIds = new ArrayList<>(profIds);
            nonExistingProfIds.removeAll(existingProfIds);
            if (!nonExistingProfIds.isEmpty()) {
                throw new ProfessionalNotFoundException("Los siguientes profesionales no fueron encontrados: " + nonExistingProfIds);
            }
            patientModel.setProfessionals(professionals);
        } else {
            patientModel.setProfessionals(new ArrayList<>());
        }

        Long schoolId = authCreateUserDto.getSchoolId();
        SchoolModel school = null;

        if (schoolId != null) {
            school = entityManager.find(SchoolModel.class, schoolId);
            if (school == null) {
                throw new SchoolNotFoundException("La escuela con ID " + schoolId + " no fue encontrada.");
            }
        }
        patientModel.setSchoolModel(school);

        patientRepository.save(patientModel);

        int currentAge= calculatorAge(patientModel.getId(), birthDate);

        UserModel userEntity = UserModel.builder()
                .email(email)
                .password("")
                .roles(roleEntities)
                .person(personEntity)
                .build();

        userRepository.save(userEntity);


        return PatientResponseDto.builder()
                .id(personEntity.getId())
                .email(userEntity.getEmail())
                .name(personEntity.getName())
                .lastName(personEntity.getLastName())
                .phoneNumber(personEntity.getPhoneNumber())
                .birthDate(personEntity.getBirthDate())
                .dni(personEntity.getDni())
                .hasInsurance(patientModel.isHasInsurance())
                .insuranceName(patientModel.getInsuranceName())
                .insurancePlan(patientModel.getInsurancePlan())
                .memberShipNumber(patientModel.getMemberShipNumber())
                .address(patientModel.getAddress())
                .tutorName(patientModel.getTutorName())
                .relationToPatient(patientModel.getRelationToPatient())
                .genre(patientModel.getGenre())
                .professionalIds(
                        patientModel.getProfessionals() != null
                                ? patientModel.getProfessionals().stream().map(ProfessionalModel::getId).toList()
                                : Collections.emptyList()
                )
                .schoolId(patientModel.getSchoolModel() != null ? patientModel.getSchoolModel().getId() : null)
                .age(currentAge)
                .build();
    }

    /**
     * Asocia una escuela existente a un paciente específico.
     * Este método busca al paciente por su ID y luego valida que la escuela con el ID dado exista.
     * Si ambas entidades existen, se asigna la escuela al paciente y se actualiza en la base de datos.
     * @param patientId ID del paciente al que se le asignará la escuela.
     * @param schoolId  ID de la escuela que se desea asignar al paciente.
     * @throws PatientNotFoundException Si no se encuentra un paciente con el ID proporcionado.
     * @throws SchoolNotFoundException Si no se encuentra una escuela con el ID proporcionado.
     */
    public void assignSchoolToPatient(Long patientId, Long schoolId) {
        PatientModel patient = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con ID: " + patientId));
        SchoolModel school = entityManager.find(SchoolModel.class, schoolId);
        if (school == null) {
            throw new SchoolNotFoundException("La escuela con ID " + schoolId + " no fue encontrada.");
        }
        patient.setSchoolModel(school);
        patientRepository.save(patient);
    }

    /**
     * Recupera una lista paginada de todos los pacientes registrados en el sistema.
     * Este método consulta la base de datos utilizando paginación y transforma cada entidad
     * {@code PatientModel} en un {@code PatientResponseDto}. También incluye información asociada,
     * como datos personales, usuario (si existe), escuela y profesionales.
     * @param page Número de página (empezando desde 0).
     * @param size Cantidad de elementos por página.
     * @return {@link PaginatedResponse} que contiene una lista de {@link PatientResponseDto} junto con
     * información de paginación como número de página, tamaño, total de páginas y total de elementos.
     */
    public PaginatedResponse<PatientResponseDto> getAllPatients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientModel> patientsPage = patientRepository.findAll(pageable);

        List<PatientResponseDto> patientResponseDtos = patientsPage.getContent().stream()
                .map(patient -> {
                    PersonModel person = patient.getPerson();
                    Optional<UserModel> userOptional = userRepository.findByPerson(person);

                    PatientResponseDto.PatientResponseDtoBuilder dtoBuilder = PatientResponseDto.builder()
                            .id(patient.getId())
                            .name(person.getName())
                            .lastName(person.getLastName())
                            .phoneNumber(person.getPhoneNumber())
                            .birthDate(person.getBirthDate())
                            .dni(person.getDni())
                            .email(userOptional.map(UserModel::getEmail).orElse(null))
                            .hasInsurance(patient.isHasInsurance())
                            .insuranceName(patient.getInsuranceName())
                            .insurancePlan(patient.getInsurancePlan())
                            .memberShipNumber(patient.getMemberShipNumber())
                            .address(patient.getAddress())
                            .tutorName(patient.getTutorName())
                            .relationToPatient(patient.getRelationToPatient())
                            .genre(patient.getGenre())
                            .age(calculateAgeFromBirthDate(person.getBirthDate()));

                    if (patient.getSchoolModel() != null) {
                        dtoBuilder.schoolId(patient.getSchoolModel().getId());
                    }

                    if (patient.getProfessionals() != null) {
                        List<Long> professionalIds = patient.getProfessionals().stream()
                                .map(ProfessionalModel::getId)
                                .toList();
                        dtoBuilder.professionalIds(professionalIds);
                    }

                    return dtoBuilder.build();
                })
                .toList();

        return new PaginatedResponse<>(
                patientResponseDtos,
                patientsPage.getNumber(),
                patientsPage.getSize(),
                patientsPage.getTotalPages(),
                patientsPage.getTotalElements()
        );
    }


    /**
     * Recupera la información detallada de un paciente específico a partir de su ID.
     * Este método busca al paciente en la base de datos. Si no se encuentra, lanza una excepción.
     * También obtiene la persona asociada, el usuario (si existe), los profesionales vinculados y
     * la escuela, si está asignada. Además, calcula la edad actual del paciente.
     * @param id ID del paciente a buscar.
     * @return {@link PatientResponseDto} con los datos completos del paciente, incluyendo su información
     * personal, detalles de seguro, profesionales vinculados, escuela (si corresponde) y edad.
     * @throws PatientNotFoundException Si no se encuentra un paciente con el ID proporcionado.
     */
    public PatientResponseDto getPatientById(Long id) {
        var patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con ID: " + id));

        var person = patient.getPerson();
        Long idPatient = patient.getId();
        LocalDate birthDate = personRepository.findBirthDateById(id);
        int currentAge= calculatorAge(idPatient, birthDate);
        var user = userRepository.findByPerson(person).orElse(null);

        List<Long> professionalIds = null;
        if (patient.getProfessionals() != null) {
            professionalIds = patient.getProfessionals().stream()
                    .map(prof -> prof.getId())
                    .toList();
        }

        PatientResponseDto.PatientResponseDtoBuilder dtoBuilder = PatientResponseDto.builder()
                .id(person.getId())
                .name(person.getName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                .birthDate(person.getBirthDate())
                .dni(person.getDni())
                .email(user != null ? user.getEmail() : null)
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .insurancePlan(patient.getInsurancePlan())
                .memberShipNumber(patient.getMemberShipNumber())
                .address(patient.getAddress())
                .tutorName(patient.getTutorName())
                .relationToPatient(patient.getRelationToPatient())
                .professionalIds(professionalIds)
                .age(currentAge)
                .genre(patient.getGenre());


        if (patient.getSchoolModel() != null) {
            dtoBuilder.schoolId(patient.getSchoolModel().getId());
        }

        return dtoBuilder.build();
    }

    /**
     * Actualiza la información de un paciente existente, incluyendo sus datos personales, información médica,
     * profesionales asociados, escuela asignada y el correo electrónico del usuario vinculado.
     * <p>
     * Realiza varias validaciones:
     * <ul>
     *     <li>Verifica que el paciente con el ID especificado exista.</li>
     *     <li>Verifica que la escuela referenciada exista, si se proporciona un ID de escuela.</li>
     *     <li>Valida la existencia de los profesionales indicados por ID.</li>
     *     <li>Verifica que el usuario vinculado a la persona también exista.</li>
     * </ul>
     * Luego actualiza y guarda los cambios en la base de datos.
     *
     * @param id ID del paciente que se desea actualizar.
     * @param requestDto DTO con los datos actualizados del paciente.
     * @return {@link PatientResponseDto} con la información actualizada del paciente.
     * @throws PatientNotFoundException Si no se encuentra un paciente con el ID proporcionado.
     * @throws SchoolNotFoundException Si el ID de la escuela proporcionado no existe.
     * @throws ProfessionalNotFoundException Si uno o más profesionales especificados no existen.
     * @throws UserNotFoundException Si no se encuentra el usuario vinculado a la persona del paciente.
     */
    public PatientResponseDto updatePatient(Long id, PatientRequestDto requestDto) {

        var patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con ID: " + id));

        var person = patient.getPerson();


        person.setName(requestDto.getName());
        person.setLastName(requestDto.getLastName());
        person.setPhoneNumber(requestDto.getPhoneNumber());
        person.setDni(requestDto.getDni());
        person.setBirthDate(requestDto.getBirthDate());

        patient.setInsuranceName(requestDto.getInsuranceName());
        patient.setHasInsurance(requestDto.isHasInsurance());
        patient.setInsurancePlan(requestDto.getInsurancePlan());
        patient.setMemberShipNumber(requestDto.getMemberShipNumber());
        patient.setAddress(requestDto.getAddress());
        patient.setTutorName(requestDto.getTutorName());
        patient.setRelationToPatient(requestDto.getRelationToPatient());
        patient.setGenre(requestDto.getGenre());

        if (requestDto.getSchoolId() != null) {
            SchoolModel school = schoolRepository.findById(requestDto.getSchoolId())
                    .orElseThrow(() -> new SchoolNotFoundException("Escuela no encontrada con ID: " + requestDto.getSchoolId()));
            patient.setSchoolModel(school);
        }else{
            patient.setSchoolModel(null);
        }

        List<Long> profIds = requestDto.getProfessionalIds();
        if (profIds != null && !profIds.isEmpty()) {
            List<ProfessionalModel> professionals = professionalRepository.findAllById(profIds);
            List<Long> existingProfIds = professionals.stream().map(ProfessionalModel::getId).toList();
            List<Long> nonExistingProfIds = new ArrayList<>(profIds);
            nonExistingProfIds.removeAll(existingProfIds);
            if (!nonExistingProfIds.isEmpty()) {
                throw new ProfessionalNotFoundException("Los siguientes profesionales no fueron encontrados: " + nonExistingProfIds);
            }
            patient.setProfessionals(professionals);
        } else {
            patient.setProfessionals(new ArrayList<>());
        }

        Optional<UserModel> optionalUser = userRepository.findByPerson(person);
        UserModel user = optionalUser.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado para el paciente con ID: " + id));

        user.setEmail(requestDto.getEmail());

        patientRepository.save(patient);
        userRepository.save(user);

        List<Long> professionalIds = null;
        if (patient.getProfessionals() != null) {
            professionalIds = patient.getProfessionals().stream()
                    .map(prof -> prof.getId())
                    .toList();
        }


        Long idPatient = patient.getId();
        LocalDate birthDate = personRepository.findBirthDateById(id);

        int currentAge= calculatorAge(idPatient, birthDate);

        return PatientResponseDto.builder()
                .id(person.getId())
                .name(person.getName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                .birthDate(person.getBirthDate())
                .dni(person.getDni())
                .email(user.getEmail())
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .insurancePlan(patient.getInsurancePlan())
                .memberShipNumber(patient.getMemberShipNumber())
                .address(patient.getAddress())
                .tutorName(patient.getTutorName())
                .relationToPatient(patient.getRelationToPatient())
                .professionalIds(professionalIds)
                .schoolId(patient.getSchoolModel() != null ? patient.getSchoolModel().getId() : null)
                .genre(patient.getGenre())
                .age(currentAge)
                .build();
    }


    /**
     * Recupera la información completa de un paciente a partir de su número de DNI.
     * Este método realiza lo siguiente:
     * <ul>
     *     <li>Busca el paciente mediante el DNI asociado a la persona.</li>
     *     <li>Obtiene los datos personales, información médica y vínculos (escuela y profesionales).</li>
     *     <li>Calcula la edad actual del paciente usando la fecha de nacimiento.</li>
     *     <li>Retorna un objeto {@link PatientResponseDto} con toda la información consolidada.</li>
     * </ul>
     * @param dni Número de Documento Nacional de Identidad (DNI) del paciente a buscar.
     * @return {@link PatientResponseDto} con los datos del paciente correspondiente al DNI proporcionado.
     * @throws PatientNotFoundException Si no se encuentra ningún paciente con el DNI especificado.
     */
    public PatientResponseDto getPatientByDni(String dni) {
        var patient = patientRepository.findByPersonDni(dni)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con DNI: " + dni));

        var person = patient.getPerson();
        var user = userRepository.findByPerson(person).orElse(null);

        List<Long> professionalIds = null;
        if (patient.getProfessionals() != null) {
            professionalIds = patient.getProfessionals().stream()
                    .map(prof -> prof.getId())
                    .toList();
        }

        Long schoolId = patient.getSchoolModel() != null ? patient.getSchoolModel().getId() : null;


        Long idPatient = patient.getId();
        LocalDate birthDate = personRepository.findBirthDateByDni(dni);

        int currentAge= calculatorAge(idPatient, birthDate);

        return PatientResponseDto.builder()
                .id(patient.getId())
                .name(person.getName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                .birthDate(person.getBirthDate())
                .dni(person.getDni())
                .email(user != null ? user.getEmail() : null)
                .insuranceName(patient.getInsuranceName())
                .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                .insurancePlan(patient.getInsurancePlan())
                .memberShipNumber(patient.getMemberShipNumber())
                .address(patient.getAddress())
                .tutorName(patient.getTutorName())
                .relationToPatient(patient.getRelationToPatient())
                .professionalIds(professionalIds)
                .genre(patient.getGenre())
                .schoolId(schoolId)
                .age(currentAge)
                .build();
    }


    /**
     * Busca y devuelve una lista de pacientes que coincidan con el nombre y apellido proporcionados.
     * Este método:
     * <ul>
     *     <li>Consulta la base de datos por pacientes cuyo nombre y apellido coincidan (búsqueda personalizada).</li>
     *     <li>Mapea cada entidad encontrada a un {@link PatientResponseDto}, incluyendo datos personales,
     *         profesionales asignados, escuela, y edad calculada.</li>
     * </ul>
     * @param name      Nombre del paciente.
     * @param lastName Apellido del paciente.
     * @return Lista de {@link PatientResponseDto} con los pacientes encontrados.
     * @throws PatientNotFoundException Si no se encuentra ningún paciente con el nombre y apellido especificados.
     */
    public List<PatientResponseDto> getPatientsByName(String name, String lastName) {
        List<PatientModel> patients = patientRepository.searchByFullName(name, lastName);

        if (patients.isEmpty()) {
            throw new PatientNotFoundException("No se encontraron pacientes con el nombre: " + name);
        }

        return patients.stream().map(patient -> {
            PersonModel person = patient.getPerson();
            var user = userRepository.findByPerson(person).orElse(null);

            List<Long> professionalIds = null;
            if (patient.getProfessionals() != null) {
                professionalIds = patient.getProfessionals().stream()
                        .map(prof -> prof.getId())
                        .toList();
            }

            Long schoolId = patient.getSchoolModel() != null ? patient.getSchoolModel().getId() : null;

            return PatientResponseDto.builder()
                    .id(patient.getId())
                    .name(person.getName())
                    .lastName(person.getLastName())
                    .phoneNumber(person.getPhoneNumber())
                    .birthDate(person.getBirthDate())
                    .dni(person.getDni())
                    .email(user != null ? user.getEmail() : null)
                    .insuranceName(patient.getInsuranceName())
                    .hasInsurance(patient.getInsuranceName() != null && !patient.getInsuranceName().isBlank())
                    .insurancePlan(patient.getInsurancePlan())
                    .memberShipNumber(patient.getMemberShipNumber())
                    .address(patient.getAddress())
                    .tutorName(patient.getTutorName())
                    .relationToPatient(patient.getRelationToPatient())
                    .professionalIds(professionalIds)
                    .genre(patient.getGenre())
                    .schoolId(schoolId)
                    .age(calculateAgeFromBirthDate(person.getBirthDate()))
                    .build();
        }).toList();
    }

    /**
     * Calcula la edad actual de un paciente a partir de su fecha de nacimiento.
     * Este método también verifica que el paciente exista en la base de datos mediante su ID.
     * Si el paciente no existe, lanza una excepción.
     * @param id  ID del paciente que se desea validar en la base de datos.
     * @param age Fecha de nacimiento del paciente.
     * @return Edad del paciente en años.
     * @throws PatientNotFoundException Si no se encuentra un paciente con el ID proporcionado.
     */
    private int calculatorAge(Long id, LocalDate age){
        personRepository.findById(id).orElseThrow(()->
                new PatientNotFoundException("El paciente no se encuentra en la base de datos"));
        LocalDate currentDate = LocalDate.now();
        return Period.between(age, currentDate).getYears();
    }

    /**
     * Calcula la edad actual en años a partir de una fecha de nacimiento.
     * Este método es utilizado en funcionalidades de listado y búsqueda, donde pueden retornar múltiples resultados.
     * Si la fecha de nacimiento es {@code null}, retorna 0.
     * @param birthDate Fecha de nacimiento del paciente.
     * @return Edad del paciente en años. Retorna 0 si la fecha de nacimiento es {@code null}.
     */
    private int calculateAgeFromBirthDate(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

}


