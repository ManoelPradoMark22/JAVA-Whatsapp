package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText confimarSenha;
    private Button botaoCadastrar;
    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = (EditText) findViewById(R.id.edit_cadastro_nome);
        email = (EditText) findViewById(R.id.edit_cadastro_email);
        senha = (EditText) findViewById(R.id.edit_cadastro_senha);
        confimarSenha = (EditText) findViewById(R.id.confirmar_senha);
        botaoCadastrar = (Button) findViewById(R.id.botao_cadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valorNome = nome.getText().toString();
                String valoremail = email.getText().toString();
                String valorSenha = senha.getText().toString();
                String valorConfirmarSenha = confimarSenha.getText().toString();

                if(valorNome.isEmpty()||valoremail.isEmpty()||valorSenha.isEmpty()||valorConfirmarSenha.isEmpty()){
                    Toast.makeText(CadastroUsuarioActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else if(!valorSenha.equals(valorConfirmarSenha)){
                        Toast.makeText(CadastroUsuarioActivity.this,"As senhas não são iguais!", Toast.LENGTH_LONG).show();
                }else{
                    usuario = new Usuario();
                    usuario.setNome(valorNome);
                    usuario.setEmail(valoremail);
                    usuario.setSenha(valorSenha);
                    cadastrarUsuario();
                }
            }
        });
    }
    private void cadastrarUsuario(){

        final String valorSenha = senha.getText().toString();
        final String valorConfirmarSenha = confimarSenha.getText().toString();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //responsavel por verificar se foi feito o cadastro
                if (task.isSuccessful()){ //task é o nosso objeto em questao
                   Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuário!", Toast.LENGTH_LONG).show();
                   FirebaseUser usuarioFirebase = task.getResult().getUser(); //getUser pega os dados do usuário cadastrado
                   //usuario.setId(usuarioFirebase.getUid());   antes de programar a criptografia
                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setId(identificadorUsuario);
                   usuario.salvar();

                    //Quando o usuario faz o cadastro, salvamos o email dele no Preferences para utilizar depois no adicionar contatos
                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                    preferencias.salvarDadosUsuario(identificadorUsuario);  //no cadastro: salvando o email do usuario em preferencias

                   abrirLoginUsuario();

                   //autenticacao.signOut(); //após fazer o cadastro ele tem que fazer o login agora; usavamos qnd as regras do database eram publicas. mas agora que colocamos q apenas usuarios logados podem gravar no banco de dados precisamos tirar isso
                    //finish();

                }else   {//vamos tratar as excecoes

                        String erroExcecao = "";

                        try { //para tratamento de exceções

                            throw task.getException(); //recupera a excecao e com throw lança ela

                        } catch (FirebaseAuthWeakPasswordException e) {
                            erroExcecao = "Digite uma senha mais forte! Contendo mais caracteres e com letras e números!";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erroExcecao = "O e-mail digitado é inválido! Digite um novo e-mail!";
                        } catch (FirebaseAuthUserCollisionException e) {
                            erroExcecao = "E-mail já cadastrado! Tente outro e-mail";
                        } catch (Exception e) {
                            erroExcecao = "Erro ao efetuar o cadastro!";
                            e.printStackTrace();
                        }
                        Toast.makeText(CadastroUsuarioActivity.this, erroExcecao, Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

}
