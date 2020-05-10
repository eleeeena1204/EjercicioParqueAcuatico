/*
***PISCINA NIÑOS***
* Aforo: 15 usuarios
* Los niños de entre 1 y 5 años DEBEN entrar acompañados
* De 11 años en adelante no pueden entrar
* Tiempo de acción: entre 1s y 3s
* Monitor: controla la edad y tarda entre 1s y 1'5s
*/
package ParqueAcuatico;

/*En los import, a parte de las librerías que vamos a necesitar para la ejecución,
importamos también las clases que vamos a tocar que tenemos en otro package.*/
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import javax.swing.JTextField;
import Usuarios.Listado;
import Usuarios.Usuario;

public class PiscinaNiños 
{
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo.*/
    private final int aforo;
    private Listado colaEspera, dentro, dentroEspera;
    private final Semaphore sem, semAux; 
    private boolean acceso = false;
    private Usuario usuarioAtendido;
    
    /*En el constructor vamos a necesitar principalmente los JTextField que tenemos
    que modificar para mostrar los usuarios por pantalla.*/
    public PiscinaNiños (JTextField listaColaEntradaPiscinaNiños, JTextField listaNiñosDentroPiscinaNiños, JTextField listaAdultosDentroPiscinaNiños, JTextField usuarioAtendidoPiscinaNiños)
    {
        aforo = 15; 
        colaEspera = new Listado (listaColaEntradaPiscinaNiños);
        dentro = new Listado (listaNiñosDentroPiscinaNiños);
        dentroEspera = new Listado (listaAdultosDentroPiscinaNiños);
        sem = new Semaphore (aforo, true);
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
    public ArrayList<Usuario> getDentroEspera() 
    {
        return dentroEspera.getListadoUsuarios();
    }
    public void setDentroEspera(Listado dentroEspera)
    {
        this.dentroEspera = dentroEspera;
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
    del monitor y siendo justo, que pasen en orden de llegada, también se controla que
    no entre gente que no tenga el acceso que proporciona el monitor.*/
    public boolean entrar (Usuario u) 
    {
        if (!acceso) 
        {
            return false;
        }
        try 
        {       
            colaEspera.meter(u);
            semAux.acquire();
            if ((u.getEdad() <= 5) || (u.isEsAcompañante() && (u.getEdadNiñoAcompañado() <= 5)) || (!u.isEsAcompañante() && (u.getEdad() > 5) && (u.getEdad() <= 10)))
            {
                sem.acquire();
                dentro.meter(u);
            }
            //El acompañante de los niños de 6 a 10 años se queda en la cola de espera de dentro
            else if (u.getEdadNiñoAcompañado() > 5)
            { 
                sem.acquire();
                dentroEspera.meter(u);
            }
        } 
        catch (InterruptedException ex) 
        {
            System.out.println("Error al entrar en la Piscina de niños.");
        }
        return true;
    }
    /*Con el método salir se controla de forma inversa que al entrar, que se dejan
    libres los permisos de los semáforos para que pueda pasar el siguiente usuario
    y se actualiza la cola de los usuarios de dentro y, en este caso la cola de espera
    de los usuarios acompañantes de niños de entre 6 y 10 años.*/
    public void salir (Usuario u) 
    {
        if ((u.getEdad() <= 5) || (u.isEsAcompañante() && (u.getEdadNiñoAcompañado() <= 5)) || (!u.isEsAcompañante() && (u.getEdad() > 5) && (u.getEdad() <= 10))) 
        { 
            dentro.sacar(u);
            sem.release();
        } 
        else 
        {
            dentroEspera.sacar(u);
            sem.release();
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
    /*La función del monitor es controlar las edades de los usuarios para proporcionarles
    el acceso y que aquellos que lo necesiten pasen con su correspondiente acompañante.*/
    public void accionMonitor(Usuario u) 
    {
        if ((u.getEdad() > 10) && !u.isEsAcompañante()) 
        {
            acceso = false;
            semAux.release();
        }
        else if ((u.isEsAcompañante()) || (u.getEdad() > 5) || (u.getEdad() <= 5))
        {
            acceso = true;
            semAux.release();
        } 
        usuarioAtendido = null;
    }
}
