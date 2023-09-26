package com.tcit.vms.vms.dto.response;

import com.tcit.vms.vms.model.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseStaffListDto {
    private Integer staffId;
    private String StaffName;
    private Integer DepartmentId;
    public static ResponseStaffListDto builder(Staff staff) {
        return  new ResponseStaffListDto(staff.getId(),
                staff.getStaffName(),

                staff.getDepartment().getId());
    }
}


