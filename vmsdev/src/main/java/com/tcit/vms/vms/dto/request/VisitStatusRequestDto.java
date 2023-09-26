package com.tcit.vms.vms.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class VisitStatusRequestDto {
    private Integer visitId;
    private String status;
    private Boolean isBiometricVerified;
}
