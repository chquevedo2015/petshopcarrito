package com.example.administrador.petshopcarrito;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class NavegadorDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtUsuario;
    TextView txtCorreoUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegador_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        Bundle datos = this.getIntent().getExtras();
        String recu_correo = datos.getString("variablecorreo");
        String recu_nombe = datos.getString("variablenombre");
        String recu_ape = datos.getString("variableapellido");
        String recu_cod = datos.getString("variablecodigo");
        String recu_nomCompleto = "Bienvenido: " + recu_nombe + " " + recu_ape;
        ((TextView) header.findViewById(R.id.txtCorreoUsu)).setText(recu_correo);
        ((TextView) header.findViewById(R.id.txtUsuario)).setText(recu_nomCompleto);
        ((TextView) header.findViewById(R.id.txtCodigo)).setText(recu_cod);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navegador_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_listado) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new ListadoProducto()).commit();
            // Handle the camera action
        }else if (id == R.id.nav_ubicacion) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new DetallePedido()).commit();
        } else if (id == R.id.nav_cerrar) {
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
