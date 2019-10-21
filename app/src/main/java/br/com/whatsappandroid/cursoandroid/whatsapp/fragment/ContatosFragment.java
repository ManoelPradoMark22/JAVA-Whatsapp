package br.com.whatsappandroid.cursoandroid.whatsapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.adapter.ContatoAdapter;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Contato;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos; //private ArrayList<String> contatos; se fosse para pegar apenas o Nome por exemplo
    private DatabaseReference referenciaFirebase;
    private ValueEventListener valueEventListenerContatos;

    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() { //chamdo um pouco antes do fragmento carregar totalmente. antes do onCreateView
        super.onStart();
        referenciaFirebase.addValueEventListener(valueEventListenerContatos); //so adicionado o listenet um pouco antes de o fragmento ser iniciado
        //Log.i("ValueEventListener", "onStart");
    }

    @Override
    public void onStop() {  //quando encerra o fragmento o listener é removido
        super.onStop();
        referenciaFirebase.removeEventListener(valueEventListenerContatos);
        //Log.i("ValueEventListener", "onStop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Instanciar objetos
        contatos = new ArrayList<>();

        // Inflate the layout for this fragment
        //converte o fragmentContatos para uma view e é mostrado na tela
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        //monta listView e adapter
        listView = (ListView) view.findViewById(R.id.lv_contatos);

        /*adapter = new ArrayAdapter(   //esse é o adapter padrão, nao vampos usar esse pq iremos criar um próprio!
                //aqui so pode passar um arrayList de String (ou seja, exibe apenas um item), por exemplo o nome
                getActivity(), //recupera a qual activity esse fragmento pertence
                R.layout.lista_contatos, //layout utilizado pelo adapter para exibir os itens na listView
                contatos
        );*/

        adapter = new ContatoAdapter(getActivity(), contatos);

        listView.setAdapter(adapter);

        //Recuperar contatos no Firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        referenciaFirebase = ConfiguracaoFirebase.getFirebase()
                                .child("Contatos")
                                .child(identificadorUsuarioLogado);  //queremos recuperar os contatos do usuario LOGADO apenas!

        //Listener para recuperar contatos (sera notificado toda vez q essa estrutura de nó for alterada)
        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //chamado sempre que os dados do nó forem alterados!
                //caso existam dados, eles estao na variavel dataSanpshot

                //Limpar lista
                contatos.clear(); //para nao repetir de novo!

                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren()){//getChildren percorre os filhos dos dados que estao em dataSnapshot
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);
                }

                adapter.notifyDataSetChanged(); //falamos para o adapter que os dados mudaram!
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        return view;
    }

}
