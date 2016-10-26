package com.example.diegocuervo.sinradio_pasajero;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Cuervo on 08/10/16.
 */
public class Listado_viajes extends Fragment {

View vista;
    public Listado_viajes() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String baseUrl = "http://api.sin-radio.com.ar/cliente/viajes";
        new MyHttpGetRequest().execute(baseUrl);
        View rootView = inflater.inflate(R.layout.listado_viajes, container, false);
        vista = rootView;
        return rootView;
    }

    private class MyHttpGetRequest extends AsyncTask<String, Integer, String> {

        private String APP_TAG= "Pido_Viajes";
        protected String doInBackground(String... params) {
            BufferedReader in = null;
            String baseUrl = params[0];

            try {


                HttpClient httpClient = new DefaultHttpClient();

                HttpGet get = new HttpGet(baseUrl);


              get.addHeader("mail","diegonoya93@gmail.com");


                HttpResponse response = httpClient.execute(get);
                Log.w(APP_TAG, response.getStatusLine().toString());

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                return sb.toString();

            } catch (Exception e) {
                return "Exception happened: " + e.getMessage();
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

            Toast.makeText(getActivity(),"resultado"+ result, Toast.LENGTH_LONG).show();

           try {
                JSONArray array = new JSONArray(result);
                Integer cantidad = array.length();
               Log.w(APP_TAG, "Resultado obtenido " + result + cantidad);
                Integer k=0;
                Tabla tabla = new Tabla(getActivity(), (TableLayout) vista.findViewById(R.id.tabla));
               tabla.agregarCabecera();

               while(k<cantidad){
                   JSONObject jsonObject = array.getJSONObject(k);


                    ArrayList<String> elementos = new ArrayList<String>();

                    elementos.add(jsonObject.getString("id"));
                    elementos.add(jsonObject.getString("chofer"));
                    elementos.add(jsonObject.getString("lat"));
                    elementos.add(jsonObject.getString("lon"));
                    elementos.add(jsonObject.getString("dir"));
                    tabla.agregarFilaTabla(elementos);
                    k++;

                }
            }
            catch (JSONException e) {

                e.printStackTrace();

            }

        }

    }
}
