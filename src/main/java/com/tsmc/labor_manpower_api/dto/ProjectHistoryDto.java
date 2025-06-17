package com.tsmc.labor_manpower_api.dto;

import lombok.Data;
// import java.time.LocalDate; // Or String for dates as in HTML

@Data
public class ProjectHistoryDto {
    private String projectName;
    // private LocalDate startDate; // Or String
    // private LocalDate endDate;   // Or String
    private String dates; // As per 4.html "2022/03 - 2022/12"
    private Integer participants; // Or personnelCount
    private String status; // e.g. "已完成", "進行中"
}
