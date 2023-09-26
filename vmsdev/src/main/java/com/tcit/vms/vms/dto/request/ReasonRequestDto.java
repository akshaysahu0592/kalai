package com.tcit.vms.vms.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ReasonRequestDto {
    private Integer id;
    private String reasonName;

    private Boolean isActive=true;
}
