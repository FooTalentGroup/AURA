package com.clinica.aura.models.person.model;





import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private String address;
    private LocalDate birthDate;
    private String phoneNumber;
    private String locality;
    private String cuil;
    // private String country;


}
