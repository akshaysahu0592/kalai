package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ToString
    public class ApprovalRequestDto {
        private String staffId;
        private String Designation;
        private Integer visitId;
        //private String status;

    }


