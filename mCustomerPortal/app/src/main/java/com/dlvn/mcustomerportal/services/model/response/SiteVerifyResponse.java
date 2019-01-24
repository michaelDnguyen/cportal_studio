package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SiteVerifyResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("challenge_ts")
    @Expose
    private String challengeTs;
    @SerializedName("apk_package_name")
    @Expose
    private String apkPackageName;
    @SerializedName("error-codes")
    @Expose
    private List<String> errorCodes = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getChallengeTs() {
        return challengeTs;
    }

    public void setChallengeTs(String challengeTs) {
        this.challengeTs = challengeTs;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

}
