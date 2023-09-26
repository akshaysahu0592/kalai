package com.tcit.vms.vms.network;

import com.tcit.vms.vms.model.Entity.BarCodeResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

import java.util.List;
public interface GetDataService {
    @Multipart
    @POST()
    Call<BarCodeResponse> createBarCode(@Url String url, @Part List<MultipartBody.Part> parts);
}