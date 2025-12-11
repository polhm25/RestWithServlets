package org.hlanz.servlets;

import org.hlanz.entity.Enemigo;
import org.hlanz.mensaje.EnemigoJsonUtil;
import org.hlanz.repository.EnemigoRepository;

import java.util.List;

public class EnemigoService {
    private EnemigoRepository repository = EnemigoRepository.getInstance();

    // GET - Obtener todos los enemigos
    public String obtenerTodos() {
        List<Enemigo> enemigos = repository.obtenerTodos();
        return EnemigoJsonUtil.enemigosListToJson(enemigos);
    }

    // GET - Obtener un enemigo por ID
    public String obtenerPorId(String id) {
        Enemigo enemigo = repository.obtenerPorId(id);

        if (enemigo != null) {
            return EnemigoJsonUtil.enemigoToJson(enemigo);
        } else {
            return "{\"error\":\"Enemigo no encontrado\",\"status\":404}";
        }
    }

    // POST - Crear un nuevo enemigo
    public String crear(String jsonEnemigo) {
        try {
            Enemigo nuevoEnemigo = EnemigoJsonUtil.jsonToEnemigo(jsonEnemigo);
            Enemigo enemigoCreado = repository.crear(nuevoEnemigo);

            if (enemigoCreado != null) {
                return EnemigoJsonUtil.enemigoToJson(enemigoCreado);
            } else {
                return "{\"error\":\"Error al crear enemigo\",\"status\":500}";
            }
        } catch (Exception e) {
            return "{\"error\":\"Datos inválidos\",\"status\":400}";
        }
    }

    // PUT - Actualizar un enemigo existente
    public String actualizar(String id, String jsonEnemigo) {
        try {
            Enemigo enemigo = EnemigoJsonUtil.jsonToEnemigo(jsonEnemigo);
            Enemigo actualizado = repository.actualizar(id, enemigo);

            if (actualizado != null) {
                return EnemigoJsonUtil.enemigoToJson(actualizado);
            } else {
                return "{\"error\":\"Enemigo no encontrado\",\"status\":404}";
            }
        } catch (Exception e) {
            return "{\"error\":\"Datos inválidos\",\"status\":400}";
        }
    }

    // DELETE - Eliminar un enemigo
    public String eliminar(String id) {
        boolean eliminado = repository.eliminar(id);

        if (eliminado) {
            return "{\"mensaje\":\"Enemigo eliminado correctamente\",\"status\":204}";
        } else {
            return "{\"error\":\"Enemigo no encontrado\",\"status\":404}";
        }
    }
}
