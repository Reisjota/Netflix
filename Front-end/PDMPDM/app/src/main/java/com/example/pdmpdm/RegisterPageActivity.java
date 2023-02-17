package com.example.pdmpdm;

import static com.example.pdmpdm.R.id.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterPageActivity extends AppCompatActivity {

    private EditText usernametext;
    private EditText passwordtext;
    private EditText fullnametext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        Button loginpagebutton = (Button) findViewById(login);

        loginpagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),(LoginPageActivity.class)));
                finish();
            }
        });
        /* ------------------------- Find View by Id ------------------------- */
        usernametext = findViewById(R.id.Username);
        passwordtext = findViewById(R.id.password);
        fullnametext = findViewById(R.id.full_name);

        Button loginbutton = (Button) findViewById(R.id.register);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitForm();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    String teste(String user, String pass, String fulln) {
        return "{\n" + "\"username\"" + ":" + "\"" +  user + "\"" +  ",\n" +
                "\"password\"" +":"+ "\"" + pass + "\"\n" +",\n" +
                "\"fullname\"" +":"+ "\"" + fulln + "\"\n" + "}";

    }
    public void submitForm() throws IOException {

        URL urlAPI = null;
        try {
            urlAPI = new URL("http://34.175.171.114:8080/user/create");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();

        String User = usernametext.getText().toString();
        String PW = passwordtext.getText().toString();
        String FN = fullnametext.getText().toString();

        String json = teste(User, PW, FN);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.Companion.create(json,JSON);

        Log.i("LOG_INTENT", User + PW + FN);
        Request request = new Request.Builder()
                .url(urlAPI)
                .post(body)
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
                    startActivity(new Intent(getApplicationContext(), (LoginPageActivity.class)));
                    finish();
                    showToast(FN);
                }
                else{
                    showToastError("Already exists this Username");
                }
                Log.i("Resposta",  response + " Mensagem: " + mMessage + " code: " + responseCode);
            }
        });
    }
    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterPageActivity.this,"Bem Vindo " + Text + " faça agora o Login!", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void showToastError(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterPageActivity.this, Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
