package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchPolicyHolderByPolicyIDResponse {
    @SerializedName("CPSearchPolicyHolderByPolicyIDResult")
    @Expose
    private SearchPolicyHolderByPolicyIDResult cPSearchPolicyHolderByPolicyIDResult;

    public SearchPolicyHolderByPolicyIDResult getCPSearchPolicyHolderByPolicyIDResult() {
        return cPSearchPolicyHolderByPolicyIDResult;
    }

    public void setCPSearchPolicyHolderByPolicyIDResult(SearchPolicyHolderByPolicyIDResult cPSearchPolicyHolderByPolicyIDResult) {
        this.cPSearchPolicyHolderByPolicyIDResult = cPSearchPolicyHolderByPolicyIDResult;
    }

}
