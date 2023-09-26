package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VisitByVistIdDto {
    private String id;
    /*public static String builder(Visit visit) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException {
        if (Objects.isNull(visit)) {
            return null;
        }
        String s = String.valueOf(new VisitByVistIdDto(EncryptUtil.encrypt(visit.getId().toString())));
        return s;
    }*/
}
