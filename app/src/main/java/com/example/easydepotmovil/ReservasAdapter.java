package com.example.easydepotmovil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ficheros.logica.Sistema;
import ficheros.modelo.Cabina;
import ficheros.modelo.Local;
import ficheros.modelo.Reserva;

class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.MyViewHolder> {

    private Context context;
    private List <Reserva> reservas;
    private Sistema s;

    public ReservasAdapter(Context context ,List <Reserva> reservas,Sistema s){
        this.context = context;
        this.reservas = reservas;
        this.s = s;
    }

    @NonNull
    @Override
    public ReservasAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Aqui inflamos cada una de las tarjetas dentro de el modulo de reservas
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_reserva_item,parent,false);
        return new ReservasAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservasAdapter.MyViewHolder holder, int position) {
        Reserva r = reservas.get(position);
        String idCabina = r.getIdCabina();
        String trozos[] = idCabina.split("-");
        Local l = s.buscarLocal(Integer.parseInt(trozos[0]));
        holder.txtDireccion.setText("Direccion: " + l.getDireccion());
        holder.txtFecha.setText("Fecha reserva: "+ r.getFechaInicio());
        holder.txtCabina.setText("Cabina: " + trozos[1]);

        holder.btCabina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cabina c = s.buscarCabina(idCabina,l);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        c.setAbierto(true);
                        s.actualizarCabina(c);
                        mostrarTexto("Cabina abierta");
                        try {
                            Thread.sleep(8000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        c.setAbierto(false);
                        s.actualizarCabina(c);
                        mostrarTexto("Cabina cerrada");
                    }
                }).start();
            }
            private void mostrarTexto(String texto) {
                android.os.Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.btReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moverse = new Intent(context,LoadingCierreReserva.class);
                moverse.putExtra("reserva",r.getIdReserva());
                context.startActivity(moverse);
                // lo casteamos para hacer el cierre
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtDireccion,txtFecha,txtCabina;
        Button btReserva,btCabina,btIncidencia;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDireccion = itemView.findViewById(R.id.direccionTextView);
            txtFecha = itemView.findViewById(R.id.fechaTextView);
            txtCabina = itemView.findViewById(R.id.txtCabina);
            btReserva = itemView.findViewById(R.id.botonReserva);
            btCabina = itemView.findViewById(R.id.botonCabina);
            btIncidencia = itemView.findViewById(R.id.botonIncidencia);
        }
    }
}