package com.a952000243.ingwilson.nosliwsys.tema_test;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    GoogleMap mapa;
    LatLng ubicacion;
    private OkHttpRequest okHttpRequest;
    String address="";
    String nro="";
    EditText midireccion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        midireccion=findViewById(R.id.midireccion);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            arranque();
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                    "Sin el permiso" + " no podemos ubicarte.", 0);
        }

// Obtenemos el mapa de forma asíncrona (notificará cuando esté listo)
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Toast.makeText(this, "GPS available", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "GPS not available", Toast.LENGTH_LONG).show();
        }
        LocationListener locationListener = new LocationListener()
        {
            public void onLocationChanged(Location location)
            {
                Toast.makeText(getApplicationContext(), "Se cambio de posicion", Toast.LENGTH_SHORT).show();
                Double latitude=location.getLatitude();
                Double longitude=location.getLatitude();
                Toast.makeText(getApplicationContext(), "latitud: "+ latitude.toString()+ " longitud: "+ longitude.toString(), Toast.LENGTH_SHORT).show();
                if(checkseguir==1)
                    if (mapa.getMyLocation() != null)
                        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mapa.getMyLocation().getLatitude(),
                                        mapa.getMyLocation().getLongitude()), 15));
            }
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
            }
            public void onProviderEnabled(String provider)
            {
            }
            public void onProviderDisabled(String provider)
            {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    public void solicitarPermiso(final String permiso, String justificacion, final int codigo) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Solicitud de permiso");
            dialogo1.setMessage(justificacion);
            dialogo1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permiso}, codigo);
                }
            });
            dialogo1.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permiso}, codigo);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                arranque();
            } else {
                solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                        "Sin el permiso" + "\" no podemos localizarte.", 0);
            }
        }
    }

    void arranque() {
        Toast.makeText(this,"bienvenido", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        ubicacion = new LatLng(-18.024578408795737, -70.25051112093377); //Nos ubicamos en la UNJBG
        mapa.addMarker(new MarkerOptions().position(ubicacion).title("Marcador UNJBG"));
        mapa.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
        mapa.setOnMapClickListener(this);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(false);
            mapa.getUiSettings().setCompassEnabled(true);
        } else {
            Button btnMiPos=(Button) findViewById(R.id.btnmiubi);
            btnMiPos.setEnabled(false);
        }
    }
    public void moveCamera(View view) {
        mapa.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
    }
    public void addMarker(View view) {
        mapa.addMarker(new MarkerOptions().position(
                mapa.getCameraPosition().target));
    }
    @Override public void onMapClick(LatLng puntoPulsado) {
        mapa.addMarker(new MarkerOptions().position(puntoPulsado)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

    public void ubicacion(View view) {
        if (mapa.getMyLocation() != null)
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mapa.getMyLocation().getLatitude(),
                            mapa.getMyLocation().getLongitude()), 17));
    }

    int checkseguir=0;
    public void Seguir(View view) {
        Button btnseguir=findViewById(R.id.btnseguir);
        if (checkseguir==0) {
            checkseguir = 1;
            btnseguir.setText("OFF seguir");
            Toast.makeText(this, "Se Activo el seguimiento", Toast.LENGTH_SHORT).show();
        }
        else{
            checkseguir = 0;
            btnseguir.setText("ON seguir");
            Toast.makeText(this, "Se desactivo el seguimiento", Toast.LENGTH_SHORT).show();
        }
    }

    public void geocode(View view) {
        Double latitude=mapa.getCameraPosition().target.latitude;
        Double longitude=mapa.getCameraPosition().target.longitude;
        String URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + latitude+ "," + longitude+"&key=AIzaSyATeXI_OjJV23BBHX1EiPW5sveGQekWNPo";
        okHttpRequest = new OkHttpRequest(URL);
        Log.i("URL_MAPS", URL);
// Metodo que realiza una peticion GET
        okHttpRequest.getNotParams(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("test",request.toString() + " el error:"+e.toString() );
            }
            @Override
            public void onResponse(Response response) throws IOException {
                String res = response.body().string();
                Log.i("test", "mi res: "+res);
                try {
// Se convierte el cuerpo del mensaje
// y se convierte en un objeto JSON
                    JSONObject result = new JSONObject(res);
// Se verifica que tiene el campo "results"
                    Log.i("Estadovalor",result.getString("status"));
                    if (result.has("results")) {
// Se obtiene el JSONArray a partir del campo "results"
                        JSONArray array = result.getJSONArray("results");
// Se verifica que el array no este vacio
                        if (array.length() > 0) {
// Se obtiene el primer valor del array
                            JSONObject place = array.getJSONObject(0);
// Se obtiene el JSONArray a partir del campo "address_components"
                            JSONArray components = place.getJSONArray("address_components");
// Se recorre el Array
                            for (int i = 0; i < components.length(); i++) {
// Se obtiene el JSON a partir del JSONArray
                                JSONObject component = components.getJSONObject(i);
// Se obtiene el campo del JSONArray "types" y se guarda en otro
// JSONArray
                                JSONArray types = component.getJSONArray("types");
// Se coge solo el primer elemento del JSONArray
                                String type = types.getString(0);
//Se verifica que el valor del type sea igual a "route"
                                if (type.equals("route")) {
                                    try {
// Se formatea el valor del address para UTF-8 para reconocer tildes
// y se guarda en un String
                                        address = new String(component.getString("short_name").getBytes(), "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.toString();
                                    }
                                }
                                if (type.equals("street_number")) {
                                    try {
// Se formatea el valor del address para UTF-8 para reconocer tildes
// y se guarda en un String
                                        nro = new String(component.getString("short_name").getBytes(), "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.toString();
                                    }
                                }
// Se verifica que el campo no sea vacio
                                if (!address.isEmpty()) {
// Se crea una tarea y se coloca en el hilo principal
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
// Se pasa el valor obtenido y se pasa
                                            midireccion.setText(address+" "+nro);
                                        }
                                    });
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    Log.e("JSONArrayException", e.toString());
                }
            }
        });
    }


}
