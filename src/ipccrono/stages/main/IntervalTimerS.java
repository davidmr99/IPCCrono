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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author jose
 */
public class IntervalTimerS extends Service<Boolean> {
    
    enum EstadoSesion {
        PREPARADO, TRABAJO, DESCANSO_EJERCICIO, DESCANSO_CIRCUITO, TERMINADO
    };
    HashMap<EstadoSesion, Duration> durations;
    private static final int DELAY = 1000; // no despertamos cada segundo
    //tiempos
    private static long currentTime = 0; // guarda la hora del instante actual
    private static long startTime = 0;// guarda la hora del instante inicial del intervalo
    private static long stoppedDuration = 0;// guarda la duracion del tiempo que hemos estdo detenidos
    private static Long stoppedTime;
    
    private int ejercicioActual = 1;
    private int circuitoActual = 1;
    private Rutina rutina;
    
    private EstadoSesion estadoActual = PREPARADO;
    
    // cuando se activa a true y se lanza la task solo se cambia de estado
    private boolean cambiarEstado = false;
    
    
                Thread progress = new Thread(){
                    @Override
                    public void run() {
                        while(true){
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(IntervalTimerS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if(Main.getMainController().getRutina()!= null && !Main.getMainController().isPaused()){
                                if(millisTrans>0){
                                    millisTrans--;
                                }
                                Platform.runLater(()->{
                                    System.out.println("millisTrans: "+millisTrans+"  orign: "+millisOriginal);
                                    double val = 360 - (Double.valueOf(millisTrans) / Double.valueOf(millisOriginal))*360;
                                ejercicioProgress.setValue(val);
                                    System.out.println(val);
                                
//                                System.out.println("hey runninf");
                                });
                            }
                        }
                    }
                    
                };
    
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
    private StringProperty ejercicio = new SimpleStringProperty();
    private StringProperty repeticion = new SimpleStringProperty();
    private DoubleProperty ejercicioProgress = new SimpleDoubleProperty(0);
    
    public String getTiempo() {
        return tiempo.get();
    }
    
    public void setTiempo(StringProperty value) {
        tiempo = value;
    }
    
    public String getEjercicio() {
        return ejercicio.get();
    }
    
    public void setEjercicio(StringProperty value) {
        ejercicio = value;
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
    
    public StringProperty ejercicioProperty() {
        return ejercicio;
    }
    
    public StringProperty repeticionProperty() {
        return repeticion;
    }
    
    public DoubleProperty ejercicioProgressProperty() {
        return ejercicioProgress;
    }
    
//    public IntervalTimerS(){
//        if(durations != null){
//            durations.clear();
//        }
//        currentTime = 0; // guarda la hora del instante actual
//        startTime = 0;// guarda la hora del instante inicial del intervalo
//        stoppedDuration = 0;// guarda la duracion del tiempo que hemos estdo detenidos
//        stoppedTime = 0L;
//        
//        ejercicioActual = 1;
//        circuitoActual = 1;
//        rutina = null;
//        
//        estadoActual = PREPARADO;
//        
//        // cuando se activa a true y se lanza la task solo se cambia de estado
//        cambiarEstado = false;
//        
//    }
    
    public void setSesionTipo(Rutina st) {
        rutina = st;
        
        Duration descEjs = Duration.ofSeconds(rutina.getDescansoEjs());
        Duration descRepet = Duration.ofSeconds(rutina.getDescansoEjs());
        
        durations = new HashMap<EstadoSesion, Duration>();
//        durations.put(TRABAJO, rutina.);
//        for(Ejercicio ej:rutina.getEjercicios()){
//        Duration ejDur = Duration.ofSeconds(ej.getTime());
//            durations.put(getEj(ej), ejDur)
//        }
durations.put(DESCANSO_EJERCICIO, descEjs);
durations.put(DESCANSO_CIRCUITO, descRepet);
Platform.runLater(() -> {
    int time = rutina.getEjercicios().get(0).getTime();
    tiempo.setValue(String.format("%02d", Rutina.getH(time)) + ":" + String.format("%02d", Rutina.getM(time)) + ":" + String.format("%02d", Rutina.getS(time)));
    ejercicio.setValue("Rutina: "+rutina.getName()+ "     Ejercicio: "+rutina.getEjercicios().get(0).getName());
    repeticion.setValue("REPETICION 1/"+rutina.getRepeticiones());
    ejercicioProgress.setValue(0);
    millisTrans = time*1000;
    millisOriginal = millisTrans;
});
    }
    
    public void restaurarInicio() {
        if (!this.isRunning()) {
            stoppedDuration = 0;
            ejercicioActual = 1;
            circuitoActual = 1;
            estadoActual = PREPARADO;
            stoppedTime = null;
            Platform.runLater(() -> {
                int time = rutina.getEjercicios().get(0).getTime();
                tiempo.setValue(String.format("%02d", Rutina.getH(time)) + ":" + String.format("%02d", Rutina.getM(time)) + ":" + String.format("%02d", Rutina.getS(time)));
                ejercicio.setValue("Rutina: "+rutina.getName()+ "     Ejercicio: "+rutina.getEjercicios().get(0).getName());
                repeticion.setValue("REPETICION 1/"+rutina.getRepeticiones());
                ejercicioProgress.setValue(0);
            });
        }
    }
    
    long millisTrans = 0,millisOriginal=0;
    int time=0;
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
                stoppedTime = null;
                switch (estadoActual) {
                    case TRABAJO:
                        if (ejercicioActual < rutina.getEjercicios().size()) {
                            estadoActual = DESCANSO_EJERCICIO;
                            ejercicioActual++;
                            Platform.runLater(() -> {
                                ejercicio.setValue("Rutina: "+rutina.getName()+ "     Ejercicio: Descanso ejercicio");
                            });
                        } else if (circuitoActual < rutina.getRepeticiones()){
                            estadoActual = DESCANSO_CIRCUITO;
                            circuitoActual++;
                            ejercicioActual = 1;
                            Platform.runLater(() -> {
                                ejercicio.setValue("Rutina: "+rutina.getName()+ "     Ejercicio: Descanso repeticion");
                            });
                        } else {
                            //parar la sesion
                            estadoActual = TERMINADO;
                            Platform.runLater(() -> {
                                ejercicio.setValue("Rutina: "+rutina.getName()+ "     Ejercicio: "+rutina.getEjercicios().get(0).getName());
                                ejercicioProgress.setValue(0);
                            });
                            return true;
                        }
                        break;
                    case DESCANSO_EJERCICIO:
                        estadoActual = TRABAJO;
                        Platform.runLater(() -> {
                            ejercicio.setValue("Rutina: "+rutina.getName()+ "     Ejercicio: "+rutina.getEjercicios().get(ejercicioActual - 1).getName());
                        });
                        break;
                    case DESCANSO_CIRCUITO:
                        estadoActual = TRABAJO;
                        Platform.runLater(() -> {
                            ejercicio.setValue("Rutina: "+rutina.getName()+ "     Ejercicio: "+rutina.getEjercicios().get(ejercicioActual - 1).getName());
                            repeticion.setValue("REPETICION " + circuitoActual + "/"+rutina.getRepeticiones());
                        });
                }
                Platform.runLater(() -> {
                    
                    if(estadoActual == TRABAJO){
                        time = rutina.getEjercicios().get(ejercicioActual - 1).getTime();
                    }else {
                        time = (int)durations.get(estadoActual).getSeconds();
                    }
                millisTrans = time*1000;
                millisOriginal = millisTrans;
                    tiempo.setValue(String.format("%02d", Rutina.getH(time)) + ":" + String.format("%02d", Rutina.getM(time)) + ":" + String.format("%02d", Rutina.getS(time)));
                });
//                task
//if(System.nanoTime()%(1000000)==0){
//                        millisTrans--;
//                        ejercicioProgress.setValue((millisTrans % millisOriginal)*360);
//                    }
return false;
            }
            
            // calcula el tiempo del intervalo actual, si se ha cumplido invoca a cambiaEstado
            // retorna TRUE si ha finalizado la sesion
            boolean calcula() {
                
                currentTime = System.currentTimeMillis();
                Long totalTime = (currentTime - startTime) - stoppedDuration;
                Duration duration = Duration.ofMillis(totalTime);
//                millisTrans = duration.getSeconds()*1000;
//                progress.start();
                final Long horas = duration.toHours();
                final Long minutos = duration.toMinutes();
                final Long segundos = duration.minusMinutes(minutos).getSeconds();
                
                Platform.runLater(() -> {
                    int tiempoEj;
                    if(estadoActual == TRABAJO){
                        tiempoEj = rutina.getEjercicios().get(ejercicioActual-1).getTime();
                    }else {
                        tiempoEj = (int)durations.get(estadoActual).getSeconds();
                    }
                    tiempo.setValue(String.format("%02d", Rutina.getH(tiempoEj) - horas) + ":" + String.format("%02d", Rutina.getM(tiempoEj) - minutos) + ":" + String.format("%02d", Rutina.getS(tiempoEj) - segundos));
                });
                if(estadoActual == TRABAJO){
                    if (duration.compareTo(Duration.ofSeconds(rutina.getEjercicios().get(ejercicioActual-1).getTime())) >= 0) {
                        return cambiaEstado();
                    } else {
                        return false;
                    }
                }else {
                    if (duration.compareTo(durations.get(estadoActual)) >= 0) {
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
                        ejercicioActual = 1;
                        circuitoActual = 1;
                        estadoActual = TRABAJO;
                    }
                } else { // estabamos detenidos y nos ponemos en marcha sin cambio de estado
                    Long elapsedTime = System.currentTimeMillis() - stoppedTime;
                    stoppedDuration = stoppedDuration + elapsedTime;
                    stoppedTime = null;
                }
                short n=0;
                while (true) {
                    if(n==10){
                        n=0;
                    }
                    if(Main.getMainController().getRutina()!= null && !Main.getMainController().isPaused()){
                                if(millisTrans>0){
                                    millisTrans-=10;
                                }
                                Platform.runLater(()->{
//                                    System.out.println("millisTrans: "+millisTrans+"  orign: "+millisOriginal);
                                    double val = 360 - (Double.valueOf(millisTrans) / Double.valueOf(millisOriginal))*360;
                                    ejercicioProgress.setValue(val);
//                                    System.out.println(val);
                                
//                                System.out.println("hey runninf");
                                });
                    }
//                    System.out.println("n: "+n);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        if (isCancelled()) {
                            break;
                        }
                    }
                    if(n==0){
                        System.out.println("activate");
                        if (isCancelled()) {
                            break;
                        }
                        if (calcula()) {
                            return true;
                        }
                        System.out.println("tick");
                    }
                    n++;
//                    if(System.nanoTime()%(1000000)==0){
//                        millisTrans--;
//                        ejercicioProgress.setValue((millisTrans % millisOriginal)*360);
//                    }

                }
                return false;
            }
            
            @Override
            protected void cancelled() {
                super.cancelled();
                stoppedTime = new Long(System.currentTimeMillis());
            }
        };
    }
    
    public boolean isLast(){
        System.out.println("ejeractual: "+ejercicioActual+"  total: "+rutina.getEjercicios().size() +" circuito now: "+ circuitoActual +" de: "+ rutina.getRepeticiones());
        return ejercicioActual - 1 == rutina.getEjercicios().size() && circuitoActual == rutina.getRepeticiones();
    }
    
}
