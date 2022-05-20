package com.example.intentimplicitob1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Atributos que representas sus vistas
    private EditText etTelefono;
    private ImageButton btnLlamar, btnCamara;
    private ImageView ivImagen;

    private String numeroTelefono;
    //Codigo Constante para servicio de llamada
    private final int PHONE_CODE = 100;
    //Codigo Constante para activar camara
    private final int CAMERA_CODE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarVistas();
        btnLlamar.setOnClickListener(view -> {
            obtenerInformacion();
            activarServicioLlamada();
        });

        btnCamara.setOnClickListener(view -> {
            activarServicioCamara();
        });
    }

    private void activarServicioCamara() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CAMERA_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    Bitmap foto = (Bitmap) data.getExtras().get("data");
                    ivImagen.setImageBitmap(foto);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    private void activarServicioLlamada() {
        if (!numeroTelefono.isEmpty()) {
            //Evaluan si su version de android es mayor o igual
            // a la version donde el servicio de llamada cambia
            //su forma de trabajar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //para versiones nuevas
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},PHONE_CODE);
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
        ivImagen = findViewById(R.id.ivImagen);
    }

    //Evaluan permisos para versiones antiguas
    private boolean revisarPermisos(String permiso) {
        //Valor entero que representa el permiso requerido en nuestra aplicacion
        int valorPermiso = this.checkCallingOrSelfPermission(permiso);
        return valorPermiso == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Verificar el servicio a evaluar
        switch (requestCode) {
            case PHONE_CODE:
                String permiso = permissions[0];
                int permisoOtorgado = grantResults[0];
                //asegurarse que para llamadas van a evaluar el permiso del servicio de Llamada
                if (permiso.equals(Manifest.permission.CALL_PHONE)) {
                    //Evaluar si el permiso ha sido otorgado o denegado
                    if (permisoOtorgado == PackageManager.PERMISSION_GRANTED) {
                        Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ numeroTelefono));
                        startActivity(intentLlamada);
                    } else {
                        Toast.makeText(this, "El permiso esta denegado", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case CAMERA_CODE:
                int valor = grantResults[0];
                if (valor == PackageManager.PERMISSION_GRANTED) {
                    Intent intentCamara = new Intent( "android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intentCamara, CAMERA_CODE);
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}
































