package com.clinica.aura.models.person.model;





import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "person")
public class PersonModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dni;
    private String name;
    private String lastName;
    // private String photoUrl;
    // private LocalDate birthDate;
    private String phoneNumber;
    // private String country;

    /* los campos comentandos son los que pidio Axel que saquemos, Ux y Analista estaban en la
     * reunión y dieron el okey también.
     * La fecha de nacimiento solo estará en Paciente por pedidos de los mismos integrantes*/
}
