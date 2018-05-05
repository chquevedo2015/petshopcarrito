package com.example.administrador.petshopcarrito;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    EditText txtNom, txtCor, txtApe , txtCelu, txtPass;
    Button btnRegistrarUsu;
    TextInputLayout impNom, impPassword, impApe, impCelu, impCorreo;
    boolean cor=false, pass=false, celu=false, nomusu = false , ape=false;

    Boolean validaString(String texto) {
        return  texto != null && texto.trim().length()>0;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNom= (EditText)findViewById(R.id.txtNom);
        txtCor= (EditText)findViewById(R.id.txtCorreo);
        txtPass= (EditText)findViewById(R.id.txtPass);
        txtApe=(EditText)findViewById(R.id.txtApellido);
        txtCelu=(EditText)findViewById(R.id.txtcelular);

        impNom=(TextInputLayout)findViewById(R.id.impnom) ;
        impApe=(TextInputLayout)findViewById(R.id.impapellido) ;
        impPassword=(TextInputLayout)findViewById(R.id.imppassword) ;
        impCorreo=(TextInputLayout)findViewById(R.id.impcorreo) ;
        impCelu=(TextInputLayout)findViewById(R.id.impcelular) ;


        btnRegistrarUsu =(Button) findViewById(R.id.btnRegistrarUsu);

        btnRegistrarUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Patterns.EMAIL_ADDRESS.matcher(txtCor.getText().toString()).matches()== false) {
                    impCorreo.setError("Correo Invalido");
                    cor=false;
                }else
                {
                    cor=true;
                    impCorreo.setError(null);
                }
                Pattern p= Pattern.compile("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
                if(p.matcher(txtCelu.getText().toString()).matches()==false){
                    impCelu.setError("Celular Invalido, debe digitar 9 digitos");
                    celu=false;
                }else
                {
                    celu=true;
                    impCelu.setError(null);

                }

                if(validaString(txtNom.getText().toString())){
                    nomusu=true;
                    impNom.setError(null);
                }
                else{
                    nomusu=false;
                    impNom.setError("Nombre Invalido");
                }

                if(validaString(txtApe.getText().toString())){
                    ape=true;
                    impApe.setError(null);
                }
                else{
                    ape=false;
                    impApe.setError("Apellido Invalido");
                }

                if(validaString(txtPass.getText().toString())){
                    pass=true;
                    impPassword.setError(null);
                }
                else{
                    pass=false;
                    impPassword.setError("Password Invalido");
                }

                 if(cor==true &&  pass==true &&  celu==true &&  nomusu==true  && ape==true){


                     Thread tr= new Thread(){
                         @Override
                         public void run() {

                             String NAMESPACE = "http://tempuri.org/";
                             String URl = "http://webseromar.somee.com/WebServicePetShop.asmx?WSDL";
                             String METHOD_NAME = "InsertarUsuario";
                             String SOAP_ACTION = "http://tempuri.org/InsertarUsuario";

                             SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                             request.addProperty("nom",txtNom.getText().toString());
                             request.addProperty("ape",txtApe.getText().toString());
                             request.addProperty("celular",txtCelu.getText().toString());
                             request.addProperty("correo",txtCor.getText().toString());
                             request.addProperty("pas",txtPass.getText().toString());


                             SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                             envelop.dotNet = true;
                             envelop.setOutputSoapObject(request);
                             HttpTransportSE trasporte = new HttpTransportSE(URl);

                             try {
                                 trasporte.call(SOAP_ACTION,envelop);
                                 final SoapObject  soap= (SoapObject) envelop.getResponse();
                                 //metodo para acceder a la vista desde el hilo
                                 runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         Toast.makeText(getApplicationContext(),"Usuario Registrado", Toast.LENGTH_LONG).show();
                                         Intent i = new Intent(Registro.this, Login.class);
                                         startActivity(i);

                                     }
                                 });
                             }
                             catch (Exception e) {
                                 e.getMessage();

                             }
                         }
                     };
                     tr.start();

                }
            }

        });

    }
}
