package com.tcit.vms.vms.service;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
@Service
@Slf4j
public class WebcamService {
    public String captureImage(Integer visitorId) throws IOException {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        BufferedImage image = webcam.getImage();
        ImageIO.write(image, ImageUtils.FORMAT_JPG, new File(visitorId+"selfie.jpg"));
        webcam.close();
        InputStream is= new FileInputStream(new File(visitorId+"selfie.jpg"));
        return  Base64.getEncoder().encodeToString(is.readAllBytes());
    }
}
