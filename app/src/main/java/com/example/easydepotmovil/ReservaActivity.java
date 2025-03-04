package com.example.easydepotmovil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import ficheros.modelo.Local;
import ficheros.modelo.Cliente;

public class ReservaActivity extends AppCompatActivity {

    private Local l;
    private Cliente c;
    private Sistema s;
    private ImageView imagenVolverReserva;
    private Button botonReservar;
    private TextView tituloLocal;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reserva);

        try {
            s = Sistema.getInstance();
            l = s.getLocalSeleccionado();
            c = s.getClienteLogeado();
        } catch (PersistenciaException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (LogicaException e) {
            throw new RuntimeException(e);
        }

        imagenVolverReserva = (ImageView) findViewById(R.id.imagenVolverReserva);
        botonReservar = (Button) findViewById(R.id.botonReservar);
        tituloLocal = (TextView) findViewById(R.id.txtTituloLocal);

        tituloLocal.setText(tituloLocal.getText().toString() + " " + l.getDireccion());

        imagenVolverReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReservaActivity.this, "Boton reserva pulsado",
                        Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}