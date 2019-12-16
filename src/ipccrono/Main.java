/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipccrono;

import ipccrono.main.FXMLMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author FMR
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ipccrono/main/FXMLMain.fxml"));
        
        Parent root = loader.load();
        FXMLMainController controller = loader.getController();
        Scene scene = new Scene(root);
        
        
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            resize(newValue,controller);
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            resize(scene.getWidth(),controller);
        });
        controller.setBgColor(Color.CYAN);
        resize(scene.getWidth(),controller);

        stage.setScene(scene);
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void resize(Number n, FXMLMainController controller){
        
        double w = n.doubleValue()/4;
        
        controller.getProgressCircle().setCenterX(w);
        controller.getProgressCircle().setCenterY(w);
        controller.getInnerCircle().setCenterX(w);
        controller.getInnerCircle().setCenterY(w);
        //        controller.getButton().setCenterX(w);
        //        controller.getButton().setCenterY(w);

        controller.getProgressCircle().setRadiusX(w);
        controller.getProgressCircle().setRadiusY(w);
        controller.getInnerCircle().setRadius(w - 0.25 * w);
        //        controller.getButton().setRadius(w - 0.4 * w);

        controller.getBtn().setPrefSize((w - 0.4 * w) * 2, (w - 0.4 * w) * 2);
        controller.getBtn().setLayoutX(w - 0.6 * w);
        controller.getBtn().setLayoutY(w - 0.6 * w);
        controller.getBtn().setStyle("-fx-background-radius:" + (w - 0.4 * w) * 2 + "px;");
    }
    
}
