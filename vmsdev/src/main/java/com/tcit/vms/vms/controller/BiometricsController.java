package com.tcit.vms.vms.controller;

import com.tcit.vms.vms.dto.response.ResponseDto;
import com.tcit.vms.vms.model.Entity.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.tcit.vms.vms.service.BiometricsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
@RequestMapping("/api")
public class BiometricsController {
    RestTemplate restTemplate;
    private String url="https://liveness.tech5.tech/check_liveness";
    private String urlMBAP="http://gn-testapi.tech5.tech:9090/MBAP/api";
    private String urlDecode="https://eval-idencode.tech5.tech/v1/cryptograph/decode";
    private String getURL = "https://eval-idencode.tech5.tech/v1/cryptograph?uuid=";
    @Autowired
    private BiometricsService biometricsService;
    @PostMapping("/enroll")
    public ResponseEntity<ResponseDto> enroll(@RequestBody Request request) {
        Object obj = biometricsService.enroll(request);
        ResponseDto responseDto = new ResponseDto("SUCCESS", "",obj);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
