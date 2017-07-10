package com.jarcidco.uploadphoto.Data;


import com.jarcidco.uploadphoto.Models.PostPhotoResponse;
import com.squareup.okhttp.RequestBody;

import java.io.File;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by apineda on 3/02/2017.
 */

public interface IRestClient {
    @Multipart
    @POST("/services/v1/bikePhoto")
    Call<PostPhotoResponse> postPhotos(@Part("bikeName") String bikeName, @Part("token") String token, @Part("file\"; filename=\"image.png") File image);


}
