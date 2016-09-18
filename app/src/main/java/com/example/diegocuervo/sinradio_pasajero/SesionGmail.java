package com.example.diegocuervo.sinradio_pasajero;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Diego Cuervo on 16/09/16.
 */
public class SesionGmail  extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

 public GoogleApiClient mGoogleApiClient;
    public GoogleSignInOptions gso;
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build();

    mGoogleApiClient=new GoogleApiClient.Builder(this)
        .enableAutoManage(this,this)
        .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
        .build();
        }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

