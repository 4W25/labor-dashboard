package com.tsmc.labor_manpower_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelCardStatusDto {
    private LocalDate date;
    private String action; // e.g. "關卡", "復卡"
    private String reason;
}
