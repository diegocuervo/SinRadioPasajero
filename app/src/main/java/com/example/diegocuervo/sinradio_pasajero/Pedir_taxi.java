package com.example.diegocuervo.sinradio_pasajero;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Diego Cuervo on 08/10/16.
 */
public class Pedir_taxi extends Fragment implements OnMapReadyCallback {
    MapView mMapView;
    private GoogleMap googleMap;

    String direccion;
    public Pedir_taxi() {

    }

    @TargetApi(Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pedir_taxi, container, false);

        Button button = (Button) rootView.findViewById(R.id.btnPedir_taxi_manual);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showInputDialogManual();

            }
        });
        Button button2 = (Button) rootView.findViewById(R.id.btnpedir_taxi);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showInputDialogConfirmacion(2,direccion);

            }
        });
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng arg0) {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(arg0.latitude, arg0.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


                            String address = addresses.get(0).getAddressLine(0);
                            direccion =address;
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();
                            Log.w("direccion", address);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(arg0);
                            markerOptions.title(address);
                            googleMap.clear();
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
                            googleMap.addMarker(markerOptions);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                googleMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);

            }
        });



        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
            LocationManager locationManager = (LocationManager)
                    getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));


            LatLng latlon = new LatLng(location.getLatitude(),location.getLongitude());
            Log.w("direccion", "cayo en al escepcion"+location.getLatitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latlon);
            Geocoder geoco = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> direccionesActual;

            direccionesActual = geoco.getFromLocation(latlon.latitude, latlon.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = direccionesActual.get(0).getAddressLine(0);
            String posactual =address;
            Log.w("direccion", address);
            direccion =address;
            Log.w("direccion", String.valueOf(latlon.longitude));
            markerOptions.title(posactual);
            googleMap.clear();
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlon));
            googleMap.addMarker(markerOptions);
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("direccion", "cayo en al escepcion"+direccion);
        }

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    protected void showInputDialogManual() {


        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.ingreso_manual, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final TextView textView = (TextView) promptView.findViewById(R.id.textView);
        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        textView.setText("Ingrese la direccion donde desea esperar al taxi");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                                try
                                {
                                    List<Address> addresses = geoCoder.getFromLocationName(editText.getText().toString(), 1);
                                    if (addresses.size() > 0)
                                    {
                                        String ciudad = addresses.get(0).getAdminArea();
                                                String provincia =addresses.get(0).getLocality();


                                        if(ciudad.equals("Ciudad Autónoma de Buenos Aires") && provincia.equals("Buenos Aires")){
                                               Double lat= addresses.get(0).getLatitude();
                                        Double lon = addresses.get(0).getLongitude();
                                        LatLng latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                                        MarkerOptions markerOption = new MarkerOptions();
                                        markerOption.position(latLng);
                                        markerOption.title(editText.getText().toString());
                                        googleMap.clear();
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                        googleMap.addMarker(markerOption);
                                        Log.d("Latitude", ""+lat);
                                        Log.d("Longitude", ""+lon);
                                    }
                                        else{
                                            Toast.makeText(getActivity(),"La direccion ingresada no se encuentra dentro de la Ciudad Autonoma de Buenos Aires",Toast.LENGTH_LONG).show();
                                        }
                                }
                                    else{
                                        Toast.makeText(getActivity(),"La direccion ingresada no es Valida",Toast.LENGTH_LONG).show();
                                    }
                                }

                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        })
        .setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });



        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    protected void showInputDialogConfirmacion(final int id_fila,final String destino) {

        // get prompts.xml view

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.confirma_envio, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final TextView textView = (TextView) promptView.findViewById(R.id.textView);
        final TextView textView_observaciones = (TextView) promptView.findViewById(R.id.textView_observaciones);
        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        textView.setText("Estas seguro que desea esperar el taxi en:"+destino);
        textView_observaciones.setText("Desea agregar alguna observasion(timbre, piso, N° departamento) ?");
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(),"En breve le notificaremos la llegada del Chofer a "+direccion,Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });



        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

}
