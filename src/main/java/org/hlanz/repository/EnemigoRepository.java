package org.hlanz.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.hlanz.config.MongoDBConnection;
import org.hlanz.entity.Enemigo;

import java.util.ArrayList;
import java.util.List;

public class EnemigoRepository {
    private static EnemigoRepository instance;
    private MongoCollection<Document> collection;

    private EnemigoRepository() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection("enemigos");
    }

    public static EnemigoRepository getInstance() {
        if (instance == null) {
            synchronized (EnemigoRepository.class) {
                if (instance == null) {
                    instance = new EnemigoRepository();
                }
            }
        }
        return instance;
    }

    // Convertir Document de MongoDB a Enemigo
    private Enemigo documentToEnemigo(Document doc) {
        if (doc == null) return null;

        Enemigo enemigo = new Enemigo();
        enemigo.setId(doc.getObjectId("_id"));
        enemigo.setNombre(doc.getString("nombre"));
        enemigo.setGenero(doc.getString("genero"));
        enemigo.setPais(doc.getString("pais"));
        enemigo.setAfiliacion(doc.getString("afiliacion"));
        enemigo.setActivo(doc.getBoolean("activo", false));

        return enemigo;
    }

    // Convertir Enemigo a Document de MongoDB
    private Document enemigoToDocument(Enemigo enemigo) {
        Document doc = new Document();

        if (enemigo.getId() != null) {
            doc.append("_id", enemigo.getId());
        }
        doc.append("nombre", enemigo.getNombre());
        doc.append("genero", enemigo.getGenero());
        doc.append("pais", enemigo.getPais());
        doc.append("afiliacion", enemigo.getAfiliacion());
        doc.append("activo", enemigo.isActivo());

        return doc;
    }

    public List<Enemigo> obtenerTodos() {
        List<Enemigo> enemigos = new ArrayList<>();

        try {
            for (Document doc : collection.find()) {
                enemigos.add(documentToEnemigo(doc));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener enemigos: " + e.getMessage());
        }

        return enemigos;
    }

    public Enemigo obtenerPorId(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            Document doc = collection.find(Filters.eq("_id", objectId)).first();
            return documentToEnemigo(doc);
        } catch (Exception e) {
            System.err.println("Error al obtener enemigo por ID: " + e.getMessage());
            return null;
        }
    }

    public Enemigo crear(Enemigo enemigo) {
        try {
            Document doc = enemigoToDocument(enemigo);
            collection.insertOne(doc);
            enemigo.setId(doc.getObjectId("_id"));
            return enemigo;
        } catch (Exception e) {
            System.err.println("Error al crear enemigo: " + e.getMessage());
            return null;
        }
    }

    public Enemigo actualizar(String id, Enemigo enemigo) {
        try {
            ObjectId objectId = new ObjectId(id);
            Document doc = enemigoToDocument(enemigo);
            doc.remove("_id"); // No actualizar el ID

            collection.replaceOne(Filters.eq("_id", objectId), doc);
            enemigo.setId(objectId);
            return enemigo;
        } catch (Exception e) {
            System.err.println("Error al actualizar enemigo: " + e.getMessage());
            return null;
        }
    }

    public boolean eliminar(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            return collection.deleteOne(Filters.eq("_id", objectId)).getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar enemigo: " + e.getMessage());
            return false;
        }
    }
}
