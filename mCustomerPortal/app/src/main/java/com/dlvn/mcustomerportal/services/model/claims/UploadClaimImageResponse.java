package com.dlvn.mcustomerportal.services.model.claims;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadClaimImageResponse {

    @SerializedName("SubmitPropsImageResult")
    @Expose
    private UploadClaimImageResult testStreamResult;

    /**
     * @return The approvalLoginResult
     */
    public UploadClaimImageResult getStreamResult() {
        return testStreamResult;
    }

    /**
     * @param streamResult The ApprovalLoginResult
     */
    public void setStreamResult(UploadClaimImageResult streamResult) {
        this.testStreamResult = streamResult;
    }
}
