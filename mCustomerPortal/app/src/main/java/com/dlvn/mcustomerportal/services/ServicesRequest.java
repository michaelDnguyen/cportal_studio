package com.dlvn.mcustomerportal.services;

import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.response.getMapMarkerResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;
import com.dlvn.mcustomerportal.services.model.response.loginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @arthor nn.tai
 * @date Oct 20, 2016
 */
public interface ServicesRequest {

	// @GET("DLPadAuthLogin/{username};{password};{deviceid}")
	// Call<List<User>> getUser(@Path("username") String username,
	// @Path("password") String password,
	// @Path("deviceid") String deviceid);

//	// @Streaming
//	// @GET
//	// Call<ResponseBody> downloadFileAttach(@Url String fileUrl);
//	@Streaming
//	@POST("GetAttachFile")
//	Call<ResponseBody> downloadFileAttach(@Body BaseRequest request);
	
	@POST("GetMapMarker")
	Call<getMapMarkerResponse> getMapMarker(@Body BaseRequest request);
	
	@POST("PDLPadAuthLogin")
	Call<loginResponse> PDLPadAuthLogin(@Body BaseRequest request);

	@POST("CPRegisterAccountLink")
	Call<loginNewResponse> CPRegisterAccount(@Body BaseRequest request);
}
