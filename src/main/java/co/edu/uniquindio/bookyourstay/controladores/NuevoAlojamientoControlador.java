package co.edu.uniquindio.bookyourstay.controladores;

import co.edu.uniquindio.bookyourstay.modelo.factory.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.factory.AlojamientoFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NuevoAlojamientoControlador implements Initializable {

    @FXML
    private Spinner<Integer> capacidadSpinner;

    @FXML
    private TextField txtCiudad;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private ImageView imagenPreview;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;


    @FXML
    private ComboBox<String> comboTipo;

    @FXML
    private VBox vboxServicios;

    @FXML
    private Label lblServicios;

    @FXML
    private TableColumn<Alojamiento, String> colCiudad;

    @FXML
    private TableColumn<Alojamiento, String> colDescripcion;

    @FXML
    private TableColumn<Alojamiento, String> colId;

    @FXML
    private TableColumn<Alojamiento, String> colNombre;

    @FXML
    private TableColumn<Alojamiento, String> colpreioPorNoche;

    @FXML
    private TableView<Alojamiento> tablaAlojamientos;

    private IActualizacion iActualizacion;
    private File archivoImagenSeleccionada;


    private final ControladorPrincipal controladorPrincipal;

    public NuevoAlojamientoControlador() {
        this.controladorPrincipal = ControladorPrincipal.getInstancia();
    }

    public void setInterfazActualizacion(IActualizacion iActualizacion){
        this.iActualizacion = iActualizacion;
    }



    public void registrarAlojamiento(ActionEvent actionEvent) {
        try {

            float precio = Float.parseFloat(txtPrecio.getText());

            String rutaImagen = null;
            if (archivoImagenSeleccionada != null) {
                rutaImagen = copiarImagen(archivoImagenSeleccionada);
            }

            List<String> serviciosSeleccionados = new ArrayList<>();
            for (Node node : vboxServicios.getChildren()) {
                if (node instanceof CheckBox checkBox && checkBox.isSelected()) {
                    serviciosSeleccionados.add(checkBox.getText());
                }
            }

            controladorPrincipal.getAdministradorService().registrarAlojamiento(
                    comboTipo.getValue(), // Tipo como String
                    txtNombre.getText(),
                    txtCiudad.getText(),
                    txtDescripcion.getText(),
                    precio,
                    capacidadSpinner.getValue(),
                    rutaImagen,
                    serviciosSeleccionados
            );

            controladorPrincipal.mostrarAlerta("Alojamiento registrado correctamente", Alert.AlertType.INFORMATION);
            iActualizacion.actualizarTabla();
            limpiarFormulario();

        } catch (Exception e) {
            controladorPrincipal.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * metodo para seleccionar una imagen
     * @param e
     */
    public void seleccionarImagen(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) imagenPreview.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            archivoImagenSeleccionada = file; // Guarda el archivo
            Image imagen = new Image(file.toURI().toString());
            imagenPreview.setImage(imagen);
        }
    }


    private void limpiarFormulario() {
        comboTipo.setValue(null);
        txtNombre.setText("");
        txtCiudad.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        capacidadSpinner.getValueFactory().setValue(1);
        imagenPreview.setImage(null);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        comboTipo.getItems().addAll(controladorPrincipal.getAlojamientoService().obtenerTiposAlojamiento());
        comboTipo.setOnAction(event -> {
            String tipoSeleccionado = comboTipo.getValue();
            vboxServicios.getChildren().clear();  // Limpiar servicios anteriores

            try {
                List<String> servicios = AlojamientoFactory.obtenerServiciosPorTipo(tipoSeleccionado);

                for (String servicio : servicios) {
                    CheckBox checkBox = new CheckBox(servicio);
                    vboxServicios.getChildren().add(checkBox);
                }
            } catch (Exception e) {
                Label errorLabel = new Label("Seleccione un tipo");
                vboxServicios.getChildren().add(errorLabel);
            }
        });

    }
    public String copiarImagen(File imagenOriginal) {
        try {
            File carpetaImagenes = new File("data/imagenes");
            if (!carpetaImagenes.exists()) {
                carpetaImagenes.mkdirs();
            }

            String nombreArchivo = imagenOriginal.getName();

            // Evitar sobrescribir
            File destino = new File(carpetaImagenes, nombreArchivo);
            if (destino.exists()) {
                String nombreSinExtension = nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.'));
                String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.'));
                nombreArchivo = nombreSinExtension + "_" + System.currentTimeMillis() + extension;
                destino = new File(carpetaImagenes, nombreArchivo);
            }

            Files.copy(imagenOriginal.toPath(), destino.toPath());
            return "data/imagenes/" + nombreArchivo;

        } catch (IOException e) {
            System.err.println("Error copiando imagen: " + e.getMessage());
            return null;
        }
    }
}

