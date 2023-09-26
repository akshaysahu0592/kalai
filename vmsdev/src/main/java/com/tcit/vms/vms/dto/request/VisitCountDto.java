package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VisitCountDto {
    private Integer TotalVisitCount;
    private Integer VisitPendingCount;
    private Integer VisitCheckedInCount;
    private Integer VisitVerifiedCount;
    private Integer VisitCompletedCount;
    private Integer VisitMissedCount;

}
