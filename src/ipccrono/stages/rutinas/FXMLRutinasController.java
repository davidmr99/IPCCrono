/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipccrono.stages.rutinas;

import ipccrono.stages.rutina.Rutina;
import ipccrono.Main;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.w3c.dom.events.MouseEvent;

/**
 * FXML Controller class
 *
 * @author FMR
 */
public class FXMLRutinasController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private ListView<Rutina> listView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//        listView.setCellFactory(lv -> new ListCelda());
        listView.setCellFactory(new Callback<ListView<Rutina>, ListCell<Rutina>>() {
            @Override
            public ListCell<Rutina> call(ListView<Rutina> param) {
                return new ListCelda();
            }
        });
        listView.getItems().add(new Rutina("Rutina fitness", 3, 20, null, 5));
    }    

    @FXML
    private void cronometro(ActionEvent event) {
        Main.switchScene(Main.MAIN_STAGE);
    }

    @FXML
    private void addRutina(ActionEvent event) {
        Main.switchScene(Main.ADD_EDIT_RUTINA_STAGE);
    }
    
}
class ListCelda extends ListCell<Rutina> {
    @Override
    protected void updateItem(Rutina item, boolean empty) {
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
            
            Button btn2 = new Button();
            Image img2 = new Image("/ipccrono/resources/pencil.png");
            ImageView imgView2 = new ImageView(img2);
            imgView2.setFitHeight(28);
            imgView2.setFitWidth(28);
            btn2.setGraphic(imgView2);
            btn2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Main.switchScene(Main.ADD_EDIT_RUTINA_STAGE);
                }
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
            gp.add(btn, 1, 0);
            gp.add(btn2, 2, 0);
            
//            HBox h = new HBox(new Label(item.getName()),imgView,imgView2);
//            h.setSpacing(5);
//            setGraphic(h);
            setCursor(Cursor.HAND);
            setGraphic(gp);
        }
    }
}