package com.attendance.scheduler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminDTO {

    @NotNull
    private Long id;

    @NotNull
    private String adminId;

    @NotNull
    private String adminPassword;
}
