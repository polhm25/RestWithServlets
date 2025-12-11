package org.hlanz.entity;

import org.bson.types.ObjectId;

public class Enemigo {
    private ObjectId id;
    private String nombre;
    private String genero;
    private String pais;
    private String afiliacion;
    private boolean activo;

    // Constructor vacío
    public Enemigo() {}

    // Constructor completo
    public Enemigo(ObjectId id, String nombre, String genero, String pais, String afiliacion, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.pais = pais;
        this.afiliacion = afiliacion;
        this.activo = activo;
    }

    // Getters y Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getIdAsString() {
        return id != null ? id.toHexString() : null;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getAfiliacion() { return afiliacion; }
    public void setAfiliacion(String afiliacion) { this.afiliacion = afiliacion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
