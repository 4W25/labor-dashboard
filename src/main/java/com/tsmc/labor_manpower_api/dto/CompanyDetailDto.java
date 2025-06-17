package com.tsmc.labor_manpower_api.dto;

import com.tsmc.labor_manpower_api.model.enums.CompanyType;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CompanyDetailDto {
    // Basic Info (from 4.html left panel)
    private Long companyId;
    private String companyName;
    private CompanyType companyType;
    private String unifiedBusinessNumber; // 統一編號
    private BigDecimal evaluationScore; // 評鑑分數
    private String contactPerson; // 主要聯絡人
    private String contactPhone; // 聯絡電話
    private String contactEmail; // 聯絡郵箱
    private String address; // 公司地址
    private Long parentCompanyId;
    private String parentCompanyName;

    // Stats (from 4.html top cards) - These might come from a separate CompanyPageStatsDto
    // Or be included directly if simple enough
    private Long personnelCount; // 人力數
    private Long maxAttendance; // 最大出工人力 (Placeholder)
    private Long avgAttendance; // 平均出工人力 (Placeholder)
    private Long highRiskPersonnelCount; //高風險健康人員
    private String violationPersonnelPercentage; //違規人員比例

    // Other details
    private BigDecimal registeredCapital;
    private String industryType;
    private String website;
    private String remarks;

    // Associated Lists (from 4.html tabs)
    private List<CompanyPersonnelDto> keyPersonnel; // 主要人力列表
    private List<ProjectHistoryDto> projectHistory; // 台積專案紀錄 (近三年)
    private List<SafetyIncidentDto> recentViolations; // 近一年違規事件 (might be just a summary here, or full list)

    // Data for charts on 4.html could also be here or fetched by separate endpoints
    // For example:
    // private ChartDataDto experienceDistribution;
    // private ChartDataDto violationTrend;
    // private ChartDataDto jobTypeMonthlyDistribution;
    // private ChartDataDto healthRiskMonthlyDistribution;
}
