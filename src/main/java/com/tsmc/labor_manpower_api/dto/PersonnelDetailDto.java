package com.tsmc.labor_manpower_api.dto;

import com.tsmc.labor_manpower_api.model.enums.EmploymentType;
import com.tsmc.labor_manpower_api.model.enums.Gender;
import com.tsmc.labor_manpower_api.model.enums.PersonnelStatus;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PersonnelDetailDto {
    // Basic Info (from 3.html left panel)
    private Long personnelId;
    private String name;
    private String workId;
    private String idNumber; // 身分證號
    private String mainCompanyName; // 主包公司
    private String subCompanyName; // 次包公司 (if any)
    private String department; // 部門
    private String jobTitle; // 職稱/崗位
    private PersonnelStatus currentStatus; // 目前狀態
    private LocalDate tsmcStartDate; // 台積入廠日 (年資 calculated from this)
    private Integer seniorityYears; // 年資 (calculated)
    private Integer seniorityMonths; // 年資 (calculated)
    private String profilePhotoPath;

    // Contact Info (from 3.html right panel - 基本資料 tab)
    private Gender gender;
    private LocalDate dateOfBirth;
    private Integer age; // Calculated
    private String nationality;
    private String contactNumber; //本人電話
    private String email; // 本人郵箱
    private String address; // 住址
    private String bloodType; // 血型
    private String emergencyContactName; // 緊急聯絡人
    private String emergencyContactPhone; // 緊急聯絡人電話

    // Employment Info (from 3.html right panel - 職務資料 tab)
    private EmploymentType employmentType; // 雇用性質
    // TSMC Start Date already included
    // Current Status already included
    private String safetyRecordSummary; // 勞安記錄 (brief summary, full history might be separate)
    private String healthGrade; // 健康等級 (e.g. "1", "2")
    private String remarks; // 備註

    // Associated Lists
    private List<JobTypeDto> jobTypes; // 工種列表 (from 3.html right panel - 工種列表 tab)
    private List<CertificationDto> certifications; // 證照列表 (from 3.html right panel - 證照列表 tab)
    private List<SafetyIncidentDto> safetyIncidents; // 違規記錄 (from 3.html right panel - 違規記錄 tab)
    // private List<ProjectHistoryDto> projectHistory; // 參與項目 (This might be extensive, TBD if needed here or separate endpoint)
    // private List<HealthRecordDto> healthRecords; // 體檢記錄
    // private List<TrainingRecordDto> trainingRecords; // 訓練記錄
}
