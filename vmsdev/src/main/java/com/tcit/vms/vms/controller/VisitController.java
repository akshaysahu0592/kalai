package com.tcit.vms.vms.controller;
import com.tcit.vms.vms.dto.request.*;
import com.tcit.vms.vms.dto.response.*;
import com.tcit.vms.vms.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class VisitController {
    //testing
    @Autowired
    private VisitService visitService;

    @GetMapping("/GetVisitDetailsByVisitId/{id}")
    public ResponseEntity<VisitResponseDto> getVisitDetailsByVisitId(@PathVariable Integer id) {
        VisitResponseDto dto = visitService.getVisitDetailsByVisitId(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/GetVisitByVisitId")
    public VisitResponseDto GetVisitByVisitId(@RequestBody VisitByVistIdDto visitByVistIdDto) throws Exception {
        return visitService.GetVisitByVisitId(visitByVistIdDto);
    }


    @GetMapping("/GetVisitDetailsbyVisitorid/{id}")
    public ResponseEntity<VisitResponseDto> getVisitbyVisitorid(@PathVariable Integer id) {
        VisitResponseDto dto = visitService.getVisitDetailsByVisitorId(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/GetAllVisits")
    public List<ResponseVisitsListDto> getVisitList(@RequestBody SearchVisitRequestDto requestDto) {
        return visitService.getVisitList(requestDto);
    }
    @PostMapping("/GetAllVisitsHistory")
    public List<VisitStaffResponseDto> GetAllVisitsHistory(@RequestBody SearchVisitRequestHistoryDto requestDto) {
        return visitService.GetAllVisitsHistory(requestDto);
    }
    @GetMapping("/GetAllVisitsForTab")
    public List<ResponseVisitsListTabDto> GetAllVisitsForTab() {
        return visitService.GetAllVisitsForTab();
    }
   @GetMapping("/getAllVisitsForSecurityApproval")
    public List<ResponseVisitsListSecurityApprovalDto> getAllVisitsForSecurityApproval() {
        return visitService.getAllVisitsForSecurityApproval();
    }
    @GetMapping("/getCheckInList")
    public List<VisitResponseDto> getAllPendingVisits()
    {
        return visitService.getAllPendingVisits();
    }
   @PutMapping("/UpdateBiometricVerified")
    public ResponseDto updateBiometricVerified(@RequestBody VisitStatusRequestDto dto)
    {
        visitService.updateBiometricVerified(dto);
        return new ResponseDto("","BiometricVerified Updated Successfully", "");
    }
    @PostMapping("/UpdateVisitStatus")
    public ResponseDto updateVisitStatus(@RequestBody VisitStatusRequestDto dto) {
        return visitService.updateVisitStatus(dto);
    }
    @GetMapping("/getAllVisitByStaffId/{id}")
    public ResponseEntity<List<VisitStaffResponseDto>> getVisitbyStaffid(@PathVariable Integer id) {
        List<VisitStaffResponseDto> dto = visitService.getVisitDetailsByStaffId(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/getVisitCount")
    public VisitCountDto getVisitCount() {
       return visitService.getVisitCount();
    }

    @PostMapping("/approveOrRejectVisitorByHost")
    public ResponseDto approveOrRejectVisitorByHost(@RequestBody ApproveRejectDto approveRejectDto) throws Exception {
        return visitService.approveOrRejectVisitorByHost(approveRejectDto,true);
    }
    @PostMapping("/approveOrRejectVisitorBySecurity")
    public ResponseDto approveOrRejectVisitorBySecurity(@RequestBody SecurityApproveRejectDto securityApproveRejectDto) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException, MessagingException {
        return visitService.approveOrRejectVisitorBySecurity(securityApproveRejectDto,true );
    }

}
