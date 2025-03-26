package com.example.easydepotmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easydepotmovil.databinding.ActivityMapaBinding;
import com.example.easydepotmovil.databinding.ActivityReservasBinding;

import java.sql.SQLException;
import java.util.List;

import ficheros.excepciones.LogicaException;
import ficheros.excepciones.PersistenciaException;
import ficheros.logica.Sistema;
import ficheros.modelo.Reserva;

public class ReservasActivity extends AppCompatActivity {

    private List<Reserva> reservas;
    private Sistema s;
    private RecyclerView recyclerView;
    private ReservasAdapter reservasAdapter;
    private ImageView volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityReservasBinding binding = ActivityReservasBinding.inflate(getLayoutInflater());
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
        List <Reserva> reservas = s.buscarReservasAbiertasCliente(s.getClienteLogeado().getEmail());
        recyclerView = findViewById(R.id.reservasRecycler);
        reservasAdapter = new ReservasAdapter(this,reservas,s);

        recyclerView.setAdapter(reservasAdapter);
        volver = (ImageView) findViewById(R.id.imagenVolverReservas);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}