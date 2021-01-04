package com.gioia.flashlight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView button;
    Camera camera;
    Camera.Parameters parameters;
    boolean isOff = true;

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfHasFlash();
        requestCameraPermission();
        initializeApp();
    }

    protected void checkIfHasFlash(){
        // Si no tiene flash, error.
        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            //error
        }
    }

    protected void requestCameraPermission(){
        boolean cameraPermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        if(!cameraPermissionGranted){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1000);
        }
    }

    protected void initializeApp(){
        camera = Camera.open();
        parameters = camera.getParameters();
        button = findViewById(R.id.onOffButton);
        button.setImageResource(R.drawable.off_image);

        button.setOnClickListener((View view)-> {
            if(isOff){
                button.setImageResource(R.drawable.on_image);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                camera.startPreview();
                isOff = false;
            }
            else{
                button.setImageResource(R.drawable.off_image);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
                isOff = true;
            }
        });
    }

    protected void releaseCamera(){
        if(camera != null){
            camera.release();
            camera = null;
        }
    }
}