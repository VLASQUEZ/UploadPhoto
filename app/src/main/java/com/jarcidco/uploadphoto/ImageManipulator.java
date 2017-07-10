package com.jarcidco.uploadphoto;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by apineda on 23/02/2017.
 */

public class ImageManipulator {
    private final static String DEBUG_TAG = "MakePhotoActivity";
    public static byte[] convertToArray(ImageView iv){
        Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        return imageInByte;
    }
   /* public static void resizePng(String filename){

        String dir= Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString()+ "/CarpetaCiudadana";
        Bitmap b= BitmapFactory.decodeFile(dir+"/"+filename);
        Bitmap out = Bitmap.createScaledBitmap(b, 320, 480, false);

        File file = new File(dir, filename);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {}
    }*/

    public static String resizePng(String filename){
        try {
            String dir= Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString()+ "/jarcidco/"+filename;

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(dir), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=1024;

            // Find the correct scale value. It should be the power of 2.
            int scale = 2;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap b= BitmapFactory.decodeStream(new FileInputStream(dir), null, o2);
            String error=saveBitmapNoPath(b,dir);
            if(error==null)
            {
                return null;
            }
            else{
            return error;
            }

        } catch (FileNotFoundException e) {
            return e.toString();
        }
    }
    public static String saveBitmapNoPath(Bitmap bitmap, String filename){
        try{

            File file = new File(filename);
            Bitmap b= BitmapFactory.decodeFile(filename);
            FileOutputStream fOut;

            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            bitmap.recycle();
            return null;
        }
        catch(Exception e)
        {
            return e.toString();
        }
    }
    public static String saveBitmap(Bitmap bitmap, String filename){
        try{

            String dir= Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString()+ "/jarcidco";
            File imgFile = new File(dir,filename+".png");
            FileOutputStream fOut;

            fOut = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            //b.recycle();
            return null;
        }
        catch(Exception e)
        {
            return e.toString();
        }
    }
    public static int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(DEBUG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public static Bitmap getImageFromPath(String filename){
        String dir= Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString()+ "/CarpetaCiudadana";
        File imgFile = new File(dir,filename+".png");

        if(imgFile.exists()){
            Bitmap img = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

           return img;
        }
        else{
            return null;
        }
   }
    public static File getFileFromPath(String filename){
        String dir= Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString()+ "/CarpetaCiudadana";
        File imgFile = new File(dir,filename+".png");

        if(imgFile.exists()){
            return imgFile;
        }
        else{
            return null;
        }
    }
    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
                null, null, null);

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            } else {
                return -1;
            }
        } finally {
            cursor.close();
        }
    }
}
