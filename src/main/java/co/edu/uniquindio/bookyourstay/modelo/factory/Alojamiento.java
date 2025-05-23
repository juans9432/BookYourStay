package co.edu.uniquindio.bookyourstay.modelo.factory;

import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public abstract class Alojamiento implements Serializable {

    private String id, nombre, ciudad, descripcion;
    public float precioNoche;
    private int capacidadMaxima;
    private String imagenAlojamiento;
    private List<String> servicios;

    public abstract String getTipo();

    /**
     * metodo toString
     * @return
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + nombre;
    }

    public Image getImagenAlojamiento() {
        if (imagenAlojamiento != null) {
            File file = new File(imagenAlojamiento);
            if (file.exists()) {
                return new Image(file.toURI().toString());
            }
        }
        return null;
    }
}


