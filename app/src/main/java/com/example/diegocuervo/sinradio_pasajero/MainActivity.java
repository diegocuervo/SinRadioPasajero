package com.example.diegocuervo.sinradio_pasajero;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    ImageView imgProfilePic;
    TextView nombre;
    Activity actividad;
    GoogleApiClient mGoogleApiClient;
    String email;
    TextView mail;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        Log.w("asdasd", android_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle inBundle = getIntent().getExtras();
        name = inBundle.get("nombre").toString();

        email = inBundle.get("email").toString();

        String baseUrl = "http://API.SIN-RADIO.COM.AR/cliente/token/"+android_id;
        new MyHttpPostRequestToken().execute(baseUrl);
     //   startService(new Intent(MyFirebaseMessagingService.class.getName()));
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        this.actividad=this;
        Cliente_Singleton.getInstance().email=email;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        nombre = (TextView) header.findViewById(R.id.textview_nombre);
        mail = (TextView) header.findViewById(R.id.textView_email);
        imgProfilePic = (ImageView) header.findViewById(R.id.imgProfilePic);
        mail.setText(email);
        nombre.setText(name);



        try {
            String foto= inBundle.get("foto").toString();
            Glide.with(getApplicationContext()).load(foto)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);
        }
        catch (NullPointerException e){

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

                moveTaskToBack(true);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                            Toast.makeText(getApplicationContext(),"Sesion Cerrada",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), ValidacionPasajero.class);
                            startActivity(i);
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.listado_viajes) {
            fragment = new Listado_viajes();
        } else if (id == R.id.pedir_taxi) {

            fragment = new Pedir_taxi();

        } else if (id == R.id.salir) {
            moveTaskToBack(true);
            return true;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.fragment,fragment)
                .commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class MyHttpPostRequestToken extends AsyncTask<String, Integer, String> {

        public String APP_TAG = "token_envio";
        protected String doInBackground(String... params) {
            BufferedReader in = null;
            String baseUrl = params[0];


            try {

                HttpClient httpClient = new DefaultHttpClient();

                HttpPost post = new HttpPost(baseUrl);

                List<NameValuePair> nvp = new ArrayList<NameValuePair>(2);
                nvp.add(new BasicNameValuePair("token", FirebaseInstanceId.getInstance().getToken()));
                nvp.add(new BasicNameValuePair("email", email));
                Log.w(APP_TAG, FirebaseInstanceId.getInstance().getToken()+email);

                post.setEntity(new UrlEncodedFormEntity(nvp,"UTF-8"));

                HttpResponse response = httpClient.execute(post);
                Log.w(APP_TAG, response.getStatusLine().toString());
                int resCode = response.getStatusLine().getStatusCode();

                if(resCode==404 || resCode==410){

                 //   Toast.makeText(actividad, "Problemas con la coneccion. Pruebe mas tarde.", Toast.LENGTH_SHORT).show();
                }
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                return "Bienvenido "+name;

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

            Log.w(APP_TAG,"Resultado obtenido token mail" + result);
            Toast.makeText(actividad, result, Toast.LENGTH_SHORT).show();


        }

    }

}
