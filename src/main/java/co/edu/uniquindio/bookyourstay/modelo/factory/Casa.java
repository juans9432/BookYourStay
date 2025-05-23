package co.edu.uniquindio.bookyourstay.modelo.factory;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Casa extends Alojamiento implements Serializable {


    public Casa(String id, String nombre, String ciudad, String descripcion, float precioNoche, int capacidadMaxima, String imagenAlojamiento, List<String> servicios) {
        super(id, nombre, ciudad, descripcion, precioNoche, capacidadMaxima, imagenAlojamiento, servicios);
    }

    @Override
    public String getTipo() {
        return "casa";
    }



}
