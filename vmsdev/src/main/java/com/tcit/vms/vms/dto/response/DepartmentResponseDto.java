package com.tcit.vms.vms.dto.response;

import com.tcit.vms.vms.model.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class DepartmentResponseDto {
        private Integer DeptId;
        private String DeptName;
            public static DepartmentResponseDto builder(Department department) {
            return  new DepartmentResponseDto(department.getId(),
                    department.getDeptName());

        }
    }



