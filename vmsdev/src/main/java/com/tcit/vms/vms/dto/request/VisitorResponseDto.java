package com.tcit.vms.vms.dto.request;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class VisitorResponseDto {

        private Integer visitorId;
        private String name;
        private String mobileno;
        private String email;
        private String emiratesid;
        private Integer visitorTypeId;
        private Integer deptId;
        private Integer staffId;
        private Integer campusId;
        private LocalDateTime dateOfVisit;


}
