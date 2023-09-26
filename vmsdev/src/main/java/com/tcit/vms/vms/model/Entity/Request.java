package com.tcit.vms.vms.model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Request {
    private  String  transactionId;
    private  String  transactionSource;
    private  String  uid;
   /* private  List<Finger> fingerData;
    private  List<Iris> irisData;*/
    private  Face  faceData;
      /* private  double  faceThreshold;
     private  double  fingerThreshold;
    private  double  irisThreshold;
    private  Boolean  useFingerScaling;*/

}
