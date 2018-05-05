package com.example.administrador.petshopcarrito;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Administrador on 01/04/2018.
 */

public class DetalleProducto extends Fragment {

    ImageView imgDetalle;
    TextView txtDetalleDes, txtDetalleDet, txtDetallePrecio;
    Button btnVer, btnAgregar;

    String cod = "", des = "", det = "", img = "";
    double pre = 0;
    ArrayList<elemento> cesta = new ArrayList<>();
    SharedPreferences carrito;
    Gson gson = new Gson();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle mBundle = getArguments();
        if(mBundle != null) {
           // String recuperada = mBundle.getString("str");
            cod= mBundle.getString("cod");
            des= mBundle.getString("des");
            det= mBundle.getString("det");
            pre= Double.parseDouble(mBundle.getString("pre"));
            img= mBundle.getString("img");
        }

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detalle, container, false);

        imgDetalle =(ImageView)v.findViewById(R.id.imgDetalle);
        txtDetalleDes= (TextView)v.findViewById(R.id.txtDetalleDescripcion);
        txtDetalleDet= (TextView)v.findViewById(R.id.txtDetalleDet);
        txtDetallePrecio = (TextView)v.findViewById(R.id.txtDetallePrecio);
        btnAgregar = (Button)v.findViewById(R.id.btnAgregar);
        btnVer = (Button)v.findViewById(R.id.btnVerCesta);

        txtDetalleDes.setText(des);
        txtDetalleDet.setText(det);
        txtDetallePrecio.setText("S/." + pre+"");
        Picasso.with(getActivity().getApplicationContext()).load("http://www.webseromar.somee.com/Imagenes/" + img).resize(600,600).into(imgDetalle);


        //recuperando datos
        File f = new File("/data/data/"+getActivity().getPackageName() + "/shared_prefs/carrito.xml");
        carrito =this.getActivity().getSharedPreferences("carrito", getActivity().MODE_PRIVATE);
        if (f.exists()){
            String guardado = carrito.getString("cesta","");
            Type type= new TypeToken<ArrayList<elemento>>(){}.getType();
            cesta = gson.fromJson(guardado,type);

        }


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elemento e = new elemento(cod,des,pre,1);
                if(agregarProducto(e)==true)
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Se aumento la cantidad", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getActivity().getApplicationContext(),"Se agrego nuevo producto", Toast.LENGTH_SHORT).show();
                }
                String listaGson = gson.toJson(cesta);
                carrito = getActivity().getSharedPreferences("carrito", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor= carrito.edit();
                editor.putString("cesta", listaGson);
                editor.commit();
            }
        });


        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File f = new File("/data/data/"+ getActivity().getPackageName()+ "/shared_prefs/carrito.xml");
                if (f.exists()){

                    Fragment fragment = new CestaProducto();
                    FragmentManager FM = getActivity().getSupportFragmentManager();
                    FragmentTransaction FT =  FM.beginTransaction();
                    FT.replace(R.id.contenedor,fragment);
                    FT.addToBackStack(null);
                    FT.commit();

                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"cesta vacia", Toast.LENGTH_SHORT).show();
                }
            }
        });



        super.onCreate(savedInstanceState);
        return  v;
    }



    private boolean agregarProducto(elemento e)
    {
        for(int i =0 ; i< cesta.size(); i++)
        {
            if(cesta.get(i).getCod().equals(e.getCod())){
                cesta.get(i).setCan(cesta.get(i).getCan() + 1);
                return true ;
            }
        }
        cesta.add(e);
        return  false;
    }


}

