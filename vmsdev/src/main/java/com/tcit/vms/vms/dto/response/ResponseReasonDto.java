package com.tcit.vms.vms.dto.response;

import com.tcit.vms.vms.model.Reason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseReasonDto {
    private Integer reasonId;
    private String reasonName;
    public static ResponseReasonDto builder(Reason reason) {
        return  new ResponseReasonDto(reason.getId(),
                reason.getReasonName());

    }
}
