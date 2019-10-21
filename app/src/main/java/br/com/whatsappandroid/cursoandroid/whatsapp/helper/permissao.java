package br.com.whatsappandroid.cursoandroid.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class permissao {

    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes){

        if (Build.VERSION.SDK_INT >= 23){
            List<String> listaPermissoes = new ArrayList<String>();   //LISTA DAS PERMISSOES A SEREM SOLICITADAS!! ou seja, das que vc ainda nao per

            //percorre as permissoes passadas e verifica se tem a permissao liberada
            for (String permissao : permissoes){ /*para cada uma permissao (dentro da string[] permissoes) sera guardada na String permissao a cada loop do For, que sera
                utilizada para adicionar na listaPermissoes*/
                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                //validaPermissao = true se tem permissao / =false se nao tem a permissao

                if (!validaPermissao){ //se nao tem a permissao
                    listaPermissoes.add(permissao); //guardamos em listaPermissoes a permissao recorrento no FOR
                }
            }

            //caso  a lista esteja vazia, não é necessário solicitar permissão
            if (listaPermissoes.isEmpty()){
                return true;
            }

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //solicita permissao
            //aqui o array precisa ser do tipo String e nao list (fizemos isso anteriormente)
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

        }
        //else
        return  true;

    }

}
