package com.tcit.vms.vms.controller;


import com.tcit.vms.vms.dto.response.ResponseDto;
import com.tcit.vms.vms.dto.response.ResponseVisitorTypeDto;
import com.tcit.vms.vms.model.VisitorType;
import com.tcit.vms.vms.service.VisitorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class VisitorTypeController {
    @Autowired
    private VisitorTypeService visitorTypeService;

    @GetMapping("/GetAllVisitorTypeList")
    public List<ResponseVisitorTypeDto> getVisitorTypeList() {
        List<VisitorType> visitorTypeList=visitorTypeService.getVisitorTypeList();
        return visitorTypeList.stream().map(e-> ResponseVisitorTypeDto.builder(e)).collect(Collectors.toList());
    }
    @GetMapping("/GetVisitorType/{id}")
    public VisitorType getVisitorTypeById (@PathVariable("id") Integer visitorType)
    {
        VisitorType visitorType1= visitorTypeService.getVisitorTypeById(visitorType);
        return visitorType1;
    }
    @PostMapping("/VisitorType")
    public ResponseDto createVisitorType(@RequestBody VisitorType visitorType){
        visitorTypeService.createVisitorType(visitorType);
        return new ResponseDto("SUCCESS", "VisitorType Created","");
    }
    @PutMapping("/VisitorType")
    public ResponseDto updateVisitorType(@RequestBody VisitorType visitorType){
        visitorTypeService.updateVisitorType(visitorType);
        return new ResponseDto("SUCCESS", "VisitorType Updated","");
    }
    @DeleteMapping("/VisitorType")
    public ResponseDto deleteVisitorType(@RequestBody VisitorType visitorType)
    {
        visitorTypeService.deleteVisitorType(visitorType.getId());
        return new ResponseDto("SUCCESS", "VisitorType Deleted","");
    }
}


