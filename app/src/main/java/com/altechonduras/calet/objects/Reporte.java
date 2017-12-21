package com.altechonduras.calet.objects;

import android.support.annotation.Keep;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nery Ortez on 16-Dec-17.
 */
@Keep
public class Reporte {
    private String _date;
    private String _name;
    private String name;
    private String id;
    private HashMap<String, String> format;
    private ArrayList<String> order;
    private ArrayList<String> recipientes;
    private boolean mostrarSegundo;

    public Reporte(){}

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, String> getFormat() {
        return format;
    }

    public void setFormat(HashMap<String, String> format) {
        this.format = format;
    }

    public ArrayList<String> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<String> order) {
        this.order = order;
    }

    public ArrayList<String> getRecipientes() {
        return recipientes;
    }

    public void setRecipientes(ArrayList<String> recipientes) {
        this.recipientes = recipientes;
    }

    public boolean isMostrarSegundo() {
        return mostrarSegundo;
    }

    public void setMostrarSegundo(boolean mostrarSegundo) {
        this.mostrarSegundo = mostrarSegundo;
    }
}
