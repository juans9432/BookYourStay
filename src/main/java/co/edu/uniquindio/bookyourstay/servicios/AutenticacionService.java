package co.edu.uniquindio.bookyourstay.servicios;

import co.edu.uniquindio.bookyourstay.modelo.Administrador;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.Usuario;
import co.edu.uniquindio.bookyourstay.repositorios.UsuarioRepositorio;
import co.edu.uniquindio.bookyourstay.util.EmailUtil;

import java.util.Random;
import java.util.UUID;

public class AutenticacionService {

    private final UsuarioRepositorio usuarioRepositorio;

    public AutenticacionService() {
        this.usuarioRepositorio = new UsuarioRepositorio();
    }


    public AutenticacionService(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    /**
     * metodo para iniciar sesion
     * @param email
     * @param contrasenia
     * @return
     * @throws Exception
     */
    public Usuario iniciarSesion(String email, String contrasenia) throws Exception {
        // Verificar si el usuario es el admin con datos hardcoded
        if ("admin@bookyourstay.com".equals(email) && "admin123".equals(contrasenia)) {
            // Crear y devolver el usuario administrador directamente
            return new Administrador("Administrador", "12345678", "000000000", email, contrasenia, null, null, true);
        }

        // Si no es el admin, intentar obtenerlo desde el repositorio
        Usuario usuario = usuarioRepositorio.obtenerPorEmail(email);

        if (usuario == null) {
            throw new Exception("Usuario no encontrado");
        }

        if (!usuario.getContrasenia().equals(contrasenia)) {
            throw new Exception("Contraseña incorrecta");
        }


        return usuario;
    }

    /**
     * metodo para activar la cuenta
     * @param codigoIngresado
     * @return
     * @throws Exception
     */
    public boolean activarCuenta(String cedula, String codigoIngresado) throws Exception {
        Cliente cliente = (Cliente) usuarioRepositorio.obtenerPorCedula(cedula);

        if (!cliente.getCodigoActivacion().equals(codigoIngresado)) {
            throw new Exception("El código de activación no es válido");
        }

        cliente.setEstadoCuenta(true);
        cliente.setCodigoActivacion(null);
        usuarioRepositorio.actualizar(cliente);
        return true;
    }

    /**
     * metodo para cambiar contraseña
     * @param usuario
     * @param nuevaContrasena
     * @throws Exception
     */
    public void cambiarContrasena(Usuario usuario, String nuevaContrasena) throws Exception {
        if (nuevaContrasena == null || nuevaContrasena.length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres");
        }
        usuario.setContrasenia(nuevaContrasena);
    }

    /**
     * metodo para enviar codigo de recuperacion
     * @param email
     * @throws Exception
     */
    public void enviarCodigoRecuperacion(String email)  {
        Usuario usuario = usuarioRepositorio.obtenerPorEmail(email);

        String codigo = generarCodigo();
        usuario.setCodigoRecuperacion(codigo);

        EmailUtil.enviarNotificacion(usuario.getEmail(),
                "Código para cambiar contraseña - BookYourStay",
                "Hola " + usuario.getNombre() + ",\n\nTu código para cambiar la contraseña es: " + codigo +
                        "\n\nSi no solicitaste esto, ignora este mensaje.\n\nEquipo BookYourStay");
    }

    /**
     * metodo para confimrar el cambio de la contraseña
     * @param email
     * @param codigoIngresado
     * @param nuevaContrasena
     * @throws Exception
     */
    public void confirmarCambioContrasena(String email, String codigoIngresado, String nuevaContrasena) throws Exception {
        Usuario usuario = usuarioRepositorio.obtenerPorEmail(email);

        if (usuario == null) {
            throw new Exception("Usuario no encontrado.");
        }

        if (usuario.getCodigoRecuperacion() == null || !usuario.getCodigoRecuperacion().equals(codigoIngresado)) {
            throw new Exception("El código de verificación no es válido.");
        }

        if (nuevaContrasena == null || nuevaContrasena.length() < 6) {
            throw new Exception("La nueva contraseña debe tener al menos 6 caracteres.");
        }

        usuario.setContrasenia(nuevaContrasena);
        usuario.setCodigoRecuperacion(null); // Código ya usado
    }

    /**
     * metodo que genera el codigo
     * @return
     * @throws Exception
     */
    public String generarCodigo() {

        int codigo = new Random().nextInt(999999);
        String codigoFormateado = String.format("%06d", codigo);

        return codigoFormateado;

    }

    private static final Usuario ADMIN = new Administrador(
            "Administrador",
            "12345678",
            "000000000",
            "admin@bookyourstay.com",
            "admin123",
            null,
            null,
            true
    );

    public Usuario autenticar(String correo, String contrasena) {
        if (ADMIN.getEmail().equals(correo) && ADMIN.getContrasenia().equals(contrasena)) {
            return ADMIN;
        }
        return null;
    }


 }
