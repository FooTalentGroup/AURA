package com.clinica.aura.modules.user_account.models;

import com.clinica.aura.modules.person.model.PersonModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;


    @CreationTimestamp
    @Column(name = "register_date", updatable = false)
    private LocalDate registerDate;

    @UpdateTimestamp
    @Column(name = "last_login")
    private LocalDate lastLogin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleModel> roles = new HashSet<>();

    @OneToOne(targetEntity = PersonModel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "person_id", nullable = false)
    private PersonModel person;

    @Column(name = "suspension_end")
    private LocalDateTime suspensionEnd;

    public boolean isEnabled() {
        return suspensionEnd == null || suspensionEnd.isBefore(LocalDateTime.now());
    }

}
