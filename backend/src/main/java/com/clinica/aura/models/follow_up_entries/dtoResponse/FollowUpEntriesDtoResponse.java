package com.clinica.aura.models.follow_up_entries.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowUpEntriesDtoResponse {

    @Schema
    private Long id;
    //medicalRecordId
    @Schema
    private Date date;

    @Schema
    private String notes;

    @Schema
    private Long medicalRecordId;
}
