package com.tcit.vms.vms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchVisitResponseDto {
   // private Integer id;
    private String name;
    private String mobileNo;
    private String email;

    /*public static SearchVisitorResponseDto builder(Visitor visitor) {
        return new SearchVisitorResponseDto(
                visitor.getId(),
                visitor.getName()
        );*/
    }

