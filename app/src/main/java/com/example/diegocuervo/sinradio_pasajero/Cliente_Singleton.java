package com.example.diegocuervo.sinradio_pasajero;

/**
 * Created by Diego Cuervo on 24/10/16.
 */
public class Cliente_Singleton {
    private static Cliente_Singleton intancia= null;
    String email;
    protected Cliente_Singleton(){}
    public static synchronized Cliente_Singleton getInstance(){
        if(null == intancia){
            intancia = new Cliente_Singleton();
        }
        return intancia;
    }
}
