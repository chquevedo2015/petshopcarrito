package com.example.administrador.petshopcarrito;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    EditText txtValus , txtValPas;
    TextView txtRegistrarse;
    Button btnIngresar;
    Button btnValidarUsuario;
    double  total= 0;
    ArrayList<Usuario> ListaUsu;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        txtValus =(EditText) findViewById(R.id.txtUsu);
        txtValPas =(EditText) findViewById(R.id.txtPass);

        txtRegistrarse =(TextView)findViewById(R.id.txtRegistrate);
        txtRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Registro.class);
                startActivity(i);
            }
        });


        btnValidarUsuario =(Button)findViewById(R.id.btnIngresar);

        btnValidarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Thread tr= new Thread(){
                    @Override
                    public void run() {
                        String NAMESPACE = "http://tempuri.org/";
                        String URL= "http://webseromar.somee.com/WebServicePetShop.asmx?WSDL";
                        String METHOD_NAME = "validaUsuario";
                        String SOAP_ACTION= "http://tempuri.org/validaUsuario";

                        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                        request.addProperty("correo",txtValus.getText().toString());
                        request.addProperty("pas", txtValPas.getText().toString());

                        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelop.dotNet = true  ;
                        envelop.setOutputSoapObject(request);
                        HttpTransportSE trasporte = new HttpTransportSE(URL);
                        try{
                            trasporte.call(SOAP_ACTION,envelop);
                            final SoapObject soap= (SoapObject)envelop.getResponse();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ValidaUsuario(soap);
                                }
                            });

                        }catch (Exception e){
                            e.getMessage();

                        }

                    }
                };
                tr.start();
            }
        });

    }

    public  void ValidaUsuario(SoapObject soap)
    {
        ListaUsu = new ArrayList<Usuario>();
        for(int i = 0; i < soap.getPropertyCount(); i ++)
        {
            SoapObject reg = (SoapObject) soap.getProperty(i);
            Usuario ul =  new Usuario(Integer.parseInt(reg.getProperty(0).toString()),
                    reg.getProperty(1).toString(), reg.getProperty(2).toString(), reg.getProperty(3).toString(), reg.getProperty(4).toString(),
                    reg.getProperty(5).toString());
            ListaUsu.add(ul);
        }

        if(ListaUsu.size()> 0){

            String mesg = String.format("Usuario validado ok", null);
            Toast.makeText(getApplicationContext(),mesg , Toast.LENGTH_SHORT).show();
            Intent i= new Intent(getApplicationContext(),NavegadorDrawer.class);

            i.putExtra("variablecorreo",ListaUsu.get(0).getCor().toString());
            i.putExtra("variablenombre",ListaUsu.get(0).getNom().toString());
            i.putExtra("variableapellido",ListaUsu.get(0).getApe().toString());
            i.putExtra("variablecodigo",ListaUsu.get(0).getCod());

            startActivity(i);

        }
        else {
            Toast.makeText(getApplicationContext(),"Usuario o Password Incorrectos", Toast.LENGTH_SHORT).show();
        }


    }



}
