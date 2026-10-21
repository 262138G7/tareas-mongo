package com.curso.repository;

// MongoRepository procede de Spring Data MongoDB, que Maven trae con spring-boot-starter-data-mongodb.
import org.springframework.data.mongodb.repository.MongoRepository;

// Importamos el documento que gestiona este repositorio.
import com.curso.model.Tarea;

import java.util.List;

/**
 * S12-1 Demo 2: Repositorio de tareas
 *
 * Objetivo: disponer de las operaciones de guardar, buscar y borrar tareas en MongoDB sin escribirlas.
 *
 * Lo que hace este ejemplo:
 *   - Declara una interfaz vacía que extiende MongoRepository
 *   - Hereda save(), findAll(), findById(), existsById() y deleteById()
 *
 * Concepto clave: el repositorio se declara igual que con JPA. Cambia la interfaz de la que
 * se hereda (MongoRepository en lugar de JpaRepository) y el tipo del id (String en lugar de Long).
 */
// MongoRepository<Tarea, String> = documento que gestiona (Tarea) y tipo de su id (String).
public interface TareaRepository extends MongoRepository<Tarea, String> {
    List<Tarea> findByEtiquetas(String etiqueta);
}