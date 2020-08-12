package com.example.aula_galeria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1;
    private static final int TAKE_PICTURE_CODE = 2;
    private static final int OPEN_GALLERY_CODE = 3;

    public ImageView imageViewPicture;
    public ImageButton imageButtonCamera;
    public  ImageButton imageButtonGallery;

    private String currentPatch;
    private Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, OPEN_GALLERY_CODE);
    }

    private void openCamera(){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Nova imagem");
        values.put(MediaStore.Images.Media.DESCRIPTION,"descrição");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);

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
        if(requestCode == TAKE_PICTURE_CODE && resultCode == RESULT_OK){
            imageViewPicture.setImageURI(image_uri);
        }else
            if(requestCode == OPEN_GALLERY_CODE && resultCode == RESULT_OK){
                image_uri = data.getData();
                imageViewPicture.setImageURI(image_uri);
            }
    }
}