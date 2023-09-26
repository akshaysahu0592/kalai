package com.tcit.vms.vms.dto.request;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class StaffRequestDto {

    private Integer id;
    private String staffName;
    private String mobileNo;
    private String email;
    private String fileType;
    private String profPicture;
    private String password;
    private Integer departmentId;
    private String designation;
    private Integer roleId;
    private Boolean isActive=true;




}
