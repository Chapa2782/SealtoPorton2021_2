package com.sealtosoft.sealtoporton;

public class Permisos {
    public String ID;
    public int Clave;
    public String Dispositivo;
    public Boolean habilitado;

    public Permisos(String ID,Boolean habilitado){
        this.ID = ID;
        this.habilitado = habilitado;
    }

    public Permisos(){}

}
