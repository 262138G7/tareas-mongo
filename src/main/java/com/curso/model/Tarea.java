package com.curso.model;

// @Document y @Id (de Spring Data MongoDB) se importan de paquetes distintos a los de JPA.
// Maven las ha descargado al declarar spring-boot-starter-data-mongodb en el pom.xml.
// @Id es la de Spring Data (org.springframework.data.annotation), no la de jakarta.persistence.
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Importamos las anotaciones de Lombok. Maven ha descargado la librería al declarar
// org.projectlombok:lombok en el pom.xml. Lombok las lee al compilar y escribe por nosotros
// el código repetitivo, que queda dentro del .class pero no aparece en el .java.
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * S12-1 Demo 2: Documento Tarea
 *
 * Objetivo: guardar tareas en MongoDB como documentos de la colección tareas.
 *
 * Lo que hace este ejemplo:
 *   - Declara la clase Tarea como documento de la colección tareas
 *   - Usa un id de tipo String, que genera MongoDB
 *   - Usa Lombok para los getters, los setters y el constructor vacío, como en S11
 *
 * Concepto clave: una clase Java se convierte en un documento con solo anotar la clase.
 * No hay script de creación ni esquema: la colección se crea al guardar el primer documento.
 */
// @Document indica que los objetos de esta clase se guardan como documentos de MongoDB.
// collection = "tareas" fija el nombre de la colección. Sin él, Spring usaría el nombre de
// la clase en minúscula y en singular ("tarea").
@Document(collection = "tareas")
@Getter
@Setter
@NoArgsConstructor
public class Tarea {

    // @Id marca el campo que se guarda como _id, el identificador del documento.
    // Es un String porque MongoDB genera un ObjectId, un valor de 24 caracteres
    // hexadecimales (por ejemplo "6718f2a1c3b4d5e6f7a8b9c0"). Vale null hasta que se guarda.
    @Id
    private String id;

    private String titulo;

    private String descripcion;

    private boolean completada;

    private List<String> etiquetas = new ArrayList<>();

    // Este constructor sí lo escribimos nosotros: Lombok no sabe qué campos queremos recibir.
    // No incluye el id ni completada: el id lo genera MongoDB y una tarea nueva está pendiente.
    public Tarea(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    // toString() devuelve el texto con el que se imprime la tarea;
    // @Override avisa al compilador de que reescribimos el método heredado de Object.
    @Override
    public String toString() {
        return "Tarea{id=" + id + ", titulo='" + titulo + "', completada=" + completada + "}";
    }
}