package com.example.naverid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main3Activity extends AppCompatActivity {
    String strNickname, strProfile, strEmail, strAgeRange, strGender, strBirthday;
    Button button, btnSearch;
    ImageView imageView;
    TextView textView, tvWeather, tvTemperature;
    EditText etSearch;
    double r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView4);
        textView = findViewById(R.id.textView11);
        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);
        tvWeather = findViewById(R.id.tvWeather);
        tvTemperature = findViewById(R.id.tvTemperature);

        Intent intents = getIntent();
        strNickname = intents.getStringExtra("name");
        strProfile = intents.getStringExtra("profile");
        strEmail = intents.getStringExtra("email");
        strAgeRange = intents.getStringExtra("ageRange");
        strGender = intents.getStringExtra("gender");
        strBirthday = intents.getStringExtra("birthday");

        Glide.with(this).load(strProfile).into(imageView);
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);
        textView.setText(strNickname + "님");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", strNickname);
                intent.putExtra("profile", strProfile);
                intent.putExtra("email", strEmail);
                intent.putExtra("ageRange", strAgeRange);
                intent.putExtra("gender", strGender);
                intent.putExtra("birthday", strBirthday);
                startActivity(intent);
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = etSearch.getText().toString();
                Intent intent = new Intent(Main3Activity.this, ListActivity.class);
                intent.putExtra("search", tmp);
                startActivity(intent);
            }
        });

        ExampleAsyncTask asyncTask2 = new ExampleAsyncTask();
        asyncTask2.execute();
    }

    public class ExampleAsyncTask extends AsyncTask<String, Void, JsonObject> {

        ProgressDialog progressDialog = new ProgressDialog(Main3Activity.this);
        OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("\t로딩중...");
            progressDialog.show();
        }

        @Override
        protected JsonObject doInBackground(String... params) {

            Request request = new Request.Builder()
                    .url("https://api.openweathermap.org/data/2.5/weather?q=seoul&APPID=3814581e660f0e7646c4cf2e91d97c96")
                    .build();

            try {
                Response response = client.newCall(request).execute();

                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                JsonObject rootObject = parser.parse(response.body().charStream()).getAsJsonObject();
                Log.d("json1", "doInBackground: " + rootObject);
                return rootObject;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JsonObject result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d("json1", "onPostExecute: " + result);

            //요청 가져온 것들 여기서 처리
            JsonArray array = result.getAsJsonArray("weather");
            JsonElement element = array.get(0);
            String description = element.getAsJsonObject().get("description").getAsString();

            JsonElement object = result.get("main");
            String temp = object.getAsJsonObject().get("temp").getAsString();
            r = Double.parseDouble(temp) - 273.5;

            tvTemperature.setText(String.valueOf(Math.round(r*10)/10.0)+ "°C");
            tvWeather.setText(description);
        }

    }
}
