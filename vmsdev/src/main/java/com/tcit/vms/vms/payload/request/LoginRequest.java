package com.tcit.vms.vms.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}