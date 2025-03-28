package com.example.easydepotmovil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ficheros.excepciones.LogicaException;
import ficheros.excepciones.PersistenciaException;
import ficheros.logica.GestorComprobaciones;
import ficheros.logica.Sistema;
import ficheros.modelo.Local;
import ficheros.modelo.Reserva;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IncidenciaActivity extends AppCompatActivity {

    private TextView txtDireccion,txtCabina,txtFecha;
    private Button btVolver,btAceptar;
    private EditText editTelefono,editIncidencia;
    private Sistema s;
    private Reserva r;
    private Local l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_incidencia);

        try {
            s = Sistema.getInstance();
        } catch (PersistenciaException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (LogicaException e) {
            throw new RuntimeException(e);
        }

        r = s.buscarReserva(getIntent().getIntExtra("reserva",0));

        txtDireccion = findViewById(R.id.txtDireccionIncidencia);
        txtCabina = findViewById(R.id.txtCabinaIncidencia);
        txtFecha = findViewById(R.id.txtFechaIncidencia);

        l = s.buscarLocal(Integer.parseInt(r.getIdCabina().charAt(0)+""));


        // buscamos el local a traves del primer numero de la id de cabina que tiene la reserva
        txtDireccion.setText("Direccion: " + l.getDireccion());

        txtCabina.setText("Cabina: " + r.getIdCabina().charAt(2));

        txtFecha.setText("Fecha: " + r.getFechaInicio());


        btVolver = findViewById(R.id.btnCancelarIncidencia);
        btAceptar = findViewById(R.id.btnAceptarIncidencia);

        editTelefono = findViewById(R.id.editNumero);
        editIncidencia = findViewById(R.id.editIncidencia);

        btVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String telefono = editTelefono.getText().toString();
                    String incidencia = editIncidencia.getText().toString();

                    GestorComprobaciones.comprobarTelefono(telefono);

                    List<String> textos = new ArrayList<String>();
                    textos.add(telefono);
                    textos.add(incidencia);

                    GestorComprobaciones.comprobarTextoVacio(textos);
                    // recogemos el informe hecho
                    String informe = prepararInforme();
                    r.setIncidencia(true);
                    r.setDescripcionIncidencia(informe);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            s.actualizarReserva(r);
                        }
                    }).start();
                    Toast.makeText(IncidenciaActivity.this, "Incidencia enviada"
                            , Toast.LENGTH_SHORT).show();
                    finish();
                } catch (LogicaException e) {
                    Toast.makeText(IncidenciaActivity.this, e.getMessage()
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /**
     * Formatea una cabecera para el informe guardndo el dia mes y anio ademas de la hora, devuelve el informe con
     * formato de String
     * @return String
     */
    private String prepararInforme() {

        return "\n--------------------------------------------------------------------------------------\n"
                + "Informe de incidencia en local\r\n"
                + "\r\n"
                + "Fecha:" + new Timestamp(System.currentTimeMillis()) + " \r\n"
                + "Local: "+ l.getLocalId() + " \r\n"
                + "Ubicación: " +  l.getDireccion() +" \r\n"
                + "Telefono: " + editTelefono.getText().toString() + "\r\n"
                + "Descripción de resolucion de incidencia:\n"
                + editIncidencia.getText().toString();
    }

}