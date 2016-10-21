package com.example.diegocuervo.sinradio_pasajero;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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
 * Created by Diego Cuervo on 03/08/2016.
 */
public class Viaje extends AppCompatActivity {
    private Activity actividad;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viajes);
        this.actividad=this;
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        JSONObject jsonObject= new JSONObject();


        try {


            jsonObject.put("id_android",id );
        }

        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        String data =  jsonObject.toString();
        String baseUrl = "http://sinradio.ddns.net:45507/";
        new MyHttpPostRequest().execute(baseUrl, data);



    }






private class MyHttpPostRequest extends AsyncTask<String, Integer, String> {

    private String APP_TAG= "SinRadio-appChofer";
    protected String doInBackground(String... params) {
        BufferedReader in = null;
        String baseUrl = params[0];
        String jsonData = params[1];

        try {
            JSONObject obj = new JSONObject(jsonData);
            //Creamos un objeto Cliente HTTP para manejar la peticion al servidor
            HttpClient httpClient = new DefaultHttpClient();
            //Creamos objeto para armar peticion de tipo HTTP POST
            HttpPost post = new HttpPost(baseUrl);

            //Configuramos los parametos que vaos a enviar con la peticion HTTP POST
            List<NameValuePair> nvp = new ArrayList<NameValuePair>(2);
            nvp.add(new BasicNameValuePair("evento", "pedirViajes"));
            nvp.add(new BasicNameValuePair("id_android", obj.getString("id_android")));

            // post.setHeader("Content-type", "application/json");
            post.setEntity(new UrlEncodedFormEntity(nvp,"UTF-8"));

            //Se ejecuta el envio de la peticion y se espera la respuesta de la misma.
            HttpResponse response = httpClient.execute(post);
            Log.w(APP_TAG, response.getStatusLine().toString());

            //Obtengo el contenido de la respuesta en formato InputStream Buffer y la paso a formato String
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
        //Se obtiene el progreso de la peticion
        Log.w(APP_TAG,"Indicador de pregreso " + progress[0].toString());
    }

    protected void onPostExecute(String result) {
        //Se obtiene el resultado de la peticion Asincrona
        try {
            JSONArray array = new JSONArray(result);
            Integer cantidad = array.length();
            Integer k=0;
            Tabla tabla = new Tabla(actividad, (TableLayout) findViewById(R.id.tabla));
            tabla.agregarCabecera();
            while(k<cantidad){
            JSONObject jsonObject = array.getJSONObject(k);




            Log.w(APP_TAG, "Resultado obtenido " + result);


                ArrayList<String> elementos = new ArrayList<String>();

                elementos.add(jsonObject.getString("id"));
                elementos.add(jsonObject.getString("id_cliente"));
                elementos.add(jsonObject.getString("origen"));
                elementos.add(jsonObject.getString("destino"));
                elementos.add(jsonObject.getString("monto"));
                tabla.agregarFilaTabla(elementos);
                k++;

            }
        }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }


       /* try {
            JSONArray array = new JSONArray(result);

            JSONObject jsonObject = array.getJSONObject(0);


            Log.w(APP_TAG,"Anduvo el parseo puto? " + jsonObject.getString("apellido"));
            Toast.makeText(actividad, jsonObject.getString("apellido"), Toast.LENGTH_SHORT).show();
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }*/

        Toast.makeText(actividad, result, Toast.LENGTH_SHORT).show();


    }

}
}
