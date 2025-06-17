package com.tsmc.labor_manpower_api.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CompanyPersonnelDto {
    private Long personnelId;
    private String name;
    private String workId;
    private String primaryJobTypeName;
    private LocalDate tsmcStartDate;
    private String currentStatus;
    // Add other relevant fields from 4.html "主要人力列表" if needed
}
