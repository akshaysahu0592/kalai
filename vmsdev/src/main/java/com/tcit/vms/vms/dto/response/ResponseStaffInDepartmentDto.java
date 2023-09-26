package com.tcit.vms.vms.dto.response;

import com.tcit.vms.vms.model.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseStaffInDepartmentDto {
    private Integer StaffId;
    private String roleName;
    private String StaffName;
    private Integer DepartmentId;

    public static ResponseStaffInDepartmentDto addStaffList(Staff staff) {
        return  new ResponseStaffInDepartmentDto (staff.getId(),
                staff.getRole().getRoleName(),
                staff.getStaffName(),
                staff.getDepartment().getId());

    }
}

