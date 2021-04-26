package com.br.ifoodclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.ifoodclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import adapters.AdapterEmpresa;
import helpers.ConfiguracaoFirebase;
import helpers.UsuarioFirebase;
import listener.RecyclerItemListener;
import model.Empresa;

;

public class UsuarioActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;
    private RecyclerView rcvEmpresas;
    private List<Empresa> empresas = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private AdapterEmpresa adapterEmpresa;
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inicializaComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        firebaseRef = ConfiguracaoFirebase.getFirebase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood");
        setSupportActionBar(toolbar);

        rcvEmpresas.setLayoutManager(new LinearLayoutManager(this));
        rcvEmpresas.setHasFixedSize(true);
        adapterEmpresa = new AdapterEmpresa(empresas);
        rcvEmpresas.setAdapter(adapterEmpresa);

        recuperarEmpresas();

        //configurando search view
        searchView.setHint("Pesquisar restaurantes");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarEmpresas(newText);
                return true;
            }
        });

        rcvEmpresas.addOnItemTouchListener(
                new RecyclerItemListener(
                        this,
                        rcvEmpresas,
                        new RecyclerItemListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Empresa empresaSelecionada = empresas.get(position);
                                Intent i = new Intent(UsuarioActivity.this, CardapioActivity.class);

                                i.putExtra("empresa", empresaSelecionada);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );
    }

    private void pesquisarEmpresas(String minhaPesquisa) {
        DatabaseReference empresasRef = firebaseRef.child("empresas");

        //criando a query
        Query query = empresasRef.orderByChild("nome").startAt(minhaPesquisa + "\uf8ff");

        //configurando query
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresas.clear();

                for (DataSnapshot ds: snapshot.getChildren()){
                    empresas.add(ds.getValue(Empresa.class));
                }

                adapterEmpresa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperarEmpresas() {
        DatabaseReference empresaRef = firebaseRef.child("empresas");
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresas.clear();

                for (DataSnapshot ds: snapshot.getChildren()){
                    empresas.add(ds.getValue(Empresa.class));
                }

                adapterEmpresa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);

        //configurando pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisar);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;

            case R.id.menuSair:
                deslogarUsuario();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirConfiguracoes() {
        startActivity(new Intent(UsuarioActivity.this, ConfiguracaoUsuarioActivity.class));
    }

    private void deslogarUsuario(){
        try{
            autenticacao.signOut();
            finish();
            startActivity(new Intent(UsuarioActivity.this, AutenticacaoActivity.class));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void inicializaComponentes() {
        searchView = findViewById(R.id.materialSearchView);
        rcvEmpresas = findViewById(R.id.rcvEmpresas);
    }
}