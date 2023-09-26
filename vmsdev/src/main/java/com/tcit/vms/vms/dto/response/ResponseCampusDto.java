package com.tcit.vms.vms.dto.response;

import com.tcit.vms.vms.model.Campus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseCampusDto {
    private Integer CampusId;
    private String CampusName;
    public static ResponseCampusDto builder(Campus campus) {
        return  new ResponseCampusDto(campus.getId(),
                campus.getCampusName());

    }
}



