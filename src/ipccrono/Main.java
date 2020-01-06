/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipccrono;

import ipccrono.stages.ejercicios.FXMLEjerciciosController;
import ipccrono.stages.graficas.FXMLGraficasController;
import ipccrono.stages.main.FXMLMainController;
import ipccrono.stages.rutina.FXMLRutinaController;
import ipccrono.stages.rutinas.FXMLRutinasController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.AudioClip;

public class Main extends Application {
    
    private static Stage stage;
    public final static int MAIN_STAGE = 0, RUTINAS_STAGE = 1, ADD_EDIT_RUTINA_STAGE=2, ADD_SELECT_EJERCICIO = 3, GRAFICAS_STAGE = 4;
    private static Scene mainScene, rutinasScene, rutinaScene, ejerciciosScene, graficasScene;
    
    private static FXMLEjerciciosController ejsController;
    private static FXMLRutinaController rutinaController;
    private static FXMLRutinasController rutinasController;
    private static FXMLMainController mainController;
    private static FXMLGraficasController graficasController;
    
    public static AudioClip endRutina, endEjercicio, endRepeticion, click;
    
    public static final String styleSheet = "/ipccrono/css/styleSheet.css";
    
    @Override
    public void start(Stage st) throws Exception {
        stage = st;
        
        //MAIN WINDOW
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/ipccrono/stages/main/FXMLMain.fxml"));
        Parent main = mainLoader.load();
        mainScene = new Scene(main);
        mainController = mainLoader.getController();
        
        //RUTINAS WINDOW
        FXMLLoader rutinasLoader = new FXMLLoader(getClass().getResource("/ipccrono/stages/rutinas/FXMLRutinas.fxml"));
        Parent rutinas = rutinasLoader.load();
        rutinasScene = new Scene(rutinas);
        rutinasController = rutinasLoader.getController();
        
        //RUTINA WINDOW
        FXMLLoader rutinaLoader = new FXMLLoader(getClass().getResource("/ipccrono/stages/rutina/FXMLRutina.fxml"));
        Parent rutina = rutinaLoader.load();
        rutinaScene = new Scene(rutina);
        rutinaController = rutinaLoader.getController();
        
        //EJERCICIOS WINDOW
        FXMLLoader ejerciciosLoader = new FXMLLoader(getClass().getResource("/ipccrono/stages/ejercicios/FXMLEjercicios.fxml"));
        Parent ejercicios = ejerciciosLoader.load();
        ejerciciosScene = new Scene(ejercicios);
        ejsController = ejerciciosLoader.getController();
        
        //GRAFICAS WINDOW
        FXMLLoader graficasLoader = new FXMLLoader(getClass().getResource("/ipccrono/stages/graficas/FXMLGraficas.fxml"));
        Parent graficas = graficasLoader.load();
        graficasScene = new Scene(graficas);
        graficasController = graficasLoader.getController();
        stage.setScene(graficasScene);
        stage.show();
        
        sceneSetup(mainScene);
        
        String endRutinaS = "/ipccrono/resources/FinRutina.mp3";
        String endEjercicioS = "/ipccrono/resources/FinEjercicio.mp3";
        String endRepeticionS = "/ipccrono/resources/FinRepeticion.mp3";
        String clickS = "/ipccrono/resources/Click.mp3";
        
        endRutina = new AudioClip(getClass().getResource(endRutinaS).toString());
        endEjercicio = new AudioClip(getClass().getResource(endEjercicioS).toString());
        endRepeticion = new AudioClip(getClass().getResource(endRepeticionS).toString());
        click = new AudioClip(getClass().getResource(clickS).toString());
        
    }
    
    public static FXMLMainController getMainController(){
        return mainController;
    }
    
    public static FXMLEjerciciosController getEjsController(){
        return ejsController;
    }
    
    public static FXMLRutinasController getRutinasController(){
        return rutinasController;
    }
    
    public static FXMLRutinaController getRutinaController(){
        return rutinaController;
    }
    
    public static FXMLGraficasController getGraficasController(){
        return graficasController;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void switchScene(int scene){
        switch(scene){
            case MAIN_STAGE:{
                sceneSetup(mainScene);
                break;
            }
            case RUTINAS_STAGE:{
                sceneSetup(rutinasScene);
                break;
            }
            case ADD_EDIT_RUTINA_STAGE:{
                sceneSetup(rutinaScene);
                break;
            }
            case ADD_SELECT_EJERCICIO:{
                sceneSetup(ejerciciosScene);
                break;
            }
            case GRAFICAS_STAGE:{
                sceneSetup(graficasScene);
                break;
            }
        }
    }
    
    public static void sceneSetup(Scene scene){
        scene.getStylesheets().add(styleSheet);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.sizeToScene();
    }
}
