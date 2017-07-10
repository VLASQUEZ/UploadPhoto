package com.jarcidco.uploadphoto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jarcidco.uploadphoto.Data.Repositories.ReportRepository;
import com.jarcidco.uploadphoto.Models.PostPhotoResponse;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.lang.annotation.Annotation;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.Retrofit;

import static com.jarcidco.uploadphoto.ImageManipulator.saveBitmap;

public class MainActivity extends AppCompatActivity {
    private CameraHandler cameraHandler;
    private ImageView imgCaptured;
    ReportRepository repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_SETTINGS }, 0);
        }

        super.onCreate(savedInstanceState);
        repo = new ReportRepository(getApplicationContext());
        setContentView(R.layout.activity_main);
        cameraHandler=new CameraHandler(MainActivity.this,getApplicationContext());
        Button btnCapture =(Button) findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cameraHandler.startCameraApp();
            }
        });
        Button btnUpload =(Button) findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString()+ "/jarcidco";
                File photoFile;
                photoFile=new File(path+"/image.png");
                postPhoto(photoFile);
            }
        });
        imgCaptured=(ImageView) findViewById(R.id.imgCaptured);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try{
                    Uri resultUri = result.getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                    saveBitmap(bitmap, "image");
                    imgCaptured.setImageBitmap(bitmap);
                }
                catch(Exception e)
                {
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                cameraHandler.cropImage("image");
            }
        }
    }

    //SERVICIOS
    private void postPhoto(File file)
    {

        RequestBody the_file = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        Call<PostPhotoResponse> call = repo.postPhotos("LA CONSENTIDA".trim(),
                "5b9b2f99a03770a9851da237b4f6cbaddb677c88a47d5d78602c8247e6a0ce6a".trim(),file);
        call.enqueue(new Callback<PostPhotoResponse>() {
            @Override
            public void onResponse(retrofit.Response<PostPhotoResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    Converter<ResponseBody, PostPhotoResponse> errorConverter =
                            retrofit.responseConverter(PostPhotoResponse.class, new Annotation[0]);
                    try {
                        PostPhotoResponse responseError = errorConverter.convert(response.errorBody());

                        if(responseError.message != "" && responseError.message != null)
                        {
                            Toast.makeText(getApplicationContext(),responseError.message,Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }
                if(response != null && response.isSuccess())
                {
                    Toast.makeText(getApplicationContext(), "Foto subida satisfactoriamente", Toast.LENGTH_LONG).show();
                    return;
                }

            }

            @Override
            public void onFailure(Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage(t.getMessage())
                        .setTitle("Error");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
