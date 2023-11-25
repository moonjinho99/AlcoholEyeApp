package com.example.alcoholeye;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiService {
    @GET("/retrofit/get")
    @Streaming
    Call<ResponseBody> getFunc(@Query("data") JSONObject data);

    @GET("/api/idCheck")
    Call<ResponseBody> idCheck(@Query("data") JSONObject data);
    //서버 엔드포인트로 데이터 전송
    
    @Multipart
    @POST("/api/signUp")
    Call<ResponseBody> signUp(
        @Part("UserData") JSONObject signUpData,
        @Part MultipartBody.Part join_img // 이미지를 담을 Part
    );

    @GET("/api/signIn")
    Call<ResponseBody> signIn(@Query("data") JSONObject data);
    //Call<ResponseBody> getFunc(@Query("data") String data);

    //회원의 얼굴과 일치하는지 촬영 후 사진 전송
    @Multipart
    @POST("/api/checkUserImg")
    Call<ResponseBody> checkUserImg(
            @Part("userId") JSONObject userId,
            @Part MultipartBody.Part check_img // 이미지를 담을 Part
    );
    //알코올 측정값 전송
    @GET("/api/alcohol")
    Call<ResponseBody> inputAlcohol(@Query("data") JSONObject data);


}