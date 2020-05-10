/*
***TUMBONAS***
* Aforo: 20 usuarios
* Solo pueden usarlas los mayores de 15 años
* Cuando queda alguna tumbona libre compiten para ver quien llega primero a cogerla
* Tiempo de acción: entre 2s y 4s
* Monitor: controla la edad y tarda entre 500ms y 900ms
*/
package ParqueAcuatico;

/*En los import, a parte de las librerías que vamos a necesitar para la ejecución,
importamos también las clases que vamos a tocar que tenemos en otro package.*/
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import javax.swing.JTextField;
import Usuarios.Listado;
import Usuarios.Usuario;

public class Tumbonas 
{
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo.*/
    private final int aforo;
    private boolean acceso = false;
    private Listado  colaEspera, dentro;
    private final Semaphore sem, semAux;  
    private Usuario usuarioAtendido;
    
    /*En el constructor vamos a necesitar principalmente los JTextField que tenemos
    que modificar para mostrar los usuarios por pantalla.*/
    public Tumbonas(JTextField listaColaEntradaTumbonas, JTextField listaUsuariosDentroTumbonas, JTextField usuarioAtendidoTumbonas)
    {
        aforo = 20;
        colaEspera = new Listado (listaColaEntradaTumbonas);
        dentro = new Listado (listaUsuariosDentroTumbonas);
        sem = new Semaphore (aforo); //No le ponemos true porque no queremos semaforo justo
        semAux = new Semaphore (0, true);
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
    del monitor y siendo justo, que pasen en orden de llegada a ser evaluados aunque
    cuando una tumbona se queda libre, la ocupa el que llegue antes a ella.*/
    public boolean entrar(Usuario u)
    {
        colaEspera.meter(u);
        try
        {
            sem.acquire();
            semAux.acquire();
            if(!acceso)
            {
                sem.release();
                return false;
            } 
            else
            {
                dentro.meter(u);
            }
        } 
        catch(InterruptedException e)
        {
            System.out.println("Error al entrar en las Tumbonas." );
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
        Usuario u = colaEspera.seleccionarUsuario();
        colaEspera.sacar(u);
        usuarioAtendido = u;
        return u;
    }
    /*La función del monitor es controlar las edades de los usuarios.*/
    public void accionMonitor(Usuario u)
    {
        if(u.getEdad() >= 15)
        {
            acceso = true;
        } 
        semAux.release();
        usuarioAtendido = null;
    }
}
