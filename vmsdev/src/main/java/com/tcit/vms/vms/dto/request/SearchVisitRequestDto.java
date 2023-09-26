package com.tcit.vms.vms.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchVisitRequestDto {
    private String fromDate;
    private String toDate;
    private String name;
    private String mobileNo;
    private String email;
}
