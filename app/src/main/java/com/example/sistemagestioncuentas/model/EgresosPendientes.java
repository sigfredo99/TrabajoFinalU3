package com.example.sistemagestioncuentas.model;

public class EgresosPendientes {
    private String id_EgresoPendiente;
    private String uid;
    private String nomb_EgresoP;
    private String desc_EgresoP;
    private String tipo_catP;
    private String monto_EgresoP;
    private String tipo_EgresoP;
    private String dias;
    private String fechaInicial;
    private String fecha_Final;
    private String fechaPago;


    public EgresosPendientes() {
    }


    public EgresosPendientes(String id_EgresoPendiente, String uid, String nomb_EgresoP, String desc_EgresoP, String tipo_catP, String monto_EgresoP, String tipo_EgresoP, String dias, String fechaInicial, String fecha_Final, String fechaPago) {
        this.id_EgresoPendiente = id_EgresoPendiente;
        this.uid = uid;
        this.nomb_EgresoP = nomb_EgresoP;
        this.desc_EgresoP = desc_EgresoP;
        this.tipo_catP = tipo_catP;
        this.monto_EgresoP = monto_EgresoP;
        this.tipo_EgresoP = tipo_EgresoP;
        this.dias = dias;
        this.fechaInicial = fechaInicial;
        this.fecha_Final = fecha_Final;
        this.fechaPago = fechaPago;
    }



    public String getId_EgresoPendiente() {
        return id_EgresoPendiente;
    }

    public void setId_EgresoPendiente(String id_EgresoPendiente) {
        this.id_EgresoPendiente = id_EgresoPendiente;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNomb_EgresoP() {
        return nomb_EgresoP;
    }

    public void setNomb_EgresoP(String nomb_EgresoP) {
        this.nomb_EgresoP = nomb_EgresoP;
    }

    public String getDesc_EgresoP() {
        return desc_EgresoP;
    }

    public void setDesc_EgresoP(String desc_EgresoP) {
        this.desc_EgresoP = desc_EgresoP;
    }

    public String getTipo_catP() {
        return tipo_catP;
    }

    public void setTipo_catP(String tipo_catP) {
        this.tipo_catP = tipo_catP;
    }

    public String getMonto_EgresoP() {
        return monto_EgresoP;
    }

    public void setMonto_EgresoP(String monto_EgresoP) {
        this.monto_EgresoP = monto_EgresoP;
    }

    public String getTipo_EgresoP() {
        return tipo_EgresoP;
    }

    public void setTipo_EgresoP(String tipo_EgresoP) {
        this.tipo_EgresoP = tipo_EgresoP;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFecha_Final() {
        return fecha_Final;
    }

    public void setFecha_Final(String fecha_Final) {
        this.fecha_Final = fecha_Final;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }










}
