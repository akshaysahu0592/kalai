package com.tcit.vms.vms.model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestResponse {
    private MBAPError error;
    private int httpResponseCode;
    private String httpErrorMessage;
    private Boolean verificationResult;
    private String transactionId;
    private String transactionSource;
    private String matchScore;
    private String uid;
}
