package br.com.whatsappandroid.cursoandroid.whatsapp.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codificarBase64(String texto){
        return  Base64.encodeToString(texto.getBytes(), Base64.DEFAULT /*primeiro param é o texto em BYTES e o segundo é o tipo de criptografia*/)
                .replaceAll("(\\n)||(\\r)", ""); //primeiro strings (letras etc) que vc quer localizar e segundo strings que vc quer substituir
                //exemplo: batata -> replaceAll("a","o") fica bototo
                //olhar os links que identificam os caracteres de escape https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6
    }

    public static String decodificarBase64 (String textoCodificado){
       return new String(Base64.decode(textoCodificado, Base64.DEFAULT)); //convertendo de byte para string para depois dar o return
    }

}
