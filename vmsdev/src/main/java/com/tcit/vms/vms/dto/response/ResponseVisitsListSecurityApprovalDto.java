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
public class ResponseVisitsListSecurityApprovalDto {


    private Integer visitorId;
    private Integer visitId;

    private String name;
    private String emiratesId;
    private String email;
    private String mobileNo;
    private Integer visitorTypeId;
    private String visitorType;
    private String profPicture;

    private LocalDateTime dateOfVisit;
    private String duration;
    private Integer hostId;
    private String hostName;
    private Integer deptId;
    private String departmentName;
    private Integer campusId;
    private String campusName;
    private Integer approvedByHost;
    private Integer approvedBySecurty;
    private String status;
    private Integer accompanyCount;

}

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



