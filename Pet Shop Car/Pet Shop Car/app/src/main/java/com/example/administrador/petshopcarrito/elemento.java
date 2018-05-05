package com.example.administrador.petshopcarrito;

/**
 * Created by Administrador on 19/03/2018.
 */

public class elemento {

    private  String Cod, Descripcion, Detalle;
    private  double Precio;
    private  String img;
    private  int can;

    public elemento(String cod, String descripcion, String detalle, double precio, String img) {
        Cod = cod;
        Descripcion = descripcion;
        Detalle = detalle;
        Precio = precio;
        this.img = img;
    }

    public elemento(String cod, String descripcion, double precio, int can) {
        Cod = cod;
        Descripcion = descripcion;
        Precio = precio;
        this.can = can;
    }

    public elemento(String cod, String descripcion, String detalle) {
        Cod = cod;
        Descripcion = descripcion;
        Detalle = detalle;
    }

    public elemento(String descripcion, String detalle, double precio, String img) {
        Descripcion = descripcion;
        Detalle = detalle;
        Precio = precio;
        this.img = img;
    }

    public String getCod() {
        return Cod;
    }

    public void setCod(String cod) {
        Cod = cod;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String detalle) {
        Detalle = detalle;
    }

    public double getPrecio() {
        return Precio;
    }

    public int getCan() {
        return can;
    }

    public void setCan(int can) {
        this.can = can;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "elemento{" +
                "Cod='" + Cod + '\'' +
                ", Descripcion='" + Descripcion + '\'' +
                ", Detalle='" + Detalle + '\'' +
                ", Precio=" + Precio +
                ", img='" + img + '\'' +
                '}';
    }
}