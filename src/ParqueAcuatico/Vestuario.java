/*
***VESTUARIO***
* Aforo: 30 usuarios (20 adultos y 10 niños)
* Los niños de entre 11 y 17 años pueden pasar SIN acompañante
* Tiempo de acción: 3s
* Monitor: controla la edad y tarda 1s
*/
package ParqueAcuatico;

/*En los import, a parte de las librerías que vamos a necesitar para la ejecución,
importamos también las clases que vamos a tocar que tenemos en otro package.*/
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import Usuarios.Listado;
import Usuarios.Usuario;

public class Vestuario 
{
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo.*/
    private final int aforoAdultos, aforoNiños;      
    private Listado colaEspera, dentro;
    private final Semaphore semAux, semAdultos, semNiños;
    private Usuario usuarioAtendido;
    
    /*En el constructor vamos a necesitar principalmente los JTextField que tenemos
    que modificar para mostrar los usuarios por pantalla.*/
    public Vestuario(JTextField listaColaEntradaVestuario, JTextField listaUsuariosDentroVestuario, JTextField usuarioAtendidoVestuario) 
    {
        aforoAdultos = 20;                                      
        aforoNiños = 10;                                        
        semAux = new Semaphore (0, true);                      
        semAdultos = new Semaphore (aforoAdultos, true);        
        semNiños = new Semaphore (aforoNiños, true);            
        colaEspera = new Listado (listaColaEntradaVestuario);   
        dentro = new Listado (listaUsuariosDentroVestuario);    
        usuarioAtendido = null;
    }

    /*Se añaden los métodos getter y setter que necesitamos para poder actualizar
    o usar la información desde otras clases o desde la nuestra misma.*/
    public ArrayList<Usuario> getDentro() 
    {
        return dentro.getListadoUsuarios();
    }
    public void setDentro(Listado dentro) 
    {
        this.dentro = dentro;
    }
    public ArrayList<Usuario> getColaEspera() {
        return colaEspera.getListadoUsuarios();
    }
    public void setColaEspera(Listado colaEspera) 
    {
        this.colaEspera = colaEspera;
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
    public void entrar(Usuario u) 
    {
        colaEspera.meter(u);
        try 
        {
            
            semAux.acquire();
            dentro.meter(u);
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(Vestuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*Con el método salir se controla de forma inversa que al entrar, que se dejan
    libres los permisos de los semáforos para que pueda pasar el siguiente usuario
    y se actualiza la cola de los usuarios de dentro.*/
    public void salir(Usuario u) 
    {
        dentro.sacar(u);
        if (((u.getEdadNiñoAcompañado() < 11) && (u.getEdadNiñoAcompañado() > 0)) || ((u.getEdad() >= 11) && (u.getEdad() < 18)))
        {
            semNiños.release();
        }  
        else 
        {
            semAdultos.release();
        }
    }
    /*Se saca el siguiente usuario de la cola de espera que va a ser atendido por
    el monitor.*/
    public Usuario siguienteUsuario()
    {
        Usuario u = colaEspera.seleccionarUsuario();
        colaEspera.sacar(u);
        usuarioAtendido = u;
        return u;
    }
    /*La función del monitor es controlar las edades de los usuarios y que aquellos
    que lo necesiten pasen con su correspondiente acompañante.*/
    public void accionMonitor (Usuario u)
    {
        try 
        {
            if ((u.getEdad() > 17) && !u.isEsAcompañante()) 
            {
                semAdultos.acquire();
                semAux.release();
            } 
            else if (u.getEdad() <= 10) 
            { 
                semNiños.acquire();
                semAux.release();
            }
            else if (u.isEsAcompañante()) 
            {
                semAdultos.acquire();
                semAux.release();
            } 
            else 
            {
                semNiños.acquire();
                semAux.release();
            }
            usuarioAtendido = null;
        } 
        catch (InterruptedException e) 
        {
            System.out.println("Error en la acción del monitor en el Vestuario.");
        }
    }
}
