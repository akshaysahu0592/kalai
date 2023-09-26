package com.tcit.vms.vms.model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DecodeResponse {
    private String face_template;
    private String cryptograph_id;
}
