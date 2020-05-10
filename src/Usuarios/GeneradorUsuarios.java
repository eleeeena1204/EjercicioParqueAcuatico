/*
***GENERADOR USUARIOS***
* En esta clase se crean los usuarios para limpiar el código y tener contralada 
*la creación, las edades, los acompañantes y el tiempo de espera entre cada 
*creación de usuario.
*/
package Usuarios;

/*En los import, a parte de las librerías que vamos a necesitar para la ejecución,
importamos también las clases que vamos a tocar que tenemos en otro package.*/
import InterfazGrafica.Paso;
import ParqueAcuatico.ParqueAcuatico;
import java.util.concurrent.CyclicBarrier;

public class GeneradorUsuarios extends Thread
{
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo.*/
    private final Paso paso;
    private final int generar; 
    private final ParqueAcuatico pa;
    
    /*En el constructor vamos a necesitar el parque acuático en sí y la clase 
    paso para el permiso de ejecución.*/
    public GeneradorUsuarios (ParqueAcuatico p_pa, Paso p_paso) 
    {
        generar = 5000;
        pa = p_pa;
        paso = p_paso;
    }
    
    /*Se crean los usuarios cada tiempo aleatorio fijado, se crean con un id y con 
    una edad y en caso de ser menor, se crea además un usuario acompañante y se 
    añade el id del usuario al que es acompañante.*/
    @Override
    public void run() 
    {
        paso.mirar();
        for (int id = 1; id <= generar; id++) 
        {            
            CyclicBarrier barrera = new CyclicBarrier(2); 
            int edad = (int) (Math.random() * 49) + 1;  
            if (edad <= 10) 
            {//Si edad <=10 necesita un adulto acompañándole
                int edadAdultoAcompanante = (int) (Math.random() * 32) + 18; 
                Usuario u = new Usuario(id, barrera, pa, paso);
                u.setIdCompleto("ID" + id + "-" + edad + "-" + (id + 1));
                u.setEdad(edad);
                Usuario acompañante = new Usuario((id + 1), barrera, pa, paso);         
                acompañante.setEdad(edadAdultoAcompanante);      
                acompañante.setIdCompleto("ID" + (id + 1) + "-" + edadAdultoAcompanante + "-" + u.getIdentificador());
                acompañante.setEdadNiñoAcompañado(edad);
                acompañante.setEsAcompañante(true);
                u.setIdAcompañante(acompañante.getIdentificador());
                acompañante.setIdAcompañante(u.getIdentificador());
                u.setAcompañante(acompañante);
                acompañante.setAcompañante(u);
                u.start();       
                acompañante.start();    
                id++;       
            } 
            else 
            {
                Usuario u = new Usuario(id, barrera, pa, paso); 
                Usuario acompañante = null;      
                u.setEdad(edad); 
                u.setIdCompleto("ID" + id + "-" + edad);     
                u.setEsAcompañante(false);
                u.setAcompañante(acompañante);
                u.start();       
            }
            try 
            {
                Thread.sleep((int) (Math.random() * 300) + 400);
            } 
            catch (InterruptedException e) {
                System.out.println ("Error en sleep de GeneradorUsuarios.");
            }        
            paso.mirar();
        }
    }
}
