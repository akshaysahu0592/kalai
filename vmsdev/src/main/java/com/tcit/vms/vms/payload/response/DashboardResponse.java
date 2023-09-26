package com.tcit.vms.vms.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardResponse {
    Long visitorExpected;
    Long visitorCompleted;
    Long visitorDefault;
    Long visitorPending;
    Long visitorInCampus;
}
