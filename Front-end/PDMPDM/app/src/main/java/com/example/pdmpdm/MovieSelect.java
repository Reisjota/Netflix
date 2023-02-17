package com.example.pdmpdm;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MovieSelect extends AppCompatActivity {
    LinearLayout layout;
    Button addview;
    public int p123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_movieselect);

        layout = findViewById(R.id.layout);

        try {
            run();
            //Log.i("teste2","" + p123);

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < p123 ; i++){
            ImageView imageView = new ImageView(MovieSelect.this);

            // setting the image in the layout
            imageView.setImageResource(R.drawable.ic_launcher_background);

            // calling addview with width and height
            addvieW(imageView, 200, 200);
        }

    }
    private final OkHttpClient client = new OkHttpClient();

    public void run() throws Exception {
        URL urlAPI = null;
        try {
            urlAPI = new URL("http://34.175.171.114:8080/movie/search/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(urlAPI)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    String mensagem = responseBody.string();
                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        p123 = responseHeaders.size();
                    }
                    JSONArray array = new JSONArray(response.body().string());
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = array.getJSONObject(j);
                        URI Image = new URI( object.getString(object.getString("linkPoster")));
                        Log.i("res"," " + Image);
                    }
                    Log.i("responseBody"," " + mensagem);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void addvieW(ImageView imageView, int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

        // setting the margin in linearlayout
        params.setMargins(0, 10, 0, 10);
        imageView.setLayoutParams(params);

        // adding the image in layout
        layout.addView(imageView);
    }
}
