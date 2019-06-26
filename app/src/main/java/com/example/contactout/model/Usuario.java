package com.example.contactout.model;

public class Usuario {

    private String id;
    private String email;
    private Contato[] listaContatos;

    public Usuario(){}

    public Usuario(String id, String email){
        this.id = id;
        this.email = email;
        this.listaContatos = new Contato[5];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contato[] getListaContatos() {
        return listaContatos;
    }

    public void setListaContatos(Contato[] listaContatos) {
        this.listaContatos = listaContatos;
    }
}
