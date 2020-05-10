/*
***VESTUARIO***
* Aforo: 20 usuarios 
* Los niños de entre 1 a 5 años no pueden pasar
* Los niños de 6 a 10 años pasan CON acompañante
* Se entra por parejas
* Tiempo de acción: entre 2s y 5s
* Monitor: controla las parejas y tarda 1s y el acceso por edad
*/
package ParqueAcuatico;

/*En los import, a parte de las librerías que vamos a necesitar para la ejecución,
importamos también las clases que vamos a tocar que tenemos en otro package.*/
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import Usuarios.Listado;
import Usuarios.Usuario;

public class PiscinaOlas 
{
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo
    y en concreto las CyclicBarrier para las parejas.*/
    private final int aforo;
    private final CyclicBarrier pareja; 
    private Listado colaEspera, dentro;
    private boolean acceso = false;
    private final Semaphore sem, semAux;
    private Usuario usuarioAtendido;
    
    /*En el constructor vamos a necesitar principalmente los JTextField que tenemos
    que modificar para mostrar los usuarios por pantalla.*/
    public PiscinaOlas (JTextField listaColaEntradaPiscinaOlas, JTextField listaUsuariosDentroPiscinaOlas, JTextField usuarioAtendidoPiscinaOlas)
    {
        aforo = 20;
        pareja = new CyclicBarrier (2);
        sem = new Semaphore (aforo, true);  
        semAux = new Semaphore (0, true);
        colaEspera = new Listado (listaColaEntradaPiscinaOlas);
        dentro = new Listado (listaUsuariosDentroPiscinaOlas);
        usuarioAtendido = null;
    }

    /*Se añaden los métodos getter y setter que necesitamos para poder actualizar
    o usar la información desde otras clases o desde la nuestra misma.*/
    public ArrayList<Usuario> getColaEspera() 
    {
        return colaEspera.getListadoUsuarios();
    }
    public void setColaEspera(Listado colaEspera) 
    {
        this.colaEspera = colaEspera;
    }
    public ArrayList<Usuario> getDentro() 
    {
        return dentro.getListadoUsuarios();
    }
    public void setDentro(Listado dentro) 
    {
        this.dentro = dentro;
    }
    public Usuario getUsuarioAtendido() 
    {
        return usuarioAtendido;
    }
    public void setUsuarioAtendido(Usuario usuarioAtendido) 
    {
        this.usuarioAtendido = usuarioAtendido;
    }
    
    /*El método entrar controla que primero los usuarios pasen por la cola de espera
    y con el semáforo auxiliar binario controlamos que pasen de uno en uno al control
    del monitor y siendo justo, que pasen en orden de llegada.*/
    public boolean entrar(Usuario u) 
    {
        colaEspera.meter(u);
        try 
        {
            pareja.await();
            semAux.acquire();
            if (!acceso) 
            {
                return false;
            }
            if ((u.getEdad() > 10) && !u.isEsAcompañante()) 
            {
                try 
                {
                    sem.acquire();
                    pareja.await();
                    dentro.meter(u);
                } 
                catch (BrokenBarrierException e) 
                {
                    System.out.println("Error en la entrada a la Piscina de olas sin acompañante.");
                }
            } 
            else if ((u.getEdad() <= 10) || u.isEsAcompañante()) 
            {
                try 
                {
                    sem.acquire();
                    dentro.meter(u);
                } 
                catch (InterruptedException e) 
                {
                    System.out.println("Error en la entrada a la Piscina de olas con acompañante.");
                }
            }
        }  
        catch (BrokenBarrierException | InterruptedException ex) 
        {
            Logger.getLogger(PiscinaOlas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    /*Con el método salir se controla de forma inversa que al entrar, que se dejan
    libres los permisos de los semáforos para que pueda pasar el siguiente usuario
    y se actualiza la cola de los usuarios de dentro.*/
    public void salir(Usuario u) 
    {
        dentro.sacar(u);
        sem.release();
    }
    /*Se saca el siguiente usuario de la cola de espera que va a ser atendido por
    el monitor.*/
    public Usuario siguienteUsuario() 
    {
        Usuario u = (Usuario) colaEspera.seleccionarUsuario();
        colaEspera.sacar(u);
        usuarioAtendido = u;
        return u;
    }
    /*La función del monitor es controlar las edades de los usuarios y que aquellos
    que lo necesiten pasen con su correspondiente acompañante.*/
    public void accionMonitor(Usuario u) 
    {
        if (u.getEdad() <= 5) 
        {
            acceso = false;
            semAux.release();
        } 
        else if (u.isEsAcompañante())
        {
            if (u.getAcompañante().getEdad() <= 5)
            {
                acceso = false;
                semAux.release();
            } 
            else
            {
                acceso = true;
                sem.release();
            }
        } 
        else if (u.getEdad() < 11) 
        {
            acceso = true;
            semAux.release();
        } 
        else 
        { 
            acceso = true;
            semAux.release();
        }
        usuarioAtendido = null;
    }
}
