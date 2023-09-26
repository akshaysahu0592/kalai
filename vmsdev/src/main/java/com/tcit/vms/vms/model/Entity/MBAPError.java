package com.tcit.vms.vms.model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MBAPError {

    private int errorCode;
    private String errorMessage;
    private String documentation;
}
