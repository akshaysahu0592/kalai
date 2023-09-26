package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchVisitRequestHistoryDto {
    private String fromDate;
    private String toDate;
    private String searchText;
    private String status;
}
