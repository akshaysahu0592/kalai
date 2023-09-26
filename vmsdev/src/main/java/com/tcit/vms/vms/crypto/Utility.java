package com.tcit.vms.vms.crypto;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.util.Properties;

public class Utility {
    public static Properties getProperties(ResourceLoader resourceLoader) {
        try (InputStream is =  resourceLoader.getResource("classpath:cryptoGeneration.properties").getInputStream()){
            Properties properties = new Properties();
            properties.load(is);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] readByteArrayFromFile(String urlString) {
        // Working fine
        DataInputStream dis = null;
        byte[] fileData = null;
        try {
            File file = new File(urlString);
            fileData = new byte[(int) file.length()];
            dis = new DataInputStream((new FileInputStream(file)));
            dis.readFully(fileData);
            dis.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return fileData;
    }
    public static MultipartBody.Part byteArrayToMultipart(String key, byte[] content, String filename, String contentType) {

        RequestBody prtFile =
                RequestBody.create( MediaType.parse(contentType),content);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(key, filename, prtFile);
    }
    public static MultipartBody.Part fileToMultiPart(String key,File file,String contentType){

        RequestBody requestFile =
                RequestBody.create(MediaType.parse(contentType),file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData(key, file.getName(), requestFile);

        return body;

    }
}


