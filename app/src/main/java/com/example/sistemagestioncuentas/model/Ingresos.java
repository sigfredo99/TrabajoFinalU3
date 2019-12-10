package com.example.sistemagestioncuentas.model;

import android.widget.DatePicker;

public class Ingresos {
    private String id_ingreso;
    private  String uid;
    private String nomb_Ingreso;
    private String desc_Ingreso;
    private String tipo_cat;
    private String monto_Ingreso;
    private String tipo_Ingreso;
    private String dias_Ingreso;
    private String fecha_Ingreso;

    public Ingresos(String id_ingreso, String uid, String nomb_Ingreso, String desc_Ingreso, String tipo_cat, String monto_Ingreso, String tipo_Ingreso, String dias_Ingreso, String fecha_Ingreso) {
        this.id_ingreso = id_ingreso;
        this.uid = uid;
        this.nomb_Ingreso = nomb_Ingreso;
        this.desc_Ingreso = desc_Ingreso;
        this.tipo_cat = tipo_cat;
        this.monto_Ingreso = monto_Ingreso;
        this.tipo_Ingreso = tipo_Ingreso;
        this.dias_Ingreso = dias_Ingreso;
        this.fecha_Ingreso = fecha_Ingreso;
    }


    public String getId_ingreso() {
        return id_ingreso;
    }

    public void setId_ingreso(String id_ingreso) {
        this.id_ingreso = id_ingreso;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNomb_Ingreso() {
        return nomb_Ingreso;
    }

    public void setNomb_Ingreso(String nomb_Ingreso) {
        this.nomb_Ingreso = nomb_Ingreso;
    }

    public String getDesc_Ingreso() {
        return desc_Ingreso;
    }

    public void setDesc_Ingreso(String desc_Ingreso) {
        this.desc_Ingreso = desc_Ingreso;
    }

    public String getTipo_cat() {
        return tipo_cat;
    }

    public void setTipo_cat(String tipo_cat) {
        this.tipo_cat = tipo_cat;
    }

    public String getMonto_Ingreso() {
        return monto_Ingreso;
    }

    public void setMonto_Ingreso(String monto_Ingreso) {
        this.monto_Ingreso = monto_Ingreso;
    }

    public String getTipo_Ingreso() {
        return tipo_Ingreso;
    }

    public void setTipo_Ingreso(String tipo_Ingreso) {
        this.tipo_Ingreso = tipo_Ingreso;
    }


    public String getDias_Ingreso() {
        return dias_Ingreso;
    }

    public void setDias_Ingreso(String dias_Ingreso) {
        this.dias_Ingreso = dias_Ingreso;
    }

    public String getFecha_Ingreso() {
        return fecha_Ingreso;
    }

    public void setFecha_Ingreso(String fecha_Ingreso) {
        this.fecha_Ingreso = fecha_Ingreso;
    }




    public Ingresos() {
    }


}
