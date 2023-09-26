package com.tcit.vms.vms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class ResponseDto {
    private String status;
    private String message;
    private Object data;

}
