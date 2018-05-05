package com.example.administrador.petshopcarrito;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by Administrador on 01/04/2018.
 */

public class ListadoProducto extends Fragment {
    @Nullable

    RecyclerView recycler ;
    RecyclerView.Adapter adap;
    ArrayList<elemento> listaCard;
    TextView txtprueba;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_listado, container ,false);

        super.onCreate(savedInstanceState);
        recycler =(RecyclerView)v.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        Thread tr= new Thread(){
            @Override
            public void run() {
                String NAMESPACE = "http://tempuri.org/";
                String URL= "http://webseromar.somee.com/WebServicePetShop.asmx?WSDL";
                String METHOD_NAME = "listarProductos";
                String SOAP_ACTION= "http://tempuri.org/listarProductos";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelop.dotNet = true  ;
                envelop.setOutputSoapObject(request);
                HttpTransportSE trasporte = new HttpTransportSE(URL);
                try{
                    trasporte.call(SOAP_ACTION,envelop);
                    final SoapObject soap= (SoapObject)envelop.getResponse();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listarProductos(soap);
                        }
                    });

                }catch (Exception e){
                }
            }
        };
        tr.start();

        ((Adaptador)adap).setOnItemClickListener(new Adaptador.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                elemento item = listaCard.get(position);


                Fragment fragment = new DetalleProducto();
                Bundle args = new Bundle();
                args.putString("cod",item.getCod());
                args.putString("des",item.getDescripcion());
                args.putString("det",item.getDetalle());
                args.putString("pre",item.getPrecio() + "");
                args.putString("img",item.getImg());
                fragment.setArguments(args);

                FragmentManager FM = getActivity().getSupportFragmentManager();
                FragmentTransaction FT =  FM.beginTransaction();
                FT.replace(R.id.contenedor,fragment);
                FT.addToBackStack(null);
                FT.commit();
            }
        });

        recycler =(RecyclerView)v.findViewById(R.id.recycler);
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(ll);
        return  v;

    }

    public void listarProductos(SoapObject soap)
    {
        listaCard = new ArrayList<elemento>();
        for(int i = 0; i< soap.getPropertyCount(); i++)
        {
            SoapObject reg = (SoapObject)soap.getProperty(i);
            elemento el = new elemento(reg.getProperty(0).toString(),reg.getProperty(1).toString(),
                    reg.getProperty(2).toString(), Double.parseDouble(reg.getProperty(4).toString()),
                    reg.getProperty(5).toString());
            listaCard.add(el);
        }
        adap = new Adaptador(listaCard, getContext());
        recycler.setAdapter(adap);
    }


}
