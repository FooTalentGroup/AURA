package com.clinica.aura.exceptions;
/**
 * Excepción lanzada cuando los campos de búsqueda de nombre o apellido contienen caracteres no válidos.
 * Esta excepción se utiliza en validaciones previas a la búsqueda de pacientes por nombre o apellido.
 * Se espera que los valores contengan únicamente letras, incluyendo letras acentuadas y espacios.
 * Si se ingresan números, símbolos u otros caracteres especiales, se lanza esta excepción.
 * Esta clase es utilizada por el controlador para retornar una respuesta con código HTTP 400 (Bad Request).
 *
 * @see com.clinica.aura.modules.patient.controller.PatientController#getPatientsByName(String, String)
 */
public class InvalidNameFormatException extends RuntimeException {
    public InvalidNameFormatException(String message) {
        super(message);
    }
}

