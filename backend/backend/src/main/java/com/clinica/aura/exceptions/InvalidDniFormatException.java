package com.clinica.aura.exceptions;
/**
 * Excepción lanzada cuando el formato del DNI ingresado no cumple con los requisitos esperados.
 * Se utiliza para validar que el DNI contenga exactamente 8 dígitos numéricos antes de realizar una búsqueda.
 *
 * Esta excepción se lanza comúnmente en el servicio cuando el usuario envía un DNI que contiene
 * letras, símbolos, espacios u otra cantidad de dígitos diferente de 8.
 */
public class InvalidDniFormatException extends RuntimeException {
    public InvalidDniFormatException(String message) {
        super(message);
    }
}

