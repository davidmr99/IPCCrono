/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipccrono.stages.rutina;

import ipccrono.stages.ejercicios.Ejercicio;
import javafx.collections.ObservableList;

/**
 *
 * @author FMR
 */
public class Rutina {
    private String name;
    private int repeticiones;
    private int tDescansoRepeticiones;
    private ObservableList<Ejercicio> ejercicios;
    private int tDescansoEjercicios;
    
    
    public Rutina(String nombre, int repet, int descansoRepet, ObservableList<Ejercicio> ejs, int descansoEjs){
        update(nombre,repet,descansoRepet,ejs,descansoEjs);
    }
    
    public String getName(){
        return this.name;
    }
    
    public int getRepeticiones(){
        return this.repeticiones;
    }
    
    public int getDescansoRepet(){
        return this.tDescansoRepeticiones;
    }
    
    public int getDescansoEjs(){
        return this.tDescansoEjercicios;
    }
    
    public ObservableList<Ejercicio> getEjercicios(){
        return this.ejercicios;
    }

    void update(String nombre, int repet, int descansoRepet, ObservableList<Ejercicio> ejs, int descansoEjs) {
        name = nombre;
        repeticiones = repet;
        tDescansoRepeticiones = descansoRepet;
        ejercicios = ejs;
        tDescansoEjercicios = descansoEjs;
    }
    
    @Override
    public String toString() {
        return name + ", "+repeticiones+" repes con descanso: "+tDescansoRepeticiones+", ejs: "+ejercicios;
    }
}
