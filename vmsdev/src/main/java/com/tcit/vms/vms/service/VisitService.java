package com.tcit.vms.vms.service;

import com.tcit.vms.vms.crypto.CryptoGeneration;
import com.tcit.vms.vms.dto.request.*;
import com.tcit.vms.vms.dto.response.*;
import com.tcit.vms.vms.exception.UserNotFoundException;
import com.tcit.vms.vms.model.*;
import com.tcit.vms.vms.model.Entity.Face;
import com.tcit.vms.vms.model.Entity.Request;
import com.tcit.vms.vms.repository.*;

import com.tcit.vms.vms.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class VisitService {
    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private CampusService campusService;
    @Autowired
    private CampusRepository campusRepository;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private SendMailService sendMailService;
    @Autowired
    private CryptoGeneration cryptoGeneration;
    @Autowired
    private BiometricsService biometricsService;
   public Visit createVisit(VisitRequestCreateDto visitRequestDto, Visitor visitor) throws InvalidAlgorithmParameterException, MessagingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException {
        Staff staff=null;
        if(visitRequestDto.getStaffId()!=null)
        {
            //staff = staffService.findByIdAndIsActive(visitRequestDto.getStaffId());
            staff=staffService.getStaffById(visitRequestDto.getStaffId());
        }
        Visit visit = Visit.builder()
                .visitor(visitor)
                .dateOfVisit(visitRequestDto.getDateOfVisit())
                .duration(visitRequestDto.getDuration())
                .campus(campusService.getCampusById(visitRequestDto.getCampusId()))
                .department((departmentService.getDepartmentById(visitRequestDto.getDepartmentId())))
                .staff(staff)
                .agenda(visitRequestDto.getAgenda())
                .accompanyCount(visitRequestDto.getAccompanyCount())
                .approvedByHost(visitRequestDto.getApprovedByHost())
                .approvedBySecurity(visitRequestDto.getApprovedBySecurity())
                .createdDate(LocalDateTime.now())
                .isActive(true)
                .status("Pending")
                .build();

        visit = visitRepository.save(visit);
        return visit;
    }
      public ResponseDto sendEmailToDept(Visit visit, Visitor visitor) throws Exception {

        String data = EncryptUtil.encrypt(visit.getId().toString());

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(visit.getDepartment().getDeptEmail());
        helper.setFrom(sender);
        helper.setSubject("Visit Details");
        String htmlStr = getHtmlToDept(visit);
        helper.setText(htmlStr, true);
        javaMailSender.send(msg);
        log.info("sendEmailToDept email send to :{}", visit.getDepartment().getDeptEmail());
        return new ResponseDto("SUCCESS", "Visit Details sent to Host ", "");
    }
    @Autowired
    private ResourceLoader resourceLoader;
    private String getHtmlToDept(Visit visit) {
        Resource resource = resourceLoader.getResource("classpath:approve.html");

        try (InputStream inputStream = resource.getInputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(replaceTokenToDept(line, visit));
            }

            String htmlContent = stringBuilder.toString();

            return htmlContent;
        } catch (IOException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    private String replaceTokenToDept(String data, Visit visit) throws Exception {
        if (data.contains("@@{name}@@")) {
            return data.replace("@@{name}@@", visit.getVisitor().getName());
        }
        if (data.contains("@@{email}@@")) {
            return data.replace("@@{email}@@", visit.getVisitor().getEmail());
        }
        if (data.contains("@@{mobileNo}@@")) {
            return data.replace("@@{mobileNo}@@", visit.getVisitor().getMobileNo());
        }
        if (data.contains("@@{visitorType}@@")) {
            return data.replace("@@{visitorType}@@", visit.getVisitor().getVisitorType().getName());
        }
        if (data.contains("@@{dateOfVisit}@@")) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return data.replace("@@{dateOfVisit}@@", df.format(visit.getDateOfVisit()));
        }
        if (data.contains("@@{duration}@@")) {
            return data.replace("@@{duration}@@", visit.getDuration());
        }
        if (data.contains("@@{accompanies}@@")){
            return  data.replace("@@{accompanies}@@", visit.getAccompanyCount().toString());
        }

        if (data.contains("@@{url}@@")) {
            String link = "http://vms.tcit.ae/#/approvevisit?id=";
            link = link + EncryptUtil.encrypt(visit.getId().toString()) + "&type=host";
            return data.replace("@@{url}@@", link);
        }
        return data;
    }
    public Optional<List<Visit>> findByVisitorIdAndDateOfVisit(Integer visitorId, LocalDateTime dateOfVisit) {
        return visitRepository.findByVisitorIdAndDateOfVisit(visitorId, dateOfVisit);
    }
    public ResponseDto approveOrRejectVisitorByHost(ApproveRejectDto approveRejectDto, boolean sendMailToVisitor) throws Exception {
        visitRepository.updateApprovedByHost(approveRejectDto.getApprovedByHost(), approveRejectDto.getVisitId(),approveRejectDto.getReason(),approveRejectDto.getComments());
        Visit visits = visitRepository.findById(approveRejectDto.getVisitId()).orElseThrow();
        if (approveRejectDto.getApprovedByHost() == 1 && sendMailToVisitor) {
            this.sendEmailToSecurity(visits);
            return new ResponseDto("SUCCESS", "Visit approved by host", "");
        }
        if (approveRejectDto.getApprovedByHost() == 2 && sendMailToVisitor) {
            visits.setStatus("Declined");
           sendMailService.sendDeclinedEmailToVisitorByHost(visits);
            return new ResponseDto("Error", "Visit Rejected by host", "");
        }
        return null;
    }
    public ResponseDto sendEmailToSecurity(Visit visit) throws Exception {
        MimeMessage msg = javaMailSender.createMimeMessage();
        List<Staff> securities = staffRepository.findByRoleId(4);
        for (Staff staff : securities) {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(staff.getEmail());
            helper.setFrom(sender);
            helper.setSubject("Visit Details");
            String htmlStr = getNewHtmlToSecurity(visit, staff);
            helper.setText(htmlStr, true);
            javaMailSender.send(msg);
            log.info("Security email send to :{}", staff.getEmail());
        }
        return new ResponseDto("SUCCESS", "Visit Details sent to Security ", "");
    }
    private String getNewHtmlToSecurity(Visit visit, Staff staff) {
        Resource resource = resourceLoader.getResource("classpath:approve.html");
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(replaceTokenToSecurity(line, visit, staff));
            }
            String htmlContent = stringBuilder.toString();
            return htmlContent;
        } catch (IOException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    private String replaceTokenToSecurity(String data, Visit visit, Staff staff) throws Exception {
        if (data.contains("@@{name}@@")) {
            return data.replace("@@{name}@@", visit.getVisitor().getName());
        }
        if (data.contains("@@{email}@@")) {
            return data.replace("@@{email}@@", visit.getVisitor().getEmail());
        }
        if (data.contains("@@{mobileNo}@@")) {
            return data.replace("@@{mobileNo}@@", visit.getVisitor().getMobileNo());
        }
        if (data.contains("@@{visitorType}@@")) {
            return data.replace("@@{visitorType}@@", visit.getVisitor().getVisitorType().getName());
        }
        if (data.contains("@@{dateOfVisit}@@")) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return data.replace("@@{dateOfVisit}@@", df.format(visit.getDateOfVisit()));
        }
        if (data.contains("@@{duration}@@")) {
            return data.replace("@@{duration}@@", visit.getDuration());
        }
        if (data.contains("@@{accompanies}@@")){
            return  data.replace("@@{accompanies}@@", visit.getAccompanyCount().toString());
        }
        if (data.contains("@@{url}@@")) {
            String link = "http://vms.tcit.ae/#/approvevisit?id=";
            link = link + EncryptUtil.encrypt(visit.getId().toString()) + "&type=security&securityId=" + EncryptUtil.encrypt(staff.getId().toString()) ;
            return data.replace("@@{url}@@", link);
        }
        return data;
    }
    public ResponseDto approveOrRejectVisitorBySecurity(SecurityApproveRejectDto securityApproveRejectDto, boolean sendMailToVisitor) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException, IOException  {
        visitRepository.updateApprovedBySecurity(securityApproveRejectDto.getApprovedBySecurity(), securityApproveRejectDto.getVisitId(),securityApproveRejectDto.getReason(),securityApproveRejectDto.getComments(),securityApproveRejectDto.getApprovedBySecurityId());
        Visit visit = visitRepository.findById(securityApproveRejectDto.getVisitId()).orElseThrow();
        if (securityApproveRejectDto.getApprovedBySecurity() == 1 && sendMailToVisitor) {
            if (visit.getVisitor().getCryptograph()== null) {
                String crypto = cryptoGeneration.generation(visit.getVisitor(), visit.getDateOfVisit());
                Visitor visitor = visitorRepository.findById(visit.getVisitor().getId()).get();
                visitor.setCryptograph(crypto);
                visitorRepository.save(visit.getVisitor());
                Request request = getRequest(visit.getVisitor());
                biometricsService.enroll(request);
            }
            if(visit.getAccompanies()!=null && visit.getAccompanies().size()>0)
            {
                visit.getAccompanies().stream().forEach(e->{
                    String crypto = cryptoGeneration.generation(e, visit.getDateOfVisit());
                    Visitor visitor = visitorRepository.findById(e.getId()).get();
                    visitor.setCryptograph(crypto);
                    visitorRepository.save(visitor);
                    try {
                        Request request = getRequest(e);
                        biometricsService.enroll(request);
                    } catch (IOException ex) {
                        log.error("IOException in getRequest for Accompany id :{}", e.getId());
                        throw new RuntimeException(ex);
                    }
                });
            }
            sendMailService.sendEmailToVisitor(visit);
            return new ResponseDto("SUCCESS", "Visit approved by Security", "");
        }
        if (securityApproveRejectDto.getApprovedBySecurity() == 2 && sendMailToVisitor) {
            visit.setStatus("Declined");
            sendMailService.sendDeclinedEmailToVisitor(visit);
            return new ResponseDto("Error", "Visit Rejected by Security", "");
        }
        return null;
    }

    public Request getRequest( Visitor visitor) throws IOException {
        Request request=new Request();
        request.setTransactionId(visitor.toString());
        request.setTransactionSource("VMS Khalifa");
        request.setUid(visitor.getId().toString());
        String FOLDER_PATH= "C:/Projects/VMSMedia/image/VISITOR/"+visitor.getId()+"/"+visitor.getProfPicture();
        byte[] file= FileCopyUtils.copyToByteArray(new File(FOLDER_PATH));
        Face face=new Face();
        face.setImage(Base64.getEncoder().encodeToString(file));
        request.setFaceData(face);
        face.setPos("F");
        face.setTemplate(null);
        face.setQuality(Double.valueOf("0.0"));
        return request;
   }
    public VisitResponseDto getVisitDetailsByVisitId(Integer id) {
        Optional<Visit> optionalVisit = Optional.ofNullable(visitRepository.findByIdAndIsActive(id, true).orElseThrow(() -> new UserNotFoundException("Visit not found or InActive!!!!!")));

        if (optionalVisit.isPresent()) {
            Visit visit = optionalVisit.get();
            return convertDto(visit);
        } else {
            return null;
        }
    }
    /*private VisitResponseDto convertDto(Visit visit) {
        VisitResponseDto dto = new VisitResponseDto();
        dto.setVisitorId(visit.getVisitor().getId());
        dto.setVisitId(visit.getId());
        dto.setName(visit.getVisitor().getName());
        dto.setEmiratesId(visit.getVisitor().getEmiratesId());
        dto.setEmail(visit.getVisitor().getEmail());
        dto.setMobileNo(visit.getVisitor().getMobileNo());
        dto.setVisitorTypeId(visit.getVisitor().getVisitorType().getId());
        dto.setVisitorType(visit.getVisitor().getVisitorType().getName());
        dto.setProfPicture(visit.getVisitor().getProfPicture());
        dto.setDateOfVisit(visit.getDateOfVisit());
        dto.setDuration(visit.getDuration());
        if(visit.getStaff()!=null)
        dto.setHostId(visit.getStaff().getId());
        if(visit.getStaff()!=null)
        dto.setHostName(visit.getStaff().getStaffName());
        dto.setDeptId(visit.getDepartment().getId());
        dto.setDepartmentName(visit.getDepartment().getDeptName());
        dto.setCampusId(visit.getCampus().getId());
        dto.setCampusName(visit.getCampus().getCampusName());
        dto.setStatus(visit.getStatus());
        dto.setApprovedByHost(visit.getApprovedByHost());
        dto.setApprovedBySecurity(visit.getApprovedBySecurity());
        if(visit.getReason()!=null)
        dto.setReasonName(visit.getReason().getReasonName());
        dto.setComments(visit.getComments());
        dto.setAccompanyCount(visit.getAccompanyCount());



            if(dto.getAccompanyDetails() != null && !dto.getAccompanyDetails().isEmpty()) {
                dto.setAccompanyDetails(visit.getAccompanies().stream().forEach(e -> {
                    // VisitRequestCreateDto dto = new VisitRequestCreateDto();
                    dto.setName(e.getName());
                    dto.setMobileNo(e.getMobileNo());
                    dto.setEmail(e.getEmail());
                    dto.setProfPicture(e.getProfPicture());
                    dto.setEmiratesId(e.getEmiratesId());
                });
            }
        return dto;
    }
*/
   /* public VisitResponseDto getVisitDetailsByVisitId(Integer id) {
        Optional<Visit> optionalVisit = Optional.ofNullable(visitRepository.findByIdAndIsActive(id, true).orElseThrow(() -> new UserNotFoundException("Visit not found or InActive!!!!!")));

        if (optionalVisit.isPresent()) {
            Visit visit = optionalVisit.get();
            return convertDto(visit, VisitAccompanyDto.builder().build());
        } else {
            return null;
        }
    }*/
    private VisitResponseDto convertDto(Visit visit) {
        VisitResponseDto dto = new VisitResponseDto();
        dto.setVisitorId(visit.getVisitor().getId());
        dto.setVisitId(visit.getId());
        dto.setName(visit.getVisitor().getName());
        dto.setEmiratesId(visit.getVisitor().getEmiratesId());
        dto.setEmail(visit.getVisitor().getEmail());
        dto.setMobileNo(visit.getVisitor().getMobileNo());
        dto.setVisitorTypeId(visit.getVisitor().getVisitorType().getId());
        dto.setVisitorType(visit.getVisitor().getVisitorType().getName());
        dto.setProfPicture(visit.getVisitor().getProfPicture());
        dto.setDateOfVisit(visit.getDateOfVisit());
        dto.setDuration(visit.getDuration());
        if(visit.getStaff()!=null)
            dto.setHostId(visit.getStaff().getId());
        if(visit.getStaff()!=null)
            dto.setHostName(visit.getStaff().getStaffName());
        dto.setDeptId(visit.getDepartment().getId());
        dto.setDepartmentName(visit.getDepartment().getDeptName());
        dto.setCampusId(visit.getCampus().getId());
        dto.setCampusName(visit.getCampus().getCampusName());
        dto.setStatus(visit.getStatus());
        dto.setApprovedByHost(visit.getApprovedByHost());
        dto.setApprovedBySecurity(visit.getApprovedBySecurity());
        if(visit.getReason()!=null)
            dto.setReasonName(visit.getReason().getReasonName());
        dto.setComments(visit.getComments());
        dto.setAccompanyCount(visit.getAccompanyCount());
        if(visit.getAccompanies() != null && !visit.getAccompanies().isEmpty()) {
            List<VisitorAccompanyDto> dtoList = visit.getAccompanies().stream().map(e -> {
                VisitorAccompanyDto accompanyDto = new VisitorAccompanyDto();
                accompanyDto.setName(e.getName());
                accompanyDto.setMobileNo(e.getMobileNo());
                accompanyDto.setEmail(e.getEmail());
                accompanyDto.setPicture(e.getProfPicture());
                accompanyDto.setEmiratesId(e.getEmiratesId());
                return accompanyDto;
            }).collect(Collectors.toList());
            dto.setAccompanyDetails(dtoList);
        }

        return dto;
    }

    public VisitResponseDto GetVisitByVisitId(VisitByVistIdDto visitByVistIdDto) throws Exception {
        String decryptId = EncryptUtil.decrypt(visitByVistIdDto.getId());

        Optional<Visit> optionalVisit = Optional.ofNullable(visitRepository.findByIdAndIsActive(Integer.valueOf(decryptId), true)).orElseThrow(() -> new UserNotFoundException("Visit not found or InActive!!!!!"));

        if (optionalVisit.isPresent()) {
            Visit visit = optionalVisit.get();
            return convertDto(visit);
        } else {
            return null;
        }
    }
  /* public VisitResponseDto GetVisitByVisitId(VisitByVistIdDto visitByVistIdDto) throws Exception {
       String decryptId = EncryptUtil.decrypt(visitByVistIdDto.getId());

       Optional<Visit> optionalVisit = Optional.ofNullable(visitRepository.findByIdAndIsActive(Integer.valueOf(decryptId), true)).orElseThrow(() -> new UserNotFoundException("Visit not found or InActive!!!!!"));

       if (optionalVisit.isPresent()) {
           Visit visit = optionalVisit.get();
           return convertDto(visit, VisitAccompanyDto.builder().build());
       } else {
           return null;
       }
   }*/
    public List<ResponseVisitsListSecurityApprovalDto> getAllVisitsForSecurityApproval() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusYears(1);
        String status = "Pending";
        List<Visit> visitList = visitRepository.findAllVisitorsWithFiltersForSecurityApproval(startDate, endDate, status);
        List<ResponseVisitsListSecurityApprovalDto> visitResponseDtoList = visitList.stream().map(this::convertToDtoSecurity).collect(Collectors.toList());
        return visitResponseDtoList;
    }
    private ResponseVisitsListSecurityApprovalDto convertToDtoSecurity(Visit visit) {
        ResponseVisitsListSecurityApprovalDto dto = new ResponseVisitsListSecurityApprovalDto();
        dto.setVisitorId(visit.getVisitor().getId());
        dto.setVisitId(visit.getId());
        dto.setName(visit.getVisitor().getName());
        dto.setEmiratesId(visit.getVisitor().getEmiratesId());
        dto.setEmail(visit.getVisitor().getEmail());
        dto.setMobileNo(visit.getVisitor().getMobileNo());
        dto.setVisitorTypeId(visit.getVisitor().getVisitorType().getId());
        dto.setVisitorType(visit.getVisitor().getVisitorType().getName());
        dto.setProfPicture(visit.getVisitor().getProfPicture());
        dto.setDateOfVisit(visit.getDateOfVisit());
        dto.setDuration(visit.getDuration());
        if(visit.getStaff()!=null)
        dto.setHostId(visit.getStaff().getId());
        if(visit.getStaff()!=null)
        dto.setHostName(visit.getStaff().getStaffName());
        dto.setDeptId(visit.getDepartment().getId());
        dto.setDepartmentName(visit.getDepartment().getDeptName());
        dto.setCampusId(visit.getCampus().getId());
        dto.setCampusName(visit.getCampus().getCampusName());
        dto.setApprovedByHost(visit.getApprovedByHost());
        dto.setApprovedBySecurty(visit.getApprovedBySecurity());
        dto.setStatus("Pending");
        dto.setAccompanyCount(visit.getAccompanyCount());
        return dto;
    }
    public List<ResponseVisitsListTabDto> GetAllVisitsForTab() {

        LocalDateTime startDate = LocalDateTime.now().with(LocalTime.now().minusMinutes(30));
        LocalDateTime endDate = LocalDateTime.now().with(LocalTime.now().plusMinutes(30));
        String status = "Pending";
        List<Visit> visits = visitRepository.findAllVisitsWithFiltersForTab(startDate, endDate, status);
        List<ResponseVisitsListTabDto> visitResponseDtoList = visits.stream().map(this::convertToDtoTab).collect(Collectors.toList());
        return visitResponseDtoList;
    }
    private ResponseVisitsListTabDto convertToDtoTab(Visit visit) {
        ResponseVisitsListTabDto dto = new ResponseVisitsListTabDto();
        dto.setVisitorId(visit.getVisitor().getId());
        dto.setVisitId(visit.getId());
        dto.setName(visit.getVisitor().getName());
        dto.setEmail(visit.getVisitor().getEmail());
        dto.setMobileNo(visit.getVisitor().getMobileNo());
        dto.setProfPicture(visit.getVisitor().getProfPicture());
        dto.setDateOfVisit(visit.getDateOfVisit());
        dto.setDuration(visit.getDuration());
        return dto;
    }
    public List<ResponseVisitsListDto> getVisitList(SearchVisitRequestDto requestDto) {
        //LocalDateTime currenTime=LocalDateTime.now();
        //LocalDateTime thresholdTime=currenTime.minusMinutes(10);
        visitRepository.updateStatus(LocalDateTime.now().minusMinutes(10));
        //visitRepository.updateStatus(LocalDateTime.now()..plusMinutes(10));
        LocalDateTime startDate = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (requestDto.getFromDate() == null || requestDto.getFromDate().isEmpty()) {
            startDate = LocalDateTime.now().with(LocalTime.MIDNIGHT);
            requestDto.setFromDate(startDate.format(df));
        }
        LocalDateTime endDate = null;
        if (requestDto.getToDate() == null || requestDto.getToDate().isEmpty()) {
            endDate = LocalDateTime.now().plusDays(1).with(LocalTime.MIDNIGHT);
            requestDto.setToDate(endDate.format(df));
        }
        List<Visit> visits = visitRepository.findAllVisitsWithFilters(requestDto);
        //visitRepository.updateStatus("missed","Pending",LocalDateTime.now());
        List<ResponseVisitsListDto> dtos = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(1);
        if (visits != null && visits.size() > 0) {
            log.info("visits size{}", visits.size());
            AtomicReference<ResponseVisitsListDto> dto = new AtomicReference<>(new ResponseVisitsListDto());
            visits.forEach(e -> {
                dto.set(convertToDto(e, count.get()));
                count.getAndIncrement();
                dtos.add(dto.get());
            });
        }
        return dtos;
    }
    private ResponseVisitsListDto convertToDto(Visit visit, int count) {
        ResponseVisitsListDto dto = new ResponseVisitsListDto();
        dto.setSNo(count);
        dto.setVisitorId(visit.getVisitor().getId());
        dto.setVisitId(visit.getId());
        dto.setName(visit.getVisitor().getName());
        dto.setMobileNo(visit.getVisitor().getMobileNo());
        /*if(visit.getStaff()!=null)
        dto.setHostId(visit.getStaff().getId());
        if(visit.getStaff()!=null)
        dto.setHostName(visit.getStaff().getStaffName());*/
        dto.setDateOfVisit(visit.getDateOfVisit());
        dto.setStatus(visit.getStatus());
        dto.setDuration(visit.getDuration());
        dto.setAccompanyCount(visit.getAccompanyCount());
        return dto;
    }
  public List<VisitStaffResponseDto> GetAllVisitsHistory(SearchVisitRequestHistoryDto requestDto) {
      visitRepository.updateStatus(LocalDateTime.now().minusMinutes(10));
       LocalDateTime startDate = null;
       DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
       if (requestDto.getFromDate() == null || requestDto.getFromDate().isEmpty()) {
           startDate = LocalDateTime.now().with(LocalTime.MIDNIGHT);
           requestDto.setFromDate(startDate.format(df));
       }
       LocalDateTime endDate = null;
       if (requestDto.getToDate() == null || requestDto.getToDate().isEmpty()) {
           endDate = LocalDateTime.now().plusDays(1).with(LocalTime.MIDNIGHT);
           requestDto.setToDate(endDate.format(df));
       }
       List<Visit> visits = visitRepository.findAllVisitsWithFiltersHistory(requestDto);
       List<Visit> visitList=visits.stream().filter(e->{
           if (e.getDateOfVisit().isBefore(LocalDateTime.now())
           ) {
                   return true;
               }
           else {
               if ((e.getApprovedBySecurity() == 1 || e.getApprovedBySecurity() == 2) && (e.getApprovedByHost() == 1)) {
                   return true;
               } else {
                   return false;
               }
           }
       }).collect(Collectors.toList());
       visitList=visitList.stream().filter(e->{
           if(requestDto.getSearchText()!=null && !requestDto.getSearchText().isEmpty())
           {
              //if.getVisitor().getName().contains(requestDto.getSearchText()) || (e.getVisitor().getMobileNo().contains(requestDto.getSearchText()) || (e.getVisitor().getEmail().contains(requestDto.getSearchText()) ))){
                  if(e.getVisitor().getName().toLowerCase().contains (requestDto.getSearchText().toLowerCase()) || (e.getVisitor().getMobileNo().contains(requestDto.getSearchText()) || (e.getVisitor().getEmail().toLowerCase().contains(requestDto.getSearchText().toLowerCase()) ))){

                   return true;
               }
               else {
                   return false;
               }
           }
           else {
               return true;
           }
       }).collect(Collectors.toList());
       List<VisitStaffResponseDto> visitResponseDtoList = visitList.stream().map(this::convertDtoLists).collect(Collectors.toList());
       return visitResponseDtoList;
   }

    public VisitResponseDto getVisitDetailsByVisitorId(Integer id) {
        Optional<Visit> visitList = visitRepository.findFirstByVisitorIdOrderByCreatedDateDesc(id);
        if (visitList.isPresent()) {
            VisitResponseDto visitResponseDtoList =convertDtos(visitList.get());
            return visitResponseDtoList;
        }
       else{
            Optional<Visitor> visitorList = visitorRepository.findById(id);
            if (visitorList.isPresent()) {
                VisitResponseDto visitResponseDtoList =convertDtovisitor(visitorList.get());
                return visitResponseDtoList;
            }
        }
        return null;
    }
    private VisitResponseDto convertDtos(Visit visit) {
        VisitResponseDto dto = new VisitResponseDto();
        dto.setVisitorId(visit.getVisitor().getId());
        dto.setVisitId(visit.getId());
        dto.setName(visit.getVisitor().getName());
        dto.setEmiratesId(visit.getVisitor().getEmiratesId());
        dto.setEmail(visit.getVisitor().getEmail());
        dto.setMobileNo(visit.getVisitor().getMobileNo());
        dto.setVisitorTypeId(visit.getVisitor().getVisitorType().getId());
        dto.setVisitorType(visit.getVisitor().getVisitorType().getName());
        dto.setProfPicture(visit.getVisitor().getProfPicture());
        dto.setDateOfVisit(visit.getDateOfVisit());
        dto.setDuration(visit.getDuration());
        if(visit.getStaff()!=null)
        dto.setHostId(visit.getStaff().getId());
        if(visit.getStaff()!=null)
        dto.setHostName(visit.getStaff().getStaffName());
        dto.setDeptId(visit.getDepartment().getId());
        dto.setDepartmentName(visit.getDepartment().getDeptName());
        dto.setCampusId(visit.getCampus().getId());
        dto.setCampusName(visit.getCampus().getCampusName());
        return dto;
    }
    private VisitResponseDto convertDtovisitor(Visitor visitor)
    {
        VisitResponseDto dto=new VisitResponseDto();
        dto.setVisitorId(visitor.getId());
        dto.setName(visitor.getName());
        dto.setMobileNo(visitor.getMobileNo());
        dto.setEmail(visitor.getEmail());
        dto.setVisitorTypeId(visitor.getVisitorType().getId());
        dto.setProfPicture(visitor.getProfPicture());
        dto.setEmiratesId(visitor.getEmiratesId());
        return dto;
    }
       public void updateBiometricVerified(VisitStatusRequestDto dto) {
        Visit visitDb = getVisitById(dto.getVisitId());

        if (dto.getIsBiometricVerified() != null) {
            visitDb.setIsBioMetricVerified(dto.getIsBiometricVerified());
        }
        visitDb= visitRepository.save(visitDb);
    }
      public ResponseDto updateVisitStatus(VisitStatusRequestDto dto) {
        // updateBiometricVerified(dto);
        //visitRepository.updateBioStatus(LocalDateTime.now());
        Visit visit = null;
        LocalDateTime currentTime = LocalDateTime.now();
        String message = null;
        visit = visitRepository.findById(dto.getVisitId()).orElse(null);
        if (visit != null) {
            visitRepository.save(visit);
            if (dto.getStatus().equals("checkedin")) {
                if(dto.getIsBiometricVerified().equals(false)) {
                    visit.setIsBioMetricVerified(dto.getIsBiometricVerified());
                }
                else {
                    visit.setIsBioMetricVerified(true);
                }
                visit.setCheckIn(LocalDateTime.now());
                visit.setStatus(dto.getStatus());
            } else {
                visit.setCheckOut(LocalDateTime.now());
                visit.setStatus("completed");
            }
            visitRepository.save(visit);
            message = "Details Updated Successfully";
        }else {
            message="Visit Details Not Available";
        }
        ResponseDto responseDto= new ResponseDto("",message,"");
        return  responseDto;
    }
    public List<VisitStaffResponseDto> getVisitDetailsByStaffId(Integer id) {
        List<Visit> visits = visitRepository.findByStaffId(id);
           List<VisitStaffResponseDto> visitResponseDtoList=  visits.stream().filter(Visit::isActive).map(this::convertDtoLists).collect(Collectors.toList());
             return visitResponseDtoList;
       }
    private VisitStaffResponseDto convertDtoLists(Visit visit) {
        VisitStaffResponseDto dto = new VisitStaffResponseDto();
        dto.setVisitorId(visit.getVisitor().getId());
        dto.setVisitId(visit.getId());
        dto.setName(visit.getVisitor().getName());
        dto.setEmiratesId(visit.getVisitor().getEmiratesId());
        dto.setEmail(visit.getVisitor().getEmail());
        dto.setMobileNo(visit.getVisitor().getMobileNo());
        dto.setVisitorTypeId(visit.getVisitor().getVisitorType().getId());
        dto.setVisitorType(visit.getVisitor().getVisitorType().getName());
        dto.setProfPicture(visit.getVisitor().getProfPicture());
        dto.setDateOfVisit(visit.getDateOfVisit());
        dto.setDuration(visit.getDuration());

       if(visit.getStaff()!=null)
        dto.setHostId(visit.getStaff().getId());
        if(visit.getStaff()!=null)
        dto.setHostName(visit.getStaff().getStaffName());
        dto.setDeptId(visit.getDepartment().getId());
        dto.setDepartmentName(visit.getDepartment().getDeptName());
        dto.setCampusId(visit.getCampus().getId());
        dto.setCampusName(visit.getCampus().getCampusName());
        dto.setApprovedByHost(visit.getApprovedByHost());
        dto.setApprovedBySecurity(visit.getApprovedBySecurity());
        dto.setStatus(visit.getStatus());
        dto.setIsActive(true);
        if(visit.getReason()!=null)
        dto.setReasonName(visit.getReason().getReasonName());
        dto.setComments(visit.getComments());
        dto.setAccompanyCount(visit.getAccompanyCount());
        return dto;
    }
    private VisitResponseDto convertDtoList(Visit visit) {
        VisitResponseDto dto = new VisitResponseDto();
        dto.setVisitorId(visit.getVisitor().getId());
        dto.setVisitId(visit.getId());
        dto.setName(visit.getVisitor().getName());
        dto.setEmiratesId(visit.getVisitor().getEmiratesId());
        dto.setEmail(visit.getVisitor().getEmail());
        dto.setMobileNo(visit.getVisitor().getMobileNo());
        dto.setVisitorTypeId(visit.getVisitor().getVisitorType().getId());
        dto.setVisitorType(visit.getVisitor().getVisitorType().getName());
        dto.setDateOfVisit(visit.getDateOfVisit());
        dto.setDuration(visit.getDuration());
        if(visit.getStaff()!=null)
        dto.setHostId(visit.getStaff().getId());
        if(visit.getStaff()!=null)
        dto.setHostName(visit.getStaff().getStaffName());
        dto.setDeptId(visit.getDepartment().getId());
        dto.setDepartmentName(visit.getDepartment().getDeptName());
        dto.setCampusId(visit.getCampus().getId());
        dto.setCampusName(visit.getCampus().getCampusName());
        dto.setStatus(visit.getStatus());
        return dto;
    }
       public VisitCountDto getVisitCount() {
        LocalDateTime localDateTime = LocalDateTime.now().with(LocalTime.MIDNIGHT);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDate = localDateTime.format(df);
        localDateTime = LocalDateTime.now().plusDays(1).with(LocalTime.MIDNIGHT);
        String endDate = localDateTime.format(df);
        localDateTime=LocalDateTime.now().plusDays(1).with(LocalTime.MIDNIGHT).plusMinutes(10);
        String endDates = localDateTime.format(df);
        VisitCountDto dto=new VisitCountDto();

        dto.setTotalVisitCount(visitRepository.getTotalVisitCount(startDate,endDate));
        dto.setVisitPendingCount(visitRepository.getVisitPendingCount(startDate,endDate));

        dto.setVisitCheckedInCount(visitRepository.getVisitCheckedInCount(startDate,endDate));
        dto.setVisitVerifiedCount(visitRepository.getVisitVerifiedCount(startDate,endDate));

        dto.setVisitCompletedCount(visitRepository.getVisitCompletedCount(startDate,endDate));
        dto.setVisitMissedCount(visitRepository.getVisitMissedCount(startDate,endDates));
        return dto;
    }
    public List<VisitResponseDto> getAllPendingVisits() {
        List<Visit> visits = visitRepository.findByStatusAndIsBioMetricVerified("pending",false);
        List<VisitResponseDto> visitResponseDtoList=  visits.stream().filter(Visit::isActive).map(this::convertDtoList).collect(Collectors.toList());
        return visitResponseDtoList;
    }
    public Visit getVisitById(Integer id) throws RuntimeException {
        return visitRepository.findById(id).orElseThrow(() -> new RuntimeException("No Visit found for ID " + id));
    }
    public Visit createNewVisit(Visit visitorDetails) {
        return visitRepository.save(visitorDetails);
    }
    public void updateInActive(Integer id) {
        visitRepository.updateInActive(id);
    }
}









