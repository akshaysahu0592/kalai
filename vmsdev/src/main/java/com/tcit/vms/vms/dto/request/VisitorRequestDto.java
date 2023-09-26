package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VisitorRequestDto {
    private Integer visitorId;
    private String name;
    private String mobileNo;
    private String email;
    private Integer visitorTypeId;
    private String profPicture;
   private String emiratesId;
    private String fileType;
   /* public static VisitorRequestDto builder(Visitor visitor)
    {
        return new VisitorRequestDto(visitor.getId(),
                visitor.getName(),
                visitor.getMobileNo(),
                visitor.getEmail(),
                visitor.getVisitorType().getId()

        );
    }*/
    }
