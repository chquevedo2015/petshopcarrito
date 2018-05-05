package com.example.administrador.petshopcarrito;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;


public class CestaProducto extends Fragment {

    ArrayList<elemento> cesta = new ArrayList<>();
    SharedPreferences carrito;
    Gson gson = new Gson();
    Button btnPagar;
    double total=0;
    LinearLayout layout,  contenedor;

    PayPalConfiguration m_configuracion;
    String m_PayplaClientId = "ARzoL6928Tq1jd5PDRASVlrBkQyFqCyaN6izLbjsh-5ne0lB1t3awPTA_Gp01YXhtbyK77uWbTL2FJsj";

    Intent m_service;
    int m_paypalRequestCode=999;
    String DetalleIns;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carrito= getActivity().getSharedPreferences("carrito", getActivity().MODE_PRIVATE);
        String guardado = carrito.getString("cesta","");
        Type type= new TypeToken<ArrayList<elemento>>(){}.getType();
        cesta = gson.fromJson(guardado,type);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==m_paypalRequestCode){
            if(resultCode == Activity.RESULT_OK){
                PaymentConfirmation confirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null) {
                    String state = confirmation.getProofOfPayment().getState();
                    if (state.equals("approved")) {
                        //Toast.makeText( getActivity().getApplicationContext(), "Pago aprobado", Toast.LENGTH_SHORT).show();

                        DetalleIns = "";
                        for(int i=0; i< cesta.size();i++)
                        {
                            DetalleIns = DetalleIns + cesta.get(i).getCod().toString() + '|' + cesta.get(i).getCan() + ',';
                        }

                        Thread tr= new Thread(){
                            @Override
                            public void run() {

                                String NAMESPACE = "http://tempuri.org/";
                                String URl = "http://webseromar.somee.com/WebServicePetShop.asmx?WSDL";
                                String METHOD_NAME = "InsertarPedido";
                                String SOAP_ACTION = "http://tempuri.org/InsertarPedido";


                                Bundle datos = getActivity().getIntent().getExtras();
                                Integer  recu_codigo = datos.getInt("variablecodigo");

                                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                                request.addProperty("CodUsuario",recu_codigo);
                                request.addProperty("Monto", new BigDecimal(total).toString());
                                request.addProperty("Detalle",DetalleIns.toString());


                                SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                                envelop.dotNet = true;
                                envelop.setOutputSoapObject(request);
                                HttpTransportSE trasporte = new HttpTransportSE(URl);

                                try {
                                    trasporte.call(SOAP_ACTION,envelop);
                                    if ( envelop.bodyIn instanceof SoapFault) {
                                        String str= ((SoapFault)  envelop.bodyIn).faultstring;
                                        Log.i("", str);

                                    } else {
                                        SoapObject soap =(SoapObject)envelop.bodyIn;
                                        Log.d("WS", String.valueOf(soap));
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText( getActivity(),"Pedido Registrado ok", Toast.LENGTH_LONG).show();

                                            //limpiamos lista
                                            cesta.clear();
                                            //grabamos la preferencia
                                            String jsonList = gson.toJson(cesta);
                                            carrito = getActivity().getSharedPreferences("carrito", getActivity().MODE_PRIVATE);
                                            SharedPreferences.Editor editor = carrito.edit();
                                            editor.putString("cesta", jsonList);
                                            //comint es guardar datos y cerrar preferncias
                                            editor.commit();
                                            cuerpocesta();
                                        }
                                    });
                                }
                                catch (Exception e) {
                                    String mensaje="";
                                    mensaje = e.getMessage().toString();
                                }

                            }
                        };
                        tr.start();


                    } else
                        Toast.makeText(getActivity(), "error en el pago", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(), "Confirmacion Vacia", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cesta_producto, container, false);
        layout = (LinearLayout)v.findViewById(R.id.llCesta);
        contenedor = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        contenedor.setOrientation(LinearLayout.VERTICAL);
        contenedor.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cuerpocesta();

        m_configuracion =new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(m_PayplaClientId);
        Intent m_service = new Intent(getActivity(), PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuracion);
        getActivity().startService(m_service);

        btnPagar =(Button)v.findViewById(R.id.btnPagar);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (total >0)
                {
                    PayPalPayment payment=new PayPalPayment(new BigDecimal(total),"USD","Carrito de Compras",PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent=new Intent(getActivity(),PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuracion);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
                    startActivityForResult(intent,m_paypalRequestCode);
                }

                else
                {
                    Toast.makeText(getActivity(),"No existe detalle para registrar", Toast.LENGTH_SHORT).show();
                }

            }

        });

        return  v;
    }


    private  void cuerpocesta()
    {
        //LinearLayout linearLayout = null;
        layout.removeAllViews();
        contenedor.removeAllViews();

        total=0;

        for(int j=0; j< cesta.size();j ++)
        {
            elemento e = cesta.get(j);
            LinearLayout contenSecundario = new LinearLayout(getActivity());
            contenSecundario.setOrientation(LinearLayout.HORIZONTAL);

            TextView txtProd  = new TextView(getActivity().getApplicationContext());
            txtProd.setLayoutParams(new LinearLayout.LayoutParams(450 , ViewGroup.LayoutParams.WRAP_CONTENT));
            txtProd.setText(e.getDescripcion());
            txtProd.setTextColor(Color.parseColor("#000000"));
            txtProd.setId(j + 10);

            TextView  txtPre  = new TextView(getActivity().getApplicationContext());
            txtPre.setLayoutParams(new LinearLayout.LayoutParams(200 , ViewGroup.LayoutParams.WRAP_CONTENT));
            txtPre.setText(e.getPrecio() + "" );
            txtPre.setTextColor(Color.parseColor("#000000"));
            txtPre.setId(j + 11);

            TextView  txtCan  = new TextView(getActivity().getApplicationContext());
            txtCan.setLayoutParams(new LinearLayout.LayoutParams(200 , ViewGroup.LayoutParams.WRAP_CONTENT));
            txtCan.setText(e.getCan() + "" );
            txtCan.setTextColor(Color.parseColor("#000000"));
            txtCan.setId(j + 12);

            TextView  txtMonto  = new TextView(getActivity().getApplicationContext());
            txtMonto.setLayoutParams(new LinearLayout.LayoutParams(150 , ViewGroup.LayoutParams.WRAP_CONTENT));
            double montoCompra =e.getPrecio() * e.getCan();
            txtMonto.setText(montoCompra + "" );
            txtMonto.setTextColor(Color.parseColor("#000000"));
            txtMonto.setId(j + 13);

            final Button btnRestar = new Button(getActivity().getApplicationContext());
            btnRestar.setText("-");
            btnRestar.setLayoutParams(new LinearLayout.LayoutParams(120,ViewGroup.LayoutParams.WRAP_CONTENT));

            final String cod = cesta.get(j).getCod();
            btnRestar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0; i< cesta.size();i++)
                    {
                        if(cesta.get(i).getCod().equalsIgnoreCase(cod)){
                            cesta.get(i).setCan(cesta.get(i).getCan() - 1);
                            if(cesta.get(i).getCan()< i){
                                cesta.remove(i);
                            }
                        }
                    }
                    String jsonList = gson.toJson(cesta);
                    carrito = getActivity().getSharedPreferences("carrito", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor= carrito.edit();
                    editor.putString("cesta",jsonList);
                    editor.commit();
                    cuerpocesta();
                }
            });
            contenSecundario.addView(txtProd);
            contenSecundario.addView(txtPre);
            contenSecundario.addView(txtCan);
            contenSecundario.addView(txtMonto);
            contenSecundario.addView(btnRestar);
            contenedor.addView(contenSecundario);
            //contenedor.addView(boton);
            total += montoCompra;
        }

        final TextView txtTextoTotal = new TextView(getActivity().getApplicationContext());
        txtTextoTotal.setText("Total a pagar: ");
        txtTextoTotal.setTextColor(Color.parseColor("#000000"));
        txtTextoTotal.setLayoutParams(new LinearLayout.LayoutParams(620,ViewGroup.LayoutParams.WRAP_CONTENT));
        txtTextoTotal.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        final TextView txtTotal = new TextView(getActivity().getApplicationContext());
        txtTotal.setText(total + "");
        txtTotal.setLayoutParams(new LinearLayout.LayoutParams(200,ViewGroup.LayoutParams.WRAP_CONTENT));
        txtTotal.setTextColor(Color.parseColor("#000000"));
        txtTotal.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        LinearLayout contenSecundario2 = new LinearLayout(getActivity());
        contenSecundario2.setOrientation(LinearLayout.HORIZONTAL);
        contenSecundario2.addView(txtTextoTotal);
        contenSecundario2.addView(txtTotal);

        contenedor.addView(contenSecundario2);
        layout.addView(contenedor);
    }




}
