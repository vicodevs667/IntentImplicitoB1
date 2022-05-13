package com.example.intentimplicitob1;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    //Atributos que representas sus vistas
    private EditText etTelefono;
    private ImageButton btnLlamar, btnCamara;

    private String numeroTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarVistas();
        btnLlamar.setOnClickListener(view -> {
            obtenerInformacion();
            activarServicioLlamada();
        });
    }

    private void activarServicioLlamada() {
        if (!numeroTelefono.isEmpty()) {
            //Evaluan si su version de android es mayor o igual
            // a la version donde el servicio de llamada cambia
            //su forma de trabajar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //para versiones nuevas
            } else {
                //para versiones antiguas
                configurarVersionAntigua();
            }
        }
    }

    private void configurarVersionAntigua() {
        //Crear un Intent Implicito
        //En el constructor configuran la accion que quieren
        //que se realice
        //Un segundo parametro es una URI que es algo parecido
        //a una URL donde configuras tus parametros que envias.
        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numeroTelefono));
        if (revisarPermisos(Manifest.permission.CALL_PHONE)) {
            startActivity(intentCall);
        }
    }

    private void obtenerInformacion() {
        numeroTelefono = etTelefono.getText().toString();
    }

    private void inicializarVistas() {
        etTelefono = findViewById(R.id.etTelefono);
        btnCamara = findViewById(R.id.btnCamara);
        btnLlamar = findViewById(R.id.btnLlamar);
    }

    private boolean revisarPermisos(String permiso) {
        //Valor entero que representa el permiso requerido en nuestra aplicacion
        int valorPermiso = this.checkCallingOrSelfPermission(permiso);
        return valorPermiso == PackageManager.PERMISSION_GRANTED;
    }
}
































