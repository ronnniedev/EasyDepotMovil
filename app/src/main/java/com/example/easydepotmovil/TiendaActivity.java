package com.example.easydepotmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.widget.Toast;

import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easydepotmovil.databinding.ActivityTiendaBinding;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Semaphore;

import ficheros.excepciones.LogicaException;
import ficheros.excepciones.PersistenciaException;
import ficheros.logica.Sistema;
import ficheros.modelo.Articulo;
import ficheros.modelo.Cliente;
import ficheros.modelo.Local;

public class TiendaActivity extends AppCompatActivity {

    private Sistema s;
    private List<Articulo> articulos;
    private RecyclerView recyclerView;
    private Button btnVolver,btnComprar;
    private TextView txtPuntos,txtPrecio;
    private ArticulosAdapter articulosAdapter;
    private Cliente c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityTiendaBinding binding = ActivityTiendaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            s = Sistema.getInstance();
        } catch (PersistenciaException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (LogicaException e) {
            throw new RuntimeException(e);
        }

        c = s.getClienteLogeado();
        Local l = s.getLocalSeleccionado();
        articulos = l.getArticulos();
        System.out.println("Articulos: " + articulos.size());
        recyclerView = findViewById(R.id.recyclerTienda);
        articulosAdapter = new ArticulosAdapter(this,articulos,s);

        recyclerView.setAdapter(articulosAdapter);

        // Declaracion de variables
        btnVolver = findViewById(R.id.btnVolverTienda);
        btnComprar = findViewById(R.id.btnComprarArticulos);

        txtPuntos = findViewById(R.id.txtPuntos);
        txtPrecio = findViewById(R.id.txtPrecio);

        txtPuntos.setText("Puntos: " + c.getPuntosTienda());
        txtPrecio.setText("A pagar: 0");


        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < articulos.size(); i++) {
                    ArticulosAdapter.MyViewHolder holder = (ArticulosAdapter.MyViewHolder)
                            recyclerView.findViewHolderForAdapterPosition(i);
                    if (holder != null) {
                        EditText selector = holder.itemView.findViewById(R.id.editNumBigArticulo);
                        int numero = Integer.parseInt(selector.getText().toString());
                        Articulo a = articulos.get(i);
                        a.setStock(numero + a.getStock());
                    }
                }
                finish();
            }
        });

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c.getPuntosTienda() < articulosAdapter.getPrecio()){
                    Toast.makeText(TiendaActivity.this
                            , "No tienes suficientes puntos", Toast.LENGTH_SHORT).show();
                }else{
                    c.setPuntosTienda(c.getPuntosTienda()-articulosAdapter.getPrecio());
                    articulosAdapter.resetPrecio();
                    txtPrecio.setText("A pagar: 0");
                   new Thread(new Runnable() {
                        @Override
                        public void run() {
                            s.actualizarCliente(c);
                        }
                    }).start();
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           Semaphore semaforo = new Semaphore(1);
                           for (Articulo a: articulos) {
                               try {
                                   semaforo.acquire();
                                   s.actualizarArticulo(a);
                                   semaforo.release();
                               } catch (InterruptedException e) {
                                   throw new RuntimeException(e);
                               }
                           }
                       }
                   }).start();
                   txtPuntos.setText("Puntos: " + c.getPuntosTienda());
                   articulosAdapter.actualizarStocks(recyclerView);
                    Intent cargarVenta = new Intent(TiendaActivity.this,
                            activity_vending_machine_loading.class);
                    startActivity(cargarVenta);

                }
            }
        });

    }

    public void cambiarPrecio(int precio){
        txtPrecio.setText("A pagar: " + precio);
    }
}