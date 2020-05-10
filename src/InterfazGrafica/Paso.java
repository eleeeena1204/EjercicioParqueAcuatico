/*
***PASO***
* Esta clase define un cerrojo con un Condition para la variable booleana cerrado
* que es comprobada por un proceso. Si vale false (abierto) el proceso puede continuar
* y si es true (cerrado) el proceso se detiene.
*/
package InterfazGrafica;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Paso
{
    private boolean cerrado = false;
    private Lock cerrojo = new ReentrantLock();
    private Condition parar = cerrojo.newCondition();

    /*Se comprueba si el boton de pausa ha sido pulsado o no.*/
    public void mirar()
    {
        try
        {
            cerrojo.lock();
            while (cerrado)
            {
                try
                {
                    parar.await();
                } 
                catch(InterruptedException ie){ }
            }
        }
        finally
        {
            cerrojo.unlock();
        }
    }
    /*Se abren el paso a los procesos para que puedan continuar su ejecución.*/
    public void abrir()
    {
        try
        {
            cerrojo.lock();
            cerrado = false;
            parar.signalAll();
        }
        finally
        {
            cerrojo.unlock();
        }
    }
    /*Se cierra el paso para los procesos y se detiene su ejecución.*/
    public void cerrar()
    {
        try
        {
            cerrojo.lock();
            cerrado = true;
        }
        finally
        {
            cerrojo.unlock();
        }
    }
}
