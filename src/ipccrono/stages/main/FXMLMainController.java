/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipccrono.stages.main;

import ipccrono.Main;
import ipccrono.stages.ejercicios.Ejercicio;
import ipccrono.stages.rutina.Rutina;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
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
    
    private static Rutina rutina = null;
    private int i,t,totalTime;
    private IntervalTimerS timer;
    
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
    @FXML
    private Label nRepeticion;
    @FXML
    private Label rutinaYEjercicio;
    @FXML
    private Label rutinaTime;
    
    private Thread th;
    
    
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
        if(rutina == null) {
            btn.setDisable(true);
            restart.setDisable(true);
            next.setDisable(true);
            progressCircle.lengthProperty().unbind();
            progressCircle.setLength(135);
            
            th = new Thread(task);
            th.setDaemon(true);
            th.start();
            
            nRepeticion.textProperty().unbind();
            nRepeticion.setText("REPETICION 0/0");
            rutinaYEjercicio.textProperty().unbind();
            rutinaYEjercicio.setText("Rutina: -     Ejercicio: -");
            ejercicioTime.textProperty().unbind();
            ejercicioTime.setText("00:00:00");
//            rutinaTime.textProperty().unbind();
rutinaTime.setText("De: 00:00:00");

        }else {
            th.interrupt();
            timer = null;
            timer = new IntervalTimerS();
            timer.restaurarInicio();
            timer.setSesionTipo(rutina);
            ejercicioTime.textProperty().bind(timer.tiempoProperty());
            rutinaYEjercicio.textProperty().bind(timer.ejercicioProperty());
            nRepeticion.textProperty().bind(timer.repeticionProperty());
            
            timer.setOnSucceeded(c -> {
                if (timer != null && timer.getValue() != null && timer.getValue()) {
                    System.out.println("FINISHED!");
                    init();
                    timer.restaurarInicio();
//                    timer = new IntervalTimerS();
//                    timer.setSesionTipo(rutina);
                }
            });
            
            btn.setDisable(false);
            paused = true;
            imageBtn.setImage(playImg);
            restart.setDisable(true);
            next.setDisable(true);
            progressCircle.setStartAngle(90);
            progressCircle.lengthProperty().bind(timer.ejercicioProgressProperty());//setLength(0);
            
//            nRepeticion.setText("REPETICION 1/"+rutina.getRepeticiones());
//            rutinaYEjercicio.setText("Rutina: "+rutina.getName()+ "     Ejercicio: "+rutina.getEjercicios().get(0).getName());
//            Ejercicio ej0 = rutina.getEjercicios().get(0);
//            ejercicioTime.setText(format(Rutina.getH(ej0.getTime()))+":"+format(Rutina.getM(ej0.getTime()))+":"+format(Rutina.getS(ej0.getTime())));
rutinaTime.setText("De: "+ format(Rutina.getH(rutina.getTime()))+":"+format(Rutina.getM(rutina.getTime()))+":"+format(Rutina.getS(rutina.getTime())));
        }
    }
    
    public String format(int n){
        return String.format("%02d", n);
    }
    
    @FXML
    private void action(ActionEvent e) {
        if(e.getSource() == btn && paused) {
            imageBtn.setImage(pauseImg);
            restart.setDisable(true);
            next.setDisable(true);
            play();
            paused = !paused;
            //cambiar a imagen pausado y continuar con el task del coronometro
        } else if(e.getSource() == btn && !paused) {
            imageBtn.setImage(playImg);
            restart.setDisable(false);
            next.setDisable(false);
            pause();
            //cambiar a imagen play y pausar el task
            paused = !paused;
        }else if(e.getSource() == next){
            if(timer.isLast()){
                System.out.println("LAST");
                restart.fire();
            }else{
                System.out.println("next");
                timer.setEjercicioProgress(0d);
                timer.setCambiarEstado(true);
                timer.restart();
            }
        }else if(e.getSource() == restart){
            System.out.println("restart");
            timer.restart();
            init();
//            timer.restaurarInicio();
        }
        
    }
    
    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
        init();
    }
    
//    protected void call() throws Exception{
//        Platform.runLater(new Runnable() {
//            @Override public void run() {
//                int totalTime = rutina.getTime();
//                for(int i = 1; i <= rutina.getRepeticiones();i++){
//                    for(Ejercicio ej : rutina.getEjercicios()){
//                        rutinaYEjercicio.setText("Rutina: "+rutina.getName()+ "     Ejercicio: "+ej.getName());
//                        for(int t = ej.getTime(); t >0;t--){
//                            ejercicioTime.setText(format(Rutina.getH(t))+":"+format(Rutina.getM(t))+":"+format(Rutina.getS(t)));
//                            rutinaTime.setText("De: "+ format(Rutina.getH(totalTime))+":"+format(Rutina.getM(totalTime))+":"+format(Rutina.getS(totalTime)));
//                            totalTime --;
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException ex) {
//                                Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                        }
//                    }
//                    nRepeticion.setText("REPETICION "+i+"/"+rutina.getRepeticiones());
//                }
//            }});
//    }
    
    public boolean isPaused(){
        return paused;
    }
    
    public void play() {
        if(timer.getState() == Worker.State.READY){
            timer.start();
        }else{
            timer.restart();
        }
//        if(th != null && th.isInterrupted()){
//            try {
//                th.join();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }else{
//
//        Task play = new Task() {
//            @Override
//            protected Object call() throws Exception {
//
//                totalTime = rutina.getTime();
//                for(i = 0; i < rutina.getRepeticiones();i++){
//                    for(int o=0;o<rutina.getEjercicios().size();o++){
//
//                        Ejercicio ej = rutina.getEjercicios().get(o);
//
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                rutinaYEjercicio.setText("Rutina: "+rutina.getName()+ "     Ejercicio: "+ej.getName());
//                            }
//                        });
//                        for(t = ej.getTime(); t >0;t--){
//                            Platform.runLater(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ejercicioTime.setText(format(Rutina.getH(t))+":"+format(Rutina.getM(t))+":"+format(Rutina.getS(t)));
//                                    rutinaTime.setText("De: "+ format(Rutina.getH(totalTime))+":"+format(Rutina.getM(totalTime))+":"+format(Rutina.getS(totalTime)));
//                                }
//                            });
//                            totalTime --;
//                            Thread.sleep(1000);
//                        }
//                        //DESCANSO ENTRE EJERCICIOS
//                        if(o != rutina.getEjercicios().size()){
//                            for(t = rutina.getDescansoEjs(); t > 0;t--) {
//                                Platform.runLater(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        rutinaYEjercicio.setText("Rutina: "+rutina.getName()+ "     Descanso ejercicios");
//                                        ejercicioTime.setText(format(Rutina.getH(t))+":"+format(Rutina.getM(t))+":"+format(Rutina.getS(t)));
//                                        rutinaTime.setText("De: "+ format(Rutina.getH(totalTime))+":"+format(Rutina.getM(totalTime))+":"+format(Rutina.getS(totalTime)));
//                                    }
//                                });
//                                totalTime --;
//                                Thread.sleep(1000);
//                            }
//                        }
//                    }
//                    /*DESCANSO ENTRE REPETICIONES*/
//                    for(t = rutina.getDescansoRepet(); t > 0;t--) {
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                rutinaYEjercicio.setText("Rutina: "+rutina.getName()+ "     Descanso repeticiones");
//                                int t = rutina.getDescansoRepet();
//                                ejercicioTime.setText(format(Rutina.getH(t))+":"+format(Rutina.getM(t))+":"+format(Rutina.getS(t)));
//                                rutinaTime.setText("De: "+ format(Rutina.getH(totalTime))+":"+format(Rutina.getM(totalTime))+":"+format(Rutina.getS(totalTime)));
//                            }
//                        });
//                        totalTime --;
//                        Thread.sleep(1000);
//                    }
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            nRepeticion.setText("REPETICION "+(i + 1)+"/"+rutina.getRepeticiones());
//                        }
//                    });
//                }
//                return null;
//            }
//        };
//        th = new Thread(play);
//        th.setDaemon(true);
//        th.start();
//        }
    }
    private void pause(){
        timer.cancel();
//        th.interrupt();
    }
    
    public Rutina getRutina(){
        return rutina;
    }
}
