package com.example.dws.Repositories;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Shop;
import org.springframework.stereotype.Repository;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Repository
public class CommentRepository {
    private HashMap<Long, Comment> coments = new HashMap<>();  // Usamos Long como clave en el HashMap
    private AtomicLong nextId = new AtomicLong(0); // Para generar IDs únicos para las tiendas

    public Collection<Comment> findAll() {
        return coments.values();
    }

    // Encontrar un comentario por su ID
    public Comment findById(long id) {
        return coments.get(id);
    }

    // Guardar o actualizar un comentario
    public void save(Comment comment) {
        this.coments.put(comment.getCommentId(), comment);  // Guardar o actualizar el comentario en el mapa
    }

    // Eliminar un comentario por su ID
    public void deleteById(long id) {
        this.coments.remove(id);  // Eliminar la tienda
    }
}
