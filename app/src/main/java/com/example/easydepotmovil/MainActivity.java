package com.example.easydepotmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    private Button botonLogin;
    private Button botonRegistrar;
    private Sistema s;
    private EditText editUsuario;
    private EditText editPassword;

    /**
     * Pantalla login
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        botonLogin = (Button) findViewById(R.id.btnLogin);
        botonRegistrar = (Button) findViewById(R.id.btnRegistrar);
        editUsuario = (EditText) findViewById(R.id.editUsuario);
        editPassword = (EditText) findViewById(R.id.editPassword);

        try {
            s = Sistema.getInstance();
        } catch (PersistenciaException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (LogicaException e) {
            throw new RuntimeException(e);
        }

        // Nos mueve a la siguiente pantalla, la de registro en este caso
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moverse = new Intent(MainActivity.this,RegistroActivity.class);
                startActivity(moverse);
            }
        });

        // Nos lleva al mapa de navegación en caso de que el login sea correcto, en caso contrario
        // devuelve un toas con el error
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    s.loginMovil(editUsuario.getText().toString(),editPassword.getText().toString());
                    Intent logearse = new Intent(MainActivity.this,MapaActivity.class);
                    startActivity(logearse);
                } catch (LogicaException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}