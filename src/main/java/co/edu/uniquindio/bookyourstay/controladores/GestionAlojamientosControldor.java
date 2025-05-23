package co.edu.uniquindio.bookyourstay.controladores;

import co.edu.uniquindio.bookyourstay.modelo.factory.Alojamiento;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GestionAlojamientosControldor implements Initializable, IActualizacion {


    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnEditar;

    @FXML
    private TableColumn<Alojamiento, String> colCiudad;

    @FXML
    private TableColumn<Alojamiento, String> colDescripcion;

    @FXML
    private TableColumn<Alojamiento, String> colId;

    @FXML
    private TableColumn<Alojamiento, String> colHuespedes;

    @FXML
    private TableColumn<Alojamiento, String> colNombre;

    @FXML
    private TableColumn<Alojamiento, String> colpreioPorNoche;

    @FXML
    private TableView<Alojamiento> tablaAlojamientos;

    private final ControladorPrincipal controladorPrincipal;

    @Setter
    private GestionOfertasControlador gestionOfertasControlador;

    private ObservableList<Alojamiento> alojamientoObservableList;

    private Alojamiento alojamientoSeleccionado;

    @FXML
    private TextField txtBusquedaRapida;

    public GestionAlojamientosControldor() {
        this.controladorPrincipal = ControladorPrincipal.getInstancia();
    }

    public FXMLLoader navegarVentana(String nombreArchivoFxml, String tituloVentana) {
        try {

            // Cargar la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombreArchivoFxml));
            Parent root = loader.load();

            // Crear la escena
            Scene scene = new Scene(root);

            // Crear un nuevo escenario (ventana)
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(tituloVentana);

            // Mostrar la nueva ventana
            stage.show();

            return loader;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void irCrearNuevoAlojamiento(ActionEvent actionEvent) {
        FXMLLoader loader = navegarVentana("/nuevoAlojamiento.fxml", "Administrador - crear alojamiento");
        NuevoAlojamientoControlador controlador = loader.getController();
        controlador.setInterfazActualizacion(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().toString()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNombre())));
        colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCiudad()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion().toString()));
        colpreioPorNoche.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", cellData.getValue().getPrecioNoche()))
        );
        colHuespedes.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacidadMaxima()))
        );

        alojamientoObservableList = FXCollections.observableArrayList();
        cargarAlojamientos();

        tablaAlojamientos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean seleccionado = newSel != null;
            btnEliminar.setDisable(!seleccionado);
            btnEditar.setDisable(!seleccionado);
        });

    }


    public void eliminarAlojamiento(ActionEvent e) {
        Alojamiento alojamientoSeleccionado = tablaAlojamientos.getSelectionModel().getSelectedItem();

        if (alojamientoSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de que desea eliminar este alojamiento?");
            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                ControladorPrincipal.getInstancia()
                        .getAdministradorService()
                        .eliminarAlojamientoConOfertas(alojamientoSeleccionado.getId());

                ControladorPrincipal.getInstancia()
                        .mostrarAlerta("Alojamiento eliminado correctamente", Alert.AlertType.INFORMATION);

                cargarAlojamientos(); // Actualizar la tabla después de eliminar
            }
        } else {
            ControladorPrincipal.getInstancia()
                    .mostrarAlerta("Seleccione un alojamiento para eliminar", Alert.AlertType.WARNING);
        }
    }

    /**
     * metodo para cargar los alojamientos
     */
    private void cargarAlojamientos() {
        List<Alojamiento> alojamientos = ControladorPrincipal.getInstancia()
                .getAlojamientoRepositorio().listar();

        alojamientoObservableList.setAll(alojamientos);

        tablaAlojamientos.setItems(alojamientoObservableList);
    }

    @Override
    public void actualizarTabla() {
        cargarAlojamientos();
    }

}

