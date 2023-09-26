package com.tcit.vms.vms.controller;

import com.tcit.vms.vms.dto.response.ResponseCampusDto;
import com.tcit.vms.vms.dto.response.ResponseDto;
import com.tcit.vms.vms.model.Campus;
import com.tcit.vms.vms.service.CampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CampusController {
    @Autowired
    private CampusService campusService;
    /*@GetMapping("/GetAllCampus")
    public ResponseDto getAllCampus() {
        return campusService.getAllCampus();
    }*/
    /*@GetMapping("/GetCampus/{id}")
    public Campus getCampusById (@PathVariable("id") Integer campusId)
    {
        return campusService.getCampusById(campusId);
    }*/

    @GetMapping("/GetAllCampusList")
    public List<ResponseCampusDto> getCampusList() {
        List<Campus> campusList=campusService.getCampusList();
        return campusList.stream().map(e-> ResponseCampusDto.builder(e)).collect(Collectors.toList());
    }
    @PostMapping("/Campus")
    public ResponseDto createCampus(@RequestBody Campus campus){
        campusService.createCampus(campus);
        return new ResponseDto("SUCCESS", "Campus Created","");
    }
    @PutMapping("/Campus")
    public ResponseDto updateCampus(@RequestBody Campus campus){
        campusService.updateCampus(campus);
        return new ResponseDto("SUCCESS", "Campus Updated","");
    }
    @DeleteMapping("/Campus")
    public ResponseDto deleteCampus(@RequestBody Campus campus)
    {
        campusService.deleteCampus(campus.getId());
        return new ResponseDto("SUCCESS", "Campus Deleted","");
    }

}
