package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VisitAccompanyDto {
    private Integer id;
    private Integer visitId;
    private Integer visitorId;
    private Boolean isActive;

}
