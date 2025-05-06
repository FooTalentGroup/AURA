package com.clinica.aura.models.follow_up_entries.model;

import com.clinica.aura.models.medical_records.model.MedicalRecordsModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "follow_up_entries")
public class FollowUpEntriesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="medical_record_id", nullable = false)
    private MedicalRecordsModel medicals;

    private Date date;

    private String notes;
}
