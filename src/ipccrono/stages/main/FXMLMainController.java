/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipccrono.stages.main;

import ipccrono.Main;
import ipccrono.stages.rutina.Rutina;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;

/**
 *
 * @author FMR
 */
public class FXMLMainController implements Initializable {
    
    private Rutina rutina = null;
    
    @FXML
    private Pane pane;
    @FXML
    private Arc progressCircle;
    @FXML
    private Circle innerCircle;
    @FXML
    private Button btn;
    private ImageView menuButton;
    
    private boolean menuShown = false;
    private Pane menu;
    @FXML
    private BorderPane mainPane;
    @FXML
    private HBox hBar;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView imageBtn;
    @FXML
    private Label ejercicioTime;
    @FXML
    private Button restart;
    @FXML
    private Button next;
    
    private boolean paused = true;
    
    private Image playImg,pauseImg;
    
    Task<Long> task = new Task<Long>() {
        @Override
        protected Long call() throws Exception {
            double angle = 0.0;
            while (rutina == null) {
                if (isCancelled()) {
                    break;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                progressCircle.setStartAngle(angle);
                angle += 1;
            }
            return 0l;
        }
    };
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playImg = new Image(getClass().getResourceAsStream("/ipccrono/resources/play.png"));
        pauseImg = new Image(getClass().getResourceAsStream("/ipccrono/resources/pausa.png"));
        init();
    }
    
//    public void setBgColor(Color c){
//        pane.setStyle("-fx-background-color: rgb("+c.getRed()*255+","+c.getGreen()*255+","+c.getBlue()*255+");");
//        innerCircle.setFill(c);
//    }
    
    public Arc getProgressCircle() {
        return progressCircle;
    }
    
    public Circle getInnerCircle() {
        return innerCircle;
    }
    
    public Button getBtn() {
        return btn;
    }
    
    private void menu(ActionEvent event) {
        if(menuShown){
            AnchorPane.setTopAnchor(menu, 460d);
            AnchorPane.setBottomAnchor(menu, null);
            menuButton.setRotate(270);
        }else{
            AnchorPane.setBottomAnchor(menu, 0d);
            AnchorPane.setTopAnchor(menu, null);
            menuButton.setRotate(90);
        }
        menuShown = !menuShown;
    }
    
    @FXML
    private void rutinas(ActionEvent event) throws Exception{
        Main.switchScene(Main.RUTINAS_STAGE);
    }
    
    public void init(){
        if(rutina == null) {
            btn.setDisable(true);
            restart.setDisable(true);
            next.setDisable(true);
            progressCircle.setLength(135);
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();
            
        }else {
            btn.setDisable(false);
            restart.setDisable(false);
            next.setDisable(false);
            progressCircle.setLength(0);
        }
    }
    
    @FXML
    private void action(ActionEvent e) {
        if(e.getSource() == btn && paused) {
            imageBtn.setImage(pauseImg);
            //cambiar a imagen pausado y continuar con el task del coronometro
        } else if(e.getSource() == btn && !paused) {
            imageBtn.setImage(playImg);
            //cambiar a imagen play y pausar el task
        }
        paused = !paused;
    }
}
