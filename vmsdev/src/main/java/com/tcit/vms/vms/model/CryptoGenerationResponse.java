package com.tcit.vms.vms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CryptoGenerationResponse {

    private String uuid;
    private String image;
    private String path;
}
