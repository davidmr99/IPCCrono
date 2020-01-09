/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipccrono.stages.ejercicios;

import ipccrono.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author FMR
 */
public class FXMLEjerciciosController implements Initializable {
    
    @FXML
    private BorderPane mainPane;
    @FXML
    private TextField nameField;
    @FXML
    private TextField durationField;
    @FXML
    private ListView<Ejercicio> ejerciciosListView;
    @FXML
    private Button addEj;
    @FXML
    private Button addNewEj;
    
    private ObservableList<Ejercicio> ejercicios;
    @FXML
    private Button cancel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        addEj.setOnMouseEntered((event) -> {
            addEj.setStyle("-fx-background-color:#ffc849;");
        });
        addEj.setOnMouseExited((event) -> {
            addEj.setStyle("-fx-background-color:orange;");
        });
        cancel.setOnMouseEntered((event) -> {
            cancel.setStyle("-fx-background-color:#ffc849;");
        });
        cancel.setOnMouseExited((event) -> {
            cancel.setStyle("-fx-background-color:orange;");
        });
        
        addNewEj.setOnMouseEntered((event) -> {
            addNewEj.setStyle("-fx-background-color:#ffc849;-fx-border-color:grey;");
        });
        addNewEj.setOnMouseExited((event) -> {
            addNewEj.setStyle("-fx-background-color:#ffd196;-fx-border-color:grey;");
        });
        
        ejercicios = FXCollections.observableArrayList();
        updateButton();
        ejerciciosListView.setCellFactory(new Callback<ListView<Ejercicio>, ListCell<Ejercicio>>() {
            @Override
            public ListCell<Ejercicio> call(ListView<Ejercicio> param) {
                return new ListCelda();
            }
        });
        ejercicios.addAll(new Ejercicio("Abdominales",5),new Ejercicio("Sentadilas",3),new Ejercicio("Flexiones",10));
        nameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                updateButton();
            }
        });
        durationField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    durationField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                updateButton();
            }
        });
//        ejerciciosListView.getItems().addListener(new ListChangeListener() {
//            @Override
//            public void onChanged(ListChangeListener.Change change) {
//                System.out.println("Detected a change! ");
//            }
//        });
addEj.disableProperty().bind(Bindings.isEmpty(ejercicios).or(Bindings.isNull(ejerciciosListView.getSelectionModel().selectedItemProperty())));
ejerciciosListView.setItems(ejercicios);

    }
    
    private void updateButton(){
        if(!nameField.getText().trim().isEmpty() && !durationField.getText().trim().isEmpty()){
            addNewEj.setDisable(false);
        }else{
            addNewEj.setDisable(true);
        }
    }
    
    public void removeEj(Ejercicio e){
        ejercicios.remove(e);
    }
    
    @FXML
    private void add(ActionEvent event) {
        System.out.println("rutinas before save: "+ Main.getRutinasController().getRutinas());
        Main.getRutinaController().addEjercicio(ejerciciosListView.getSelectionModel().getSelectedItem());
        System.out.println("rutinas after save: "+ Main.getRutinasController().getRutinas());
        Main.switchScene(Main.ADD_EDIT_RUTINA_STAGE);
    }
    
    @FXML
    private void cancel(ActionEvent event) {
        Main.switchScene(Main.ADD_EDIT_RUTINA_STAGE);
    }
    
    @FXML
    private void addEjercicio(ActionEvent event) {
        Ejercicio ej = new Ejercicio(nameField.getText(), Integer.parseInt(durationField.getText()));
        ejercicios.add(ej);
        ejerciciosListView.getSelectionModel().select(ej);
        nameField.setText("");
        durationField.setText("");
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
                Main.getEjsController().removeEj(item);
            });
            btn.setStyle("-fx-background-color:orange;");
            
            
            btn.setOnMouseEntered((event) -> {
                btn.setStyle("-fx-background-color:#ffc849;");
            });
            btn.setOnMouseExited((event) -> {
                btn.setStyle("-fx-background-color:orange;");
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