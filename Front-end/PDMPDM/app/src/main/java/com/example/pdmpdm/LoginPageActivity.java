package com.example.pdmpdm;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginPageActivity extends AppCompatActivity {
    private EditText usernametext;
    private EditText passwordtext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_login);

        usernametext = findViewById(R.id.Username);
        passwordtext = findViewById(R.id.password);

        Button registerpagebutton = (Button) findViewById(R.id.register);

        registerpagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),(RegisterPageActivity.class)));
                finish();
            }
        });

        Button loginbutton = (Button) findViewById(R.id.login);

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
    String teste(String player1, String player2) {
        return "{\n" + "\"username\"" + ":" + "\"" +  player1 + "\"" +  ",\n" +
                "\"password\"" +":"+ "\"" + player2 + "\"\n" + "}";

    }
    public void submitForm() throws IOException {

            URL urlAPI = null;
            try {
                urlAPI = new URL("http://34.175.171.114:8080/user/login");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();

            String User = usernametext.getText().toString();
            String PW = passwordtext.getText().toString();

            String json = teste(User, PW);

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            RequestBody body = RequestBody.Companion.create(json,JSON);

            Log.i("LOG_INTENT", User + PW);
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
                            startActivity(new Intent(getApplicationContext(), (MovieSelect.class)));
                            finish();
                            showToast(User);

                        }
                        else{
                            showToastError("Wrong Username n password");
                        }
                    Log.i("Resposta",  response + " Mensagem: " + mMessage + " code: " + responseCode);
                }
            });

    }
    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginPageActivity.this,"Ola "+ Text, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void showToastError(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginPageActivity.this, Text, Toast.LENGTH_LONG).show();
            }
        });
    }

}