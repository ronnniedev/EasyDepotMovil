package com.example.easydepotmovil;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easydepotmovil.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.easydepotmovil.databinding.ActivityMapaBinding;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ficheros.excepciones.LogicaException;
import ficheros.excepciones.PersistenciaException;
import ficheros.logica.Sistema;
import ficheros.modelo.Local;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMapaBinding binding;
    private Sistema s;
    private List<Local> locales;
    private ImageView logout;
    private Map<Marker,Local> marcadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            s = Sistema.getInstance();
            locales = s.getLocales();
        } catch (PersistenciaException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (LogicaException e) {
            throw new RuntimeException(e);
        }

        binding = ActivityMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.menuNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.itemLogOut){
                finish();
            }else if(id == R.id.itemSoporte){
                Intent correo = new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("mailto:veronicapersonal1995@gmail.com"));
                startActivity(correo);
            }
            return true;
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Marca la posicion inicial
        LatLng posicionInicial = new LatLng(43.501459, -5.783492);
        // Emparejamos los marcadores con su respectivo local
        marcadores = new HashMap<Marker,Local>();
        for(Local l: locales){
            String trozos[] = l.getCoordenadas().split(",");
            Double latitud = Double.parseDouble(trozos[0]);
            Double longitud = Double.parseDouble(trozos[1]);
            Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(latitud,longitud))
                    .title(l.getDireccion()));
            marcadores.put(m,l);
        }
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(posicionInicial));
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Local l = marcadores.get(marker);
        s.setLocalSeleccionado(l);
        Intent moverse = new Intent(MapaActivity.this,ReservaActivity.class);
        startActivity(moverse);
        return false;
    }
}