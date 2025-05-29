package com.clinica.aura.modules.patient.repository;

import com.clinica.aura.modules.patient.model.PatientModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientModel, Long> {

    /**
     * Recupera un paciente según su número de documento nacional de identidad (DNI).
     * @param dni Número de documento del paciente.
     * @return {@link Optional} con el {@link PatientModel} encontrado, o vacío si no existe.
     */
    Optional<PatientModel> findByPersonDniAndDeletedFalse(String dni);


    /**
     * Realiza una búsqueda flexible de pacientes por nombre y/o apellido, permitiendo combinaciones en cualquier orden.
     * Este método utiliza una consulta personalizada que permite:
     * <ul>
     *     <li>Buscar por nombre parcial o completo (insensible a mayúsculas/minúsculas).</li>
     *     <li>Buscar por apellido parcial o completo.</li>
     *     <li>Buscar combinaciones "nombre + apellido" o "apellido + nombre".</li>
     *     <li>Ignorar parámetros nulos o vacíos, lo que permite búsquedas abiertas o con una sola palabra.</li>
     * </ul>
     * Es especialmente útil para búsquedas donde el usuario puede ingresar cualquier orden o combinación de nombre y apellido.
     *
     * @param kw1 Primera palabra clave (name).
     * @param kw2 Segunda palabra clave (lastName).
     * @return Lista de pacientes que coincidan con los criterios especificados.
     */
    @Query("""
    SELECT p FROM PatientModel p
    WHERE (
        (:kw1 IS NULL OR :kw1 = '' OR
         LOWER(p.person.name) LIKE LOWER(CONCAT('%', :kw1, '%')) OR
         LOWER(p.person.lastName) LIKE LOWER(CONCAT('%', :kw1, '%')) OR
         LOWER(CONCAT(p.person.name, ' ', p.person.lastName)) LIKE LOWER(CONCAT('%', :kw1, '%')) OR
         LOWER(CONCAT(p.person.lastName, ' ', p.person.name)) LIKE LOWER(CONCAT('%', :kw1, '%'))
        )
    )
    AND
    (
        (:kw2 IS NULL OR :kw2 = '' OR
         LOWER(p.person.lastName) LIKE LOWER(CONCAT('%', :kw2, '%')) OR
         LOWER(p.person.name) LIKE LOWER(CONCAT('%', :kw2, '%')) OR
         LOWER(CONCAT(p.person.lastName, ' ', p.person.name)) LIKE LOWER(CONCAT('%', :kw2, '%')) OR
         LOWER(CONCAT(p.person.name, ' ', p.person.lastName)) LIKE LOWER(CONCAT('%', :kw2, '%'))
        )
    )
    OR
    (
        (:kw1 IS NOT NULL AND :kw1 != '' AND :kw2 IS NOT NULL AND :kw2 != '' AND (
            LOWER(CONCAT(p.person.name, ' ', p.person.lastName)) = LOWER(CONCAT(:kw1, ' ', :kw2)) OR
            LOWER(CONCAT(p.person.lastName, ' ', p.person.name)) = LOWER(CONCAT(:kw2, ' ', :kw1))
        ))
    )
    AND p.deleted = false
""")
    List<PatientModel> searchByFullName(@Param("kw1") String kw1, @Param("kw2") String kw2);


    /**
     * Busca todos los pacientes activos.
     * @return Lista de pacientes activos.
     */
    @Query("SELECT p FROM PatientModel p WHERE p.deleted = false")
    Page<PatientModel> findAllActive(Pageable pageable);

    Optional<PatientModel> findByIdAndDeletedFalse(Long id);
}
