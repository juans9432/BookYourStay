package co.edu.uniquindio.bookyourstay.controladores;

import co.edu.uniquindio.bookyourstay.modelo.factory.Alojamiento;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class AlojamientosRentablesControlador implements Initializable {
    @FXML
    private TableColumn<Alojamiento, String> ciudad;

    @FXML
    private TableColumn<Alojamiento, String> descripcion;

    @FXML
    private TableColumn<Alojamiento, String> huespedes;

    @FXML
    private TableColumn<Alojamiento, String> id;

    @FXML
    private TableColumn<Alojamiento, String> nombre;

    @FXML
    private TableColumn<Alojamiento, String> precio;

    @FXML
    private TableView<Alojamiento> tablaAlojamientosPopulares;

    private final ControladorPrincipal controladorPrincipal;

    private ObservableList<Alojamiento> alojamientosRObservableList;

    public AlojamientosRentablesControlador() {
        this.controladorPrincipal = ControladorPrincipal.getInstancia();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        nombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        ciudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCiudad()));
        descripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        precio.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrecioNoche())));
        huespedes.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacidadMaxima())));

        alojamientosRObservableList = FXCollections.observableArrayList();
        cargarAlojamientosP();
    }

    private void cargarAlojamientosP() {
        alojamientosRObservableList.setAll(controladorPrincipal.getAdministradorService().obtenerAlojamientosMasRentables());
        tablaAlojamientosPopulares.setItems(alojamientosRObservableList);
    }
}
