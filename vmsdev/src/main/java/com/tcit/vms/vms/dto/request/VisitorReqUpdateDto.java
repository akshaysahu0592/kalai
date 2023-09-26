package com.tcit.vms.vms.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class VisitorReqUpdateDto {
    private Integer id;
    private String name;
    private String mobileNo;

    public String email;

    private String profPicture;
    private String cryptograph;
    private String emiratesId;
    private String fileType;

}
