package com.tcit.vms.vms.dto.response;

import com.tcit.vms.vms.model.Visitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchVisitorResponseDto {
    private Integer visitorId;
    private String name;
    private String mobileNo;
    private String email;
    public static SearchVisitorResponseDto builder(Visitor visitor)
    {
        return new SearchVisitorResponseDto(visitor.getId(),
                visitor.getName(),
                visitor.getMobileNo(),
                visitor.getEmail());
    }
}
