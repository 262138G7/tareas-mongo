package com.curso.controller;

import java.util.List;

// HttpStatus enumera los códigos de estado de HTTP y ResponseEntity representa la respuesta
// completa (estado y cuerpo). Ambas proceden de Spring, que Maven trae con los starters del pom.xml.
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Estas anotaciones proceden de Spring MVC, que Maven trae con spring-boot-starter-webmvc.
import org.springframework.web.bind.annotation.*;

import com.curso.model.Tarea;
import com.curso.repository.TareaRepository;

// RequiredArgsConstructor procede de Lombok, que Maven ha descargado con el pom.xml.
import lombok.RequiredArgsConstructor;

/**
 * S12-1 Demo 2: Controlador REST de tareas sobre MongoDB
 *
 * Objetivo: ofrecer las cinco operaciones del CRUD de tareas como API REST.
 *
 * Lo que hace este ejemplo:
 *   - GET /api/tareas devuelve todas las tareas en JSON
 *   - GET /api/tareas/{id} devuelve una tarea, o 404 si no existe
 *   - POST /api/tareas guarda una tarea nueva y devuelve 201
 *   - PUT /api/tareas/{id} reemplaza una tarea existente, o devuelve 404
 *   - DELETE /api/tareas/{id} borra la tarea y devuelve 204, o 404 si no existe
 *
 * Concepto clave: el controlador es el mismo que con JPA. Solo cambia el tipo del id, que es
 * un String: el repositorio oculta si por debajo hay una tabla o una colección.
 */
@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaController {

    // final = el campo se asigna una sola vez, en el constructor que genera Lombok.
    private final TareaRepository tareaRepository;

    /**
     * Devuelve todas las tareas.
     *
     * Ejemplo:
     *   GET /api/tareas  →  [{"id":"6718...","titulo":"Instalar MongoDB", ...}, ...]
     */
    @GetMapping
    public List<Tarea> listar(@RequestParam(required = false) String etiqueta) {
        if (etiqueta == null) {
            return tareaRepository.findAll();
        }
        return tareaRepository.findByEtiquetas(etiqueta);
    }
    /**
     * Devuelve una tarea por su id.
     *
     * Ejemplo:
     *   GET /api/tareas/6718f2a1c3b4d5e6f7a8b9c0  →  200 y la tarea
     *   GET /api/tareas/no-existe                 →  404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtener(@PathVariable String id) {
        // findById() devuelve un Optional. map(ResponseEntity::ok) lo convierte en un 200 con
        // la tarea si existe; orElse() devuelve el 404 si el Optional está vacío.
        return tareaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Guarda una tarea nueva.
     *
     * Ejemplo:
     *   POST /api/tareas  {"titulo": "Comprar café"}  →  201 y la tarea con su id
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tarea crear(@RequestBody Tarea tarea) {
        // Anulamos el id recibido: save() inserta si el id es null y reemplaza el documento
        // si ya existe, y aquí queremos que siempre cree una tarea nueva.
        tarea.setId(null);
        return tareaRepository.save(tarea);
    }

    /**
     * Reemplaza una tarea existente.
     *
     * Ejemplo:
     *   PUT /api/tareas/6718...  {"titulo": "Comprar té", "completada": true}  →  200 y la tarea
     *   PUT /api/tareas/no-existe  {...}                                       →  404
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable String id, @RequestBody Tarea tarea) {
        // existsById() comprueba que haya un documento con ese id, para devolver 404 si no.
        if (!tareaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Ponemos en la tarea el id de la ruta: save() reemplaza el documento que tiene ese _id.
        tarea.setId(id);
        return ResponseEntity.ok(tareaRepository.save(tarea));
    }

    /**
     * Borra una tarea por su id.
     *
     * Ejemplo:
     *   DELETE /api/tareas/6718...      →  204 (borrada, sin cuerpo)
     *   DELETE /api/tareas/no-existe    →  404 (no existe)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        // existsById() devuelve true si hay un documento con ese id. Lo comprobamos antes de borrar
        // para poder distinguir "borrada" (204) de "no existía" (404).
        if (!tareaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tareaRepository.deleteById(id);
        // 204 = hecho y sin cuerpo.
        return ResponseEntity.noContent().build();
    }
}