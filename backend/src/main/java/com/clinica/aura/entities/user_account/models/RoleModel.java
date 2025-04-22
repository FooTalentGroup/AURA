package com.clinica.aura.entities.user_account.models;

import com.clinica.aura.entities.user_account.Enum.EnumRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "roles")
public class RoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private EnumRole enumRole;

    @ManyToMany(fetch = FetchType.EAGER , targetEntity = PermissionModel.class)
    @JoinTable(name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionModel> permissions = new HashSet<>();

    public RoleModel(EnumRole enumRole) {
        this.enumRole = enumRole;
    }
}
