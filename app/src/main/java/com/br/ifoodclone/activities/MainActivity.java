package com.br.ifoodclone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.br.ifoodclone.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ocultando a ActionBar na MainActivity
        //getSupportActionBar().hide();

        // Código responsável por contar o tempo para mudar da splash para Autenticacao
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirAutenticacao(); // Chamada do método
            }
        }, 3000);
    }

    // Método responsável por chamar a tela de Autenticacao
    private void abrirAutenticacao(){
        Intent i = new Intent(MainActivity.this, AutenticacaoActivity.class);
        startActivity(i);
        finish(); // Para encerrar a Activity
    }
}