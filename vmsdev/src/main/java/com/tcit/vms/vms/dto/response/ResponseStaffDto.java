package com.tcit.vms.vms.dto.response;

import com.tcit.vms.vms.model.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class ResponseStaffDto {
        private Integer staffId;
        private String  staffName;
        private String  mobileNo;
        private String  email;
        private   Integer departmentId;
        private String departmentName;
        private Integer roleId;
        private String roleName;
        private String designation;

        public static ResponseStaffDto builder(Staff staff) {
            if(Objects.isNull(staff)){
                return null;
            }
            return  new ResponseStaffDto(staff.getId(),

                    staff.getStaffName(),
                    staff.getMobileNo(),

                    staff.getEmail(),
                    staff.getDepartment().getId(),
                    staff.getDepartment().getDeptName(),
                    staff.getRole().getId(),
                    staff.getRole().getRoleName(),
                    staff.getDesignation()
                   );
        }
    }


