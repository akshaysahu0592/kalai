package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SecurityApproveRejectDto {

    private Integer visitId;

    private Integer approvedBySecurity;
    private Integer reason;
    private String comments;


}
