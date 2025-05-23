package co.edu.uniquindio.bookyourstay.repositorios;

import co.edu.uniquindio.bookyourstay.modelo.Factura;
import co.edu.uniquindio.bookyourstay.modelo.Usuario;
import co.edu.uniquindio.bookyourstay.util.Constantes;
import co.edu.uniquindio.bookyourstay.util.Persistencia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorio {

    private final List<Usuario> usuarios;

    public UsuarioRepositorio() {
        this.usuarios = leerDatos();
    }

    public void agregar(Usuario usuario) {
        usuarios.add(usuario);
        guardarDatos(usuarios);
    }

    public void eliminar(Usuario usuario) {
        usuarios.remove(usuario);
        guardarDatos(usuarios);
    }

    public void actualizar(Usuario usuarioActualizado) throws Exception {
        List<Usuario> usuarios = listar();

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCedula().equals(usuarioActualizado.getCedula())) {
                usuarios.set(i, usuarioActualizado);
                break;
            }
        }

        guardarDatos(usuarios); // este método debe sobrescribir el archivo serializado
    }

    public Usuario obtenerPorCedula(String cedula) {
        for (Usuario u : usuarios) {
            if (u.getCedula().equals(cedula)) {
                return u;
            }
        }
        return null;
    }

    public Usuario obtenerPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> listar() {
        return new ArrayList<>(usuarios);
    }

    public void guardarDatos(List<Usuario> datos) {
        try {
            Persistencia.serializarObjeto(Constantes.RUTA_USUARIOS, datos);
        } catch (IOException e) {
            System.err.println("Error guardando usuarios: " + e.getMessage());
        }
    }


    public List<Usuario> leerDatos() {
        try {
            File carpeta = new File("data");
            if (!carpeta.exists()) {
                boolean creada = carpeta.mkdirs();
                if (!creada) {
                    System.err.println("No se pudo crear la carpeta de datos.");
                }
            }
            Object datos = Persistencia.deserializarObjeto(Constantes.RUTA_USUARIOS);
            if (datos != null) {
                return (List<Usuario>) datos;
            }
        } catch (Exception e) {
            System.err.println("Error cargando usuarios: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
