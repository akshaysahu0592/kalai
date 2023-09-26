package com.tcit.vms.vms.controller;

import com.tcit.vms.vms.dto.request.*;
import com.tcit.vms.vms.dto.response.*;
import com.tcit.vms.vms.model.Staff;
import com.tcit.vms.vms.repository.StaffRepository;
import com.tcit.vms.vms.service.DepartmentService;
import com.tcit.vms.vms.service.StaffService;
import com.tcit.vms.vms.service.StorageService;
import com.tcit.vms.vms.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController

@RequestMapping("/api")
public class StaffController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private StaffRepository staffRepository;
    @Value("${profile.picture.base.url}") private String pictureUrl;

    @GetMapping("/GetAllStaffs")
    public List<ResponseStaffDto> staffs() {
        List<Staff> staff = staffService.getStaffs();
        return staff.stream().map(e -> ResponseStaffDto.builder(e)).collect(Collectors.toList());
    }

    @GetMapping("/GetStaffsList")
    public List<ResponseStaffListDto> getStaff() {
        List<Staff> staffList = staffService.getActiveStaffs();
        return staffList.stream().map(e -> ResponseStaffListDto.builder(e)).collect(Collectors.toList());
    }

    /*@PostMapping("/AddStaff")
    public ResponseDto createStaff(@RequestBody StaffRequestDto staffRequestDto) throws IOException {

        Staff staff=staffService.createStaff(staffRequestDto);
        return new ResponseDto("SUCCESS","Staff Added Successfully", staff.getId());
    }*/
    @PostMapping("/AddStaff")
    public ResponseDto createStaff(@RequestBody StaffRequestDto staffRequestDto) throws IOException {

        return staffService.createStaff(staffRequestDto);
       // return new ResponseDto("SUCCESS","Staff Added Successfully", staff.getId());
    }
    @PutMapping("/UpdateStaff")
    public ResponseDto updateStaff(@RequestBody StaffReqUpdateDto staffReqUpdateDto) throws IOException {

            return staffService.updateStaff(staffReqUpdateDto);

            //return new ResponseDto("SUCCESS", "Staff Updated Successfully",staff.getId());
           }
    @PutMapping("/UpdateStaffProfile")
    ResponseDto UpdateStaffProfile(@RequestBody StaffReqUpdateProfileDto dto) throws IOException {

        return staffService.UpdateStaffProfile(dto);


        }

    @GetMapping("/GetStaffByStaffIdList/{id}")
    public List<ResponseStaffListDto> getStaffByIdList(@PathVariable Integer id) {
        Optional<Staff> staff = staffService.getStaffByIdList(id);
        return staff.stream().filter(Staff::isActive).map(ResponseStaffListDto::builder).collect(Collectors.toList());
    }

    @GetMapping("/GetStaffByStaffId/{id}")
    public ResponseStaffDto getStaffById(@PathVariable Integer id) {
        Staff staff = staffService.getStaffById(id);
        return ResponseStaffDto.builder(staff);
    }
    @GetMapping("/getStaffProfile/{id}")
    public ResponseStaffProfileDto getStaffProfileById (@PathVariable("id") Integer userId) {
        Staff staff= staffService.getStaffProfileById(userId);
        return ResponseStaffProfileDto.builder(staff);
    }

    @DeleteMapping("/DeleteStaff")
    public ResponseDto deleteStaff(@RequestBody Staff staff)
    {
        staffService.deleteStaff(staff.getId());
        return new ResponseDto("SUCCESS", "Staff Details Deleted ", "");
    }
    @GetMapping("/GetStaffsByDepartmentIdList/{id}")
    public List<ResponseStaffInDepartmentDto> getStaffsByDepartmentId(@PathVariable Integer id) {
        List<Staff> staff = staffService.getStaffsByDepartmentId(id);
        return staff.stream().filter(Staff::isActive).map(ResponseStaffInDepartmentDto::addStaffList).collect(Collectors.toList());
    }

    @PostMapping("/uploadProfPicture")
    public ResponseEntity<?> uploadImage2(@RequestBody ImageRequestDto imageRequestDto) throws IOException {
        ResponseDto uploadImage =
                storageService.uploadImage(imageRequestDto.getImg64(),
                        imageRequestDto.getId(),
                        imageRequestDto.getFileType(),
                        imageRequestDto.getSection());
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }
    @GetMapping("staff/getImageById/{id}")
    public ResponseEntity<?> getPicturePath(@PathVariable Integer id) throws IOException {
        Staff staff= staffService.getStaffById(id);
        if(staff.getProfPicture() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }
        String imageName = staff.getProfPicture();
        String imageType = imageName.substring(imageName.indexOf('.')+1);

        byte[] imageData= storageService.getImageFromFileSystemByPath(pictureUrl + "/STAFF/" + id + "/"+ staff.getProfPicture());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/"+imageType))
                .body(imageData);
    }
    @PutMapping("/ChangePwd")
    public ResponseDto changePassword(@RequestBody ChangePasswordDto changePasswordDto)
    {
        UserDetailsImpl obj = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Staff> staffDb = staffRepository.findByEmail(obj.getEmail());

        if (staffDb == null || staffDb.isEmpty()) {

            return new ResponseDto("Error", "Invalid email id.", "");
        }
        Staff staffobj=staffDb.stream().filter(e->e.isActive()).findFirst().get();

        if (staffobj == null) {
            return new ResponseDto("Error","StaffName not found... Please contact Admin!","");
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matched = passwordEncoder.matches(changePasswordDto.getOldPassword(), staffobj.getPassword());
            if (matched) {
                String newPassword= passwordEncoder.encode(changePasswordDto.getNewPassword());
                staffobj.setPassword(newPassword);
                staffRepository.save(staffobj);
                return  new ResponseDto("SUCCESS","Password Changed Successfully", "");
            }
            else{
                return new ResponseDto("Error","Invalid Old Password", "");
            }
        }
    }
   @GetMapping("/forgot-password")
    public ResponseDto forgotPassword(@RequestParam String email) throws Exception {
        return staffService.forgotPassword(email);
    }
    @PutMapping("/reset-password")
    public ResponseDto resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeyException, ClassNotFoundException, MessagingException {

        return staffService.resetPassword(resetPasswordDto);
    }
}
