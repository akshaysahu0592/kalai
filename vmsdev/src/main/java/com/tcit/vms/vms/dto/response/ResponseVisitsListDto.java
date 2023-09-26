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
public class ResponseVisitsListDto {
    private Integer SNo;
    private Integer visitorId;
    private Integer visitId;

    private String name;
    private String mobileNo;
    private Integer hostId;
    private String hostName;
    private LocalDateTime dateOfVisit;
    private String duration;
    private String status;
    private Boolean isActive;
    private Integer accompanyCount;


   /* public ResponseVisitsListDto(Integer id, String name, String mobileNo, String staffName, LocalDateTime dateOfVisit, String duration) {
    }

    public static ResponseVisitsListDto builder(Visit visit)
    {
        return new ResponseVisitsListDto(visit.getId(),
                visit.getVisitor().getName(),
                visit.getVisitor().getMobileNo(),
                visit.getStaff().getStaffName(),
                visit.getDateOfVisit(),
                visit.getDuration());
    }*/
}
