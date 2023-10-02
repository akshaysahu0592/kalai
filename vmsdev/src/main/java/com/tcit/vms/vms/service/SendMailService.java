package com.tcit.vms.vms.service;
import com.tcit.vms.vms.model.Visit;
import com.tcit.vms.vms.model.Visitor;
import com.tcit.vms.vms.repository.StaffRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class SendMailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${image.url}")
    private String imageUrl;
    @Autowired
    private PdfGenerationService pdfGenerationService;

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private ResourceLoader resourceLoader;
    public void sendEmailToVisitor(Visit visit) {
        this.sendEmailWithHtml(visit, sender,visit.getVisitor(), "visitor");
        if(visit.getAccompanies()!=null && visit.getAccompanies().size()>0)
        {
            visit.getAccompanies().stream().forEach(e->this.sendEmailWithHtml(visit, sender,e, "Accompany"));
        }
    }
    public void sendEmailWithHtml(Visit visit, String from,Visitor visitor, String visitorType) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(msg, true);
            helper.setTo(visitor.getEmail());
            helper.setFrom(from,"Visitor Management System");
            helper.setSubject("Update on your Visitor Request ");
            String htmlStr= getVisitorHtml(visit,visitor, visitorType);
            helper.setText(htmlStr, true);
            InputStream file = new ByteArrayInputStream(pdfGenerationService.generatePdfFromHtml(htmlStr));
            log.info("pdf generated for {}:{}", visitorType, visitor.getEmail());
            FileSystemResource resource = new FileSystemResource(new File("VisitorPass.pdf"));
            helper.addAttachment("VisitorPass.pdf",resource);
            javaMailSender.send(msg);
            log.info("Email send to {} : {}", visitorType, visitor.getEmail());
        } catch (MessagingException e) {
            log.error("messaging exception occurred for : {},{}", visitor.getEmail(),e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }  catch (Exception e) {
            log.error(" exception occurred for : {},{}", visitor.getEmail(),e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private String getVisitorHtml(Visit visit, Visitor visitor, String visitorType){
        Resource resource = resourceLoader.getResource("classpath:crypto.html");

        try (InputStream inputStream = resource.getInputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(replaceVisitorToken(line, visit, visitor, visitorType));
            }

            String htmlContent = stringBuilder.toString();
            return htmlContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String replaceVisitorToken(String data, Visit visit, Visitor visitor, String visitorType){
        if (data.contains("@@{name}@@")){
            return  data.replace("@@{name}@@", visitor.getName());
        }
        if (data.contains("@@{mobileNo}@@")){
            return  data.replace("@@{mobileNo}@@", visitor.getMobileNo());
        }
        if (data.contains("@@{email}@@")){
            return  data.replace("@@{email}@@", visitor.getEmail());
        }
        if (data.contains("@@{visitorType}@@")){
            return  data.replace("@@{visitorType}@@", visitor.getVisitorType().getName());
        }
        if (data.contains("@@{dateOfVisit}@@")) {
            DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return data.replace("@@{dateOfVisit}@@", df.format(visit.getDateOfVisit()));
        }
        if (data.contains("@@{duration}@@")){
            return  data.replace("@@{duration}@@", visit.getDuration());
        }
        if (data.contains("@@{visitorId}@@")){
            data=data.replace("@@{visitorId}@@", visitor.getId().toString());
        }
        if (data.contains("@@{accompanies}@@")){
            return  data.replace("@@{accompanies}@@", visit.getAccompanyCount().toString());
        }
        if (data.contains("@@{visitorType}@@")){
            return  data.replace("@@{visitorType}@@", visitorType);
        }
        String cryptoImageUrl=imageUrl+"/crypto/"+visitor.getId()+"/"+visitor.getId()+".png";
        if (data.contains("@@{IMAGE_URL}@@")){
            data=data.replace("@@{IMAGE_URL}@@", cryptoImageUrl);
        }
        return data;
    }
    public void sendDeclinedEmailToVisitor(Visit visit) {
        log.info("sendEmailToVisitor called!!!");
        this.sendDeclinedEmailWithHtml(visit, sender);
    }
    public void sendDeclinedEmailToVisitorByHost(Visit visits) {
        log.info("sendEmailToVisitor called!!!");
        this.sendDeclinedEmailWithHtmlByHost(visits, sender);
    }
    public void sendDeclinedEmailWithHtml(Visit visit, String from) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(msg, true);
            String[] list=new String[]{visit.getVisitor().getEmail(),visit.getStaff().getEmail()};
            helper.setTo(list);
            helper.setFrom(from,"Visitor Management System");
            helper.setSubject("Update on your Visitor Request ");
            String htmlStr= getDeclinedVisitorHtml(visit);
            helper.setText(htmlStr, true);

            javaMailSender.send(msg);
            log.info("DeclinedEmail email send to visitor :{}", list);
        } catch (MessagingException e) {
            log.error("messaging exception occurred for : {},{}", visit.getVisitor().getEmail(),e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }  catch (Exception e) {
            log.error(" exception occurred for : {},{}", visit.getVisitor().getEmail(),e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void sendDeclinedEmailWithHtmlByHost(Visit visits, String from) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(msg, true);
            helper.setTo(visits.getVisitor().getEmail());
            helper.setFrom(from,"Visitor Management System");
            helper.setSubject("Update on your Visitor Request ");
            String htmlStr= getDeclinedVisitorHtml(visits);
            helper.setText(htmlStr, true);

            javaMailSender.send(msg);
            log.info("sendDeclinedEmailWithHtmlByHost email send to :{}", visits.getVisitor().getEmail());
        } catch (MessagingException e) {
            log.error("messaging exception occurred for : {},{}", visits.getVisitor().getEmail(),e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }  catch (Exception e) {
            log.error(" exception occurred for : {},{}", visits.getVisitor().getEmail(),e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private String getDeclinedVisitorHtml(Visit visit){
        Resource resource = resourceLoader.getResource("classpath:cryptodeclined.html");
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            // String imageLink=imageUrl+"outputs"+visit.getVisitor().getId()+"cryptograph.png";

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(replaceDeclinedVisitorToken(line, visit));
            }

            String htmlContent = stringBuilder.toString();
                        return htmlContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String replaceDeclinedVisitorToken(String data, Visit visit){
        if (data.contains("@@{name}@@")){
            return  data.replace("@@{name}@@", visit.getVisitor().getName());
        }
        if (data.contains("@@{mobileNo}@@")){
            return  data.replace("@@{mobileNo}@@", visit.getVisitor().getMobileNo());
        }
        if (data.contains("@@{email}@@")){
            return  data.replace("@@{email}@@", visit.getVisitor().getEmail());
        }
        if (data.contains("@@{visitorType}@@")){
            return  data.replace("@@{visitorType}@@", visit.getVisitor().getVisitorType().getName());
        }
        if (data.contains("@@{dateOfVisit}@@")) {
            DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return data.replace("@@{dateOfVisit}@@", df.format(visit.getDateOfVisit()));
        }
        if (data.contains("@@{duration}@@")){
            return  data.replace("@@{duration}@@", visit.getDuration());
        }
        if (data.contains("@@{accompanies}@@")){
            return  data.replace("@@{accompanies}@@", visit.getAccompanyCount().toString());
        }
        if (data.contains("@@{reason}@@")) {
            return data.replace("@@{reason}@@", visit.getReason().getReasonName());
        }
        if (data.contains("@@{comments}@@")){
            return  data.replace("@@{comments}@@", visit.getComments());
        }
        if (data.contains("@@{visitorId}@@")){
            data=data.replace("@@{visitorId}@@", visit.getId().toString());
        }
        return data;
    }
}







