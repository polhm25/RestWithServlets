package org.hlanz.mensaje;

import org.hlanz.entity.Enemigo;

import java.util.List;

public class EnemigoJsonUtil {
    public static String enemigoToJson(Enemigo e) {
        String id = e.getId() != null ? "\"" + e.getIdAsString() + "\"" : "null";
        return String.format(
                "{\"id\":%s,\"nombre\":\"%s\",\"genero\":\"%s\",\"pais\":\"%s\",\"afiliacion\":\"%s\",\"activo\":%b}",
                id,
                escaparJson(e.getNombre()),
                escaparJson(e.getGenero()),
                escaparJson(e.getPais()),
                escaparJson(e.getAfiliacion()),
                e.isActivo()
        );
    }

    public static String enemigosListToJson(List<Enemigo> enemigos) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < enemigos.size(); i++) {
            json.append(enemigoToJson(enemigos.get(i)));
            if (i < enemigos.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    public static Enemigo jsonToEnemigo(String json) {
        Enemigo enemigo = new Enemigo();

        enemigo.setNombre(extraerValor(json, "nombre"));
        enemigo.setGenero(extraerValor(json, "genero"));
        enemigo.setPais(extraerValor(json, "pais"));
        enemigo.setAfiliacion(extraerValor(json, "afiliacion"));

        String activoStr = extraerValor(json, "activo");
        if (activoStr != null) {
            enemigo.setActivo(Boolean.parseBoolean(activoStr));
        }

        return enemigo;
    }

    private static String extraerValor(String json, String clave) {
        String patron = "\"" + clave + "\"";
        int inicio = json.indexOf(patron);
        if (inicio == -1) return null;

        inicio = json.indexOf(":", inicio) + 1;
        int fin;

        json = json.substring(inicio).trim();

        if (json.startsWith("\"")) {
            inicio = 1;
            fin = json.indexOf("\"", inicio);
            return json.substring(inicio, fin);
        } else {
            fin = json.indexOf(",");
            if (fin == -1) fin = json.indexOf("}");
            return json.substring(0, fin).trim();
        }
    }

    private static String escaparJson(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
