package com.example.administrador.petshopcarrito;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class DetallePedido extends Fragment {

    RecyclerView recyclerPedido;
    RecyclerView.Adapter adapPedido;
    ArrayList<Pedido> listaCardPedido;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_detalle_pedido, container ,false);

        super.onCreate(savedInstanceState);
        recyclerPedido =(RecyclerView)v.findViewById(R.id.recyclerPedido);
        recyclerPedido.setHasFixedSize(true);
        recyclerPedido.setLayoutManager(new LinearLayoutManager(getContext()));
        Thread tr= new Thread(){
            @Override
            public void run() {

                Bundle datos = getActivity().getIntent().getExtras();
                Integer  recu_codigo = datos.getInt("variablecodigo");

                String NAMESPACE = "http://tempuri.org/";
                String URL= "http://webseromar.somee.com/WebServicePetShop.asmx?WSDL";
                String METHOD_NAME = "ListadoPedido";
                String SOAP_ACTION= "http://tempuri.org/ListadoPedido";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("CodUsuario",recu_codigo);
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
                           listarPedidos(soap);
                        }
                    });

                }catch (Exception e){
                    String mensaje="";
                    mensaje = e.getMessage().toString();
                }
            }
        };
        tr.start();

        recyclerPedido =(RecyclerView)v.findViewById(R.id.recyclerPedido);
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerPedido.setLayoutManager(ll);
        return  v;

    }

    public void listarPedidos(SoapObject soap)
    {
        listaCardPedido = new ArrayList<Pedido>();
        for(int i = 0; i< soap.getPropertyCount(); i++)
        {
            SoapObject reg = (SoapObject)soap.getProperty(i);
             Pedido ped = new Pedido(reg.getProperty(4).toString(), Integer.parseInt(reg.getProperty(1).toString()),
                     Double.parseDouble(reg.getProperty(5).toString()) );
            listaCardPedido.add(ped);
        }
        adapPedido = new AdaptadorPedido(listaCardPedido, getContext());
        recyclerPedido.setAdapter(adapPedido);


    }







}
