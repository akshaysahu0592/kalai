package com.tcit.vms.vms.controller;

import com.tcit.vms.vms.dto.request.VisitorRequestDto;
import com.tcit.vms.vms.dto.request.*;
import com.tcit.vms.vms.dto.response.*;
import com.tcit.vms.vms.model.Visitor;
import com.tcit.vms.vms.repository.VisitorRepository;
import com.tcit.vms.vms.service.StorageService;
import com.tcit.vms.vms.service.VisitService;
import com.tcit.vms.vms.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Slf4j
public class VisitorController {
    @Autowired
    private VisitorService visitorService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private VisitorRepository visitorRepository;

    @PostMapping("/createVisitDetails")
    public ResponseDto createVisitorDetails(@RequestBody VisitRequestCreateDto visitRequestDto) throws IOException {
        return visitorService.createVisitor(visitRequestDto);
    }
   @PutMapping("/updateVisitorDetails")
    public ResponseDto updateVisitorDetails(@RequestBody VisitorReqUpdateDto visitorReqUpdateDto) throws IOException {
      return  visitorService.updateVisitor(visitorReqUpdateDto);
    }
    @DeleteMapping("/deleteVisitorDetails")
    public ResponseDto deleteVisitor(@RequestBody Visitor visitor) {
        visitorService.deleteVisitor(visitor.getId());
        return new ResponseDto("", "Visitor Details Deleted", "");
    }
    @GetMapping("/getAllVisitors")
    public List<ResponseVisitorDto> getAllVisitors() {
        List<Visitor> visitorList = visitorService.getAllVisitors();
        return visitorList.stream().map(e -> ResponseVisitorDto.builder(e)).collect(Collectors.toList());
    }
    @GetMapping("/getVisitorsByStaffId/{id}")
    public List<Visitor> getVisitorsByStaffId(@PathVariable Integer id)
    {
        List<Visitor> visitors = visitorService.getVisitorsByStaffId(id);
        return visitors.stream().filter(Visitor::isActive).collect(Collectors.toList());
    }
    @GetMapping("/searchVisitor/{search}")
    public List<SearchVisitorResponseDto> searchVisitor(@PathVariable String search)
    {
     return visitorService.searchVisitors(search);
    }
    @GetMapping("/GetVisitorByVisitorId/{id}")
    public Visitor getVisitorById(@PathVariable Integer id) {
        Visitor visitor =  visitorService.getVisitorById(id);
        return visitor;
    }
    @GetMapping("/todayVisitor")
    public List<Visitor> getVisitorByToday() {
        List<Visitor> visitorDetails = visitorService.getAllVisitorDetailsByToday();
        return visitorDetails;
    }
    @PostMapping("/AddVisitor")
    public ResponseDto addVisitor(@RequestBody VisitorRequestDto dto) throws IOException {
        return visitorService.addVisitor(dto);
          //new ResponseDto("","Visitor Created Successfully","");
    }
    @GetMapping("/getImageById/{id}")
    public ResponseEntity<?> getPicturePath(@PathVariable Integer id) throws IOException {
        Visitor visitor= visitorService.getVisitorById(id);
        if(visitor.getProfPicture() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }
        String imageName = visitor.getProfPicture();
        String imageType = imageName.substring(imageName.indexOf('.')+1);
        //byte[] imageData= storageService.getImageFromFileSystemByPath(pictureUrl + "/VISITOR/" + visitorId + "/"+ visitor.getProfPicture());
        byte[] imageData= storageService.getImageFromFileSystemByPath(id + "/"+ visitor.getProfPicture());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/"+imageType))
                .body(imageData);
    }
}

