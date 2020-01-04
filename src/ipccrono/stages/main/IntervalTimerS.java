///*
//* To change this license header, choose License Headers in Project Properties.
//* To change this template file, choose Tools | Templates
//* and open the template in the editor.
//*/
//package ipccrono.stages.main;
//
//import static ipccrono.stages.main.IntervalTimerS.EstadoSesion.*;
//import ipccrono.stages.rutina.Rutina;
//import java.time.Duration;
//import java.util.HashMap;
//import javafx.application.Platform;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
//import javafx.concurrent.Service;
//import javafx.concurrent.Task;
//
///**
// *
// * @author jose
// */
//public class IntervalTimerS extends Service<Boolean> {
//    
//    enum EstadoSesion {
//        PREPARADO, TRABAJO, DESCANSO_EJERCICIO, DESCANSO_CIRCUITO, TERMINADO
//    };
//    HashMap<EstadoSesion, Duration> durations;
//    private static final int DELAY = 1000; // no despertamos cada segundo
//    //tiempos
//    private static long currentTime = 0; // guarda la hora del instante actual
//    private static long startTime = 0;// guarda la hora del instante inicial del intervalo
//    private static long stoppedDuration = 0;// guarda la duracion del tiempo que hemos estdo detenidos
//    private static Long stoppedTime;
//    
//    private int ejercicioActual = 1;
//    private int circuitoActual = 1;
//    private Rutina rutina;
//    
//    private EstadoSesion estadoActual = PREPARADO;
//    
//    // cuando se activa a true y se lanza la task solo se cambia de estado
//    private boolean cambiarEstado = false;
//    
//    /**
//     * Get the value of canviarEstado
//     *
//     * @return the value of canviarEstado
//     */
//    public boolean isCambiarEstado() {
//        return cambiarEstado;
//    }
//    
//    /**
//     * Set the value of cambiarEstado
//     *
//     * @param cambiarEstado new value of canviarEstado
//     */
//    public void setCambiarEstado(boolean cambiarEstado) {
//        if (!this.isRunning()) {
//            this.cambiarEstado = cambiarEstado;
//        }
//    }
//    
//    // propiedad donde se muestra el tiempo transcurrido
//    private StringProperty tiempo = new SimpleStringProperty();
//    
//    public String getTiempo() {
//        return tiempo.get();
//    }
//    
//    public void setTiempo(StringProperty value) {
//        tiempo = value;
//    }
//    
//    public StringProperty tiempoProperty() {
//        return tiempo;
//    }
//    
//    public void setSesionTipo(Rutina st) {
//        rutina = st;
//        
//        Duration descEjs = Duration.ofSeconds(rutina.getDescansoEjs());
//        Duration descRepet = Duration.ofSeconds(rutina.getDescansoEjs());
//        
//        durations = new HashMap<EstadoSesion, Duration>();
//        durations.put(TRABAJO, rutina.getT_ejercicio());
//        durations.put(DESCANSO_EJERCICIO, descEjs);
//        durations.put(DESCANSO_CIRCUITO, descRepet);
//    }
//    
//    public void restaurarInicio() {
//        if (!this.isRunning()) {
//            stoppedDuration = 0;
//            ejercicioActual = 1;
//            circuitoActual = 1;
//            estadoActual = PREPARADO;
//            stoppedTime = null;
//            Platform.runLater(() -> {
//                tiempo.setValue(String.format("%02d", 0) + ":" + String.format("%02d", 0));
//            });
//        }
//    }
//    
//    @Override
//    protected Task<Boolean> createTask() {
//        return new Task<Boolean>() {
//            
//            //actualiza el estado actual, Es invocado cuando ha finalizado un intervalo -duraciones-
//            // retorna TRUE si ha finalizado la sesion
//            // si cambiamos de estado y estamos detenidos en mitad de un intervalo desaparece
//            boolean cambiaEstado() {
//                // rest de los tiempos acumulados y el inicial del intervalo
//                // el estado puede ser detenido o en marcha, hay que comprobar
//                startTime = currentTime;
//                stoppedDuration = 0;
//                stoppedTime = null;
//                switch (estadoActual) {
//                    case TRABAJO:
//                        if (ejercicioActual < rutina.getN_ejercicios()) {
//                            estadoActual = DESCANSO_EJERCICIO;
//                            ejercicioActual++;
//                        } else if (circuitoActual < rutina.getN_circuitos()) {
//                            estadoActual = DESCANSO_CIRCUITO;
//                            circuitoActual++;
//                            ejercicioActual = 1;
//                        } else {
//                            //parar la sesion
//                            estadoActual = TERMINADO;
//                            return true;
//                        }
//                        break;
//                    case DESCANSO_EJERCICIO:
//                        estadoActual = TRABAJO;
//                        break;
//                    case DESCANSO_CIRCUITO:
//                        estadoActual = TRABAJO;
//                }
//                Platform.runLater(() -> {
//                    tiempo.setValue(String.format("%02d", 0) + ":" + String.format("%02d", 0));
//                });
//                return false;
//            }
//            
//            // calcula el tiempo del intervalo actual, si se ha cumplido invoca a cambiaEstado
//            // retorna TRUE si ha finalizado la sesion
//            boolean calcula() {
//                
//                currentTime = System.currentTimeMillis();
//                Long totalTime = (currentTime - startTime) - stoppedDuration;
//                Duration duration = Duration.ofMillis(totalTime);
//                long segundosTrans = duration.getSeconds();
//                final Long minutos = duration.toMinutes();
//                final Long segundos = duration.minusMinutes(minutos).getSeconds();
//                
//                Platform.runLater(() -> {
//                    tiempo.setValue(String.format("%02d", minutos) + ":" + String.format("%02d", segundos));
//                });
//                if (duration.compareTo(durations.get(estadoActual)) >= 0) {
//                    return cambiaEstado();
//                } else {
//                    return false;
//                }
//            }
//            
//            @Override
//            protected Boolean call() {
//                if (estadoActual == TERMINADO) {
//                    return true;
//                }
//                // si estamos parados y queremos adelantar el estado se invoca al servicio
//                if (cambiarEstado) {
//                    cambiarEstado = false;
//                    return cambiaEstado();
//                }
//                // si no estabamos detenidos es el arranque del servicio
//                if (stoppedTime == null) {
//                    startTime = currentTime = System.currentTimeMillis();
//                //                    if (firstTime) {
//                if (estadoActual == PREPARADO) { // es lla primera vez que arrancamos el servicio
//                    ejercicioActual = 1;
//                    circuitoActual = 1;
//                    estadoActual = TRABAJO;
//                }
//                } else { // estabamos detenidos y nos ponemos en marcha sin cambio de estado
//                    Long elapsedTime = System.currentTimeMillis() - stoppedTime;
//                    stoppedDuration = stoppedDuration + elapsedTime;
//                    stoppedTime = null;
//                }
//                while (true) {
//                    try {
//                        Thread.sleep(DELAY);
//                    } catch (InterruptedException ex) {
//                        if (isCancelled()) {
//                            break;
//                        }
//                    }
//                    if (isCancelled()) {
//                        break;
//                    }
//                    if (calcula()) {
//                        return true;
//                    }
//                }
//                return false;
//            }
//            
//            @Override
//            protected void cancelled() {
//                super.cancelled();
//                stoppedTime = new Long(System.currentTimeMillis());
//            }
//        };
//    }
//    
//}
