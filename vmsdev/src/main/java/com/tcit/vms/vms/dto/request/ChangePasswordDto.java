package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDto {
    private Integer staffId;
    private String oldPassword;
    private String newPassword;
}
