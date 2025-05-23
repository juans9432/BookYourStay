package co.edu.uniquindio.bookyourstay.repositorios;

import co.edu.uniquindio.bookyourstay.modelo.Factura;
import co.edu.uniquindio.bookyourstay.modelo.Reserva;
import co.edu.uniquindio.bookyourstay.util.Constantes;
import co.edu.uniquindio.bookyourstay.util.Persistencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReservaRepositorio {
    //
    private List<Reserva> reservas;

    public ReservaRepositorio() {
        this.reservas = leerDatos();
    }

    public void agregar(Reserva reserva) {
        reservas.add(reserva);
        guardarDatos(reservas);
    }

    public void eliminar(Reserva reserva) {
        reservas.remove(reserva);
        guardarDatos(reservas);
    }


    public List<Reserva> obtenerPorCliente(String cedulaCliente) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reserva : reservas) {
            if (reserva.getCliente().getCedula().equals(cedulaCliente)) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    public List<Reserva> listar() {
        return new ArrayList<>(reservas);
    }

    public void guardarDatos(List<Reserva> reservas) {
        try {
            Persistencia.serializarObjeto(Constantes.RUTA_RESERVAS, reservas);
        } catch (IOException e) {
            System.err.println("Error guardando reservas: " + e.getMessage());
        }
    }


    public List<Reserva> leerDatos() {
        try {
            Object datos = Persistencia.deserializarObjeto(Constantes.RUTA_RESERVAS);
            if (datos != null) {
                return (List<Reserva>) datos;
            }
        } catch (Exception e) {
            System.err.println("Error cargando reservas: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
