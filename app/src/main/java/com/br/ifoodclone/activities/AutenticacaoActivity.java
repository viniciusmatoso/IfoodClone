package com.br.ifoodclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import com.br.ifoodclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import helpers.ConfiguracaoFirebase;
import helpers.UsuarioFirebase;

public class AutenticacaoActivity extends AppCompatActivity {

    // Instanciando os elementos do código
    private Button btnAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso, tipoUsuario;
    private LinearLayout linearTipoUsuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);

        // Para ocultar a ActionBar apenas em uma activity especifica
        //getSupportActionBar().hide();

        // Chamando o método de inicialização dos componentes
        inicializaComponentes();

        // Pegando as informaçoes da configuração Firebase Autenticacao
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        // Para deslogar o usuário
        // autenticacao.signOut();

        // Verifica usuário logado
        verificaUsuarioLogado();


        tipoAcesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){

                    linearTipoUsuario.setVisibility(View.VISIBLE);

                } else{

                    linearTipoUsuario.setVisibility(View.GONE);

                }

            }
        });

        // Configurando o botão acessar
        btnAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Recuperando as informações digitadas
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                // Verifica o Email
                if (!email.isEmpty()) {
                    // Verifica a Senha
                    if (!senha.isEmpty()) {
                        // Caso o email e senha não estejam vazios... executamos esse trecho

                        // Verificamos o estado do Switch
                        if (tipoAcesso.isChecked()) { // Cadastro
                            autenticacao.createUserWithEmailAndPassword(
                                    email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Caso o cadastro seja realizado com sucesso
                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Cadastro realizado com sucesso!",
                                                Toast.LENGTH_SHORT).show();

                                        //recuperando tipo do usuário
                                        String tipoUsuario = getTipoUsuario();
                                        UsuarioFirebase.atualizarTipoUsuario(tipoUsuario);

                                        // Chamando a Activity Home
                                        abrirTelaPrincipal(tipoUsuario);

                                    } else {
                                        // Em caso de erro, mostrar as mensagens correspondentes
                                        String erroExcecao = "";

                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException e) {
                                            erroExcecao = "Digite uma senha mais forte!";
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            erroExcecao = "Por favor, digite um e-mail válido!";
                                        } catch (FirebaseAuthUserCollisionException e) {
                                            erroExcecao = "E-mail já cadastrado!";
                                        } catch (Exception e) {
                                            erroExcecao = "ao cadastrar usuário: " + e.getMessage();
                                        }

                                        // Montagem da mensagem em caso de erro
                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Erro: " + erroExcecao,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else { // Login
                            autenticacao.signInWithEmailAndPassword(
                                    email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        // Mensagem de Sucesso
                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Logado com sucesso!",
                                                Toast.LENGTH_SHORT).show();


                                        //recuperando tipo do usuário
                                        String tipoUsuario = task.getResult().getUser().getDisplayName();

                                        // Chamando a tela principal
                                        abrirTelaPrincipal(tipoUsuario);

                                    } else {
                                        // Mensagem de Erro
                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Erro ao fazer login!" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        // Mensagem em caso de Senha Vazia
                        Toast.makeText(AutenticacaoActivity.this,
                                "Preencha a senha!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Mensagem em caso de E-mail Vazio
                    Toast.makeText(AutenticacaoActivity.this,
                            "Preencha o e-mail!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método responsável por verificar usuário logado
    private void verificaUsuarioLogado(){
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if(usuarioAtual != null) {
            String tipoUsuario = usuarioAtual.getDisplayName();
            abrirTelaPrincipal(tipoUsuario);
        }

    }

    //if ternario para ver se é empresa ou usuário
    private String getTipoUsuario() {
        return tipoUsuario.isChecked() ? "E" : "U";
    }

    // Método responsável por abrir a tela principal
    private void abrirTelaPrincipal(String tipoUsuario) {
        if(tipoUsuario.equals("E")){

            startActivity(new Intent(getApplicationContext(), EmpresaActivity.class));
            finish();
        }else{

            startActivity(new Intent(getApplicationContext(), UsuarioActivity.class));
            finish();
        }

    }

    // Inicializando todos os elementos gráficos
    private void inicializaComponentes() {
        campoEmail = findViewById(R.id.edtCadastroEmail);
        campoSenha = findViewById(R.id.edtCadastroSenha);
        btnAcessar = findViewById(R.id.btnAcessar);
        tipoUsuario = findViewById(R.id.switchTipoUsuario);
        tipoAcesso = findViewById(R.id.switchAcesso);
        linearTipoUsuario = findViewById(R.id.linearTipoUsuario);
    }
}