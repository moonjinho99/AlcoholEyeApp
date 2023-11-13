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
    @GET("/api/signUp")
    Call<ResponseBody> signUp(@Query("data") JSONObject data);
    @GET("/api/signIn")
    Call<ResponseBody> signIn(@Query("data") JSONObject data);
    //Call<ResponseBody> getFunc(@Query("data") String data);

//    @FormUrlEncoded
//    @POST("/retrofit/post")
//    Call<ResponseBody> postFunc(@Field("data") String data);
//
//    @FormUrlEncoded
//    @PUT("/retrofit/put/{id}")
//    Call<ResponseBody> putFunc(@Path("id") String id, @Field("data") String data);
//
//    @DELETE("/retrofit/delete/{id}")
//    Call<ResponseBody> deleteFunc(@Path("id") String id);


}