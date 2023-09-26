package com.tcit.vms.vms.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class ImageRequestDto {
    private Integer id;
    private String fileType;
    private String section;
    private String img64;
}
