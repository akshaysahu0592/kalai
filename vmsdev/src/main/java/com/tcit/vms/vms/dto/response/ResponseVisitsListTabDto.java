package com.tcit.vms.vms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseVisitsListTabDto {

    private Integer visitorId;
    private Integer visitId;

    private String name;
    private String email;
    private String mobileNo;
    private String profPicture;
    private LocalDateTime dateOfVisit;
    private String duration;

}
