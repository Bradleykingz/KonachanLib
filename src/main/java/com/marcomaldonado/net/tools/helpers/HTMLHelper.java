package com.marcomaldonado.net.tools.helpers;

/**
 * Created by Mxrck on 19/11/2015.
 */
public class HTMLHelper {

    private static HTMLHelper instance;

    protected HTMLHelper()
    {
    }

    public static HTMLHelper getInstance()
    {
        if (instance == null) {
            instance = new HTMLHelper();
        }
        return instance;
    }

    public String get(String url)
    {
        return null;
    }

}
