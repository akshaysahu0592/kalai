package com.tcit.vms.vms.service;

import com.tcit.vms.vms.model.Entity.Request;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class BiometricsService {
    RestTemplate restTemplate;
    private String urlMBAP="http://gn-testapi.tech5.tech:9090/MBAP/api";
    public Object enroll(Request request) {
        restTemplate = getRestTemplate();
        HttpEntity<Request> requestEntity = new HttpEntity(request, new HttpHeaders());
        Object res= restTemplate.postForObject(urlMBAP+"/enroll", requestEntity, Object.class);
        return res;
    }
    private RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
