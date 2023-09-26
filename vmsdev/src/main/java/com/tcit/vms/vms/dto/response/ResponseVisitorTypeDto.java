package com.tcit.vms.vms.dto.response;

import com.tcit.vms.vms.model.VisitorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseVisitorTypeDto {
    private Integer Id;
    private String VisitorType;
    public static ResponseVisitorTypeDto builder(VisitorType visitorType) {
        return  new ResponseVisitorTypeDto(visitorType.getId(),
                visitorType.getName());

    }
}



