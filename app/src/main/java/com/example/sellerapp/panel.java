package com.example.sellerapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class panel extends AppCompatActivity {
    private static String TAG = "panel";
    TextView store_idTextView, panel_titleTextView;
    Button recognizeBtn;
    Set<String> location_status = new HashSet<>();
    LinearLayoutCompat L1, L2, L3, L4, L5, L6;
    ArrayList<LinearLayoutCompat> L = new ArrayList<>();
    ArrayList<TextView> T = new ArrayList<>();
    TextView T1, T2, T3, T4, T5, T6;
    RadioGroup radioGroup;
    RadioButton machine1, machine2, machine3;
    String machineId = "1";
    String location_id;
    private void component() {
        store_idTextView = findViewById(R.id.store_idTextView);
        panel_titleTextView = findViewById(R.id.panel_titleTextView);
        radioGroup = findViewById(R.id.radioGroup);
        machine1 = findViewById(R.id.machine1);
        machine2 = findViewById(R.id.machine2);
        machine3 = findViewById(R.id.machine3);
        recognizeBtn = findViewById(R.id.recognizeBtn);
        L1 = findViewById(R.id.L1);
        L2 = findViewById(R.id.L2);
        L3 = findViewById(R.id.L3);
        L4 = findViewById(R.id.L4);
        L5 = findViewById(R.id.L5);
        L6 = findViewById(R.id.L6);
        T1 = findViewById(R.id.T1);
        T2 = findViewById(R.id.T2);
        T3 = findViewById(R.id.T3);
        T4 = findViewById(R.id.T4);
        T5 = findViewById(R.id.T5);
        T6 = findViewById(R.id.T6);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pannel);

        component();

        L.add(L1);
        L.add(L2);
        L.add(L3);
        L.add(L4);
        L.add(L5);
        L.add(L6);
        T.add(T1);
        T.add(T2);
        T.add(T3);
        T.add(T4);
        T.add(T5);
        T.add(T6);

        L1.setOnClickListener(v -> {
            location_id = "1";
            if (location_status.contains(location_id))
                dialog();
            else {
                Intent intent = new Intent(this, ScanAndStore.class);
                intent.putExtra("store_id", store_idTextView.getText().toString());
                intent.putExtra("machine_id", machineId);
                intent.putExtra("location_id", location_id);
                startActivity(intent);
            }
        });
        L2.setOnClickListener(v -> {
            location_id = "2";
            if (location_status.contains(location_id))
                dialog();
            else {
                Intent intent = new Intent(this, ScanAndStore.class);
                intent.putExtra("store_id", store_idTextView.getText().toString());
                intent.putExtra("machine_id", machineId);
                intent.putExtra("location_id", location_id);
                startActivity(intent);
            }
        });
        L3.setOnClickListener(v -> {
            location_id = "3";
            if (location_status.contains(location_id))
                dialog();
            else {
                Intent intent = new Intent(this, ScanAndStore.class);
                intent.putExtra("store_id", store_idTextView.getText().toString());
                intent.putExtra("machine_id", machineId);
                intent.putExtra("location_id", location_id);
                startActivity(intent);
            }
        });
        L4.setOnClickListener(v -> {
            location_id = "4";
            if (location_status.contains(location_id))
                dialog();
            else {
                Intent intent = new Intent(this, ScanAndStore.class);
                intent.putExtra("store_id", store_idTextView.getText().toString());
                intent.putExtra("machine_id", machineId);
                intent.putExtra("location_id", location_id);
                startActivity(intent);
            }
        });
        L5.setOnClickListener(v -> {
            location_id = "5";
            if (location_status.contains(location_id))
                dialog();
            else {
                Intent intent = new Intent(this, ScanAndStore.class);
                intent.putExtra("store_id", store_idTextView.getText().toString());
                intent.putExtra("machine_id", machineId);
                intent.putExtra("location_id", location_id);
                startActivity(intent);
            }
        });
        L6.setOnClickListener(v -> {
            location_id = "6";
            if (location_status.contains(location_id))
                dialog();
            else {
                Intent intent = new Intent(this, ScanAndStore.class);
                intent.putExtra("store_id", store_idTextView.getText().toString());
                intent.putExtra("machine_id", machineId);
                intent.putExtra("location_id", location_id);
                startActivity(intent);
            }
        });

        String id = getSharedPreferences("store_id", MODE_PRIVATE).getString("store_id", null);
        store_idTextView.setText(id);

        recognizeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, Recognize.class);
            startActivity(intent);
        });

        Map<String, String> postData = new HashMap<>();
        postData.put("store_id", id);
        postData.put("machine_id", machineId);
        StorePanel storePanel = new StorePanel(postData);
        storePanel.execute("http://163.13.201.93/pythonAPI/StorePanel.php");

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            StorePanel storePanel1;
            L1.setBackground(getDrawable(R.drawable.machine_shape));
            L2.setBackground(getDrawable(R.drawable.machine_shape));
            L3.setBackground(getDrawable(R.drawable.machine_shape));
            L4.setBackground(getDrawable(R.drawable.machine_shape));
            L5.setBackground(getDrawable(R.drawable.machine_shape));
            L6.setBackground(getDrawable(R.drawable.machine_shape));
            T1.setText("可使用");
            T2.setText("可使用");
            T3.setText("可使用");
            T4.setText("可使用");
            T5.setText("可使用");
            T6.setText("可使用");

            if (checkedId == R.id.machine1) {
                machineId = "1";
                postData.put("store_id", id);
                postData.put("machine_id", machineId);
                storePanel1 = new StorePanel(postData);
                storePanel1.execute("http://163.13.201.93/pythonAPI/StorePanel.php");
            } else if (checkedId == R.id.machine2) {
                machineId = "2";
                postData.put("store_id", id);
                postData.put("machine_id", machineId);
                storePanel1 = new StorePanel(postData);
                storePanel1.execute("http://163.13.201.93/pythonAPI/StorePanel.php");
            } else if (checkedId == R.id.machine3) {
                machineId = "3";
                postData.put("store_id", id);
                postData.put("machine_id", machineId);
                storePanel1 = new StorePanel(postData);
                storePanel1.execute("http://163.13.201.93/pythonAPI/StorePanel.php");
            }
        });
    }
    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(panel.this);
        builder.setCancelable(true);
        builder.setTitle("是否手動設定成\"可使用\"?");
        builder.setMessage("將位置狀態設定為\"可使用\"");
        builder.setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton("確認", (dialogInterface, i) -> {
            Map<String, String> postData = new HashMap<>();
            postData.put("pickup_store_id", store_idTextView.getText().toString());
            postData.put("machine_id", machineId);
            postData.put("location_id", location_id);
            ModifyStatus modifyStatus = new ModifyStatus(postData);
            modifyStatus.execute("http://163.13.201.93/pythonAPI/ModifyStatus.php");
        });
        builder.create().show();
    }

    protected class ModifyStatus extends Http {
        public ModifyStatus(Map<String, String> postData) {
            super(postData);
        }

        @Override
        protected void onPostExecute(String postResult) {
            Log.d(TAG, "ModifyStatus.postResult : " + postResult);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(postResult);
                if (jsonObject.getString("status").equals("1")) {
                    Toast.makeText(panel.this, "更改成功!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(panel.this, "更改失敗", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }
    protected class StorePanel extends Http {
        public StorePanel(Map<String, String> postData) {
            super(postData);
        }

        @Override
        protected void onPostExecute(String postResult) {
            Log.d(TAG, postResult);
            JSONObject jsonObject;
            location_status = new HashSet<>();
            try {
                jsonObject = new JSONObject(postResult);
                panel_titleTextView.setText(jsonObject.getString("store_name"));
                if (jsonObject.getString("status").equals("1")) {
                    for (int i = 0; i < jsonObject.length()-2; i++) {
                        JSONObject object = new JSONObject(jsonObject.getString(Integer.toString(i)));
                        location_status.add(object.getString("location_id"));
                        Log.d(TAG, "in " + i + ", obj :" + object);
                    }
                    Log.d(TAG, "in location_status" + location_status);
                }
                for (int i = 1; i <= 12; i++) {
                    if (location_status.contains(Integer.toString(i))) {
                        L.get(i-1).setBackground(getDrawable(R.drawable.unavaliable_location_shape));
                        T.get(i-1).setText("使用中");
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }
}