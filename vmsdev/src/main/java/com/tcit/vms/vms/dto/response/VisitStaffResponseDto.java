package com.tcit.vms.vms.dto.response;

import com.tcit.vms.vms.dto.request.VisitorAccompanyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class VisitStaffResponseDto {

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
    private Integer approvedBySecurity;
    private String status;
    private Boolean isActive;

    private String reasonName;
    private String comments;
    private Integer accompanyCount;
    private String agenda;
    private List<VisitorAccompanyDto> accompanyDetails;
}

/*
    public VisitResponseDto( Integer id, String name, String emiratesId, String email, String mobileNo, VisitorType visitorType, LocalDateTime dateOfVisit, String duration, String staffName, String deptName, String campusName) {
    }


    public static VisitResponseDto builder(Visit visit) {
        return  new VisitResponseDto ( visit.getId(),
                visit.getVisitor().getName(),
                visit.getVisitor().getEmiratesId(),
                visit.getVisitor().getEmail(),
                visit.getVisitor().getMobileNo(),
                visit.getVisitor().getVisitorType(),
                visit.getDateOfVisit(),
                visit.getDuration(),
                visit.getStaff().getStaffName(),
                visit.getDepartment().getDeptName(),
                visit.getCampus().getCampusName());
    }*/




