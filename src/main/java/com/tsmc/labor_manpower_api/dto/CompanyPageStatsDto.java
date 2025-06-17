package com.tsmc.labor_manpower_api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CompanyPageStatsDto {
    // From 4.html cards
    private Long personnelCount; // 人力數
    private Long maxAttendance;  // 當前最大出工 (Placeholder, using personnelCount for now)
    private Long avgAttendance;  // 平均出工人力 (Placeholder)
    private BigDecimal evaluationScore; // 評鑑分數
    private Long highRiskPersonnelCount; // 高風險健康等級人數
    private String violationPersonnelPercentage; // 違規人員比例 (e.g., "8.3%")
    // Other fields from CompanyDetailDto can be part of this if they are displayed as "stats"
}
