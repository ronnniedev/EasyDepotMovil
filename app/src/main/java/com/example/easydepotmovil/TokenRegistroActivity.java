package com.example.easydepotmovil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.SQLException;

import javax.xml.transform.Result;

import ficheros.excepciones.LogicaException;
import ficheros.excepciones.PersistenciaException;
import ficheros.logica.GestorCorreo;

import ficheros.logica.GestorCorreo;
import ficheros.logica.Sistema;
import ficheros.modelo.Cliente;

public class TokenRegistroActivity extends AppCompatActivity {

    private Button botonVerificar;
    private Button botonVolver;
    private EditText editToken;
    private Sistema s;
    private TextView textoToken;

    /**
     * En esta pantalla enviaremos un correo para poder realizar el registro
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_token_registro);

        try {
            s = Sistema.getInstance();
        } catch (PersistenciaException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (LogicaException e) {
            throw new RuntimeException(e);
        }

        GestorCorreo gestorC = new GestorCorreo();
        String email = this.getIntent().getStringExtra("email");
        int token = gestorC.createEmail(email);
        String nombre = this.getIntent().getStringExtra("nombre");
        String apellidos = this.getIntent().getStringExtra("apellidos");
        String password = this.getIntent().getStringExtra("password");

        editToken = (EditText) findViewById(R.id.editToken);
        botonVerificar = (Button) findViewById(R.id.btnVerificar);
        botonVolver = (Button) findViewById(R.id.btnVolverToken);
        textoToken = (TextView) findViewById(R.id.textoToken);

        // Establece el titulo de la pantalla
        textoToken.setText("Token enviado a :\n " + email);


        botonVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    // Si el token es correcto el cliente se registra
                    if(token == Integer.parseInt(editToken.getText().toString())){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    s.addCliente(new Cliente(email,nombre,apellidos,password));
                                } catch (LogicaException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }).start();
                        // Avisamos al usuario de que el registro que es un exito
                        Toast.makeText(TokenRegistroActivity.this, "Registro exitoso"
                                , Toast.LENGTH_SHORT).show();
                        // Devolvemos un result ok para indicar a la activida anterior que
                        // el usuario introdujo el token correcto
                        setResult(RESULT_OK);
                        finish();
                    }else{
                        // Si el token no es correcto informamos al cliente
                        Toast.makeText(TokenRegistroActivity.this, "Token incorrecto"
                                , Toast.LENGTH_SHORT).show();
                    }
                }catch (NumberFormatException e){
                    //Si dejan el token vacio informamos al usuario
                    Toast.makeText(TokenRegistroActivity.this, "El campo no puede estar vacio"
                                    , Toast.LENGTH_SHORT).show();
                }


            }
        });

        // Volvemos a la pantalla anterior
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}