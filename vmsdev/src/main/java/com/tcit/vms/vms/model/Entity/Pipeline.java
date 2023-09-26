package com.tcit.vms.vms.model.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;



public class Pipeline {

    @SerializedName("encryption")
    public HashMap<String, ArrayList<EncryptionParams>> encryption;

    @SerializedName("facePipeline")
    public FaceParams facePipeline;

    @SerializedName("barcodeGenerationParameters")
    public BarcodeParams barcodeGenerationParameters;

    @SerializedName("emailSender")
    public EmailParams emailParams;

    @SerializedName("fingerPipelines")
    public ArrayList<FingerParams> fingerparams;

}

