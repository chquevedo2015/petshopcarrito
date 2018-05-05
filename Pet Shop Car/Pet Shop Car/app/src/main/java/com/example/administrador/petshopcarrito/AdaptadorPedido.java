package com.example.administrador.petshopcarrito;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrador on 22/04/2018.
 */

public class AdaptadorPedido extends RecyclerView.Adapter<AdaptadorPedido.ViewHolder> {

    private ArrayList<Pedido> pedido;
    private Context context;

    public AdaptadorPedido(ArrayList<Pedido> pedido, Context context) {
        this.pedido = pedido;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedido,parent,false);
        return new AdaptadorPedido.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pedido lista= pedido.get(position);
        holder.TxtFechaPedido.setText("Fecha : " + lista.getFechaPedido());
        holder.TxtCodPedido.setText("Codigo de Pedido: " + lista.getIdPedido() + "");
        holder.TxtMontoPedido.setText("Monto Total: " + lista.getMontoPedido() + "");
    }

    @Override
    public int getItemCount() {
        return   pedido.size();
    }



    public class ViewHolder extends  RecyclerView.ViewHolder {
        TextView TxtCodPedido, TxtFechaPedido, TxtMontoPedido;
        public ViewHolder(View itemView) {
            super(itemView);
            TxtCodPedido = (TextView) itemView.findViewById(R.id.txtCodPedido);
            TxtFechaPedido = (TextView) itemView.findViewById(R.id.txtFecha);
            TxtMontoPedido =(TextView) itemView.findViewById(R.id.txtMontoPedido);

        }
    }
}
