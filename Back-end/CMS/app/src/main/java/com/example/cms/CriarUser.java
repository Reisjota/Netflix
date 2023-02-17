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

public class CriarUser extends AppCompatActivity {

    //Vai ser usado para colocar o valor usado nos EditText
    private EditText usernametext;
    private EditText passwordtext;
    private EditText fullnametext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_user);

        //Colocar o valor dos EditText
        usernametext = findViewById(R.id.year);
        passwordtext = findViewById(R.id.director);
        fullnametext = findViewById(R.id.MovieName);

        Button registerbutton = (Button) findViewById(R.id.uploadmovief);

        registerbutton.setOnClickListener(new View.OnClickListener() {
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

        Button backuser = (Button) findViewById(R.id.back);
        //Função para ir para a página principal
        backuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),(MainActivity.class)));
                finish();
            }
        });
    }

    //Função para criar o objeto Json
    String teste(String user, String pass, String fulln) {
        return "{\n" + "\"username\"" + ":" + "\"" +  user + "\"" +  ",\n" +
                "\"password\"" +":"+ "\"" + pass + "\"\n" +",\n" +
                "\"fullname\"" +":"+ "\"" + fulln + "\"\n" + "}";

    }
    public void submitForm() throws IOException {

        URL urlAPI = null;
        try {
            //Url usado para criar o user
            urlAPI = new URL("http://34.175.171.114:8080/user/create");
        } catch (MalformedURLException e) {
            Log.i("Falha", "Falha Ligação");
            e.printStackTrace();
        }
        //Inicializar a ligação

        OkHttpClient client = new OkHttpClient();

        //Preencher o User, PW e FN com os valores colocados nas EditText
        String User = usernametext.getText().toString();
        String PW = passwordtext.getText().toString();
        String FN = fullnametext.getText().toString();

        //Criar o json
        String json = teste(User, PW, FN);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        //O body do request que irá ser realizado
        RequestBody body = RequestBody.Companion.create(json,JSON);
        //Se fores ao RUN vês o que está acontecer, como se fosse uma consola em C
        Log.i("LOG_INTENT", User + FN + PW);
        //Fazer o request
        Request request = new Request.Builder()
                //URL do site
                .url(urlAPI)
                //cuidado com o método, neste caso é .post mas podia ser .get ou .delete e por ai fora
                .post(body)
                //realizar o request
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            //Caso o request der erro
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                e.printStackTrace();
            }

            @Override
            //Caso ocorra uma resposta
            public void onResponse(Call call, Response response) throws IOException {
                //Fica com o body da mensagem
                String mMessage = response.body().string();
                //Código HTTP da resposta
                int responseCode = response.code();
                //Caso o request tenha dado correto
                if(response.code() == 200) {
                    //Vai para a página inicial
                    startActivity(new Intent(getApplicationContext(),(MainActivity.class)));
                    finish();
                    //Função para aparecer um Toast
                    showToast(FN);
                }
                else{
                    //Função para aparecer um Toast de erro
                    showToastError("Este User já existe");
                }
                //Para ver a resposta no run
                Log.i("Resposta",  response + " Mensagem: " + mMessage + " code: " + responseCode);
            }
        });
    }
    //Aparecer o Toast
    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CriarUser.this,"User " + Text + " criado!", Toast.LENGTH_LONG).show();
            }
        });
    }
    //Aparecer o Toast de Erro
    public void showToastError(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CriarUser.this, Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}