package com.tcit.vms.vms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseGetVisitVisitorDto {
    private Integer Visitorid;
    private String  name;
    private String  mobileNo;
    private String  email;
    private   Integer visitorTypeId;
    private String profPicture;
    private String emiratesId;
}
