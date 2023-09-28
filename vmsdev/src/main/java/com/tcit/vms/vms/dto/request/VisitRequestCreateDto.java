package com.tcit.vms.vms.dto.request;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class VisitRequestCreateDto {
    private Integer id;
    private String name;
    private String mobileNo;

    public String email;
    private Integer visitorTypeId;
    private String profPicture;
    private String cryptograph;
    private String emiratesId;
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Dubai")
    private LocalDateTime dateOfVisit;
    private String duration;

    private Integer campusId;
    private Integer departmentId;
    private Integer staffId;

    private String agenda;
    private Integer accompanyCount;
    private Integer approvedByHost;
    private Integer approvedBySecurity;
    private String fileType;
    private String createdBy;
    private String visitType;
    private Boolean isActive=true;
    private String imageBase64;
    private List<VisitorAccompanyDto> accompanyDetails;

}
