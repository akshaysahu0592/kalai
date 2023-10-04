package com.tcit.vms.vms.service;

import com.tcit.vms.vms.dto.request.*;
import com.tcit.vms.vms.dto.response.ResponseDto;
import com.tcit.vms.vms.exception.ApplicationValidationException;
import com.tcit.vms.vms.exception.UserNotFoundException;
import com.tcit.vms.vms.model.Department;
import com.tcit.vms.vms.model.Staff;

import com.tcit.vms.vms.model.Visitor;
import com.tcit.vms.vms.model.VisitorType;
import com.tcit.vms.vms.repository.StaffRepository;

import com.tcit.vms.vms.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.*;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StaffService {
    private Random random = new Random();
    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 1440;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StorageService storageService;
    @Autowired
    PasswordEncoder encoder;
    @Value("${spring.mail.username}")
    private String sender;

    public List<Staff> getStaffs() {
        List<Staff> allStaffs = staffRepository.findAll();
        return allStaffs.stream().filter(s -> Objects.nonNull(s.isActive()) && s.isActive()).collect(Collectors.toList());
    }
    public List<Staff> getActiveStaffs() {
        List<Staff> allStaffs = staffRepository.findAll();
        return allStaffs.stream().filter(s -> Objects.nonNull(s.isActive()) && s.isActive()).collect(Collectors.toList());
    }
    public Staff deleteStaff(Integer id) {
        Staff staff1 = getStaffById(id);
        staff1.setActive(false);
        return staffRepository.save(staff1);
    }
    public List<Staff> getStaffsByDepartmentId(Integer departmentId) {
        return staffRepository.findByDepartmentId(departmentId);
    }
    public Optional<Staff> getStaffByIdList(Integer id) {
        return staffRepository.findById(id);
    }
      public Staff getStaffById(Integer id) {
       Optional<Staff> staff=staffRepository.findById(id);
       if(staff.isPresent())
       {
           return staff.get();
       }
       throw new UserNotFoundException("Staff not found");
   }
    public Staff getStaffProfileById(Integer id) {
        return staffRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Staff not found!!!!!"));
    }
    public Staff findByIdAndIsActive(Integer id) {
        return staffRepository.findByIdAndIsActive(id, true).orElseThrow(() -> new UserNotFoundException("Staff not found or InActive!!!!!"));
    }
    /*public ResponseDto createStaff(StaffRequestDto staffRequestDto) throws IOException {
        ResponseDto responseDto=null;
        Staff staff = null;
        try {
            log.info("StaffService request # {}", staffRequestDto);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Department dept=departmentService.getDepartmentById(staffRequestDto.getDepartmentId());
            staff = new Staff();
            staff.setId(staffRequestDto.getId());
            staff.setStaffName(staffRequestDto.getStaffName());
            staff.setMobileNo(staffRequestDto.getMobileNo());
            staff.setEmail(staffRequestDto.getEmail());
            staff.setPassword(passwordEncoder.encode(staffRequestDto.getPassword()));
            staff.setDepartment(dept);
            staff.setDesignation(staffRequestDto.getDesignation());
            staff.setRole(roleService.getRoleById(staffRequestDto.getRoleId()));
            staff.setCreatedDate(LocalDateTime.now());
            staff.setActive(true);
            staff=staffRepository.save(staff);
            if (staffRequestDto.getProfPicture() != null && !staffRequestDto.getProfPicture().isEmpty()) {

                log.info("Upload Picture for StaffId# {}", staff.getId());
                responseDto = storageService.uploadImage(staffRequestDto.getProfPicture(),
                        staff.getId(),
                       "jpg" ,
                        "STAFF");
                staff.setProfPicture((String) responseDto.getData());
            }
            else {
                staff.setProfPicture(null);
            }
            responseDto= new ResponseDto("SUCCESS", "Staff Added Successfully", staff.getId());
        } catch (Exception e) {
            e.printStackTrace();
            responseDto= new ResponseDto("Error", "Staff email already exists","" );
        }
        return responseDto;
    }
*/
    public ResponseDto createStaff(StaffRequestDto staffRequestDto) throws IOException {
        List<Staff> staffs = staffRepository.findByEmail(staffRequestDto.getEmail());
        Staff staff=null;
        ResponseDto responseDto=null;
        if (staffs != null && !staffs.isEmpty()) {
            staff = staffs.stream().filter(e->e.isActive()).findFirst().orElse(null);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Department dept = departmentService.getDepartmentById(staffRequestDto.getDepartmentId());
        if (staff == null) {
        try {
            log.info("StaffService request # {}", staffRequestDto);

          staff = new Staff();
            staff.setId(staffRequestDto.getId());
            staff.setStaffName(staffRequestDto.getStaffName());
            staff.setMobileNo(staffRequestDto.getMobileNo());
            staff.setEmail(staffRequestDto.getEmail());
            staff.setPassword(passwordEncoder.encode(staffRequestDto.getPassword()));
            staff.setDepartment(dept);
            staff.setDesignation(staffRequestDto.getDesignation());
            staff.setRole(roleService.getRoleById(staffRequestDto.getRoleId()));
            staff.setCreatedDate(LocalDateTime.now());
            staff.setActive(true);
            staff = staffRepository.save(staff);

            if (staffRequestDto.getProfPicture() != null && !staffRequestDto.getProfPicture().isEmpty()) {

                log.info("Upload Picture for StaffId# {}", staff.getId());
                responseDto = storageService.uploadImage(staffRequestDto.getProfPicture(),
                        staff.getId(),
                        "jpg",
                        "STAFF");
                staff.setProfPicture((String) responseDto.getData());
                staffRepository.save(staff);
            }
          else {
                staff.setProfPicture(null);
           }
            return new ResponseDto("SUCCESS", "Staff Added Successfully", staff.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        return  new ResponseDto("Error", "Staff email already exists","" );
    }
    public ResponseDto updateStaff(StaffReqUpdateDto staffReqUpdateDto) throws IOException {
        log.info("StaffService request # {}", staffReqUpdateDto);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ResponseDto responseDto;
        try {
            Staff staff = staffRepository.findById(staffReqUpdateDto.getId()).orElse(null);
            if (staff.getRole().getId() == 1) {
                throw new UserNotFoundException("Updation not allowed");
            }
            if (staff == null) {
                throw new UserNotFoundException("Staff Not Found with Id " + staffReqUpdateDto.getId());
            }
            staff.setStaffName(staffReqUpdateDto.getStaffName());
            staff.setEmail(staffReqUpdateDto.getEmail());
            staff.setMobileNo(staffReqUpdateDto.getMobileNo());
            staff.setPassword(passwordEncoder.encode(staffReqUpdateDto.getPassword()));
            staff.setDepartment(departmentService.getDepartmentById(staffReqUpdateDto.getDepartmentId()));
            staff.setRole(roleService.getRoleById(staffReqUpdateDto.getRoleId()));
            staff.setDesignation(staffReqUpdateDto.getDesignation());

            staff = staffRepository.save(staff);

        responseDto= new ResponseDto("SUCCESS", "Staff Updated Successfully", staff.getId());
        }
        catch (Exception e) {
        e.printStackTrace();
        responseDto= new ResponseDto("Error", "Staff email already exists","" );
    }
        return responseDto;
    }
    public ResponseDto UpdateStaffProfile(StaffReqUpdateProfileDto dto) throws IOException {
        log.info("StaffService request # {}", dto);
        ResponseDto responseDto;
        try{
        Staff staff = staffRepository.findById(dto.getId()).orElse(null);
               if (staff.getRole().getId() == 1) {
            throw new UserNotFoundException("Updation not allowed");
        }
        if (staff == null) {
            throw new UserNotFoundException("Staff Not Found with Id " + dto.getId());
        }
        staff.setStaffName(dto.getStaffName());
        staff.setEmail(dto.getEmail());
        staff.setMobileNo(dto.getMobileNo());
        staff.setDepartment(departmentService.getDepartmentById(dto.getDepartmentId()));
        staff.setRole(roleService.getRoleById(dto.getRoleId()));
        staff = staffRepository.save(staff);
            responseDto= new ResponseDto("SUCCESS", "Staff  Profile Updated Successfully", staff.getId());
        }
        catch (Exception e) {
            e.printStackTrace();
            responseDto= new ResponseDto("Error", "Staff email already exists","" );
        }
        return responseDto;
    }
    public ResponseDto forgotPassword(String email) throws Exception {
        List<Staff> staffDb = staffRepository.findByEmail(email);

        if (staffDb == null || staffDb.isEmpty()) {

            return new ResponseDto("Error", "Invalid email id.", "");
        }
        Staff staff=staffDb.stream().filter(e->e.isActive()).findFirst().get();

        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        String encData=staff.getId().toString()+"@"+df.format(LocalDateTime.now().plusHours(24)).toString();
        String data=EncryptUtil.encrypt(encData);
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(staff.getEmail());
        helper.setFrom(sender);
        helper.setSubject("Password reset link");
        String text = "http://vms.tcit.ae/#/resetpassword?id=";
        text = text +  data+ "&type=host";
        String htmlStr= getHtml(staff.getStaffName(),text);
        helper.setText(htmlStr,true);
        javaMailSender.send(msg);
        log.info("forgotPassword email send to :{}", staff.getEmail());
        return new ResponseDto("SUCCESS", "Id and Expiry date details has been sent to your mail id ", staff.getEmail());
    }
   public ResponseDto resetPassword(ResetPasswordDto resetPasswordDto) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeyException, ClassNotFoundException, MessagingException {
        //ResetPasswordDto dto=new ResetPasswordDto();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
       try {
           DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
           String expDateStr = EncryptUtil.decrypt(resetPasswordDto.getKey());
           String[] data = expDateStr.split("@");
           LocalDateTime expiryDate = LocalDateTime.parse(data[1], df);
           if (expiryDate.isBefore(LocalDateTime.now())) {
               throw new ApplicationValidationException("Reset Password Link Expired");
           }
           Integer id = Integer.valueOf((data[0]));
           log.info("text={}", id);
           Optional<Staff> optionalStaff = staffRepository.findById(id);
           if (optionalStaff.isPresent()) {
               Staff staff = optionalStaff.get();
               staff.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
               staffRepository.save(staff);
               MimeMessage msg = javaMailSender.createMimeMessage();

               MimeMessageHelper helper = new MimeMessageHelper(msg, true);
               helper.setTo(staff.getEmail());

               helper.setFrom(sender);
               helper.setSubject("Password Updated");

               String htmlStr = getSuccessHtml(staff.getStaffName());
               helper.setText(htmlStr, true);
               javaMailSender.send(msg);
               return new ResponseDto("SUCCESS", "Password changed", "");
           } else {
               return new ResponseDto("Error", "Please provide valid Staff Id", "");
           }
       }
       catch (Exception e)
       {
           e.printStackTrace();
           return new ResponseDto("Error", "Reset password url is expired", "");
       }
    }
    @Autowired
    private ResourceLoader resourceLoader;
    private String getHtml(String email,String link){
        Resource resource = resourceLoader.getResource("classpath:generic.html");

        try (InputStream inputStream = resource.getInputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(replaceToken(line, email, link));
            }

            String htmlContent = stringBuilder.toString();
            // Process the HTML content as needed
            //System.out.println(htmlContent);
            return htmlContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String replaceToken(String data,String email, String link){
        if (data.contains("@@{name}@@")){
            return  data.replace("@@{name}@@", email);
        }
        if (data.contains("@@{url}@@")){
            return  data.replace("@@{url}@@", link);
        }
        return data;
    }
    private String getSuccessHtml(String email){
        Resource resource = resourceLoader.getResource("classpath:SuccessPassword.html");
        try (InputStream inputStream = resource.getInputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(replaceSuccessToken(line, email));
            }
            String htmlContent = stringBuilder.toString();
            // Process the HTML content as needed
            //System.out.println(htmlContent);
            return htmlContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String replaceSuccessToken(String data,String email){
        if (data.contains("@@{name}@@")){
            return  data.replace("@@{name}@@", email);
        }
        return data;
    }
}






