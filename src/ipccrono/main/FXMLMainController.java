/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipccrono.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;

/**
 *
 * @author FMR
 */
public class FXMLMainController implements Initializable {

    @FXML
    private HBox hBar;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Pane pane;
    @FXML
    private Arc progressCircle;
    @FXML
    private Circle innerCircle;
    @FXML
    private Button btn;
    @FXML
    private ImageView imageBtn;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void setBgColor(Color c){
        pane.setStyle("-fx-background-color: rgb("+c.getRed()*255+","+c.getGreen()*255+","+c.getBlue()*255+");");
        innerCircle.setFill(c);
    }

    public Arc getProgressCircle() {
        return progressCircle;
    }

    public Circle getInnerCircle() {
        return innerCircle;
    }

    public Button getBtn() {
       return btn;
    }
}
