package com.example.cms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Encontra o butão ElimUser
        Button elimuser = (Button) findViewById(R.id.ElimUser);
        //Função quando se clica no butão ElimUser
        elimuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mudar de janela para o Eliminar User
                startActivity(new Intent(getApplicationContext(),(EliminarUser.class)));
                finish();
            }
        });

        Button createUser = (Button) findViewById(R.id.createUser);

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),(CriarUser.class)));
                finish();
            }
        });

        Button createMovie = (Button) findViewById(R.id.uploadMovie);

        createMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),(uploadMovie.class)));
                finish();
            }
        });
        Button elimmovie = (Button) findViewById(R.id.DeleteMovie);

        elimmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),(EliminarMovie.class)));
                finish();
            }
        });
    }
}