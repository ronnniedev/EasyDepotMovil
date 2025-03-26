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

public class LoadingCierreReserva extends AppCompatActivity {

    private Sistema s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_cierre_reserva);


        // Hilo para actualizar la base de datos
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    s = Sistema.getInstance();
                    Intent intent = getIntent();
                    // Recogemos el numero de reservas
                    int idReserva = intent.getIntExtra("reserva",0);
                    // Realizamos el numero de reservas
                    s.cerrarReserva(idReserva);
                    // Cerramos la pantalla de carga
                    cierre();
                } catch (PersistenciaException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (LogicaException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
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
                Toast.makeText(LoadingCierreReserva.this, "Reserva cerrada",
                        Toast.LENGTH_SHORT).show();
               Intent moverse =
                       new Intent(LoadingCierreReserva.this,ReservasActivity.class);
                startActivity(moverse);
                finish();
            }
        });

    }
}