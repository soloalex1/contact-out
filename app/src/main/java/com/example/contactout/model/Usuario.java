package com.example.contactout.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private String id;
    private String email;
    private List<Contato> listaContatos;

    public Usuario(){}

    public Usuario(String id, String email){
        this.id = id;
        this.email = email;
        this.listaContatos = new ArrayList<Contato>(5);
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

    public List<Contato> getListaContatos() {
        return listaContatos;
    }

    public void setListaContatos(List<Contato> listaContatos) {
        this.listaContatos = listaContatos;
    }
}
