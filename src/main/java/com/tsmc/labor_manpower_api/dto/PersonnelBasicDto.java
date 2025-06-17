package com.tsmc.labor_manpower_api.dto;

import lombok.Data;

@Data
public class PersonnelBasicDto {
    private Long personnelId;
    private String name;
    private String workId; // Displayed as "工號"
    private String mainCompanyName; // Displayed as "主包"
    private String subCompanyName; // Displayed as "次包"
    private String primaryJobTypeName; // Displayed as "主要工種"
    private String currentStatus; // Displayed as "狀態" (from PersonnelStatus enum)
}
