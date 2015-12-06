package com.marcomaldonado.konachan.service;

public class KonachanErrors {

    public static final int GENERIC_ERROR = 0;

    public static String message( int error )
    {
        switch (error) {
            case 0:
                return "Error Genérico";
            default:
                return "Error desconocido";
        }
    }

}
