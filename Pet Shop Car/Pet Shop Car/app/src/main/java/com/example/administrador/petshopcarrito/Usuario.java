package com.example.administrador.petshopcarrito;

/**
 * Created by Administrador on 25/03/2018.
 */

public class Usuario {

    private  int cod;
    private String cor;
    private String pas;
    private String ape;
    private  String celular;
    private String nom;

    public Usuario(int cod,String nom, String cor, String pas, String ape, String celular) {
        this.cod = cod;
        this.nom = nom;
        this.cor = cor;
        this.pas = pas;
        this.ape = ape;
        this.celular = celular;

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }





    public String getApe() {
        return ape;
    }

    public void setApe(String ape) {
        this.ape = ape;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }



    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getPas() {
        return pas;
    }

    public void setPas(String pas) {
        this.pas = pas;
    }




}
