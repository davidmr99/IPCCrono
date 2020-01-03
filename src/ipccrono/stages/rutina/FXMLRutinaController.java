/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipccrono.stages.rutina;

import ipccrono.Main;
import ipccrono.stages.ejercicios.Ejercicio;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author FMR
 */
public class FXMLRutinaController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private TextField nameField;
    @FXML
    private TextField repetField;
    @FXML
    private TextField descRepetField;
    @FXML
    private TextField descEjerField;
    @FXML
    private ListView<Ejercicio> ejerciciosListView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ejerciciosListView.setCellFactory(new Callback<ListView<Ejercicio>, ListCell<Ejercicio>>() {
            @Override
            public ListCell<Ejercicio> call(ListView<Ejercicio> param) {
                return new ListCelda();
            }
        });
    }    

    @FXML
    private void accept(ActionEvent event) {
        Main.switchScene(Main.RUTINAS_STAGE);
    }

    @FXML
    private void cancel(ActionEvent event) {
        Main.switchScene(Main.RUTINAS_STAGE);
    }

    @FXML
    private void addEjercicio(ActionEvent event) {
        Main.switchScene(Main.ADD_SELECT_EJERCICIO);
    }
    
    public void addEjercicio(Ejercicio e){
        ejerciciosListView.getItems().add(e);
    }
    
    public void removeEj(Ejercicio e){
        ejerciciosListView.getItems().remove(e);
    }
}

class ListCelda extends ListCell<Ejercicio> {
    @Override
    protected void updateItem(Ejercicio item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            setGraphic(null);
            setText(null);
            Button btn = new Button();
            Image img = new Image("/ipccrono/resources/cancel.png");
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(28);
            imgView.setFitWidth(28);
            btn.setGraphic(imgView);
            btn.setOnMouseClicked((event) -> {
               Main.getRutinaController().removeEj(item);
            });
            
            GridPane gp = new GridPane();
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(84);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(8);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(8);
            gp.getColumnConstraints().addAll(col1,col2,col3);
            gp.add(new Label(item.getName()), 0, 0);
            gp.add(new Label(item.getTime() + " s"), 1, 0);
            gp.add(btn, 2, 0);
//            HBox h = new HBox(new Label(item.getName()),imgView,imgView2);
//            h.setSpacing(5);
//            setGraphic(h);
            setCursor(Cursor.HAND);
            setGraphic(gp);
        }
    }
}