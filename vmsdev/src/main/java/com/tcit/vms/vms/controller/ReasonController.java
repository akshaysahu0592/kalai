package com.tcit.vms.vms.controller;

import com.tcit.vms.vms.dto.request.ReasonRequestDto;
import com.tcit.vms.vms.dto.response.ResponseDto;
import com.tcit.vms.vms.dto.response.ResponseReasonDto;
import com.tcit.vms.vms.model.Reason;
import com.tcit.vms.vms.service.ReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ReasonController {
    @Autowired
    private ReasonService reasonService;
    @GetMapping("/GetAllReasonList")
    public List<ResponseReasonDto> getReasonList() {
        List<Reason> reasonList=reasonService.getReasonList();
        return reasonList.stream().map(e-> ResponseReasonDto.builder(e)).collect(Collectors.toList());
    }
    @PostMapping("/AddReason")
    public ResponseDto createReason(@RequestBody ReasonRequestDto reasonRequestDto) throws IOException {
        reasonService.createReason(reasonRequestDto);
        return new ResponseDto("SUCCESS", " New Reason added successfully","");
    }
    @DeleteMapping("/DeleteReason")
    public ResponseDto deleteReason(@RequestBody Reason reason)
    {
        reasonService.deleteReason(reason.getId());
        return new ResponseDto("SUCCESS", "Reason Deleted","");
    }
}
