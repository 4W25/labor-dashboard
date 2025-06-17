package com.tsmc.labor_manpower_api.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private String roleName;
    private boolean isActive;
}
