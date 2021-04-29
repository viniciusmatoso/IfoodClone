package com.br.ifoodclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.br.ifoodclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapters.AdapterProduto;
import helpers.ConfiguracaoFirebase;
import helpers.UsuarioFirebase;
import listener.RecyclerItemListener;
import model.Produto;

public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseRef;
    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood - Empresa");

        setSupportActionBar(toolbar);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        inicializaComponentes();

        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutos.setAdapter(adapterProduto);

        recuperarProdutos();

        recyclerProdutos.addOnItemTouchListener(
                new RecyclerItemListener(
                        this,
                        recyclerProdutos,
                        new RecyclerItemListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Produto produtosSelecionado = produtos.get(position);
                        produtosSelecionado.remover();

                        Toast.makeText(EmpresaActivity.this, "Produto removido com sucesso!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                })
        );

    }

    private void inicializaComponentes() {
        recyclerProdutos = findViewById(R.id.rcvProdutos);
    }

    private void recuperarProdutos() {
        DatabaseReference produtosRef = firebaseRef
                .child("produtos")
                .child(idUsuarioLogado);

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtos.clear();

                for(DataSnapshot ds: snapshot.getChildren()){
                    produtos.add(ds.getValue(Produto.class));
                }

                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair:
                 deslogarUsuario();
                 break;

            case R.id.menuConfiguracoes:
                 abrirConfiguracoes();
                 break;

            case R.id.menuNovoProduto:
                 abrirNovoProduto();
                 break;
            case R.id.menuPedidos:
                abrirPedidos();

        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirPedidos() {
        startActivity(new Intent(EmpresaActivity.this, PedidosActivity.class));
    }

    private void deslogarUsuario(){
        try{
            autenticacao.signOut();
            finish();
            startActivity(new Intent(EmpresaActivity.this, AutenticacaoActivity.class));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void abrirConfiguracoes(){
        startActivity(new Intent(EmpresaActivity.this, ConfiguracaoEmpresaActivity.class));
    }

    private void abrirNovoProduto(){
        startActivity(new Intent(EmpresaActivity.this, NovoProdutoEmpresaActivity.class));
    }
}