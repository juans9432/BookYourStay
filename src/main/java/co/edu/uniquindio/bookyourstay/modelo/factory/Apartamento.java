package co.edu.uniquindio.bookyourstay.modelo.factory;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.List;

public class Apartamento extends Alojamiento implements Serializable {

    public Apartamento(String id, String nombre, String ciudad, String descripcion, float precioNoche, int capacidadMaxima, String imagenAlojamiento, List<String> servicios) {
        super(id, nombre, ciudad, descripcion, precioNoche, capacidadMaxima, imagenAlojamiento, servicios);
    }

    @Override
    public String getTipo() {
        return "apartamento";
    }




}
