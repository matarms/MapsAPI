package com.example.mapsapi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class mapas extends AppCompatActivity implements OnMapReadyCallback{
    private final LatLng CASA = new LatLng(-23.809578, -46.808523);
    private final LatLng VICOSA = new LatLng(-20.757365, -42.876286);
    private final LatLng DPTO = new LatLng(-20.764932, -42.868450);
    private LatLng Lselected;
    private GoogleMap map;

    public LocationManager lm;
    private android.location.LocationListener listener;
    private LatLng ponto_atual;

    private double dist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);

        ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMapAsync(this);

        Intent it = getIntent();
        String s = it.getStringExtra("local");
        switch (s) {
            case "Casa":
                Lselected = CASA;
                Toast.makeText(this, "Casa", Toast.LENGTH_SHORT).show();
                break;
            case "Vicosa":
                Lselected = VICOSA;
                Toast.makeText(this, "Casa Viçosa", Toast.LENGTH_SHORT).show();
                break;
            case "Dpto":
                Lselected = DPTO;
                Toast.makeText(this, "Departamento de Informática", Toast.LENGTH_SHORT).show();
                break;
        }
        Log.d("DBUG", "comeco");

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("DBUG", "\n " + location.getLongitude() + " " + location.getLatitude());
                ponto_atual = new LatLng(location.getLatitude(), location.getLongitude());
                final Location pontoVic = new Location("gps");
                pontoVic.setLatitude(-20.757365);
                pontoVic.setLongitude( -42.876286);

                dist = location.distanceTo(pontoVic);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.d("DBUG", "trocou status");
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d("DBUG", "provider enable");
            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };


        configure_button();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("DBUG", "REQUENBT");
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        lm.requestLocationUpdates("gps", 5000, 0,listener);
    }

    public void onClickLocalization(View view){
        String tag =  view.getTag().toString();
        switch(tag){
            case "0": Lselected = CASA;
                    Toast.makeText(this, "Casa", Toast.LENGTH_SHORT).show();
                    break;
            case "1": Lselected = VICOSA;
                    Toast.makeText(this, "Casa Viçosa", Toast.LENGTH_SHORT).show();
                    break;
            case "2": Lselected = DPTO;
                    Toast.makeText(this, "Departamento de Informática", Toast.LENGTH_SHORT).show();
                    break;
        }
        CameraUpdate c = CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(Lselected)
                        .tilt(0)
                        .zoom(19)
                        .build());

        map.animateCamera(c);

    }

    public void setLocalization(View view){
        DecimalFormat df = new DecimalFormat();
        Toast.makeText(this, "Distancia de "+ df.format(dist) + " m de Vicosa", Toast.LENGTH_SHORT).show();
        map.addMarker(new MarkerOptions().position(ponto_atual).title("Minha localização atual")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        CameraUpdate c = CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(ponto_atual)
                        .tilt(0)
                        .zoom(19)
                        .build());

        map.animateCamera(c);

    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.addMarker(new MarkerOptions().position(CASA).title("Ponto"));
        map.addMarker(new MarkerOptions().position(VICOSA).title("Ponto"));
        map.addMarker(new MarkerOptions().position(DPTO).title("Ponto"));
        CameraUpdate c = CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(Lselected)
                                    .tilt(0)
                                    .zoom(20)
                                    .build());

        map.animateCamera(c);
    }
}