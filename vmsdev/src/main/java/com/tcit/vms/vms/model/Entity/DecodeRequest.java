package com.tcit.vms.vms.model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DecodeRequest {

    private String cryptograph;

    /*private byte[] encodedPNG;
    private byte[] decodedData;
    private String uniqueID;
    private Integer decodedData_Size;
    private  Integer errorBeforeFailingToDecode;*/
}
