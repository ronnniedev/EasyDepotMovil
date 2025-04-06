package com.example.easydepotmovil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.SQLException;
import java.util.List;

import ficheros.excepciones.LogicaException;
import ficheros.excepciones.PersistenciaException;
import ficheros.logica.Sistema;
import ficheros.modelo.Cabina;
import ficheros.modelo.Local;
import ficheros.modelo.Cliente;

public class ReservaActivity extends AppCompatActivity {

    private Local l;
    private Cliente c;
    private Sistema s;
    private ImageView imagenVolverReserva;
    private Button botonReservar;
    private TextView tituloLocal;
    private Button botonMinusSmall;
    private Button botonPlusSmall;
    private EditText editSmall;
    private Button botonMinusMedium;
    private Button botonPlusMedium;
    private EditText editMedium;
    private Button botonMinusBig;
    private Button botonPlusBig;
    private EditText editBig;
    private TextView txtDisSmall;
    private TextView txtDisMedium;
    private TextView txtDisBig;
    private Button botonTienda;


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

        botonTienda = (Button) findViewById(R.id.botonTiendaPuntos);
        imagenVolverReserva = (ImageView) findViewById(R.id.imagenVolverReserva);
        botonReservar = (Button) findViewById(R.id.botonReservar);
        tituloLocal = (TextView) findViewById(R.id.txtTituloLocal);
        botonMinusSmall = (Button) findViewById(R.id.minusSmall);
        botonPlusSmall = (Button) findViewById(R.id.plusSmall);
        editSmall = (EditText) findViewById(R.id.editNumSmall);
        botonMinusMedium = (Button) findViewById(R.id.minusMedium);
        botonPlusMedium = (Button) findViewById(R.id.plusMedium);
        editMedium = (EditText) findViewById(R.id.editNumMedium);
        botonMinusBig = (Button) findViewById(R.id.minusBig);
        botonPlusBig = (Button) findViewById(R.id.plusBig);
        editBig = (EditText) findViewById(R.id.editNumBig);
        txtDisSmall = (TextView) findViewById(R.id.txtDisSmall);
        txtDisMedium = (TextView) findViewById(R.id.txtDisMedium);
        txtDisBig = (TextView) findViewById(R.id.txtDisBig);

        txtDisSmall.setText(txtDisSmall.getText().toString() + calcularDisponibilidad("Pequeña"));
        txtDisMedium.setText(txtDisMedium.getText().toString() + calcularDisponibilidad("Mediana"));
        txtDisBig.setText(txtDisBig.getText().toString() + calcularDisponibilidad("Grande"));

        tituloLocal.setText(tituloLocal.getText().toString() + " " + l.getDireccion());

        botonTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moverse = new Intent(ReservaActivity.this,TiendaActivity.class);
                startActivity(moverse);
            }
        });

        botonMinusSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int numero =  bajarNumero(Integer.parseInt(editSmall.getText().toString()));
              editSmall.setText(numero + "");
            }
        });

        botonPlusSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numero =  subirNumero(Integer.parseInt(editSmall.getText().toString()));
                editSmall.setText(numero + "");
            }
        });

        botonMinusMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numero =  bajarNumero(Integer.parseInt(editMedium.getText().toString()));
                editMedium.setText(numero + "");
            }
        });

        botonPlusMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numero =  subirNumero(Integer.parseInt(editMedium.getText().toString()));
                editMedium.setText(numero + "");
            }
        });

        botonMinusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numero =  bajarNumero(Integer.parseInt(editBig.getText().toString()));
                editBig.setText(numero + "");
            }
        });

        botonPlusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numero =  subirNumero(Integer.parseInt(editBig.getText().toString()));
                editBig.setText(numero + "");
            }
        });

        imagenVolverReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cargarReserva = new Intent(ReservaActivity.this,
                        LoadingReserva.class);
                int numSmall = Integer.parseInt(editSmall.getText().toString());
                int numMedium = Integer.parseInt(editMedium.getText().toString());
                int numBig = Integer.parseInt(editBig.getText().toString());
                if(numSmall == 0 && numMedium == 0 && numBig == 0){
                    Toast.makeText(ReservaActivity.this, "Debe seleccionar una cabina",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!comprobarCabinas(numSmall,numMedium,numBig)){
                    return;
                }
                cargarReserva.putExtra("Pequeñas",numSmall);
                cargarReserva.putExtra("Medianas",numMedium);
                cargarReserva.putExtra("Grandes",numBig);
                startActivityForResult(cargarReserva,1);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Calcula la disponibilidad de una cabina para cada tipo proporcionado
     * @param tipo : String
     * @return
     */
    private int calcularDisponibilidad(String tipo) {
        int cont = 0;
        List <Cabina> cabinas = s.getLocalSeleccionado().getCabinas();

        for(Cabina c: cabinas){
            if(c.getTipo().equals(tipo) && !c.getReservada()){
                cont += 1;
            }

        }
        return cont;
    }

    /**
     * Comprueba las cabinas disponibles de cada tipo, si no hay suficientes cabinas regresa
     * a la pantalla de reservas y da un Toast
     * @param numSmall
     * @param numMedium
     * @param numBig
     * @return
     */
    private Boolean comprobarCabinas(int numSmall, int numMedium, int numBig) {
        List<Cabina> cabinas = l.getCabinas();
        int contSmall = 0;
        int contMedium = 0;
        int contBig = 0;
        for(Cabina c: cabinas){
            if(!c.getReservada()){
                if(c.getTipo().equals("Pequeña")){
                    contSmall += 1;
                }else if(c.getTipo().equals("Mediana")){
                    contMedium += 1;
                }else{
                    contBig += 1;
                }
            }
        }
        if(contSmall < numSmall){
            Toast.makeText(this, "Cabinas pequeñas insuficientes para reservar"
                    , Toast.LENGTH_SHORT).show();
            return false;
        }

        if(contMedium < numMedium){
            Toast.makeText(this, "Cabinas medianas insuficientes para reservar"
                    , Toast.LENGTH_SHORT).show();
            return false;
        }

        if(contBig < numBig){
            Toast.makeText(this, "Cabinas grandes insuficientes para reservar"
                    , Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private int subirNumero(int n) {
        if(n == 2){
            Toast.makeText(this, "Limite por cliente alcanzado", Toast.LENGTH_SHORT)
                    .show();
            return 2;
        }
        return n+1;
    }

    private int bajarNumero(int n) {
        if(n == 0){
            return 0;
        }
        return n-1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Toast.makeText(this, "Reserva realizada con exito", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}