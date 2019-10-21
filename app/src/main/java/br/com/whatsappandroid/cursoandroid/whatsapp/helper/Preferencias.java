package br.com.whatsappandroid.cursoandroid.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.HashMap;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "whatsapp.preferencias";
    private int MODE = 0; //apenas o app tem acesso ao arquivo de preferencias
    private SharedPreferences.Editor editor;

    private String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";

    //construtor
    public Preferencias(Context contextoParametro){
        //usando o contexto podemos manipular as preferencias

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);//MODE =0apenas o app tem acesso ao arquivo de preferencias
        editor = preferences.edit(); //início da possibilidade de edicao das preferencias. editar, inserir ou remover itens de preferencias
    }

    public void salvarDadosUsuario (String identificadorUsuario){

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.commit(); //salva os dados no arquivo de preferencia criado
    }

    public String getIdentificador (){  //retorna o email do usuario salvo no cadastro e no login! para ser usado como validacao no adicionar contato
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }
}
