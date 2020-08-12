package com.example.aula_galeria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1;
    private static final int TAKE_PICTURE_CODE = 2;

    private ImageView imageViewPicture;
    private ImageButton imageButtonCamera;
    private ImageButton imageButtonGallery;

    private String currentPatch;
    private Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCamera(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ){
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            }else{
                openCamera();
            }
        }
    }

    public void openGallery(View view) {
    }

    private void openCamera(){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, TAKE_PICTURE_CODE);
            } else {
                Toast.makeText(this, "Dispositivo não suporta abrir camera em aplicações externas", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }else{
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("debuggg","rc "+requestCode+"rc "+resultCode);
        if(requestCode == TAKE_PICTURE_CODE && resultCode == RESULT_OK){

            Bundle extras = data.getExtras();
            Log.d("debuggg","bundle "+extras);
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.d("debuggg","bitmap "+imageBitmap);
            imageViewPicture.setImageBitmap(imageBitmap);
            Log.d("debuggg","erro" );
        }
    }
}