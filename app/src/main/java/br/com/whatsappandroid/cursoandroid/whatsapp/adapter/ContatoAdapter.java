package br.com.whatsappandroid.cursoandroid.whatsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Contato;

public class ContatoAdapter extends ArrayAdapter<Contato>{

    private ArrayList<Contato> contatos;
    private Context context;

    public ContatoAdapter(@NonNull Context c, @NonNull ArrayList<Contato> objects) {
        super(c, 0, objects);
        this.contatos = objects;
        this.context = c;
        //adaptador é responsavel por montar a exibicao de cada item da lista
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //criando do zero a vizualicao  //chamado para cada item da lista
        View view = null;

        //Verifica se a lista de contatos está vazia
        if (contatos != null){

            //inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);//interface global que permite q utilzemos classes e serviços do sistema
            //definimos que queremos utilizar o Serviço de montagem de Layout (LAYOUT_INFLATER_SERVICE)

            //Monta a view a partir do xml
            view = inflater.inflate(R.layout.lista_contatos, parent, false);

            //recupera elemento para exibicao
            TextView nomeContato = (TextView) view.findViewById(R.id.tv_nome);
            TextView emailContato = (TextView) view.findViewById(R.id.tv_email);

            Contato contato = contatos.get(position);
            nomeContato.setText(contato.getNome());
            emailContato.setText(contato.getEmail());
        }
        return view;
    }
}
