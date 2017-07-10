package com.jarcidco.uploadphoto.Data.Repositories;

import android.content.Context;

import com.jarcidco.uploadphoto.Data.IRestClient;
import com.jarcidco.uploadphoto.Data.RestClientAuth;
import com.jarcidco.uploadphoto.Models.PostPhotoResponse;
import com.squareup.okhttp.RequestBody;

import java.io.File;

import retrofit.Call;


/**
 * Created by apineda on 16/02/2017.
 */

public class ReportRepository {
    private final IRestClient apiService;
    public ReportRepository(Context context) {
        apiService = new RestClientAuth(context).getApiService();
    }
    public Call<PostPhotoResponse> postPhotos(String bikeName,String token, File image) {
        return apiService.postPhotos(bikeName,token, image);
    }

}
