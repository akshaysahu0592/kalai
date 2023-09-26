package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BiometricVerifiedDto {
    private Integer visitId;
    private Boolean isBiometricVerified;
}
