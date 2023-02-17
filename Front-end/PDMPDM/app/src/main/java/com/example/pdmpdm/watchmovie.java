package com.example.pdmpdm;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class watchmovie extends AppCompatActivity {
    private VideoView Video;
    public Uri selectedVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_watchmovie);
        try {
            submitForm();
        } catch (IOException e) {
            Log.i("Falha do form", "Falha Form");
            e.printStackTrace();
        }
        Button lq = (Button) findViewById(R.id.lq);
        Button hq = (Button) findViewById(R.id.hq);
        lq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hq.setVisibility(View.INVISIBLE);
                lq.setVisibility(View.INVISIBLE);
                try {
                    submitForm();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        hq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hq.setVisibility(View.INVISIBLE);
                lq.setVisibility(View.INVISIBLE);
                try {
                    submitForm();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Video.setVideoURI(selectedVideo);
        MediaController mdc = new MediaController(this);
        mdc.setAnchorView(Video);
        Video.setMediaController(mdc);
        Video.start();
    }
    public void submitForm() throws IOException {

        URL urlAPI = null;
        try {
            urlAPI = new URL("http://34.175.171.114:8080/movie/streaming");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlAPI)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();

                int responseCode = response.code();

                if(response.code() == 200) {
                    try {
                        JSONObject obj = new JSONObject(mMessage);
                        selectedVideo = Uri.parse(obj.getString("movie"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
