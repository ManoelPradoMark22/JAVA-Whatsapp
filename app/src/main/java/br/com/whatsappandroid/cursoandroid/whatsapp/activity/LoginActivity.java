package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.Manifest;
import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Random;

import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.permissao;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        email = (EditText) findViewById(R.id.edit_login_email);
        senha = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar = (Button) findViewById(R.id.botao_logar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valorEmail = email.getText().toString();
                String valorSenha = senha.getText().toString();

                if (valorEmail.isEmpty()||valorSenha.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }else{
                usuario = new Usuario();
                usuario.setEmail(valorEmail);
                usuario.setSenha(valorSenha);
                validarLogin();
                }
            }
        });
    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null){ //getCurrentUser recupera o usuario atual
            abrirTelaPrincipal();
        }
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    //Quando o usuario faz login, salvamos o email dele no Preferences para utilizar depois no adicionar contatos
                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                    String identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail()); //pegando o email do usuarioLogado
                    preferencias.salvarDadosUsuario(identificadorUsuarioLogado);  //no Login: salvando o email do usuario em preferencias

                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer Login!", Toast.LENGTH_SHORT).show();
                    abrirTelaPrincipal();
                }else {

                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        erroExcecao = "Email não cadastrado!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "Senha incorreta!";
                    }catch (Exception e){
                        erroExcecao = "Erro ao efetuar o Login!";
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, erroExcecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view){
        Toast.makeText(LoginActivity.this, "Cadastrar novo usuário...", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    public void abrirRedefinirSenha(View view){
        Toast.makeText(LoginActivity.this, "Redefinir Senha...", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(LoginActivity.this, RedefinirSenhaActivity.class);
        startActivity(intent);
    }

}
