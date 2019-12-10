package com.example.sistemagestioncuentas.model;



public class IngresosPendientes {

    private String id_ingresoPendiente;
    private String uid;
    private String nomb_IngresoP;
    private String desc_IngresoP;
    private String tipo_catP;
    private String monto_IngresoP;
    private String tipo_IngresoP;
    private String dias;
    private String fechaInicial;
    private String fecha_Final;
    private String fechaPago;





    public IngresosPendientes() {
    }


    public String getId_ingresoPendiente() {
        return id_ingresoPendiente;
    }

    public void setId_ingresoPendiente(String id_ingresoPendiente) {
        this.id_ingresoPendiente = id_ingresoPendiente;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNomb_IngresoP() {
        return nomb_IngresoP;
    }

    public void setNomb_IngresoP(String nomb_IngresoP) {
        this.nomb_IngresoP = nomb_IngresoP;
    }

    public String getDesc_IngresoP() {
        return desc_IngresoP;
    }

    public void setDesc_IngresoP(String desc_IngresoP) {
        this.desc_IngresoP = desc_IngresoP;
    }

    public String getTipo_catP() {
        return tipo_catP;
    }

    public void setTipo_catP(String tipo_catP) {
        this.tipo_catP = tipo_catP;
    }

    public String getMonto_IngresoP() {
        return monto_IngresoP;
    }

    public void setMonto_IngresoP(String monto_IngresoP) {
        this.monto_IngresoP = monto_IngresoP;
    }

    public String getTipo_IngresoP() {
        return tipo_IngresoP;
    }

    public void setTipo_IngresoP(String tipo_IngresoP) {
        this.tipo_IngresoP = tipo_IngresoP;
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



    public IngresosPendientes(String id_ingresoPendiente, String uid, String nomb_IngresoP, String desc_IngresoP, String tipo_catP, String monto_IngresoP, String tipo_IngresoP, String dias, String fechaInicial, String fecha_Final, String fechaPago) {
        this.id_ingresoPendiente = id_ingresoPendiente;
        this.uid = uid;
        this.nomb_IngresoP = nomb_IngresoP;
        this.desc_IngresoP = desc_IngresoP;
        this.tipo_catP = tipo_catP;
        this.monto_IngresoP = monto_IngresoP;
        this.tipo_IngresoP = tipo_IngresoP;
        this.dias = dias;
        this.fechaInicial = fechaInicial;
        this.fecha_Final = fecha_Final;
        this.fechaPago = fechaPago;
    }





}
