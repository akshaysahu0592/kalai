package com.tcit.vms.vms.model.Entity;

import com.google.gson.annotations.SerializedName;

public class EncryptionParams {

    @SerializedName("provider_id")
    public String providerId;

    @SerializedName("key_id")
    public String keyId;
}
