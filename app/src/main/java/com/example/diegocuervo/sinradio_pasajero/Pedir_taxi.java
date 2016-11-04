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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Diego Cuervo on 08/10/16.
 */
public class Pedir_taxi extends Fragment implements OnMapReadyCallback {
    MapView mMapView;
    private GoogleMap googleMap;
    Double latitud;
    Double longitud;
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
                showInputDialogConfirmacion(direccion);

            }
        });
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

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



                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();

                            if(state.equals("Ciudad Autónoma de Buenos Aires") && city.equals("Buenos Aires")) {
                                String address = addresses.get(0).getAddressLine(0);
                                direccion =address;
                                latitud=arg0.latitude;
                                longitud = arg0.longitude;
                                Log.w("direccion", address);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(arg0);
                                markerOptions.title(address);
                                googleMap.clear();
                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
                                googleMap.addMarker(markerOptions);
                            }
                            else {
                                Toast.makeText(getActivity(),"La direccion elegida no pertenece a la Ciudad Autonoma de Buenos Aires",Toast.LENGTH_LONG).show();
                            }
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


/*
            LocationManager locationManager = (LocationManager)
                    getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
*/
            GPSTracker gps = new GPSTracker(getActivity());

            // check if GPS enabled
            if(gps.canGetLocation()){

                latitud = gps.getLatitude();
                longitud= gps.getLongitude();
                LatLng latlon = new LatLng(latitud,longitud);
                Log.w("direccion", "cayo en al escepcion"+latitud);
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
                // \n is for new line
             //   Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }




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
                                    List<Address> addresses = geoCoder.getFromLocationName(editText.getText().toString(), 10);
                                   Integer cant_direcciones=addresses.size();


                                    Integer k=0;
                                    Integer l=0;
                                    if (cant_direcciones > 0) {


                                    while(k< cant_direcciones )
                                    {

                                        l=1;

                                            String ciudad = addresses.get(k).getAdminArea();
                                            String provincia = addresses.get(k).getLocality();


                                            if (ciudad.equals("Ciudad Autónoma de Buenos Aires") && provincia.equals("Buenos Aires")) {
                                                 latitud = addresses.get(k).getLatitude();
                                                 longitud = addresses.get(k).getLongitude();
                                                LatLng latLng = new LatLng(addresses.get(k).getLatitude(), addresses.get(k).getLongitude());
                                                direccion=editText.getText().toString();
                                                MarkerOptions markerOption = new MarkerOptions();
                                                markerOption.position(latLng);
                                                markerOption.title(editText.getText().toString());
                                                googleMap.clear();
                                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                                googleMap.addMarker(markerOption);

                                                break;
                                            }

                                            k++;
                                        }
                                    }


                                        if (l==1 && cant_direcciones==k) {
                                            Toast.makeText(getActivity(), "La direccion ingresada no se encuentra dentro de la Ciudad Autonoma de Buenos Aires", Toast.LENGTH_LONG).show();
                                        }
                                    if(cant_direcciones==0){

                                            Toast.makeText(getActivity(), "La direccion ingresada no es Valida", Toast.LENGTH_LONG).show();
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
    protected void showInputDialogConfirmacion(final String destino) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.confirma_envio, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final TextView textView = (TextView) promptView.findViewById(R.id.textView);
        final TextView textView_observaciones = (TextView) promptView.findViewById(R.id.textView_observaciones);
        final EditText editText = (EditText) promptView.findViewById(R.id.editText_observacion);
        textView.setText("El taxi sera enviado a: "+destino);
        textView_observaciones.setText("Desea agregar alguna observacion(timbre, piso, N° departamento) ?");

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        JSONObject jsonObject= new JSONObject();
                        String email = Cliente_Singleton.getInstance().email;
                        Log.w("valordeatelle", editText.getText().toString());
                        try {
                            jsonObject.put("dir",destino );
                            jsonObject.put("lat",latitud );
                            jsonObject.put("lon",longitud );
                            jsonObject.put("mail",email );
                            jsonObject.put("detalle", editText.getText().toString());
                        }

                        catch (JSONException e) {

                            e.printStackTrace();

                        }
                        String data =  jsonObject.toString();
                        String baseUrl = "http://API.SIN-RADIO.COM.AR/viaje/";


                        new MyHttpPostRequestDireccion().execute(baseUrl,data);

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
    private class MyHttpPostRequestDireccion extends AsyncTask<String, Integer, String> {

        public String APP_TAG = "direccion_envio";
        protected String doInBackground(String... params) {
            BufferedReader in = null;
            String baseUrl = params[0];
            String jsonData = params[1];



            try {
                JSONObject obj = new JSONObject(jsonData);

                HttpClient httpClient = new DefaultHttpClient();

                HttpPost post = new HttpPost(baseUrl);


                List<NameValuePair> nvp = new ArrayList<NameValuePair>(5);
                nvp.add(new BasicNameValuePair("dir", obj.getString("dir")));
                nvp.add(new BasicNameValuePair("lat", obj.getString("lat")));
                nvp.add(new BasicNameValuePair("lon", obj.getString("lon")));
                nvp.add(new BasicNameValuePair("detalle", obj.getString("detalle")));
                nvp.add(new BasicNameValuePair("mail", obj.getString("mail")));

                post.setEntity(new UrlEncodedFormEntity(nvp,"UTF-8"));

                HttpResponse response = httpClient.execute(post);
                Log.w(APP_TAG, response.getStatusLine().toString());
                int resCode = response.getStatusLine().getStatusCode();

                if(resCode==404 || resCode==410){

                  //  Toast.makeText(getContext(), "Problemas con la coneccion. Pruebe mas tarde.", Toast.LENGTH_SHORT).show();
                }

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                return "En breve le notificaremos la llegada del chofer a :"+direccion;

            } catch (Exception e) {
                return "ERROR: Verifique su coneccion a internet";
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.w(APP_TAG,"Indicador de pregreso " + progress[0].toString());
        }

        protected void onPostExecute(String result) {
            Log.w(APP_TAG,"Resultado obtenido " + result);

            Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();



        }

    }
}
