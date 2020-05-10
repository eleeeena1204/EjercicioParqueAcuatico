/*
***PISCINA GRANDE***
* Aforo: 50 usuarios
* Sin restricción de edad
* Una vez llena no puede entrar nadie ni por los toboganes
* Tiempo de acción: entre 3s y 5s o hasta que el monitor te saque
* Monitor: controla el aforo para lo que tarda 500ms, en caso de estar completo
* saca a alguien aleatoriamente que tarda entre 500ms y 1s
* Si el elegido es acompañante tienen que salir ambos
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

public class PiscinaGrande 
{
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo.*/
    private int aforo;
    private Listado colaEspera, dentro;
    private Semaphore sem;
    private Usuario usuarioAtendido;
    
    /*En el constructor vamos a necesitar principalmente los JTextField que tenemos
    que modificar para mostrar los usuarios por pantalla.*/
    public PiscinaGrande (JTextField listaColaEntradaPiscinaGrande, JTextField listaUsuariosDentroPiscinaGrande, JTextField usuarioAtendidoPiscinaGrande)
    {
        aforo = 50;
        sem = new Semaphore(aforo, true);
        colaEspera = new Listado (listaColaEntradaPiscinaGrande);
        dentro =  new Listado (listaUsuariosDentroPiscinaGrande);
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
    public int getAforo() 
    {
        return aforo;
    }
    public void setAforo(int aforo) 
    {
        this.aforo = aforo;
    }
    public Semaphore getSem()
    {
        return sem;
    }
    public void setSem(Semaphore sem) 
    {
        this.sem = sem;
    }
    
    /*El método entrar controla que primero los usuarios pasen por la cola de espera
    y con el semáforo auxiliar binario controlamos que pasen de uno en uno al control
    del monitor y siendo justo, que pasen en orden de llegada.*/
    public void entrar(Usuario u) 
    {
        colaEspera.meter(u);
        try 
        {
            sem.acquire();
            dentro.meter(u);
        } 
        catch (InterruptedException ex) 
        {
            System.out.println("Error al entrar en la Piscina grande.");
        }
    }
    /*Con el método salir se controla de forma inversa que al entrar, que se dejan
    libres los permisos de los semáforos para que pueda pasar el siguiente usuario
    y se actualiza la cola de los usuarios de dentro.*/
    public void salir(Usuario u) 
    {   
        dentro.sacar(u);
        sem.release();
    }
    /*Este método se llama desde la clase toboganes porque cuando alguien se tira,
    cae en la piscina y ocupa una plaza más.*/
    public void aforoPiscinaGrande() 
    {
        try 
        {
            sem.acquire();
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(PiscinaGrande.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*Cuando un usuario se tira por los toboganes y cae en la piscina tiene que 
    realizar la función correspondiente a la piscina antes de poder salir.*/
    public void entrarDesdeTobogan(Usuario u) 
    {
        u.getPaso().mirar();
        dentro.meter(u);
        try 
        {
            Thread.sleep((int) (Math.random() * 2000) + 3000);
        } 
        catch (InterruptedException e) 
        {
            System.out.println("Error en la acción de la Piscina grande.");
        }
        u.getPaso().mirar();
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
    /*La función del monitor es controlar que si el aforo está completo, sacar a 
    alguien aleatorio, y en caso de ser acompañante, sacar a ambos usuarios.*/
    public void accionMonitorExpulsar() 
    {
        int aleatorio = (int) (Math.random() * dentro.getListadoUsuarios().size());
        Usuario u2 = dentro.getListadoUsuarios().get(aleatorio);
        if (u2.isEsAcompañante() || u2.getEdad() <=10)
        {
            dentro.sacar(u2);
            dentro.sacar(u2.getAcompañante());
            sem.release(2);
        }
        else
        {
            sem.release();
        }
    }
    /*Si el aforo no está completo aún, simplemente deja pasar al usuario.*/
    public void accionMonitor()
    {
        sem.release();
    }
}
