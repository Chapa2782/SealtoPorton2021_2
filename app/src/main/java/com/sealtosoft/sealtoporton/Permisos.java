package com.sealtosoft.sealtoporton;

public class Permisos {
    public String ID;
    public int Clave;
    public String Dispositivo;
    public Boolean habilitado;
    static int postition;

    public Permisos(String ID,Boolean habilitado){
        this.ID = ID;
        this.habilitado = habilitado;
        this.Clave = Clave;
        this.Dispositivo = Dispositivo;
    }
    public Permisos(String ID,Boolean habilitado,int Clave,String Dispositivo){
        this.ID = ID;
        this.habilitado = habilitado;
        this.Clave = Clave;
        this.Dispositivo = Dispositivo;
    }

    public Permisos(){}

    public String getID() {
        return ID;
    }



    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public static int getPostition() {
        return postition;
    }

    public static void setPostition(int postition) {
        Permisos.postition = postition;
    }
}
