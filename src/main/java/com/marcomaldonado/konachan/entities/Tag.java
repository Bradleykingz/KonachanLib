package com.marcomaldonado.konachan.entities;

import java.io.Serializable;

/**
 * Created by Mxrck on 05/12/15.
 */
public class Tag implements Serializable {

    private int id;
    private String name;
    private int count;
    private int type;
    private boolean ambiguos;

    public Tag(int id, String name, int count, int type, boolean ambiguos) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.type = type;
        this.ambiguos = ambiguos;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getType() {
        return type;
    }

    public boolean isAmbiguos() {
        return ambiguos;
    }
}
