/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipccrono.stages.main;

import ipccrono.Main;
import static ipccrono.stages.main.IntervalTimerS.EstadoSesion.*;
import ipccrono.stages.rutina.Rutina;
import java.time.Duration;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

/**
 *
 * @author jose
 */
public class IntervalTimerS extends Service<Boolean> {
    
    void setStartTimings() {
        startTimeRutina = System.currentTimeMillis();
        stoppedDurationRutina = 0;
        stoppedTimeRutina = startTimeRutina;
    }
    
    void setStoppedDuration(long stpDuration) {
        stoppedDuration = stpDuration;
    }
    
    void setStoppedDurationRutina(long stpDuration) {
        stoppedDurationRutina = stpDuration;
    }
    
    enum EstadoSesion {
        PREPARADO, TRABAJO, DESCANSO_EJERCICIO, DESCANSO_CIRCUITO, TERMINADO
    };
    HashMap<EstadoSesion, Duration> durations;
    private static long currentTime = 0; // guarda la hora del instante actual
    private static long startTime = 0;// guarda la hora del instante inicial del intervalo
    private static long startTimeRutina = 0;
    private static long stoppedTimeRutina = 0;
    private static long stoppedDurationRutina = 0;
    private static long stoppedDuration = 0;// guarda la duracion del tiempo que hemos estdo detenidos
    private static Long stoppedTime;
    
    private int ejercicioActual = 0;
    private int circuitoActual = 0;
    private Rutina rutina;
    
    private EstadoSesion estadoActual = PREPARADO;
    
    
    // cuando se activa a true y se lanza la task solo se cambia de estado
    private boolean cambiarEstado = false;
    
    public long getMillisTrans(){
        return millisTrans;
    }
    
    /**
     * Get the value of canviarEstado
     *
     * @return the value of canviarEstado
     */
    public boolean isCambiarEstado() {
        return cambiarEstado;
    }
    
    /**
     * Set the value of cambiarEstado
     *
     * @param cambiarEstado new value of canviarEstado
     */
    public void setCambiarEstado(boolean cambiarEstado) {
        if (!this.isRunning()) {
            this.cambiarEstado = cambiarEstado;
        }
    }
    
    // propiedad donde se muestra el tiempo transcurrido
    private StringProperty tiempo = new SimpleStringProperty();
    private StringProperty tiempoRemaining = new SimpleStringProperty();
    private StringProperty rutinaLabel = new SimpleStringProperty();
    private StringProperty ejercicioLabel = new SimpleStringProperty();
    private StringProperty repeticion = new SimpleStringProperty();
    private DoubleProperty ejercicioProgress = new SimpleDoubleProperty(0);
    
    public String getTiempo() {
        return tiempo.get();
    }
    
    public void setTiempo(StringProperty value) {
        tiempo = value;
    }
    
    public String getTiempoRemaining() {
        return tiempoRemaining.get();
    }
    
    public void setTiempoRemaining(StringProperty value) {
        tiempoRemaining = value;
    }
    
    public String getRutina() {
        return rutinaLabel.get();
    }
    
    public void setRutina(StringProperty value) {
        rutinaLabel = value;
    }
    
    public String getEjercicio() {
        return ejercicioLabel.get();
    }
    
    public void setEjercicio(StringProperty value) {
        ejercicioLabel = value;
    }
    
    public Double getEjercicioProgress() {
        return ejercicioProgress.get();
    }
    
    public void setEjercicioProgress(double value) {
        ejercicioProgress.set(value);
    }
    
    public String getRepeticion() {
        return repeticion.get();
    }
    
    public void setRepeticion(StringProperty value) {
        repeticion = value;
    }
    
    public StringProperty tiempoProperty() {
        return tiempo;
    }
    
    public StringProperty tiempoRemainingProperty() {
        return tiempoRemaining;
    }
    
    public StringProperty rutinaProperty() {
        return rutinaLabel;
    }
    
    public StringProperty ejercicioProperty() {
        return ejercicioLabel;
    }
    
    public StringProperty repeticionProperty() {
        return repeticion;
    }
    
    public DoubleProperty ejercicioProgressProperty() {
        return ejercicioProgress;
    }
    
    public EstadoSesion getEstadoActual(){
        return estadoActual;
    }
    
    public void setSesionTipo(Rutina st) {
        rutina = st;
        
        Duration descEjs = Duration.ofSeconds(rutina.getDescansoEjs());
        Duration descRepet = Duration.ofSeconds(rutina.getDescansoRepet());
        
        durations = new HashMap<>();
        
        durations.put(DESCANSO_EJERCICIO, descEjs);
        durations.put(DESCANSO_CIRCUITO, descRepet);
        Platform.runLater(() -> {
            Duration time = Duration.ofSeconds(rutina.getEjercicios().get(0).getTime());
            tiempo.setValue(String.format("%02d", time.toHours()) + ":" + String.format("%02d", time.minusMinutes(time.toHours()*60).toMinutes()) + ":" + String.format("%02d", time.minusSeconds(time.toMinutes()*60).getSeconds()));
            Duration timeRemaining = Duration.ofSeconds(rutina.getTime());
            tiempoRemaining.setValue("De: "+String.format("%02d", timeRemaining.toHours()) + ":" + String.format("%02d", timeRemaining.minusMinutes(timeRemaining.toHours()*60).toMinutes()) + ":" + String.format("%02d", timeRemaining.minusSeconds(timeRemaining.toMinutes()*60).getSeconds()));
            rutinaLabel.setValue("Rutina: "+rutina.getName());
            ejercicioLabel.setValue("Ejercicio: "+rutina.getEjercicios().get(0).getName());
            repeticion.setValue("REPETICION 1/"+rutina.getRepeticiones());
            ejercicioProgress.setValue(0);
            millisTrans = time.toMillis();
            millisOriginal = millisTrans;
        });
    }
    
    long millisTrans = 0,millisOriginal=0;
    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            
            //actualiza el estado actual, Es invocado cuando ha finalizado un intervalo -duraciones-
            // retorna TRUE si ha finalizado la sesion
            // si cambiamos de estado y estamos detenidos en mitad de un intervalo desaparece
            boolean cambiaEstado() {
                // rest de los tiempos acumulados y el inicial del intervalo
                // el estado puede ser detenido o en marcha, hay que comprobar
                startTime = currentTime;
                stoppedDuration = 0;
                switch (estadoActual) {
                    case TRABAJO:
                        if (ejercicioActual < rutina.getEjercicios().size()-1) {
//                            Main.endEjercicio.play();
                            estadoActual = DESCANSO_EJERCICIO;
                            ejercicioActual++;
                            Platform.runLater(() -> {
                                rutinaLabel.setValue("Rutina: "+rutina.getName());
                                ejercicioLabel.setValue("Descanso ejercicio");
                                Main.getMainController().getProgressCircle().setFill(Color.ORANGE);
                            });
                        } else if (circuitoActual < rutina.getRepeticiones()-1){
//                            Main.endRepeticion.play();
                            estadoActual = DESCANSO_CIRCUITO;
                            circuitoActual++;
                            ejercicioActual = 0;
                            Platform.runLater(() -> {
                                rutinaLabel.setValue("Rutina: "+rutina.getName());
                                ejercicioLabel.setValue("Descanso repetición");
                                Main.getMainController().getProgressCircle().setFill(Color.ORANGE);
                            });
                        } else {
//                            Main.endRutina.play();
                            //parar la sesion
                            estadoActual = TERMINADO;
                            Platform.runLater(() -> {
                                rutinaLabel.setValue("Rutina: "+rutina.getName());
                                ejercicioLabel.setValue("Ejercicio: "+rutina.getEjercicios().get(0).getName());
                                ejercicioProgress.setValue(0);
                            });
                            return true;
                        }
                        break;
                    case DESCANSO_EJERCICIO:
//                            Main.endEjercicio.play();
                        estadoActual = TRABAJO;
                        Platform.runLater(() -> {
                            rutinaLabel.setValue("Rutina: "+rutina.getName());
                            ejercicioLabel.setValue("Ejercicio: "+rutina.getEjercicios().get(ejercicioActual).getName());
                            Main.getMainController().getProgressCircle().setFill(Color.DODGERBLUE);
                        });
                        break;
                    case DESCANSO_CIRCUITO:
//                            Main.endEjercicio.play();
                        estadoActual = TRABAJO;
                        Platform.runLater(() -> {
                            rutinaLabel.setValue("Rutina: "+rutina.getName());
                            ejercicioLabel.setValue("Ejercicio: "+rutina.getEjercicios().get(ejercicioActual).getName());
                            repeticion.setValue("REPETICION " + (circuitoActual+1) + "/"+rutina.getRepeticiones());
                                Main.getMainController().getProgressCircle().setFill(Color.DODGERBLUE);
                        });
                }
                Platform.runLater(() -> {
                    
                    Duration time;
                    if(estadoActual == TRABAJO){
                        time = Duration.ofSeconds(rutina.getEjercicios().get(ejercicioActual).getTime());
                    }else {
                        time = durations.get(estadoActual);
                    }
                    millisTrans = time.toMillis();
                    millisOriginal = millisTrans;
                    tiempo.setValue(String.format("%02d", time.toHours()) + ":" + String.format("%02d", time.minusMinutes(time.toHours()*60).toMinutes()) + ":" + String.format("%02d", time.minusSeconds(time.toMinutes()*60).getSeconds()));
                    
                    
                Long totalElapsedTime = (currentTime - startTimeRutina) - stoppedDurationRutina + Main.getMainController().getEjTimeStoped() + Main.getMainController().getDescTimeStoped();
                Duration duration2 = Duration.ofMillis(totalElapsedTime);
                Duration totalRemaining = Duration.ofSeconds(rutina.getTime());
                final Long horasElapsed = duration2.toHours();
                final Long minutosElapsed = duration2.minusMinutes(horasElapsed*60).toMinutes();
                final Long segundosElapsed = duration2.minusSeconds(minutosElapsed*60 + horasElapsed * 3600).getSeconds();
                 
                
                    long finalHours = totalRemaining.minusHours(horasElapsed).toHours();
                    long finalMins = totalRemaining.minusMinutes(totalRemaining.toHours()*60 + minutosElapsed).toMinutes();
                    long finalSecs = totalRemaining.minusSeconds(totalRemaining.toMinutes()*60 + segundosElapsed).getSeconds();
                    
                    if(finalSecs < 0){
                        finalSecs += 60;
                        finalMins -= 1;
                    }
                    
                    if(finalMins < 0){
                        finalMins += 60;
                        finalHours -=1;
                    }
                    
                    if(finalHours < 0){
                        finalHours = 0;
                    }
                    
                    tiempoRemaining.setValue("De: "+String.format("%02d", finalHours) + ":" + String.format("%02d", finalMins) + ":" + String.format("%02d", finalSecs));
                });
                
                return false;
            }
            
            // calcula el tiempo del intervalo actual, si se ha cumplido invoca a cambiaEstado
            // retorna TRUE si ha finalizado la sesion
            boolean calcula() {
                
                currentTime = System.currentTimeMillis();
                Long totalTime = (currentTime - startTime) - stoppedDuration;
                Duration duration = Duration.ofMillis(totalTime);
                
                final Long horas = duration.toHours();
                final Long minutos = duration.minusMinutes(horas*60).toMinutes();
                final Long segundos = duration.minusSeconds(minutos*60 + horas * 3600).getSeconds();
                
                Long totalElapsedTime = (currentTime - startTimeRutina) - stoppedDurationRutina + Main.getMainController().getEjTimeStoped() + Main.getMainController().getDescTimeStoped();
                Duration duration2 = Duration.ofMillis(totalElapsedTime);
                Duration totalRemaining = Duration.ofSeconds(rutina.getTime());
                final Long horasElapsed = duration2.toHours();
                final Long minutosElapsed = duration2.minusMinutes(horasElapsed*60).toMinutes();
                final Long segundosElapsed = duration2.minusSeconds(minutosElapsed*60 + horasElapsed * 3600).getSeconds();
                
                Platform.runLater(() -> {
                    Duration tiempoEj;
                    if(estadoActual == TRABAJO){
                        tiempoEj = Duration.ofSeconds(rutina.getEjercicios().get(ejercicioActual).getTime());
                    }else {
                        if(durations.keySet().contains(estadoActual)){
                            tiempoEj = durations.get(estadoActual);
                        }else{
                            tiempoEj = Duration.ofSeconds(rutina.getEjercicios().get(0).getTime());
                        }
                    }
//                    System.out.println("-----------------------------------------------");
//                    System.out.println("current: "+currentTime+" , started: "+startTime+" ,stoped: "+stoppedDuration);
//                    System.out.println("tiempoEjH:" + tiempoEj.toHours()+" , horas: "+horas+" , tiempoejM: "+(tiempoEj.toMinutes()-tiempoEj.toHours()*60)+" , minutos: "+minutos+" , tiempoEjsegundos: "+(tiempoEj.getSeconds()-tiempoEj.toMinutes()*60)+" , segundos: "+segundos);
//                    System.out.println("-----------------------------------------------");
                    
                    tiempo.setValue(String.format("%02d", tiempoEj.minusHours(horas).toHours()) + ":" + String.format("%02d", tiempoEj.minusMinutes(tiempoEj.toHours()*60 + minutos).toMinutes()) + ":" + String.format("%02d", tiempoEj.minusSeconds(tiempoEj.toMinutes()*60 + segundos).getSeconds()));
                    
                    long finalHours = totalRemaining.minusHours(horasElapsed).toHours();
                    long finalMins = totalRemaining.minusMinutes(totalRemaining.toHours()*60 + minutosElapsed).toMinutes();
                    long finalSecs = totalRemaining.minusSeconds(totalRemaining.toMinutes()*60 + segundosElapsed).getSeconds();
                    
                    if(finalSecs < 0){
                        finalSecs += 60;
                        finalMins -= 1;
                    }
                    
                    if(finalMins < 0){
                        finalMins += 60;
                        finalHours -=1;
                    }
                    
                    if(finalHours < 0){
                        finalHours = 0;
                    }
                    
                    tiempoRemaining.setValue("De: "+String.format("%02d", finalHours) + ":" + String.format("%02d", finalMins) + ":" + String.format("%02d", finalSecs));
                });
                    
                if(estadoActual == TRABAJO){
                    if (duration.compareTo(Duration.ofSeconds(rutina.getEjercicios().get(ejercicioActual).getTime())) >= 0) {
                        if(ejercicioActual == rutina.getEjercicios().size()-1 && circuitoActual == rutina.getRepeticiones()-1){
                            Main.endRutina.play();
                        }else{
                            Main.endEjercicio.play();
                        }
                        return cambiaEstado();
                    } else {
                        return false;
                    }
                }else {
                    if (duration.compareTo(durations.get(estadoActual)) >= 0) {
                        switch(estadoActual){
                            case DESCANSO_CIRCUITO:
                                Main.endRepeticion.play();
                                break;
                            case DESCANSO_EJERCICIO:
                                Main.endEjercicio.play();
                                break;
                        }
                        return cambiaEstado();
                    } else {
                        return false;
                    }
                }
            }
            
            @Override
            protected Boolean call() {
                if (estadoActual == TERMINADO) {
                    return true;
                }
                // si estamos parados y queremos adelantar el estado se invoca al servicio
                if (cambiarEstado) {
                    cambiarEstado = false;
                    return cambiaEstado();
                }
                // si no estabamos detenidos es el arranque del servicio
                if (stoppedTime == null) {
                    startTime = currentTime = System.currentTimeMillis();
                    //                    if (firstTime) {
                    if (estadoActual == PREPARADO) { // es lla primera vez que arrancamos el servicio
                        ejercicioActual = 0;
                        circuitoActual = 0;
                        estadoActual = TRABAJO;
                    }
                } else { // estabamos detenidos y nos ponemos en marcha sin cambio de estado
                    Long elapsedTime = System.currentTimeMillis() - stoppedTime;
                    stoppedDuration = stoppedDuration + elapsedTime;
                    System.out.println("PARADOS: "+elapsedTime);
                    stoppedDurationRutina += elapsedTime;
                    stoppedTime = null;
                }
                short n=0;
                while (true) {
                    if(n==10){
                        n=0;
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        if (isCancelled()) {
                            break;
                        }
                    }
                    if(n==0){
                        if (isCancelled()) {
                            break;
                        }
                        if (calcula()) {
                            return true;
                        }
                    }
                    if(Main.getMainController().getRutina()!= null && !Main.getMainController().isPaused()){
                        switch(estadoActual){
                                case TRABAJO:
                                    System.out.println("Ejercicio: "+rutina.getEjercicios().get(ejercicioActual) + " miliisO: "+ millisOriginal+"  millisTr: "+millisTrans);
                                    break;
                                case DESCANSO_CIRCUITO:
                                    System.out.println("desc circuito: "+durations.get(estadoActual) + " miliisO: "+ millisOriginal+"  millisTr: "+millisTrans);
                                    break;
                                case DESCANSO_EJERCICIO:
                                    System.out.println("Desc ejerc: "+durations.get(estadoActual) + " miliisO: "+ millisOriginal+"  millisTr: "+millisTrans);
                                    break;
                            }
                        if(millisTrans>=10){
                            
                            millisTrans-=10;
                        }
                        Platform.runLater(()->{
                            double val = 360 - (Double.valueOf(millisTrans) / Double.valueOf(millisOriginal))*360;
                            ejercicioProgress.setValue(val);
                        });
                    }
                    
                    n++;

                }
                return false;
            }
            
            @Override
            protected void cancelled() {
                super.cancelled();
                
                stoppedTime = System.currentTimeMillis();
            }
            
            @Override
            protected void succeeded() {
                stoppedTimeRutina = System.currentTimeMillis();
            }
            
        };
    }
    
    public void setStoppedTime(Long l){
        stoppedTime = l;
    }
    
    public long getStoppedDuration(){
        return stoppedDurationRutina / 1000;
    }
    
    public long getExercisedDuration(){
        return ((stoppedTimeRutina - startTimeRutina) - stoppedDurationRutina) / 1000;
    }
}
