package com.tcit.vms.vms.service;

import com.tcit.vms.vms.dto.response.ResponseDto;
import com.tcit.vms.vms.model.*;
import com.tcit.vms.vms.repository.*;
import com.tcit.vms.vms.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.Optional;
@Service
public class StorageService {
    @Autowired
    private StorageRepository repository;
    @Autowired
    private FileDataRepository fileDataRepository;
    private final String FOLDER_PATH= "C:/Projects/VMSMedia";
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private VisitorRepository visitorRepository;
    private void saveFileInPath(String path, String imageBase64) throws FileNotFoundException {
        try(FileOutputStream fos = new FileOutputStream(new File(path))) {
            fos.write(DatatypeConverter.parseBase64Binary(imageBase64));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ResponseDto uploadImage(String imageBase64, Integer id, String type, String section) throws IOException {
        if(!validateImageType(type))
        {
            throw new RuntimeException("Error while upload. It supports jpg, jpeg and png format type.");
        }
        try{
            String filePath =  FOLDER_PATH  +"/image";
            File file = new File(filePath);
            if(!file.exists()){
                file.mkdir();
            }
            String location = "/"+ section.toUpperCase() +"/"+id;
            filePath = filePath  + "/"+ section.toUpperCase();
            file = new File(filePath);
            if(!file.exists()){
                file.mkdir();
            }
            filePath = filePath + "/"+id;
            file = new File(filePath);
            if(!file.exists()){
                file.mkdir();
            }
            String fileName = null;
            switch (section.toUpperCase()){
                case "VISITOR":
                    fileName ="visitor_"+id+"_"+new Date().getTime()+"."+type;
                    filePath = filePath +"/"+ fileName;
                    saveFileInPath(filePath, imageBase64);
                    Visitor visitor=visitorRepository.findById(id).get();
                    visitor.setProfPicture(fileName);
                    visitorRepository.save(visitor);
                    break;

                case "STAFF":
                    fileName ="staff_"+id+"_"+new Date().getTime()+"."+type;
                    filePath = filePath +"/"+ fileName;
                    saveFileInPath(filePath, imageBase64);
                    Staff staff=staffRepository.findById(id).get();
                    staff.setProfPicture(fileName);
                    staffRepository.save(staff);
                    break;
            }

            return new ResponseDto("SUCCESS","Image uploaded successfully ", fileName);
        }catch(Exception e){
            return new ResponseDto("Error" ,"Error Occurred while Uploding image ", "" );
        }
    }
    private Boolean validateImageType(String type) {
        return  type.equals("png")
                || type.equals("jpg")
                || type.equals("jpeg");
    }
    @Deprecated
    public String uploadImage(MultipartFile file, Integer id, String type) throws IOException {
        switch (type.toUpperCase()) {
            case "VISITOR":
                Visitor visitor = visitorRepository.findById(id).get();
                visitorRepository.save(visitor);
            case "STAFF":
                Staff staff = staffRepository.findById(id).get();
                staffRepository.save(staff);
        }
        return "file uploaded successfully : " + file.getOriginalFilename();
    }
    public String uploadImage(MultipartFile file) throws IOException {

        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }
    public byte[] downloadImage(String fileName) {
        Optional<ImageData> dbImageData = repository.findByName(fileName);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }
    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath=FOLDER_PATH+file.getOriginalFilename();
        FileData fileData=fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build());
        file.transferTo(new File(filePath));
        if (fileData != null) {
            return "file uploaded successfully : " + filePath;
        }
        return null;
    }
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath=fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
    public byte[] getImageFromFileSystemByPath(String filePath) throws IOException {
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
}
