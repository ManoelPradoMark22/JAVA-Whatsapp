package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.adapter.TabAdapter;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custom;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.SlidingTabLayout;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Contato;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;    //(android.support.v7.widget.Toolbar)
    private FirebaseAuth usuarioAutenticacao;   //manipula os usuarios

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    private String identificadorContato; //usado na parte de adicionar contato no MainActivity
    private DatabaseReference referenciaFirebase; //manipula o banco de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar); //importante colocar isso!!! suporte apenas

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);  //as abas
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);  //o viewPager abaixo das abas

        //Configurar Sliding tab
        slidingTabLayout.setDistributeEvenly(true); //distribuir nossas Tabs de forma preenchida
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccente));

        //Configurar o Adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager()); //esse getSupportFragmentManager() é o objeto q gerencia os fragmentos
        //esse tabAdapter vai ser utilizado pela viewPAger
        //a viewPager, para cada item (pagina) que vai exibir, ela chama o método getItem
        viewPager.setAdapter(tabAdapter); //esse tabadapter vai recuperar as quantidades de paginas a serem criadas e os titulos de cada pagina (conforme a clase TabAdapter)

        slidingTabLayout.setViewPager(viewPager);//passamos o viewPager para nosso slidingtabLayout

    }

    //chamado automaticamente na montagem do menu quando a activity for chamada

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //cria um objeto do tipo inflater já com um contexto pra nossa aplicacao
        //MenuInflater, essa classe é usada para inflar os menus (exibir os menus na tela)
        inflater.inflate(R.menu.menu_main, menu); //pega o seu menu e passa para o que o Android consegue exibir na tela
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {  //os botos da toolbar
        switch (item.getItemId()){//retorna qual menu foi selecionado
            case R.id.item_sair:
                deslogarUsuario();
                return true;

            case R.id.item_configuracoes:
                Toast.makeText(MainActivity.this, "Configurações...", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item_adicionar:
                abrirAdicionarContato();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void abrirAdicionarContato(){ //pode ser private pq vai ser utilizado apenas aqui!

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //Configurações do Dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("Digite o e-mail do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        //Configurações dos botões
        alertDialog.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = editText.getText().toString();

                if (emailContato.isEmpty()){ //valida se o email foi digitado
                    Toast.makeText(MainActivity.this, "Preencha o e-mail", Toast.LENGTH_SHORT).show();
                }else{

                    //verifica se o email a ser adicionado já está cadastrado no nosso app
                    identificadorContato = Base64Custom.codificarBase64(emailContato);     //criamos o identificadorContato como atributo (fora do método) pq precisaremos utilizá-lo em outro método

                    //Recupera a instância do Firebase  (vamos pegar o email que ta salvo no database para comparar com o passado)
                    referenciaFirebase = ConfiguracaoFirebase.getFirebase().child("Usuários").child(identificadorContato); //definimos a referencia que queremos para podermos fazer uma consulta

                    referenciaFirebase.addListenerForSingleValueEvent(new ValueEventListener() { //consulta apenas uma vez
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() != null){ //caso o email a ser adicionado já esteja cadastrado no app

                                //recuperar os dados do contato a ser adicionado
                                Usuario usuarioContato = dataSnapshot.getValue(Usuario.class); //pega os dados do usuario

                                //recuperando o email do usuario logado atualmente no app (base64)
                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();
                                referenciaFirebase = ConfiguracaoFirebase.getFirebase();
                                referenciaFirebase = referenciaFirebase.child("Contatos")
                                        .child(identificadorUsuarioLogado)
                                        .child(identificadorContato);
                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario(identificadorContato);
                                contato.setEmail(usuarioContato.getEmail());
                                contato.setNome(usuarioContato.getNome());

                                referenciaFirebase.setValue(contato);   //salvando no banco de dadso
                                /* exemplo dos nós no firebase
                                + Contatos
                                    + manoelprado2@hotmail.com (Usuário logado)
                                        + manoelprado.aecjr@gmail.com (Contato a ser adicionado)
                                            dados......
                                            - identificador do contato
                                            - email
                                            - nome
                                        + azevedo_dudu@hotmail.com (Contato a ser adicionado)
                                            dados......
                                 */
                                Toast.makeText(MainActivity.this, "E-mail adicionado com sucesso!", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(MainActivity.this, "E-mail digitado não foi cadastrado no app!", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    private void deslogarUsuario(){ //pode ser private pq vai ser utilizado apenas aqui!
        usuarioAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
