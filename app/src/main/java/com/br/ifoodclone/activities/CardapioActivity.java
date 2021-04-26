package com.br.ifoodclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;

import com.br.ifoodclone.R;
import com.google.firebase.database.DatabaseReference;

import helpers.ConfiguracaoFirebase;
import helpers.UsuarioFirebase;

public class CardapioActivity extends AppCompatActivity {

    private DatabaseReference firebaseRef;
    private String idUsuario;
    private EditText editUsuarioNome, editUsuarioEndereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        inicializaComponentes();


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cardápio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void inicializaComponentes() {

    }
}