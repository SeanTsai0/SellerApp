package com.example.sellerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "Index";
    Button go;
    Spinner store_Spinner;
    ArrayList<String> storeId = new ArrayList<>();

    private void component() {
        go = findViewById(R.id.go);
        store_Spinner = findViewById(R.id.store_Spinner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        component();

        go.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("store_id", MODE_PRIVATE);
            sharedPreferences.edit().putString("store_id", storeId.get(store_Spinner.getSelectedItemPosition())).apply();

            Intent intent = new Intent(this, panel.class);
            startActivity(intent);
        });

        SelectStore selectStore = new SelectStore(null);
        selectStore.execute("http://163.13.201.93/pythonAPI/SelectStore.php");
    }

    protected class SelectStore extends Http {
        public SelectStore(Map<String, String> postData){super(postData);}

        @Override
        protected void onPostExecute(String postResult) {
            Log.d(TAG, "postResult : " + postResult);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(postResult);
                String [] storeArray = new String[jsonArray.length()];
                Log.d(TAG, "jsonArray length : " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject perJsonObj = jsonArray.getJSONObject(i);
                    storeArray[i] = perJsonObj.getString("storename") + "-" + perJsonObj.getString("address");
                    storeId.add(perJsonObj.getString("id"));
                }
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, R.layout.spinner_layout, storeArray);
                adapter.setDropDownViewResource(R.layout.spinner_expand_layout);
                store_Spinner.setAdapter(adapter);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}