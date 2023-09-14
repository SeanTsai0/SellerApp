package com.example.sellerapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScanAndStore extends AppCompatActivity {
    private static String TAG = "ScanAndStore";
    SurfaceView surfaceView;
    TextView user_id, item_id, machine_id, amount, arrived_date, location_id, pickup_store_id, sender_address;
    CameraSource cameraSource;      //相機
    BarcodeDetector barcodeDetector;//Google的Vision套件
    Button confirm_button;

    private void component() {
        surfaceView = findViewById(R.id.surfaceView);
        user_id = findViewById(R.id.user_id);
        item_id = findViewById(R.id.item_id);
        machine_id = findViewById(R.id.machine_id);
        amount = findViewById(R.id.amount);
        arrived_date = findViewById(R.id.arrived_date);
        location_id = findViewById(R.id.location_id);
        pickup_store_id = findViewById(R.id.pickup_store_id);
        sender_address = findViewById(R.id.sender_address);
        confirm_button = findViewById(R.id.confirm_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_and_store);

        component();
        Intent intent = this.getIntent();//取得傳遞過來的資料
        String storeId = intent.getStringExtra("store_id");
        String machineId = intent.getStringExtra("machine_id");
        String locationId = intent.getStringExtra("location_id");
        machine_id.setText(machineId);
        location_id.setText(locationId);
        pickup_store_id.setText(storeId);

        confirm_button.setOnClickListener(v -> {
            Map<String, String> postData = new HashMap<>();
            postData.put("user_id", user_id.getText().toString());
            postData.put("item_id", item_id.getText().toString());
            postData.put("machine_id", machineId);
            postData.put("amount", amount.getText().toString());
            postData.put("arrived_date", arrived_date.getText().toString());
            postData.put("location_id", locationId);
            postData.put("pickup_store_id", storeId);
            postData.put("sender_address", sender_address.getText().toString());
            InsertOrder insertOrder = new InsertOrder(postData);
            insertOrder.execute("http://163.13.201.93/pythonAPI/InsertOrder.php");
        });

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this,barcodeDetector).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                    return;
                try{
                    cameraSource.start(surfaceHolder);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){

            @Override
            public void release() {
            }
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes=detections.getDetectedItems();
                if(qrCodes.size()!=0){
                    JSONObject jsonObject;
                    String scanResult = qrCodes.valueAt(0).displayValue;
                    Log.d(TAG, "scanResult : " + scanResult);
                    try {
                        jsonObject = new JSONObject(scanResult);
                        user_id.setText(jsonObject.getString("user_id"));
                        item_id.setText((jsonObject.getString("item_id")));
                        amount.setText(jsonObject.getString("amount"));
                        arrived_date.setText((jsonObject.getString("arrived_date")));
                        sender_address.setText(jsonObject.getString("sender_address"));
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    Log.d(TAG, qrCodes.valueAt(0).displayValue);
                }
            }
        });
    }
    protected class InsertOrder extends Http {
        public InsertOrder(Map<String, String> postData) {
            super(postData);
        }

        @Override
        protected void onPostExecute(String postResult) {
            Log.d(TAG, "onPostExecute : " + postResult);
        }
    }
}