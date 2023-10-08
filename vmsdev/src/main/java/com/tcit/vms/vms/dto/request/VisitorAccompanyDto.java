package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VisitorAccompanyDto {
    private Integer visitorId;
    private String name;
    private String mobileNo;
    public  String email;
    private String picture;
    private String emiratesId;
}
