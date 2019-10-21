package br.com.whatsappandroid.cursoandroid.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFirebase { //acrescentamos final para fazer com q a classe nao possa ser estendida!

    private static DatabaseReference referenciaFirebase;  //sendo static o valor desse atributo será o mesmo independente das instancias
    private static FirebaseAuth autenticacao;

    public static DatabaseReference getFirebase() { //como static não precisa fazer a instancia usando new

        if (referenciaFirebase == null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao (){
        if (autenticacao==null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
