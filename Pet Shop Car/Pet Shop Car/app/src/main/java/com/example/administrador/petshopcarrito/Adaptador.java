package com.example.administrador.petshopcarrito;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Administrador on 19/03/2018.
 */


public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder>
{

    private ArrayList<elemento> elementos;
    private Context context;
    public  Adaptador (ArrayList<elemento> elementos, Context context)
    {
        this.elementos =  elementos;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        elemento lista= elementos.get(position);
        holder.TxtDes.setText(lista.getDescripcion());
        holder.TxtDetalle.setText(lista.getDetalle());
        Picasso.with(context).load("http://www.webseromar.somee.com/Imagenes/" + lista.getImg()).resize(400,400).into(holder.img);

    }

    public static   interface OnItemClickListener{
        public void  onItemClick(View view, int position);
    }

    private static OnItemClickListener onItemClickListener;
    public  static OnItemClickListener getOnItemClickListener(){
        return  onItemClickListener;
    }

    public static  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        Adaptador.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView TxtDes, TxtDetalle;
        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            TxtDes = (TextView) itemView.findViewById(R.id.txtDesPro);
            TxtDetalle = (TextView) itemView.findViewById(R.id.txtDetalle);

            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view)
                {
                    int position = ViewHolder.super.getAdapterPosition();
                    onItemClickListener.onItemClick(view, position);
                }
             });

        }
    }
}
