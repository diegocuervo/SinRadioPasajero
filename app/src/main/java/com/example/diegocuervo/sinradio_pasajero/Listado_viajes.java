package com.example.diegocuervo.sinradio_pasajero;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Diego Cuervo on 08/10/16.
 */
public class Listado_viajes extends Fragment {


    public Listado_viajes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.listado_viajes, container, false);
    }


}
