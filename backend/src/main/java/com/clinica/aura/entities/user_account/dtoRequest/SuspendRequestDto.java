package com.clinica.aura.entities.user_account.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuspendRequestDto {
    private int duration;
    private TimeUnit unit;

    public enum TimeUnit {
        HOURS, DAYS, WEEKS, MONTHS
    }
}
