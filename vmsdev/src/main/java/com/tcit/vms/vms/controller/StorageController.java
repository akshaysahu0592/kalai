package com.tcit.vms.vms.controller;

import com.tcit.vms.vms.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
public class StorageController {
    @Autowired
    private StorageService service;
    private final String FOLDER_PATH= "C:\\Projects\\VMSMedia\\image";
    @PostMapping
    public ResponseEntity<Object> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = service.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }
    @PostMapping("/{type}/{id}")
    public ResponseEntity<?> uploadImage1(@RequestParam("image")MultipartFile file,
                                            @PathVariable String type,
                                            @PathVariable Integer id) throws IOException {
        String uploadImage = service.uploadImage(file, id, type);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }
    @GetMapping("/{type}/{id}/{imageName}")
    public ResponseEntity<byte[]> uploadImage1(@PathVariable String imageName,
                                          @PathVariable String type,
                                          @PathVariable Integer id) throws IOException {
         byte[] imageData =service.getImageFromFileSystemByPath(FOLDER_PATH+"\\"+type+"\\"+id+"\\"+imageName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpeg"))
                .body(imageData);
    }
    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        byte[] imageData=service.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("jpg"))
                .body(imageData);
    }
    @PostMapping("/fileSystem")
    public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = service.uploadImageToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }
    @GetMapping("/fileSystem/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageData=service.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("png"))
                .body(imageData);
    }
    @GetMapping("/getImageByPath")
    public ResponseEntity<?> getPicturePath(@RequestBody() String path) throws IOException {
        String imageType = path.substring(path.indexOf('.')+1);
        byte[] imageData= service.getImageFromFileSystemByPath(path);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/"+imageType))
                .body(imageData);
    }
}
