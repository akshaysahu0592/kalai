package com.tcit.vms.vms.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class StaffReqUpdateDto {
        private Integer id;
        private String staffName;
        private String mobileNo;
        private String email;
        private String fileType;
        private String password;
        private Integer departmentId;
        private Integer roleId;
        private String designation;


    }


