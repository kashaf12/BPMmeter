package com.itorch.kfstudio.www.bpmmeter;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;

public class HRCamera extends JavaCameraView {
    private Context myReference;
    public HRCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
        myReference = context;
    }

    public void setFlashOn(){
        if(mCamera != null){
            Camera.Parameters parameters = mCamera.getParameters();
            if(parameters != null){
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            }
        }
    }
    public void setFlashOff() {
        Log.i("HRCamera", "Toggle Flash");
        if(mCamera != null){
            Camera.Parameters parameters = mCamera.getParameters();
            if(parameters != null){
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            }
        }
    }
    public void rotateCamera(){

    }

    public void setFPS(){
        if(mCamera != null){
            Camera.Parameters parameters = mCamera.getParameters();
            if(parameters != null) {
                parameters.setPreviewFpsRange(20000,20000);
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            }
        }
    }


    public boolean isFlashOn(){
        Log.i("HRCamera", "Toggle Flash");
        if(mCamera != null){
            return !mCamera.getParameters().getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH);
        }

        return true;
    }

}
