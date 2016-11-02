package com.example.diegocuervo.sinradio_pasajero;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
public class Tabla {


    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private Activity actividad;
    private Resources rs;
    private int FILAS, COLUMNAS;


    public Tabla(Activity actividad, TableLayout tabla) {
        this.actividad = actividad;
        this.tabla = tabla;
        rs = this.actividad.getResources();
        FILAS = COLUMNAS = 0;
        filas = new ArrayList<TableRow>();
    }


    public void agregarCabecera() {
        TableRow.LayoutParams layoutCelda;
        TableRow fila = new TableRow(actividad);
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(layoutFila);

        String[] arraycabecera = rs.getStringArray((R.array.cab_tab));

        COLUMNAS = arraycabecera.length;

        for (int i = 0; i < arraycabecera.length; i++) {
            TextView texto = new TextView(actividad);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(arraycabecera[i]), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setText(arraycabecera[i]);
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            texto.setTextAppearance(actividad, R.style.estilo_celda);
            texto.setBackgroundResource(R.drawable.tabla_celda_cabecera);
            texto.setLayoutParams(layoutCelda);

            fila.addView(texto);
        }

        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }

    public void agregarFilaTabla(ArrayList<String> elementos) {
        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        final TableRow fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);
        fila.setFocusable(true);
        fila.setFocusableInTouchMode(true);
        fila.setClickable(true);
        fila.setId(Integer.parseInt(elementos.get(0)));
        fila.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



            }
        });

        for (int i = 0; i < elementos.size(); i++) {



            TextView texto = new TextView(actividad);
            texto.setText(String.valueOf(elementos.get(i)));
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            texto.setTextAppearance(actividad, R.style.estilo_celda);
            texto.setBackgroundResource(R.drawable.tabla_celda);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(texto.getText().toString()), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setLayoutParams(layoutCelda);

            fila.addView(texto);
        }


        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }


    private int obtenerAnchoPixelesTexto(String texto) {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(50);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }








    private class MyHttpPostRequest extends AsyncTask<String, Integer, String> {

        public String APP_TAG = "ECTUploadData";
        protected String doInBackground(String... params) {
            BufferedReader in = null;
            String baseUrl = params[0];
            String jsonData = params[1];

            try {
                //Creamos un objeto Cliente HTTP para manejar la peticion al servidor
                HttpClient httpClient = new DefaultHttpClient();
                //Creamos objeto para armar peticion de tipo HTTP POST
                HttpPost post = new HttpPost(baseUrl);

                //Configuramos los parametos que vaos a enviar con la peticion HTTP POST
                List<NameValuePair> nvp = new ArrayList<NameValuePair>(3);
                nvp.add(new BasicNameValuePair("evento", "ingresomonto"));
                nvp.add(new BasicNameValuePair("monto", "456.52"));
                nvp.add(new BasicNameValuePair("id_viaje","4353" ));

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

            Log.w(APP_TAG,"Resultado obtenido " + result);
            try {
                JSONArray array = new JSONArray(result);

                JSONObject jsonObject = array.getJSONObject(0);


                Log.w(APP_TAG,"Anduvo el parseo puto? " + jsonObject.getString("apellido"));
                Toast.makeText(actividad, jsonObject.getString("apellido"), Toast.LENGTH_SHORT).show();
            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }

            Toast.makeText(actividad, result, Toast.LENGTH_SHORT).show();


        }

    }
}

