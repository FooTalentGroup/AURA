package com.clinica.aura.modules.follow_up_entries.dtoResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowUpEntriesDtoResponse {

    private Long id;

    private String observations;

    private String interventions;

    private String nextSessionInstructions;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private Long professionalId;

    private Long medicalRecordId;
}
