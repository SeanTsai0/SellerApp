package com.example.sellerapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recognize extends AppCompatActivity {
    public static String TAG = "Recognize";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    EditText targetText;
    Button chooseImage, operateBtn;
    ImageView source_image;
    Bitmap bitmap;
    ProgressBar progressBar;
    String currentPhotoPath, imageFileName;

    private void component(){
        targetText = findViewById(R.id.targetText);
        chooseImage = findViewById(R.id.chooseImage);
        operateBtn = findViewById(R.id.operateBtn);
        source_image = findViewById(R.id.source_image);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recogonize);

        component();

        chooseImage.setOnClickListener(v -> {
            if(checkAndRequestPermissions(this)){
                chooseImage(this);
            }
        });
        operateBtn.setOnClickListener(v -> {
            source_image.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            Map<String, String> postData = new HashMap<>();
            postData.put("target", targetText.getText().toString());
            Bitmap imageBitmap = ((BitmapDrawable) source_image.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String base64String = Base64.encodeToString(bytes, Base64.NO_CLOSE);
            postData.put("image", base64String);
            Request request = new Request(postData);
            request.execute("http://163.13.201.93/pythonAPI/recognize.php");
        });
    }
    protected class Request extends Http{
        public Request(Map<String, String> postData){super(postData);}

        @Override
        protected void onPostExecute(String postResult){
            Log.d(TAG, postResult);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(postResult);
                String rImg = jsonObject.getString("img");
                byte[] decodeString = java.util.Base64.getDecoder().decode(rImg);
                InputStream inputStream = new ByteArrayInputStream(decodeString);
                bitmap = BitmapFactory.decodeStream(inputStream);
                source_image.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
                source_image.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        int RExtstorePermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (RExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),"FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(this);
                }
                break;
        }
    }
    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, (dialogInterface, i) -> {
            if(optionsMenu[i].equals("Take Photo")){
                dispatchTakePictureIntent();
            }
            else if(optionsMenu[i].equals("Choose from Gallery")){
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
            else if (optionsMenu[i].equals("Exit")) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.SellerApp", photoFile);
                Log.d(TAG, "photoURI : " + photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.d(TAG, "photo uri : " + photoURI);
                Log.d(TAG, "extra key : " + MediaStore.EXTRA_OUTPUT);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "resultCode : " + resultCode);
        if (resultCode != RESULT_CANCELED) {
            Log.d(TAG, "requestCode : " + requestCode);
            switch (requestCode) {
                case 102:
                    if (resultCode == RESULT_OK) {
                        File f = new File(currentPhotoPath);
                        source_image.setImageURI(Uri.fromFile(f));
                        Log.d(TAG, "ABsolute Url of Image is " + Uri.fromFile(f));
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            source_image.setImageURI(selectedImage);
                            Log.d(TAG, "absolute uri of image from gallery : " + selectedImage);
                        }
                    }
                    break;
            }
        }
    }
}