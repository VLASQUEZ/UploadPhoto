package com.jarcidco.uploadphoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

/**
 * Created by apineda on 28/04/2017.
 */

public class CameraHandler {
    private AppCompatActivity activity;
    private Context context;
    public CameraHandler(AppCompatActivity activity, Context context){
        this.context=context;
        this.activity=activity;
    }
    public void startCameraApp(){
        Uri file;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile("image"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        activity.startActivityForResult(intent,100);
    }
    private File getOutputMediaFile(String photoType){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "jarcidco");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }
        return new File(mediaStorageDir.getPath() + File.separator + photoType+".png");
    }
    public void cropImage(String photoType){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "jarcidco/"+photoType+".png");
        Uri file= Uri.fromFile(mediaStorageDir);
        CropImage.activity(file)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(activity);
    }

    public static void deleteAppFolder() {
        try{
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "jarcidco/");
            if (mediaStorageDir.isDirectory())
            {
                String[] children = mediaStorageDir.list();
                for (int i = 0; i < children.length; i++)
                {
                    new File(mediaStorageDir, children[i]).delete();
                }
            }
        }
        catch(Exception e){

        }

    }
}
