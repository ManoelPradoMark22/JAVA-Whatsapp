package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;

public class RedefinirSenhaActivity extends AppCompatActivity {

    private Button botaoRedefinirSenha;
    private TextView resultadoRedefinirSenha;
    private EditText email;
    private String textoEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha);

        email = findViewById(R.id.emailRedefinirSenha);
        botaoRedefinirSenha = findViewById(R.id.botaoRedefinirSenha);
        resultadoRedefinirSenha = findViewById(R.id.textoResultadoRedefinirSenha);

        textoEmail = resultadoRedefinirSenha.toString();


    }



}
