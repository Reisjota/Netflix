package com.example.cms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EliminarUser extends AppCompatActivity {

    private EditText usernametext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eliminar_user);

        usernametext = findViewById(R.id.year);


        Button backuser = (Button) findViewById(R.id.back);

        backuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),(MainActivity.class)));
                finish();
            }
        });

        Button eliminarUser = (Button) findViewById(R.id.Eliminar);

        eliminarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitForm();
                } catch (IOException e) {
                    Log.i("Falha do form", "Falha Form");
                    e.printStackTrace();
                }
            }
        });
    }
    String teste(String user) {
        return "{\n" + "\"username\"" + ":" + "\"" +  user + "\""  + "\n}";

    }
    public void submitForm() throws IOException {

        URL urlAPI = null;
        try {
            urlAPI = new URL("http://34.175.171.114:8080/user/delete");
        } catch (MalformedURLException e) {
            Log.i("Falha", "Falha Ligação");
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();

        String User = usernametext.getText().toString();

        String json = teste(User);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.Companion.create(json,JSON);

        Log.i("LOG_INTENT", json);
        Request request = new Request.Builder()
                .url(urlAPI)
                .delete(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();

                int responseCode = response.code();

                if(response.code() == 200) {
                    startActivity(new Intent(getApplicationContext(),(MainActivity.class)));
                    finish();
                    showToast(User);
                }
                else{
                    showToastError("Este User não existe");
                }
                Log.i("Resposta",  response + " Mensagem: " + mMessage + " code: " + responseCode);
            }
        });
    }
    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EliminarUser.this,"Movie " + Text + " Eliminado!", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void showToastError(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EliminarUser.this, Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}