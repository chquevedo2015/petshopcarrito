package com.example.administrador.petshopcarrito;

/**
 * Created by Administrador on 22/04/2018.
 */

public class Pedido {
    private  String FechaPedido;
    private  int  IdPedido;
    private  double MontoPedido;

    public Pedido(String fechaPedido, int idPedido, Double montoPedido, int idUsuario) {
        FechaPedido = fechaPedido;
        IdPedido = idPedido;
        MontoPedido = montoPedido;
        IdUsuario = idUsuario;
    }

    private int IdUsuario;

    public Pedido(String fechaPedido, int idPedido, Double montoPedido) {
        FechaPedido = fechaPedido;
        IdPedido = idPedido;
        MontoPedido = montoPedido;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "FechaPedido='" + FechaPedido + '\'' +
                ", IdPedido=" + IdPedido +
                ", MontoPedido=" + MontoPedido +
                ", IdUsuario=" + IdUsuario +
                '}';
    }

    public String getFechaPedido() {
        return FechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        FechaPedido = fechaPedido;
    }

    public int getIdPedido() {
        return IdPedido;
    }

    public void setIdPedido(int idPedido) {
        IdPedido = idPedido;
    }

    public Double getMontoPedido() {
        return MontoPedido;
    }

    public void setMontoPedido(Double montoPedido) {
        MontoPedido = montoPedido;
    }
}
