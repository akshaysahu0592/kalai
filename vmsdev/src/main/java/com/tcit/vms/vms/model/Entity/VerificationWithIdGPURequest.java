package com.tcit.vms.vms.model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerificationWithIdGPURequest {
    private String transactionId;
    private  String  transactionSource;
    private  String  uid;
    private  Face  faceData;
    private  double  faceThreshold  =  0;
}
