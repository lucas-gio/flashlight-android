package com.gioia.flashlight;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
        checkIfHasFlash();
        requestCameraPermission();
        initializeApp();
        setContentView(R.layout.activity_main);
    }

    /**
     * If this device have not any camera flash, this show an dialog with failure reason.
     */
    private void checkIfHasFlash(){
        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            Log.e(" checkIfHasFlash()", "Flash not found");

            new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.errorFlashNotFound)
                .setTitle(R.string.dialogTitleFlashNotFound)
                .setPositiveButton(R.string.ok, ((DialogInterface.OnClickListener) (dialog, id) -> {
                    finish();
                    System.exit(0);
                }))
                .create()
                .show();
        }
    }

    private void requestCameraPermission(){
        boolean cameraPermissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        if(!cameraPermissionGranted){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1000);
        }
    }

    /**
     * Open camera, prepare parameters and main button with stop/start code.
     */
    private void initializeApp(){
        camera = Camera.open();
        parameters = camera.getParameters();
        button = findViewById(R.id.onOffButton);
        button.setImageResource(R.drawable.off_image);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        button.setOnClickListener((View view)-> {
            if(camera == null){
                camera = Camera.open();
            }

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

    private void releaseCamera(){
        if(camera != null){
            camera.release();
            camera = null;
        }
    }
}