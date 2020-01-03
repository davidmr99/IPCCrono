/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipccrono.stages.ejercicios;

/**
 *
 * @author FMR
 */
public class Ejercicio {
    private String name;
    private int time;
    
    public Ejercicio(String nombre, int tiempo){
        name = nombre;
        time = tiempo;
    }
    
    public int getTime(){
        return time;
    }
    
    public String getName(){
        return name;
    }
    
    public void setTime(int tiempo){
        time = tiempo;
    }
    
    public void setName(String nombre){
        name = nombre;
    }
    @Override
    public String toString(){
        return "Ejercicio "+name+" ,"+time+"s";
    }
}
