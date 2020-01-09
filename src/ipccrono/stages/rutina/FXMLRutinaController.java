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
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
    @FXML
    private Button accept;
    
    private ObservableList<Ejercicio> ejercicios;
    
    public static boolean editingRutina = false;
    public static int index = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ejercicios = FXCollections.observableArrayList();
        updateButton();
        ejerciciosListView.setCellFactory((ListView<Ejercicio> param) -> new ListCelda());
        ejerciciosListView.getSelectionModel()
               .selectedIndexProperty()
               .addListener((observable, oldvalue, newValue) -> {

        Platform.runLater(() -> {
            ejerciciosListView.getSelectionModel().clearSelection();
        });

    });
        ChangeListener<String> sL = (ObservableValue<? extends String> e, String oldValue, String newValue) -> {
            updateButton();
        };
        
        nameField.textProperty().addListener(sL);
        ejerciciosListView.itemsProperty().addListener((observable) -> {
            System.out.println("list change");
            updateButton();
        });
        
        repetField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                repetField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            updateButton();
        });
        descRepetField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                descRepetField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            updateButton();
        });
        descEjerField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                descEjerField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            updateButton();
        });
        ejerciciosListView.setItems(ejercicios);
    }    

    @FXML
    private void accept(ActionEvent event) {
        System.out.println("rutinas ebfore: "+ Main.getRutinasController().getRutinas());
        if(editingRutina){
            Main.getRutinasController().getRutinas().get(index).update(nameField.getText(), Integer.parseInt(repetField.getText()), Integer.parseInt(descRepetField.getText()), ejercicios, Integer.parseInt(descEjerField.getText()));
            System.out.println("rutinas after edit: "+ Main.getRutinasController().getRutinas());
        }else {
            Main.getRutinasController().getRutinas().add(new Rutina(nameField.getText(), Integer.parseInt(repetField.getText()), Integer.parseInt(descRepetField.getText()), ejercicios, Integer.parseInt(descEjerField.getText())));
            System.out.println("rutinas after save: "+ Main.getRutinasController().getRutinas());
        }
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
        System.out.println("añadiendo ej");
        ejercicios.add(e);
        updateButton();
    }
    
    public void removeEj(Ejercicio e){
        ejercicios.remove(e);
        updateButton();
    }
    
    public void clearData(){
        nameField.setText("");
        repetField.setText("");
        descRepetField.setText("");
        descEjerField.setText("");
        ejercicios = FXCollections.observableArrayList();
        ejerciciosListView.setItems(ejercicios);
    }
    
    public void setData(String name, int nRepeticiones, int descRep, int descEj, ObservableList<Ejercicio> ejsList){
        nameField.setText(name);
        repetField.setText(String.valueOf(nRepeticiones));
        descRepetField.setText(String.valueOf(descRep));
        descEjerField.setText(String.valueOf(descEj));
        
        ListChangeListener<Ejercicio> lCL = (ListChangeListener.Change<? extends Ejercicio> c) -> {
            System.out.println("list change");
            updateButton();
        };
        
        ejsList.addListener(lCL);
        ejercicios = ejsList;
        ejerciciosListView.setItems(ejercicios);
        ejerciciosListView.refresh();
    }
    
    
    private void updateButton(){
        if(!nameField.getText().trim().isEmpty() 
                && !repetField.getText().trim().isEmpty() 
                && !descRepetField.getText().trim().isEmpty() 
                && !descEjerField.getText().trim().isEmpty()
                && !ejercicios.isEmpty()){
            accept.setDisable(false);
        }else{
            accept.setDisable(true);
        }
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
            
            btn.setStyle("-fx-background-color:orange;");
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
            btn.setCursor(Cursor.HAND);
            setGraphic(gp);
        }
    }
}