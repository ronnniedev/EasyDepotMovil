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

public class LoadingReserva extends AppCompatActivity {

    private Sistema s;

    /**
     * Pantalla de carga que espera a que la BDDS se actualize con la reserva o reservas pertinentes
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hilo para actualizar la base de datos
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    s = Sistema.getInstance();
                    Intent intent = getIntent();
                    // Recogemos el numero de reservas
                    int numSmall = intent.getIntExtra("Pequeñas",0);
                    int numMedium = intent.getIntExtra("Medianas",0);
                    int numBig = intent.getIntExtra("Grandes",0);
                    // Realizamos el numero de reservas
                    realizarReservas(numSmall,"Pequeña");
                    realizarReservas(numMedium,"Mediana");
                    realizarReservas(numBig,"Grande");
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

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_reserva);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Metodo para realizar las reservas, va recorriendo el numero de reservas a realizar
     * @param num : int
     * @param tipo : String
     * @throws LogicaException
     */
    private void realizarReservas(int num, String tipo) throws LogicaException {
        for(int i = 0; i < num;i++){
            s.addReserva(s.getClienteLogeado().getEmail(),s.getLocalSeleccionado().getLocalId(),tipo);
        }
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
                Toast.makeText(LoadingReserva.this, "Reservas realizadas", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}