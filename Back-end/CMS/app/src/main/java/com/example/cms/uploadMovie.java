package com.example.cms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class uploadMovie<phoneNumber> extends AppCompatActivity {
    private EditText usernametext;
    private EditText movieNametext;
    private Button movieFile;
    private ImageButton poster;
    private EditText yeartext;
    private EditText directortext;
    private EditText totaltimetext;
    private EditText genretext;
    private VideoView MOVIEVIEW;
    public int teste = 0;
    public Uri selectedImage;
    public Uri selectedVideo;
    public InputStream POSTERFINAL;
    public InputStream VIDEOFINAL;
    String teste1;
    String teste2;

    private static final MediaType MEDIA_TYPE_JPEG = MediaType.get("image/jpeg");
    private static final MediaType MEDIA_TYPE_MP4 = MediaType.get("video/mp4");
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_movie);


        //Colocar o valor dos EditText
        yeartext = findViewById(R.id.year);
        movieNametext = findViewById(R.id.MovieName);
        totaltimetext = findViewById(R.id.TotalTime);
        movieFile = (Button) findViewById(R.id.buttonvideo);
        poster = (ImageButton) findViewById(R.id.imageFile);
        usernametext = findViewById(R.id.username);
        MOVIEVIEW = (VideoView) findViewById(R.id.videoView);
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teste = 2;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,3);
            }
        });

        movieFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teste = 1;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent,"Select a Video "),3);
            }
        });
        Button uploadfilebutton = (Button) findViewById(R.id.uploadmovief);

        uploadfilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Cria User
                    submitForm();
                } catch (IOException e) {
                    Log.i("Falha do form", "Falha Form");
                    e.printStackTrace();
                }
            }
        });

        Button backumovie = (Button) findViewById(R.id.back);
        //Função para ir para a página principal
        backumovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),(MainActivity.class)));
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            if (teste == 2){
                selectedImage = data.getData();
                teste1 = selectedImage.getPath();
                ImageButton imageBTT;
                imageBTT = findViewById(R.id.imageFile);
                imageBTT.setImageURI(selectedImage);
                Log.i("Image Path", String.valueOf(selectedImage));

            }
            else if(teste == 1) {
                selectedVideo = data.getData();
                teste2 = selectedVideo.getPath();
                MOVIEVIEW.setVideoURI(selectedVideo);
                MediaController mdc = new MediaController(this);
                mdc.setAnchorView(MOVIEVIEW);
                MOVIEVIEW.setMediaController(mdc);
                MOVIEVIEW.start();
                Log.i("Video Path", String.valueOf(selectedVideo));

            }
        }
    }


    public void submitForm() throws IOException {

        URL urlAPI = null;
        try {
            //Url usado para criar o user
            urlAPI = new URL("http://34.175.171.114:8080/movie/upload");
        } catch (MalformedURLException e) {
            Log.i("Falha", "Falha Ligação");
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();


        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File sourceFile = new File(path,teste1);
        File sourceFile2 = new File(path,teste2);

        String User = usernametext.getText().toString();
        String MName = movieNametext.getText().toString();
        String MYear = yeartext.getText().toString();
        String MTT = totaltimetext.getText().toString();
        Log.d("file", "File...::::" + sourceFile + " : " + sourceFile.exists());
        Log.d("file", "File...::::" + sourceFile2 + " : " + sourceFile2.exists());


        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)

                //.addFormDataPart("data", teste1, RequestBody.create(MEDIA_TYPE_JPEG, sourceFile))
                .addFormDataPart("uploaded_file", teste1, RequestBody.create(sourceFile,MEDIA_TYPE_JPEG));

        requestBody.addFormDataPart("username",User);
        requestBody.addFormDataPart("moviename",MName);
        requestBody.addFormDataPart("year",MYear);
        requestBody.addFormDataPart("totaltime",MTT);
        requestBody.addFormDataPart("uploaded_file", "ficheiro2", RequestBody.create(sourceFile2,MEDIA_TYPE_MP4));

        RequestBody requestBodyy = requestBody.build();


        Request request = new Request.Builder()
                .url(urlAPI)
                .post(requestBodyy)
                .build();




        Log.i("REGIS_ITENT", User + " " + POSTERFINAL + " " + MName + " " + MYear + " " + MTT + " " + VIDEOFINAL);

        client.newCall(request).enqueue(new Callback() {
            @Override
            //Caso o request der erro
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                int responseCode = response.code();
                if (response.code() == 200) {
                    startActivity(new Intent(getApplicationContext(), (MainActivity.class)));
                    finish();
                    showToast(movieNametext.getText().toString());
                } else {
                    showToastError("Erro de envio");
                }
                Log.i("Resposta", response + " Mensagem: " + mMessage + " code: " + responseCode);
            }
        });
    }
    //Aparecer o Toast
    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(uploadMovie.this,"User " + Text + " criado!", Toast.LENGTH_LONG).show();
            }
        });
    }
    //Aparecer o Toast de Erro
    public void showToastError(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(uploadMovie.this, Text, Toast.LENGTH_LONG).show();
            }
        });
    }
    public static JSONObject uploadImage(String memberId, String sourceImageFile) {

        URL urlAPI = null;
        try {
            //Url usado para criar o user
            urlAPI = new URL("http://34.175.171.114:8080/movie/upload");
        } catch (MalformedURLException e) {
            Log.i("Falha", "Falha Ligação");
            e.printStackTrace();
        }
        try {
            File sourceFile = new File(sourceImageFile);

            Log.d("file", "File...::::" + sourceFile + " : " + sourceFile.exists());
            //Determining the media type
            final MediaType MEDIA_TYPE = sourceImageFile.endsWith("jgep") ?

                    MediaType.parse("image/jgep") : MediaType.parse("image/png");


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uploaded_file", "ficheiro", RequestBody.create(sourceFile,MEDIA_TYPE_JPEG))
                    .addFormDataPart("result", "my_image")
                    .build();

            Request request = new Request.Builder()
                    .url(urlAPI)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("Erro", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("Erro", "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }
    }

