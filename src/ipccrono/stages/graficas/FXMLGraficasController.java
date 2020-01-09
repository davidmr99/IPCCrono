/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipccrono.stages.graficas;

import ipccrono.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author JoseluXtv
 */
public class FXMLGraficasController implements Initializable {
    
    @FXML
    private HBox hBar;
    @FXML
    private PieChart tarta;
    @FXML
    private StackedBarChart<String, Number> barras;
    
    double ejercicioReal;
    double ejercicioTeorico;
    double descansoReal;
    double descansoTeorico;
    @FXML
    private Button boton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tarta.setTitle("Rutina");
        
        boton.setOnMouseEntered((event) -> {
            boton.setStyle("-fx-background-color:#ffc849;");
        });
        boton.setOnMouseExited((event) -> {
            boton.setStyle("-fx-background-color:orange;");
        });
        //Diagrama de barras
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        barras.setTitle("Comparación teórico contra real");
        x.setLabel("Tiempo");
        y.setLabel("Teórico/Real");
    }
    
    public void setup(){
        //Diagrama de tarta
        tarta.setData(getTartaData(ejercicioReal, descansoReal));
        
        
        XYChart.Series trabajo = new XYChart.Series();
        trabajo.setName("Trabajo");
        trabajo.getData().add(new XYChart.Data("Teórico", ejercicioTeorico));
        trabajo.getData().add(new XYChart.Data("Real", ejercicioReal));
        
        XYChart.Series descanso = new XYChart.Series();
        descanso.setName("Descanso");
        descanso.getData().add(new XYChart.Data("Teórico", descansoTeorico));
        descanso.getData().add(new XYChart.Data("Real", descansoReal));
        
        barras.setAnimated(true);
        barras.getData().addAll(trabajo, descanso);
    }
    
    private ObservableList<Data> getTartaData(double ejercicio, double descanso) {
        ObservableList<Data> answer = FXCollections.observableArrayList();
        answer.addAll(
                new PieChart.Data("Ejercicio", ejercicio),
                new PieChart.Data("Descanso", descanso));
        return answer;
    }
    
    public void setData(double ejReal, double ejTeorico, double descReal, double descTeorico){
        ejercicioReal = ejReal;
        ejercicioTeorico = ejTeorico;
        descansoReal = descReal;
        descansoTeorico = descTeorico;
        System.out.println("ejercicioReal: "+ ejercicioReal+ " ejercicioTeorico: "+ejercicioTeorico+" descansoReal: "+descansoReal+" descansoTeorico: "+descansoTeorico);
        setup();
    }
    public void clear(){
        barras.setAnimated(false);
        barras.getData().clear();
        barras.setAnimated(true);
    }

    @FXML
    private void volver(ActionEvent event) {
        Main.switchScene(Main.MAIN_STAGE);
    }
    
}
