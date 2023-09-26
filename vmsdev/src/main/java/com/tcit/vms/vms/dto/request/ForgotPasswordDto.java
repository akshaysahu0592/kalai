package com.tcit.vms.vms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForgotPasswordDto implements Serializable {
    private Integer staffId;
    private LocalDateTime currentDate;
    private LocalDateTime expiryDate;
}
