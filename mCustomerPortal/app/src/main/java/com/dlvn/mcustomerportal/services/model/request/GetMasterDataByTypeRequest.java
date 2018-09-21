package com.dlvn.mcustomerportal.services.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMasterDataByTypeRequest {

    @SerializedName("Project")
    @Expose
    private String project;
    @SerializedName("Type")
    @Expose
    private String type;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
