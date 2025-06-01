package com.example.easydepotmovil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.SQLException;

import ficheros.excepciones.LogicaException;
import ficheros.excepciones.PersistenciaException;
import ficheros.logica.Sistema;

public class activity_vending_machine_loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hilo para actualizar la base de datos
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Cerramos la pantalla de carga
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                cierre();

            }
        }).start();


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vending_machine_loading);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Metodo para cerrar la actividad una vez realizadas todas las reservas, devuelve un toast
     * indicandole al usuario que se han realizado todas las reservas adecuadamente
     */
    private void cierre(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity_vending_machine_loading.this,
                        "Compra realizada con exito", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}