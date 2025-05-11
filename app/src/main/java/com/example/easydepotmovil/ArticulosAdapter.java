package com.example.easydepotmovil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.Cloudinary;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Semaphore;

import ficheros.logica.Sistema;
import ficheros.modelo.Articulo;
import ficheros.modelo.Cliente;
import ficheros.persistencia.GestorCloudinary;


public class ArticulosAdapter extends RecyclerView.Adapter<ArticulosAdapter.MyViewHolder> {
    private int precio;
    private List<Articulo> articulos;
    private Sistema s;
    private TiendaActivity context;
    private Semaphore semaforo = new Semaphore(1);
    private GestorCloudinary cloudinary = new GestorCloudinary();

    public ArticulosAdapter(TiendaActivity context,List <Articulo>articulos,Sistema s){
        this.precio = 0;
        this.context = context;
        this.articulos = articulos;
        this.s = s;
    }

    public int getPrecio() {
        return precio;
    }

    @NonNull
    @Override
    public ArticulosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_articulo_item,parent,false);
        return new ArticulosAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cliente c = s.getClienteLogeado();
        Articulo a = articulos.get(position);

        holder.txtPrecio.setText("Precio: " + a.getPrecio());
        holder.txtNombre.setText("Nombre: " + a.getNombre());
        holder.txtStock.setText("Stock: " + a.getStock());

        actualizarBotones(holder.btnMinus,holder.btnPlus,a,holder);
        cargaImagen(a,holder);

    }

    private void cargaImagen(Articulo a,MyViewHolder holder) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(a.getImagen().equals("sin imagen")){
                    holder.barraArticulo.setVisibility(View.INVISIBLE);
                    holder.imagenArticulo.setVisibility(View.VISIBLE);
                }else{
                    try {
                        URL url = new URL(cloudinary.getUrl(a.getImagen()));
                        Bitmap bitmap = BitmapFactory.decodeStream(url
                                .openConnection()
                                .getInputStream());
                        setImagen(bitmap,holder);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

    }

    private void setImagen(Bitmap bitmap,MyViewHolder holder) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                holder.imagenArticulo.setImageBitmap(bitmap);
                holder.barraArticulo.setVisibility(View.INVISIBLE);
                holder.imagenArticulo.setVisibility(View.VISIBLE);
            }
        };
        ejecutarEnHiloPrincipal(r);
    }

    private void ejecutarEnHiloPrincipal(Runnable r) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(r);
    }

    @Override
    public int getItemCount() {
        return articulos.size();
    }

    public void actualizarStocks(RecyclerView recyclerView) {
        for (int i = 0; i < articulos.size(); i++) {
            ArticulosAdapter.MyViewHolder holder = (ArticulosAdapter.MyViewHolder)
                    recyclerView.findViewHolderForAdapterPosition(i);
            Articulo a = articulos.get(i);
            if (holder != null) {
                EditText selector = holder.itemView.findViewById(R.id.editNumBigArticulo);
                TextView stock = holder.itemView.findViewById(R.id.disponiblesArticulo);

                selector.setText("0");
                stock.setText("Stock: " + a.getStock());
            }
            Button minus = holder.itemView.findViewById(R.id.minusBigArticulo);
            Button plus = holder.itemView.findViewById(R.id.plusBigArticulo);

            actualizarBotones(minus,plus,a,holder);

        }
    }

    private void actualizarBotones(Button minus,Button plus,Articulo a,MyViewHolder holder){
        int stock = a.getStock();

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valorSelector = Integer.parseInt(holder.selector.getText().toString());
                if(valorSelector != 0){
                    valorSelector -= 1;
                    holder.selector.setText(valorSelector + "");
                    precio -= a.getPrecio();
                    context.cambiarPrecio(precio);
                    a.setStock(a.getStock()+1);
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valorSelector = Integer.parseInt(holder.selector.getText().toString());
                if(valorSelector != stock){
                    valorSelector += 1;
                    holder.selector.setText(valorSelector + "");
                    precio += a.getPrecio();
                    context.cambiarPrecio(precio);
                    a.setStock(a.getStock()-1);
                }
            }
        });


    }

    public void resetPrecio() {
        precio = 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtPrecio,txtNombre,txtStock;
        private Button btnPlus,btnMinus;
        private EditText selector;
        private ImageView imagenArticulo;
        private ProgressBar barraArticulo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPrecio = itemView.findViewById(R.id.precioArticulo);
            txtNombre = itemView.findViewById(R.id.nombreArticulo);
            txtStock = itemView.findViewById(R.id.disponiblesArticulo);
            btnPlus = itemView.findViewById(R.id.plusBigArticulo);
            btnMinus = itemView.findViewById(R.id.minusBigArticulo);
            selector = itemView.findViewById(R.id.editNumBigArticulo);
            imagenArticulo = itemView.findViewById(R.id.imagenArticulo);
            barraArticulo = itemView.findViewById(R.id.cargaImagen);
        }
    }
}
