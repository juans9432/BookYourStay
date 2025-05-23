package co.edu.uniquindio.bookyourstay.repositorios;

import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.factory.Alojamiento;
import co.edu.uniquindio.bookyourstay.util.Constantes;
import co.edu.uniquindio.bookyourstay.util.Persistencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlojamientoRepositorio {

    private List<Alojamiento> alojamientos;

    public AlojamientoRepositorio() {
        this.alojamientos = leerDatos();
    }

    public void agregar(Alojamiento alojamiento) {
        alojamientos.add(alojamiento);
        guardarDatos(alojamientos);
    }



    public void eliminar(Alojamiento alojamiento) {
        alojamientos.remove(alojamiento);
        guardarDatos(alojamientos);
    }

    public Alojamiento obtenerPorId(String id) {
        for (Alojamiento a : alojamientos) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }


    public List<Alojamiento> obtenerPorCiudad(String ciudad){
        List<Alojamiento> alojamientosCiudad = new ArrayList<>();
        for (Alojamiento a : alojamientos) {
            if (a.getCiudad().equals(ciudad)) {
                alojamientosCiudad.add(a);
            }
        }
        return alojamientosCiudad;
    }

    public List<Alojamiento> listar() {
        return new ArrayList<>(alojamientos);
    }

    public void guardarDatos(List<Alojamiento> alojamientos) {
        try {
            Persistencia.serializarObjeto(Constantes.RUTA_ALOJAMIENTOS, alojamientos);
        } catch (IOException e) {
            System.err.println("Error guardando alojamientos: " + e.getMessage());
        }
    }


    public List<Alojamiento> leerDatos() {
        try {
            Object datos = Persistencia.deserializarObjeto(Constantes.RUTA_ALOJAMIENTOS);
            if (datos != null) {
                return (List<Alojamiento>) datos;
            }
        } catch (Exception e) {
            System.err.println("Error cargando alojamientos: " + e.getMessage());
        }
        return new ArrayList<>();
    }




}
