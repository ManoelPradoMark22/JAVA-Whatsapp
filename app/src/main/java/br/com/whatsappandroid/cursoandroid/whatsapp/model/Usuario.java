package br.com.whatsappandroid.cursoandroid.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;

    public Usuario(){//construtor vazio
    }

    public void salvar(){
        DatabaseReference referenciaFirebasa = ConfiguracaoFirebase.getFirebase();
        referenciaFirebasa.child("Usuários").child(getId()).setValue(this); //usando this passo o proprio usuario como objeto (para isso tem q ter o construtor iniado, pode estar vazio)
    }

    @Exclude //nao vai salvar o id no banco de dados
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude //nao vai salvar a senha no banco de dados
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
