package com.example.easydepotmovil;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ficheros.excepciones.LogicaException;
import ficheros.excepciones.PersistenciaException;
import ficheros.logica.GestorComprobaciones;
import ficheros.logica.Sistema;

public class RegistroActivity extends AppCompatActivity {

    private Button botonVolver;
    private Button botonAceptar;
    private EditText editEmail;
    private EditText editNombre;
    private EditText editApellidos;
    private EditText editNewPassword;
    private Sistema s;

    /**
     * Pantalla para introducir los datos de registro  y registrar un nuevo usuario
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        // TODO comprobar por que si cargas el mail en una caja de texto sale en todas
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    s = Sistema.getInstance();
                } catch (PersistenciaException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (LogicaException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        botonVolver = (Button) findViewById(R.id.btnVolver);
        botonAceptar = (Button) findViewById(R.id.btnAceptar);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editNombre = (EditText) findViewById(R.id.editNombre);
        editApellidos = (EditText) findViewById(R.id.editApellidos);
        editNewPassword = (EditText) findViewById(R.id.editNewPassword);

        // Boton de volver a la pantalla anterior
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String nombre = editNombre.getText().toString();
                String apellidos = editApellidos.getText().toString();
                String password = editNewPassword.getText().toString();
                Intent goToken = new Intent(RegistroActivity.this,TokenRegistroActivity.class);

                List <String>  datos = new <String>ArrayList();

                datos.add(email);
                datos.add(nombre);
                datos.add(apellidos);
                datos.add(password);

                try {
                    // Realizamos las comprobaciones pertinentes
                   GestorComprobaciones.comprobarTextoVacio(datos);
                   GestorComprobaciones.comprobarEmail(email);
                    // si ya esta registrado el cliente lanzara excepcion
                    s.buscarCliente(email);
                    // Le pasamos los datos pertinentes al intent
                    goToken.putExtra("email",email);
                    goToken.putExtra("nombre",nombre);
                    goToken.putExtra("apellidos",apellidos);
                    goToken.putExtra("password",password);
                    startActivityForResult(goToken,1);
                } catch (LogicaException e) {
                    Toast.makeText(RegistroActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Codigo muerto, esta reservado para una pequeña cosa que quiero hacer a futuro >:3c
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) finish();
    }
}