package com.br.ifoodclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.ifoodclone.R;

import helpers.UsuarioFirebase;
import model.Empresa;
import model.Produto;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    private EditText etPrecoProduto, etNomeProduto, etDescricaoProduto;
    private String idUsuarioLogado;
    private String idProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializaComponentes();

        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

    }

    private void inicializaComponentes() {
        etDescricaoProduto = findViewById(R.id.editDescricaoProduto);
        etNomeProduto = findViewById(R.id.editNomeProduto);
        etPrecoProduto = findViewById(R.id.editPrecoProduto);

    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_LONG).show();
    }

    public void validarDadosProduto(View view) {

        String nome = etNomeProduto.getText().toString();
        String descricao = etDescricaoProduto.getText().toString();
        String preco = etPrecoProduto.getText().toString();

        if(!nome.isEmpty() || !descricao.isEmpty() || !preco.isEmpty()){

            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setPreco(Double.parseDouble(preco));
            produto.setIdUsuario(idUsuarioLogado);
            produto.salvar();
            finish();

            exibirMensagem("Produto salvo com sucesso!");

        }else{
            exibirMensagem("Todos os campos são obrigatórios!");
        }

    }
}
