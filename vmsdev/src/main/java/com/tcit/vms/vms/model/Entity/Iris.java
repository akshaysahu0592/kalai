package com.tcit.vms.vms.model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Iris {
    private  String  pos;
    private  String  image;
    private  String  template;
    private  Double  quality;
    private  Double  blur;
}
