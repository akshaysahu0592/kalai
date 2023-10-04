package com.tcit.vms.vms.service;

import com.tcit.vms.vms.dto.request.*;
import com.tcit.vms.vms.dto.response.ResponseDto;
import com.tcit.vms.vms.dto.response.SearchVisitorResponseDto;
import com.tcit.vms.vms.exception.ApplicationValidationException;
import com.tcit.vms.vms.exception.UserNotFoundException;
import com.tcit.vms.vms.model.Visit;
import com.tcit.vms.vms.model.Visitor;
import com.tcit.vms.vms.model.VisitorType;
import com.tcit.vms.vms.repository.StaffRepository;
import com.tcit.vms.vms.repository.VisitRepository;
import com.tcit.vms.vms.repository.VisitorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
@Slf4j
public class VisitorService {
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private VisitService visitService;
    @Autowired
    private SendMailService sendMailService;
    @Autowired
    private VisitorTypeService visitorTypeService;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StorageService storageService;
    public Visitor getVisitorById(Integer id) throws RuntimeException {
        return visitorRepository.findById(id).orElseThrow(() -> new RuntimeException("No Visitor found for ID " + id));
    }
    public ResponseDto createVisitor(VisitRequestCreateDto visitRequestDto) {
        ResponseDto responseDto = null;
         try {
            Visitor visitor = creatingVisitor(visitRequestDto);
             if (isVisitEntryExits(visitor.getId(), visitRequestDto.getDateOfVisit())) {
                 DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                 throw new ApplicationValidationException(" Visiting request already available for " + df.format(visitRequestDto.getDateOfVisit()));
             }

             Visit visit = visitService.createVisit(visitRequestDto, visitor);
            if ((visitRequestDto.getCreatedBy().equals("host") || visitRequestDto.getCreatedBy().equals("security")) && visitRequestDto.getAccompanyDetails() != null && !visitRequestDto.getAccompanyDetails().isEmpty()) {
                visitRequestDto.getAccompanyDetails().stream().forEach(e -> {
                    VisitRequestCreateDto dto = new VisitRequestCreateDto();
                    dto.setName(e.getName());
                    dto.setMobileNo(e.getMobileNo());
                    dto.setEmail(e.getEmail());
                    dto.setProfPicture(e.getPicture());
                    dto.setEmiratesId(e.getEmiratesId());
                    dto.setVisitorTypeId(visitRequestDto.getVisitorTypeId());

                    try {
                        Visitor acc = creatingVisitor(dto);
                        createVisitAccompany(visit, acc);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
            if (visitRequestDto.getCreatedBy().equals("visitor")) {
                visitService.sendEmailToDept(visit, visitor);

            } else if (visitRequestDto.getCreatedBy().equals("host")) {
                ApproveRejectDto approveRejectDto = new ApproveRejectDto();
                approveRejectDto.setVisitId(visit.getId());
                approveRejectDto.setApprovedByHost(1);
                visitService.approveOrRejectVisitorByHost(approveRejectDto, true);
            } else if (visitRequestDto.getCreatedBy().equals("security")) {
                SecurityApproveRejectDto securityApproveRejectDto = new SecurityApproveRejectDto();
                securityApproveRejectDto.setVisitId(visit.getId());
                securityApproveRejectDto.setApprovedBySecurity(1);
                visitService.approveOrRejectVisitorBySecurity(securityApproveRejectDto, true);
            }
           return new ResponseDto("SUCCESS", "Visitor created ", "");
        }
        catch (Exception e) {
            e.printStackTrace();
          }
       return new ResponseDto("ERROR", "Exception occurred please check the log", "");
    }
       private Visitor creatingVisitor(VisitRequestCreateDto visitRequestDto) throws IOException {
        List<Visitor> visitors = visitorRepository.findByMobileNo(visitRequestDto.getMobileNo());
        Visitor visitor = null;
        if (visitors != null && !visitors.isEmpty()) {
            visitor = visitors.stream().filter(e->e.isActive()).findFirst().orElse(null);
        }
        VisitorType visitorType = visitorTypeService.getvisitorTypeById(visitRequestDto.getVisitorTypeId());

        if (visitor == null) {

            visitor = Visitor.builder()
                    .name(visitRequestDto.getName())
                    .mobileNo(visitRequestDto.getMobileNo())
                    .email(visitRequestDto.getEmail())
                    .visitorType(visitorType)

                    .createdDate(LocalDateTime.now())
                    .isActive(true)
                    .build();
        }
        if (visitRequestDto.getEmiratesId() != null) {
            visitor.setEmiratesId(visitRequestDto.getEmiratesId());
        }
        visitor = visitorRepository.save(visitor);

        if (visitRequestDto.getProfPicture() != null && !visitRequestDto.getProfPicture().isEmpty()) {
            log.info("Upload Picture for VisitorId : {}", visitor.getId());
            String imageBase64 = visitRequestDto.getProfPicture();
            ResponseDto responseDto = storageService.uploadImage(visitRequestDto.getProfPicture(),
                    visitor.getId(),
                    visitRequestDto.getFileType() == null ? "jpg" : visitRequestDto.getFileType(),
                    "VISITOR");
            visitRequestDto.setProfPicture((String) responseDto.getData());
            visitor = visitorRepository.save(visitor);
        }
        return visitor;
    }
    private void createVisitAccompany(Visit visit, Visitor acc) throws IOException {
        List<Visitor> accompanies = visit.getAccompanies();
        if(Objects.isNull(accompanies) || accompanies.isEmpty())
            accompanies = new ArrayList<>();
        accompanies.add(acc);
        visit.setAccompanies(accompanies);
        visitRepository.save(visit);
    }
    public boolean isVisitEntryExits(Integer visitorId, LocalDateTime dateOfVisit) {
        Optional<List<Visit>> result = visitService.findByVisitorIdAndDateOfVisit(visitorId, dateOfVisit);
        if (result.isPresent()) {
            return !result.get().isEmpty();
        }
        return false;
    }
    public ResponseDto updateVisitor(VisitorReqUpdateDto visitorReqUpdateDto) {
        ResponseDto responseDto;
        try {
            Visitor visitor = visitorRepository.findById(visitorReqUpdateDto.getId()).orElse(null);
            if (visitor == null) {
                throw new UserNotFoundException("Visitor Not Found with Id " + visitorReqUpdateDto.getId());
            }
            visitor.setName(visitorReqUpdateDto.getName());
            visitor.setMobileNo(visitorReqUpdateDto.getMobileNo());
            visitor.setEmail(visitorReqUpdateDto.getEmail());
            visitor.setCryptograph(visitorReqUpdateDto.getCryptograph());
            visitor.setEmiratesId(visitorReqUpdateDto.getEmiratesId());
            visitorRepository.save(visitor);

            if (visitorReqUpdateDto.getProfPicture() != null && !visitorReqUpdateDto.getProfPicture().isEmpty()) {
                log.info("Upload Picture for VisitorId# {}", visitor.getId());
                responseDto = storageService.uploadImage(visitorReqUpdateDto.getProfPicture(),
                        visitor.getId(),
                        visitorReqUpdateDto.getFileType() == null ? "jpg" : visitorReqUpdateDto.getFileType(),
                        "VISITOR");
                visitorReqUpdateDto.setProfPicture((String) responseDto.getData());
                visitorRepository.save(visitor);
            }
            responseDto = new ResponseDto("SUCCESS", "Visitor Updated ", "");
        } catch (Exception e) {
            e.getStackTrace();
            responseDto = new ResponseDto("Error", "Mobile Number already exists ", "");
        }
        return responseDto;
    }
    public ResponseDto deleteVisitor(Integer id) {
        Visitor visitor1 = getVisitorById(id);
        visitor1.setActive(false);
        visitService.updateInActive(id);
        visitorRepository.save(visitor1);
        return new ResponseDto("Error", "Mobile Number already exists ", "");
    }
    public List<Visitor> getAllVisitors() {
        List<Visitor> allvisitor = visitorRepository.findAll();
        return allvisitor.stream().filter(s -> Objects.nonNull(s.isActive()) && s.isActive()).collect(Collectors.toList());
    }
    public List<Visitor> getVisitorsByStaffId(Integer staffId) {
        return visitorRepository.findVisitorsByStaffId(staffId);
    }
    public List<SearchVisitorResponseDto> searchVisitors(String search) {
        List<Visitor> visitors = visitorRepository.findAllVisitorsWithFilters(search);
        List<SearchVisitorResponseDto> dtos = new ArrayList<>();
        if (visitors != null && visitors.size() > 0) {
            log.info("visitors size{}", visitors.size());
            //SearchVisitorResponseDto dto = new SearchVisitorResponseDto();
            AtomicReference<SearchVisitorResponseDto> dto = new AtomicReference<>(new SearchVisitorResponseDto());
            visitors.forEach(e -> {
                dto.set(convertToDto(e));
                dtos.add(dto.get());
            });
        }
        return dtos;
    }
    public List<SearchVisitorResponseDto> searchVisitorsHistory(String searchText) {
        List<Visitor> visitors = visitorRepository.findAllVisitorsWithFilters(searchText);
        List<SearchVisitorResponseDto> dtos = new ArrayList<>();
        if (visitors != null && visitors.size() > 0) {
            log.info("visitors size{}", visitors.size());
            //SearchVisitorResponseDto dto = new SearchVisitorResponseDto();
            AtomicReference<SearchVisitorResponseDto> dto = new AtomicReference<>(new SearchVisitorResponseDto());
            visitors.forEach(e -> {
                dto.set(convertToDto(e));
                dtos.add(dto.get());
            });
        }
        return dtos;
    }
    private SearchVisitorResponseDto convertToDto(Visitor visitor) {
        SearchVisitorResponseDto dto = new SearchVisitorResponseDto();
        dto.setVisitorId(visitor.getId());
        dto.setName(visitor.getName());
        dto.setMobileNo(visitor.getMobileNo());
        dto.setEmail(visitor.getEmail());
        return dto;
    }
    public List<Visitor> getAllVisitorDetailsByToday() {
        LocalDateTime localDateTime = LocalDateTime.now().with(LocalTime.MIDNIGHT);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDate = localDateTime.format(df);
        localDateTime = LocalDateTime.now().plusDays(1).with(LocalTime.MIDNIGHT);
        String endDate = localDateTime.format(df);
        return visitorRepository.findByVisitingDate(startDate, endDate);
    }
   public ResponseDto addVisitor(VisitorRequestDto dto) {
        List<Visitor> visitors = visitorRepository.findByMobileNo(dto.getMobileNo());
        Visitor visitor = null;
        ResponseDto responseDto=null;

        if (visitors != null && !visitors.isEmpty()) {
            visitor = visitors.stream().filter(e -> e.isActive()).findFirst().orElse(null);
        }
        if (visitor == null) {
            try {
                visitor = Visitor.builder()
                        .id(dto.getVisitorId())
                        .name(dto.getName())
                        .mobileNo(dto.getMobileNo())
                        .email(dto.getEmail())
                        .visitorType(visitorTypeService.getVisitorTypeById(dto.getVisitorTypeId()))

                        .emiratesId(dto.getEmiratesId())
                        .createdDate(LocalDateTime.now())
                        .isActive(true)
                        .build();
                visitorRepository.save(visitor);
                if (dto.getProfPicture() != null && !dto.getProfPicture().isEmpty()) {
                    log.info("Upload Picture for VisitorId# {}", visitor.getId());
                    responseDto = storageService.uploadImage(dto.getProfPicture(),
                            visitor.getId(),
                            dto.getFileType() == null ? "jpg" : dto.getFileType(),
                            "VISITOR");
                    dto.setProfPicture((String) responseDto.getData());
                    visitorRepository.save(visitor);

                return new ResponseDto("SUCCESS", "Visitor Created ", "");
                           }
            }catch (Exception e) {
                e.getStackTrace();
            }
        } return new ResponseDto("ERROR","Mobile Number already exists","");
    }
    }







