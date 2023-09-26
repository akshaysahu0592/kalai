package com.tcit.vms.vms.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import com.itextpdf.html2pdf.HtmlConverter;
    @Service
    @Slf4j
    public class PdfGenerationService implements ServletContextAware {
        private ServletContext servletContext;
        @Autowired
        public void setServletContext(ServletContext servletContext) {
            this.servletContext = servletContext;
        }
        public byte[] generatePdfFromHtml(String htmlContent) throws Exception {
            OutputStream fos = null;
            try {
                fos = new FileOutputStream("VisitorPass.pdf");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            HtmlConverter.convertToPdf(htmlContent, fos);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.writeTo(fos);
            baos.toByteArray();
            fos.close();
            return baos.toByteArray();
        }
    }
