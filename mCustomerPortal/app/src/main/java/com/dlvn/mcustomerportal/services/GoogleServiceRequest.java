package com.dlvn.mcustomerportal.services;

import com.dlvn.mcustomerportal.services.model.response.SiteVerifyResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GoogleServiceRequest {

    @POST("siteverify")
    Call<SiteVerifyResponse> getSiteVerifyReCaptCha2(@Query("secret") String secret, @Query("response") String response);

}
