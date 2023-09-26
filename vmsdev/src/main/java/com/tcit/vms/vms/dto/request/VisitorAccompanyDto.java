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

    private String name;
    private String mobileNo;

    public String email;
    private String profPicture;
    private String emiratesId;

}
