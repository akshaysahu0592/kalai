package com.tcit.vms.vms.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.google.gson.Gson;

import com.tcit.vms.vms.model.CryptoGenerationResponse;
import com.tcit.vms.vms.model.Entity.*;
import com.tcit.vms.vms.model.Visit;
import com.tcit.vms.vms.model.Visitor;
import com.tcit.vms.vms.network.GetDataService;
import com.tcit.vms.vms.network.RetrofitClientInstance;
import com.tcit.vms.vms.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MultipartBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Slf4j
@Service
public class CryptoGeneration {

    private static Properties properties = null;

    private static boolean includeFaceTemplate = true;
    private static boolean includeCompressedImage = true;
    private static int compressionLevel = 1;
    private static boolean includeDemographics = true;

    private static int errorCorrection = 0;
    private static int gridSize = 6;
    private static int thickness = 1;
    private static Date expiryDate = null;

    private static int cols = 0;
    private static int rows = 0;

    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private static String outputDirectory;

    @Autowired
    private ResourceLoader resourceLoader;

   /* @Autowired
    private VisitorService visitorService;
*/
    private void readAllProperties() {

        try {

            if (properties.getProperty("compressionLevel") != null) {

                compressionLevel = Integer.parseInt(properties.getProperty("compressionLevel"));
            }

            String includeFaceTmpl = properties.getProperty("includeFaceTemplate");

            if (includeFaceTmpl != null) {

                includeFaceTemplate = includeFaceTmpl.equalsIgnoreCase("true");
            }

            String includeCompressFace = properties.getProperty("includeCompressedImage");

            if (includeCompressFace != null) {

                includeCompressedImage = includeCompressFace.equalsIgnoreCase("true");
            }

            String includeDemo = properties.getProperty("includeDemographics");

            if (includeDemo != null) {

                includeDemographics = includeDemo.equalsIgnoreCase("true");
            }

            if (properties.getProperty("errorCorrection") != null) {

                errorCorrection = Integer.parseInt(properties.getProperty("errorCorrection"));

            }

            if (properties.getProperty("gridSize") != null) {

                gridSize = Integer.parseInt(properties.getProperty("gridSize"));

            }

            if (properties.getProperty("thickness") != null) {

                thickness = Integer.parseInt(properties.getProperty("thickness"));

            }

            try {
                Calendar calendar=Calendar.getInstance();
                calendar.add(Calendar.DATE,1);

                expiryDate =calendar.getTime();

            } catch (Exception e) {

            }

            if (properties.getProperty("rows") != null) {

                rows = Integer.parseInt(properties.getProperty("rows"));

            }

            if (properties.getProperty("cols") != null) {

                cols = Integer.parseInt(properties.getProperty("cols"));

            }

        } catch (Exception e) {

        }

    }
    private String saveFile(byte[] data, String dirName, String fileName) {

        FileOutputStream outPut = null;

        try {
            String storageDir = properties.getProperty("outputFilesPath");

            String path = storageDir + File.separator + dirName + File.separator + fileName;

            File myFile = new File(path);
            if (!myFile.getParentFile().exists()) {
                myFile.getParentFile().mkdirs();
            }

            if (myFile.exists()) {
                myFile.delete();
            }
            outPut = new FileOutputStream(myFile);
            outPut.write(data);
            outPut.close();

            return myFile.getAbsolutePath();
        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (outPut != null) {

                try {
                    outPut.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        return null;
    }
    public void generation(Visitor visitor, LocalDateTime dateOfVisit) {

        properties = Utility.getProperties(resourceLoader);

        //outputDirectory = String.valueOf(System.currentTimeMillis());

        if (properties != null) {

            readAllProperties();
            String url = properties.getProperty("idencodeBaseUrl");

            try {
                Integer refId = generateCryptograph(url, visitor, dateOfVisit);
                log.info("Cryptograph Generated for visitor:{}", visitor.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            log.info("error reading properties file for visitor:{}", visitor.getId());
        }
    }
    private Integer generateCryptograph(String url, Visitor visitor, LocalDateTime dateOfVisit)
            throws IOException {
        final CryptoGenerationResponse cryptoGenerationResponse = new CryptoGenerationResponse();
        ArrayList<MultipartBody.Part> parts = new ArrayList<>();
// face image
        //byte[] faceImageBytes = Base64.getDecoder().decode(visitorDetails.getVisitor().getProfPicture());
        byte[] faceImageBytes=new FileInputStream("C:\\Projects\\VMSMedia\\image\\VISITOR\\"+visitor.getId()+"\\"+visitor.getProfPicture()).readAllBytes();
        if (faceImageBytes.length>0) {
            parts.add(Utility.byteArrayToMultipart("face_image", faceImageBytes, "face.jpg", "image/jpeg"));
        } else {
            throw new RuntimeException("Face image file not exists");
        }
//demog
        MultipartBody.Part demographics = null;
        String txt = Arrays.asList(visitor.getName(), dateOfVisit.toString(), visitor.getEmail(), visitor.getId().toString())
                .stream()
                .collect(Collectors.joining(", "));
        demographics = Utility.byteArrayToMultipart("demog", txt.getBytes(), "demog.txt","application/octet-stream");
        parts.add(demographics);
        String pipelineJson = new Gson().toJson(getPipeline(dateOfVisit));

        MultipartBody.Part pipelinePart = Utility.byteArrayToMultipart("pipeline", pipelineJson.getBytes(),
                "pipeline.json", "application/json");
        parts.add(pipelinePart);

        if (parts.size() > 1) {

            GetDataService service = RetrofitClientInstance.getRetrofitInstance(url).create(GetDataService.class);

            Call<BarCodeResponse> call = service.createBarCode(url + "enroll", parts);

            System.out.println("sending enroll request to " + url + "enroll" + " ....");

            call.enqueue(new Callback<BarCodeResponse>() {
                @Override
                public void onResponse(Call<BarCodeResponse> call, Response<BarCodeResponse> response) {

                    if (response != null) {

                        if (response.code() == 400) {

                            try {
                                String errorResp = response.errorBody().string();

                                System.out.println("crypto generation failed : " + errorResp);

                            } catch (IOException e) {

                            }

                        } else if (response.code() == 200 && response.body() != null && response.body().image != null) {

                            System.out.println("crypto generation success");



                            try {

                                String responseString  = new Gson().toJson(response.body());

                                saveFile(responseString.getBytes(),
                                        visitor.getId().toString(), "response.json");

                                String path = saveFile(Base64.getDecoder().decode(response.body().image.getBytes()),
                                        visitor.getId().toString(), visitor.getId().toString()+".png");

                                if(path!=null) {

                                    System.out.println("cryptograph saved to " + path);
                                }
                                cryptoGenerationResponse.setUuid(response.body().uuid);
                                cryptoGenerationResponse.setImage(Base64.getEncoder().encodeToString(Base64.getDecoder().decode(response.body().image.getBytes())));
                                cryptoGenerationResponse.setPath(path);
                                //enroll method(
                            } catch (Exception e) {

                            }

                        } else {

                            System.out.println("crypto generation failed ");
                        }

                    } else {
                        System.out.println("unable to get response from server");

                    }
                }

                @Override
                public void onFailure(Call<BarCodeResponse> call, Throwable t) {

                    System.out.println("crypto generation failed : " + t.getLocalizedMessage());

                }

            });

        } else {
            System.out.println("empty data");
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        visitor.setCryptograph(cryptoGenerationResponse.getImage());

        return visitor.getId();
    }
    private Pipeline getPipeline(LocalDateTime dateOfVisit){
        Pipeline pipeline = new Pipeline();

        FaceParams faceParams = new FaceParams(includeFaceTemplate, 0.6f, 1, includeCompressedImage,
                compressionLevel);
        pipeline.facePipeline = faceParams;

        BarcodeParams par = new BarcodeParams();

        par.blockRows = rows;
        par.blockCols = cols;
        par.errorCorrection = errorCorrection;
        par.gridSize = gridSize;
        par.thickness = thickness;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        // par.expirationDate = sdf.format(c.getTime());
        Date expDate = CommonUtils.getDateByMinsOrHours(dateOfVisit,
                2);

        par.expirationDate = sdf.format(expDate);

        /*EmailParams emailParams = new EmailParams();
        emailParams.emailto = visitorDetails.getVisitor().getEmail();
        emailParams.subject = "Your Tech5 IDencode";

        pipeline.emailParams = emailParams;*/

        pipeline.barcodeGenerationParameters = par;
        return pipeline;
    }
    private Date convertStringToDate(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        //String dateInString = "7-Jun-2013";
        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }


}
