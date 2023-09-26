package com.tcit.vms.vms.controller;


import com.tcit.vms.vms.dto.response.ResponseDto;
import com.tcit.vms.vms.exception.UserNotFoundException;
import com.tcit.vms.vms.model.Staff;
import com.tcit.vms.vms.payload.request.LoginRequest;
import com.tcit.vms.vms.payload.response.JwtResponse;
import com.tcit.vms.vms.repository.StaffRepository;
import com.tcit.vms.vms.jwt.JwtUtils;
import com.tcit.vms.vms.service.StaffService;
import com.tcit.vms.vms.service.UserDetailsImpl;
import com.tcit.vms.vms.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired(required = true)
    AuthenticationManager authenticationManager;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/Auth/Authentication")
    public ResponseEntity<?> signin(@RequestBody @Valid LoginRequest loginRequest) throws UserNotFoundException {
        log.info("Authentication {}", loginRequest);
        List<Staff> staffDb = staffRepository.findByEmail(loginRequest.getEmail());

        if (staffDb == null || staffDb.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid User or Password");
        } else {
            Staff staff=staffDb.stream().filter(e->e.isActive()).findFirst().get();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matched = passwordEncoder.matches(loginRequest.getPassword(), staff.getPassword());
            //if (loginRequest.getPassword().equals(staffDb.getPassword())) {
            if (matched) {
                Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
                log.info("authenticate called");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());
                //staffRepository.save(staffDb);
                log.info("staff repo");
                ResponseEntity responseEntity = ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .body(new JwtResponse(jwtCookie.getValue(),
                                "Bearer",
                                userDetails.getId(),
                                userDetails.getName(),
                                userDetails.getEmail(),
                                userDetails.getRoleId(),
                                roles
                        ));
                log.info("response{}", responseEntity);
                return responseEntity;
            } else {
                return ResponseEntity.badRequest().body(new ResponseDto("", "Password Mismatched !", ""));
            }
        }
    }
}




