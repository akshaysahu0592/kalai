package com.tcit.vms.vms.dto.response;


import com.tcit.vms.vms.model.Visitor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseVisitorDto {
    private Integer id;
    private String  name;
    private String  mobileNo;
    private String  email;
    private   Integer visitorType;
    private String visitorTypeName;
    private String profPicture;
    private String emiratesId;

    public static ResponseVisitorDto builder(Visitor visitor) {
        if(Objects.isNull(visitor)){
            return null;
        }
        return  new ResponseVisitorDto(visitor.getId(),

                visitor.getName(),
                visitor.getMobileNo(),

                visitor.getEmail(),
                visitor.getVisitorType().getId(),
                visitor.getVisitorType().getName(),
                visitor.getProfPicture(),
                visitor.getEmiratesId()

        );
    }
}
