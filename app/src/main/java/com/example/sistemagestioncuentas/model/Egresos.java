package com.example.sistemagestioncuentas.model;

public class Egresos {
    private String id_egreso;
    private String uid;
    private String nomb_Egreso;
    private String desc_Egreso;
    private String tipo_cat;
    private String monto_Egreso;
    private String tipo_Egreso;
    private String dias_Egreso;
    private String fecha_Egreso;

    public Egresos() {
    }
    public String getId_egreso() {
        return id_egreso;
    }

    public void setId_egreso(String id_egreso) {
        this.id_egreso = id_egreso;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNomb_Egreso() {
        return nomb_Egreso;
    }

    public void setNomb_Egreso(String nomb_Egreso) {
        this.nomb_Egreso = nomb_Egreso;
    }

    public String getDesc_Egreso() {
        return desc_Egreso;
    }

    public void setDesc_Egreso(String desc_Egreso) {
        this.desc_Egreso = desc_Egreso;
    }

    public String getTipo_cat() {
        return tipo_cat;
    }

    public void setTipo_cat(String tipo_cat) {
        this.tipo_cat = tipo_cat;
    }

    public String getmonto_Egreso() { return monto_Egreso;
    }

    public void setmonto_Egreso(String nomto_Egreso) {
        this.monto_Egreso = nomto_Egreso;
    }

    public String getTipo_egreso() {
        return tipo_Egreso;
    }

    public void setTipo_egreso(String tipo_Egreso) {
        this.tipo_Egreso = tipo_Egreso;
    }

    public String getDias_Egreso() {
        return dias_Egreso;
    }

    public void setDias_Egreso(String dias_Egreso) {
        this.dias_Egreso = dias_Egreso;
    }

    public String getFecha_Egreso() {
        return fecha_Egreso;
    }

    public void setFecha_Egreso(String fecha_Egreso) {
        this.fecha_Egreso = fecha_Egreso;
    }




    public Egresos(String id_egreso, String uid, String nomb_Egreso, String desc_Egreso, String tipo_cat, String monto_Egreso, String tipo_Egreso, String dias_Egreso, String fecha_Egreso) {
        this.id_egreso = id_egreso;
        this.uid = uid;
        this.nomb_Egreso = nomb_Egreso;
        this.desc_Egreso = desc_Egreso;
        this.tipo_cat = tipo_cat;
        this.monto_Egreso = monto_Egreso;
        this.tipo_Egreso = tipo_Egreso;
        this.dias_Egreso = dias_Egreso;
        this.fecha_Egreso = fecha_Egreso;
    }
}
