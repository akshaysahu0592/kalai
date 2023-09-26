package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckedInVisitDto {
    private Integer visitorId;
    private String name;
    private String mobileNo;
    private String email;
    private LocalDateTime checkIn;
}
