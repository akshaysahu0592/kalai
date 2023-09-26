package com.tcit.vms.vms.model.Entity;


import com.google.gson.annotations.SerializedName;

public class BarcodeTitle {


    @SerializedName("text")
    public String text;

    @SerializedName("location")
    public String location;

    @SerializedName("alignment")
    public String alignment;

    @SerializedName("offset")
    public int offset;
}
