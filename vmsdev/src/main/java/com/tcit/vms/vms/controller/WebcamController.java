package com.tcit.vms.vms.controller;


import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class WebcamController {
    @GetMapping("/capture/{}")
    public  void captureImage() throws IOException {
        Webcam webcam = Webcam.getDefault();
        webcam.open();

        BufferedImage image = webcam.getImage();

        ImageIO.write(image, ImageUtils.FORMAT_JPG, new File("selfie.jpg"));
    }
}
