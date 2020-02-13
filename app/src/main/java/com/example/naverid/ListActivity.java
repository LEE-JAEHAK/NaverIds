package com.example.naverid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListActivity extends AppCompatActivity {
    String tmp;
    ArrayList<BlogList> arrayList = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

       // mWebView = (WebView) findViewById(R.id.webView);

        //mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
//        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
//        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
//        mWebSettings.setSupportMultipleWindows(true); // 새창 띄우기 허용 여부
//        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
//        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
//        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
//        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
//        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
//        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
//        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부


        Intent intent = getIntent();
        tmp = intent.getStringExtra("search");

        ExampleAsyncTask asyncTask = new ExampleAsyncTask();
        asyncTask.execute();
    }

    public class ExampleAsyncTask extends AsyncTask<String, Void, JsonObject> {

        ProgressDialog progressDialog = new ProgressDialog(ListActivity.this);
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
                    .url("https://openapi.naver.com/v1/search/news.json?query=" + tmp + "&display=10")
                    .addHeader("X-Naver-Client-Id", "YYVcX2Awo6gErINXvrUM")
                    .addHeader("X-Naver-Client-Secret", "wrAmpDdqIx")
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

        @Override       //요청 가져온것 여기서 처리
        protected void onPostExecute(JsonObject result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d("json1", "onPostExecute: " + result);

            Adapter adapter = new Adapter(arrayList, ListActivity.this);
            listView = findViewById(R.id.listView);

            for (int i = 0; i < 10; i++) {
                JsonArray array = result.getAsJsonArray("items");
                JsonElement element = array.get(i);
                String title = element.getAsJsonObject().get("title").getAsString();
                String link = element.getAsJsonObject().get("link").getAsString();
                String description = element.getAsJsonObject().get("description").getAsString();
                String pubDate = element.getAsJsonObject().get("pubDate").getAsString();
                arrayList.add(new BlogList(Html.fromHtml(title).toString(), Html.fromHtml(link).toString(),
                        Html.fromHtml(description).toString(), Html.fromHtml(pubDate).toString()));
            }

            listView.setAdapter(adapter);

//            mTitle.setText(title);
//            //mWebView.loadUrl(link); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
//            mLink.setText(Html.fromHtml(link).toString());
//            mDescription.setText(Html.fromHtml(description).toString());
//            mPubdate.setText(Html.fromHtml(pubDate).toString());

        }
    }
}
