package co.edu.uniquindio.bookyourstay.controladores;

import co.edu.uniquindio.bookyourstay.modelo.Factura;
import co.edu.uniquindio.bookyourstay.modelo.factory.Alojamiento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ReservaControlador implements Initializable {

    @FXML
    private TextField ciudadTextField;

    @FXML
    private DatePicker fechaEntradaPicker;

    @FXML
    private DatePicker fechaSalidaPicker;

    @FXML
    private ListView<Alojamiento> listaAlojamientos;

    @FXML
    private Spinner<Integer> spinnerHuespedes;

    private final ControladorPrincipal controladorPrincipal;

    public ReservaControlador() {
        this.controladorPrincipal = ControladorPrincipal.getInstancia();
    }


    public void buscarAlojamiento(ActionEvent e) {
        try {
            String ciudad = ciudadTextField.getText().trim();

            List<Alojamiento> alojamientosEncontrados;

            if (ciudad.isEmpty()) {
                alojamientosEncontrados = controladorPrincipal.getAlojamientoRepositorio().listar();
            } else {
                alojamientosEncontrados = controladorPrincipal.getAlojamientoRepositorio().obtenerPorCiudad(ciudad);
            }

            if (alojamientosEncontrados == null || alojamientosEncontrados.isEmpty()) {
                controladorPrincipal.mostrarAlerta("No se encontraron alojamientos en esta ciudad", Alert.AlertType.INFORMATION);
            } else {
                ObservableList<Alojamiento> listaObservable = FXCollections.observableArrayList(alojamientosEncontrados);
                listaAlojamientos.setItems(listaObservable);
            }

        } catch (Exception ex) {
            controladorPrincipal.mostrarAlerta("Error buscando alojamientos: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void cargarAlojamientosDisponibles() {
        try {
            List<Alojamiento> alojamientosDisponibles = controladorPrincipal.getAlojamientoRepositorio().listar();
            ObservableList<Alojamiento> listaObservable = FXCollections.observableArrayList(alojamientosDisponibles);
            listaAlojamientos.setItems(listaObservable);
        } catch (Exception e) {
            controladorPrincipal.mostrarAlerta("Error cargando alojamientos disponibles: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarAlojamientosDisponibles();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1);
        spinnerHuespedes.setValueFactory(valueFactory);
    }

    public void realizarReserva(ActionEvent e){
        try{
            Alojamiento alojamiento = listaAlojamientos.getSelectionModel().getSelectedItem();
            LocalDate inicio = fechaEntradaPicker.getValue();
            LocalDate fin = fechaSalidaPicker.getValue();
            int huespedes = spinnerHuespedes.getValue();
            String cedula = controladorPrincipal.getUsuarioActual().getCedula();

            controladorPrincipal.getReservaService().realizarReserva(cedula, alojamiento, inicio, fin, huespedes);
            limpiarCampos();
            controladorPrincipal.mostrarAlerta("reserva creada exitosamente", Alert.AlertType.INFORMATION);

        } catch (Exception ex){
            controladorPrincipal.mostrarAlerta(ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void limpiarCampos(){
        fechaEntradaPicker.setValue(null);
        fechaSalidaPicker.setValue(null);
        spinnerHuespedes.getValueFactory().setValue(1);
    }
}

