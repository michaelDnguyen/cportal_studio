package com.dlvn.mcustomerportal.services;

import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.claims.CPGetClaimIDResponse;
import com.dlvn.mcustomerportal.services.model.claims.CPGetDocTypeResponse;
import com.dlvn.mcustomerportal.services.model.claims.GetClaimsStatusResponse;
import com.dlvn.mcustomerportal.services.model.claims.SyncClaimDetailResponse;
import com.dlvn.mcustomerportal.services.model.claims.UploadClaimImageResponse;
import com.dlvn.mcustomerportal.services.model.request.GetFeeByClientIDRequest;
import com.dlvn.mcustomerportal.services.model.request.GetMasterDataByTypeRequest;
import com.dlvn.mcustomerportal.services.model.request.HistoryPaymentDetailRequest;
import com.dlvn.mcustomerportal.services.model.request.HistoryPaymentRequest;
import com.dlvn.mcustomerportal.services.model.request.SavePaymentRequest;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyInfoByPOLIDResponse;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyListByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResponse;
import com.dlvn.mcustomerportal.services.model.response.GetClientProfileByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetFeeByClientIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetFeeOfBasicPolByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetMasterData_Category_Response;
import com.dlvn.mcustomerportal.services.model.response.GetMasterData_City_Response;
import com.dlvn.mcustomerportal.services.model.response.GetMasterData_Product_Response;
import com.dlvn.mcustomerportal.services.model.response.GetPointAccountByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetPointByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetPriceILPResponse;
import com.dlvn.mcustomerportal.services.model.response.GetProductByCategoryResponse;
import com.dlvn.mcustomerportal.services.model.response.GetTaxInvoiceResponse;
import com.dlvn.mcustomerportal.services.model.response.HistoryPaymentDetailResponse;
import com.dlvn.mcustomerportal.services.model.response.HistoryPaymentResponse;
import com.dlvn.mcustomerportal.services.model.response.LoyaltyPointConformResponse;
import com.dlvn.mcustomerportal.services.model.response.SavePaymentResponse;
import com.dlvn.mcustomerportal.services.model.response.SearchPolicyHolderByPolicyIDResponse;
import com.dlvn.mcustomerportal.services.model.response.getMapMarkerResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

/**
 * @arthor nn.tai
 * @date Oct 20, 2016
 */
public interface ServicesRequest {

//	// @Streaming
//	// @GET
//	// Call<ResponseBody> downloadFileAttach(@Url String fileUrl);
//	@Streaming
//	@POST("GetAttachFile")
//	Call<ResponseBody> downloadFileAttach(@Body BaseRequest request);

    @POST("GetMapMarker")
    Call<getMapMarkerResponse> getMapMarker(@Body BaseRequest request);

    @POST("PDLPadAuthLogin")
    Call<GetTaxInvoiceResponse> PDLPadAuthLogin(@Body BaseRequest request);

    @POST("CPRegisterAccountLink")
    Call<loginNewResponse> CPRegisterAccount(@Body BaseRequest request);

    @POST("CPGetPolicyListByCLIID")
    Call<CPGetPolicyListByCLIIDResponse> CPGetPolicyListByCLIID(@Body BaseRequest request);

    @POST("CPGetPolicyInfoByPOLID")
    Call<CPGetPolicyInfoByPOLIDResponse> CPGetPolicyInfoByPOLID(@Body BaseRequest request);

    @POST("CPGetPointByCLIID")
    Call<GetPointByCLIIDResponse> GetPointByCLIID(@Body BaseRequest request);

    @POST("CPGetFeeOfBasicPolByCLIID")
    Call<GetFeeOfBasicPolByCLIIDResponse> GetFeeOfBasicPolByCLIID(@Body BaseRequest request);

    @POST("GetMasterDataByType")
    Call<GetMasterData_Category_Response> GetMasterDataByType(@Body GetMasterDataByTypeRequest request);

    @POST("GetMasterDataByType")
    Call<GetMasterData_City_Response> GetListCityMasterData(@Body GetMasterDataByTypeRequest request);

    @POST("GetMasterDataByType")
    Call<GetMasterData_Product_Response> GetListProductMasterData(@Body GetMasterDataByTypeRequest request);

    //Lấy danh sách sản phẩm theo category in Loyalty Point
    @POST("CPGetProductByCategory")
    Call<GetProductByCategoryResponse> GetProductByCategory(@Body BaseRequest request);

    @POST("CPLoyaltyPointConfirmation")
    Call<LoyaltyPointConformResponse> LoyaltyPointConform(@Body BaseRequest request);

    @POST("Pay_SavePayment")
    Call<SavePaymentResponse> savePaymentToServer(@Body SavePaymentRequest request);

    @POST("Pay_GetFeeByClientID")
    Call<GetFeeByClientIDResponse> getFeeByClientID(@Body GetFeeByClientIDRequest request);

    //Submit yêu cầu từ khách hàng
    @POST("CPSubmitForm")
    Call<CPSubmitFormResponse> SubmitFormContact(@Body BaseRequest request);

    //get Notification/Mailbox
    @POST("CPGetClientProfileByCLIID")
    Call<GetClientProfileByCLIIDResponse> GetClientProfileByCLIID(@Body BaseRequest request);

    //Lấy thông tin quá trình tích lũy điểm thưởng
    @POST("CPGetPointAccountByCLIID")
    Call<GetPointAccountByCLIIDResponse> GetPointAccountByCLIID(@Body BaseRequest request);

    //Lấy thông tin lịch sử giao dịch
    @POST("Pay_HistoryPayment")
    Call<HistoryPaymentResponse> GetHistoryPayment(@Body HistoryPaymentRequest request);

    @POST("Pay_HistoryPaymentDetail")
    Call<HistoryPaymentDetailResponse> GetHistoryDetailPayment(@Body HistoryPaymentDetailRequest request);

    @POST("GetPriceILP")
    Call<GetPriceILPResponse> GetPriceILP(@Body BaseRequest request);

    @POST("CPSearchPolicyHolderByPolicyID")
    Call<SearchPolicyHolderByPolicyIDResponse> SearchPolicyHolderByPolicyID(@Body BaseRequest request);

    @POST("GetTaxInvoice")
    Call<GetTaxInvoiceResponse> GetTaxInvoice(@Body BaseRequest request);

    @POST("CPSaveLog")
    Call<GetTaxInvoiceResponse> CPSaveLog(@Body BaseRequest request);

    //get list claim doctype
    @POST("CPGetDocType")
    Call<CPGetDocTypeResponse> CPGetDocType(@Body BaseRequest request);

    //get claims id from server
    @POST("GetSubmissionID")
    Call<CPGetClaimIDResponse> GetClaimID(@Body BaseRequest request);

    //upload image to server
    @Streaming
    @POST("SubmitPropsImage")
    Call<UploadClaimImageResponse> UploadClaimImage(@Body BaseRequest request);

    //get proposal status of submission
    @POST("GetProposalStatus")
    Call<GetClaimsStatusResponse> GetClaimsStatus(@Body BaseRequest request);

    //get List Claims from server
    @POST("CPGetClientProfileByCLIID")
    Call<SyncClaimDetailResponse> SyncClaimListByCLIID(@Body BaseRequest request);

    //get Claims Detail Sync Data
    @POST("CPGetClientProfileByCLIID")
    Call<SyncClaimDetailResponse> SyncClaimsDetailByClaimID(@Body BaseRequest request);
}
