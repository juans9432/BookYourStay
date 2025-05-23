package co.edu.uniquindio.bookyourstay.modelo.factory;

import javafx.scene.image.Image;

import java.util.List;

public class AlojamientoFactory {

    public static Alojamiento crearAlojamiento(String tipo, String id, String nombre, String ciudad,
                                               String descripcion, float precioNoche, int capacidadMaxima, String imagenAlojamiento, List<String> servicios) throws Exception {
        switch (tipo.toLowerCase()){
            case "casa":
                return new Casa(id, nombre, ciudad, descripcion, precioNoche, capacidadMaxima, imagenAlojamiento, servicios);
            case "apartamento":
                return new Apartamento(id, nombre, ciudad, descripcion, precioNoche, capacidadMaxima, imagenAlojamiento, servicios);
            case "hotel":
                return new Hotel(id, nombre, ciudad, descripcion, precioNoche, capacidadMaxima, imagenAlojamiento, servicios);
            default:
                throw new Exception("tipo de alojamiento no valido");
        }
    }

    public static List<String> obtenerServiciosPorTipo(String tipo) throws Exception {
        switch (tipo.toLowerCase()) {
            case "casa":
                return List.of("Parrilla", "Patio", "Cocina equipada");
            case "apartamento":
                return List.of("WiFi", "Aire acondicionado", "Ascensor");
            case "hotel":
                return List.of("Servicio a la habitación", "Desayuno incluido", "Piscina");
            default:
                throw new Exception("Tipo de alojamiento no válido");
        }
    }

    public static List<String> obtenerTiposAlojamiento() {
        return List.of("Hotel", "Apartamento", "Casa");
    }
}
