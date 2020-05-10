/*
***TOBOGANES***
* Tobogán A: para usuarios de entre 11 y 14 años
* Tobogán B: para usuarios de entre 15 y 17 años
* Tobogán C: para usuarios a partir de 18
* Tiempo de acción: entre 2s y 3s
* Monitor: cada uno controla el acceso y tarda entre 400ms y 500ms
*/
package ParqueAcuatico;

/*En los import, a parte de las librerías que vamos a necesitar para la ejecución,
importamos también las clases que vamos a tocar que tenemos en otro package.*/
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import javax.swing.JTextField;
import Usuarios.Listado;
import Usuarios.Usuario;

public class Toboganes 
{
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo.*/
    private final JTextField tA, tB, tC;
    private Listado colaEspera, colaA, colaB, colaC;
    private final Semaphore semA, semB, semC, semMonA, semMonB, semMonC;
    private final PiscinaGrande pg;
    private Usuario usuarioAtendidoA, usuarioAtendidoB, usuarioAtendidoC, enToboganA, enToboganB, enToboganC;
    
    /*En el constructor vamos a necesitar principalmente los JTextField que tenemos
    que modificar para mostrar los usuarios por pantalla.*/
    public Toboganes(PiscinaGrande piscinaGrande, JTextField listaColaEntradaToboganes, JTextField usuarioToboganA, JTextField usuarioToboganB, JTextField usuarioToboganC) 
    {
        tA = usuarioToboganA;
        tB = usuarioToboganB;
        tC = usuarioToboganC;
        pg = piscinaGrande;
        colaEspera = new Listado (listaColaEntradaToboganes);
        colaA = new Listado (usuarioToboganA);
        colaB = new Listado (usuarioToboganB);
        colaC = new Listado (usuarioToboganC);
        semA = new Semaphore (1, true);
        semB = new Semaphore (1, true);
        semC = new Semaphore (1, true);
        semMonA = new Semaphore (0, true);
        semMonB = new Semaphore (0, true);
        semMonC = new Semaphore (0, true);
        usuarioAtendidoA = null;
        usuarioAtendidoB = null;
        usuarioAtendidoC = null;
        enToboganA = null;
        enToboganB = null;
        enToboganC = null;
    }

    /*Se añaden los métodos getter y setter que necesitamos para poder actualizar
    o usar la información desde otras clases o desde la nuestra misma.*/
    public ArrayList<Usuario> getColaA() 
    {
        return colaA.getListadoUsuarios();
    }
    public void setColaA(Listado colaA) 
    {
        this.colaA = colaA;
    }
    public ArrayList<Usuario> getColaB() 
    {
        return colaB.getListadoUsuarios();
    }
    public void setColaB(Listado colaB) 
    {
        this.colaB = colaB;
    }
    public ArrayList<Usuario> getColaC() 
    {
        return colaC.getListadoUsuarios();
    }
    public void setColaC(Listado colaC) 
    {
        this.colaC = colaC;
    }
    public ArrayList<Usuario> getColaEspera() 
    {
        return colaEspera.getListadoUsuarios();
    }
    public void setColaEspera(Listado colaEspera) 
    {
        this.colaEspera = colaEspera;
    }
    public Usuario getUsuarioAtendidoA() 
    {
        return usuarioAtendidoA;
    }
    public void setUsuarioAtendidoA(Usuario usuarioAtendido) 
    {
        this.usuarioAtendidoA = usuarioAtendido;
    }
    public Usuario getUsuarioAtendidoB() 
    {
        return usuarioAtendidoB;
    }
    public void setUsuarioAtendidoB(Usuario usuarioAtendidoB) 
    {
        this.usuarioAtendidoB = usuarioAtendidoB;
    }
    public Usuario getUsuarioAtendidoC() 
    {
        return usuarioAtendidoC;
    }
    public void setUsuarioAtendidoC(Usuario usuarioAtendidoC) 
    {
        this.usuarioAtendidoC = usuarioAtendidoC;
    }
    public Usuario getEnToboganA() 
    {
        return enToboganA;
    }
    public void setEnToboganA(Usuario enToboganA) 
    {
        this.enToboganA = enToboganA;
    }
    public Usuario getEnToboganB() 
    {
        return enToboganB;
    }
    public void setEnToboganB(Usuario enToboganB) 
    {
        this.enToboganB = enToboganB;
    }
    public Usuario getEnToboganC() 
    {
        return enToboganC;
    }
    public void setEnToboganC(Usuario enToboganC) 
    {
        this.enToboganC = enToboganC;
    }
    
    /*El método entrar controla que primero los usuarios pasen por la cola de espera
    y con el semáforo auxiliar binario controlamos que pasen de uno en uno al control
    del monitor y siendo justo, que pasen en orden de llegada.*/
    public boolean entrar(Usuario u) 
    {
        if ((u.getEdad() <= 10) || (u.isEsAcompañante())) 
        {
            return false; 
        }
        try 
        {   
            colaEspera.meter(u);
            if (u.getEdad() < 15) 
            {  
                semA.acquire();
                colaA.meterSinMostrar(u);
                semMonA.acquire();
            } 
            else if ((u.getEdad() >= 15) && (u.getEdad() < 18)) 
            {
                semB.acquire();
                colaB.meterSinMostrar(u);
                semMonB.acquire();
            } 
            else if (u.getEdad() >= 18) 
            {
                semC.acquire();
                colaC.meterSinMostrar(u);
                semMonC.acquire();
            }
            //Se intenta entrar a la piscina en caso de que haya hueco
            try
            {
                pg.getSem().acquire(); 
            }
            catch (InterruptedException ex) 
            {
                System.out.println("Error al entrar en la piscina desde los toboganes.");
            }
            colaEspera.sacar(u);
        } 
        catch (InterruptedException e) 
        {
            System.out.println("Error al entrar en los Toboganes.");
        }
        return true;
    }
    /*Cuando un usuario se tira por un tobogán, cae en la piscina grande y hace
    la acción correspondiente antes de salir.*/
    public void caerEnPiscinaGrande(Usuario u) 
    {
        if (u.getEdad() < 15) 
        {
            tA.setText("");
            enToboganA = null;
            semA.release();
        } 
        else if ((u.getEdad() >= 15) && (u.getEdad() < 18)) 
        {
            tB.setText("");
            enToboganB = null;
            semB.release();
        } 
        else if (u.getEdad() >= 18) 
        {
            tC.setText("");
            enToboganC = null;
            semC.release();
        }
        try 
        {
            Thread.sleep((int) (Math.random() * 1000) + 2000);
        } 
        catch (InterruptedException e) 
        {
            System.out.println("Error en la acción de los Toboganes.");
        }
        pg.entrarDesdeTobogan(u);
    }
    /*Se saca el siguiente usuario de la cola de espera del tobogán A (que aunque
    no se muestra por pantalla, se controla) que va a ser atendido por el monitor A.*/
    public Usuario siguienteUsuarioA()
    {
        Usuario u = colaA.seleccionarUsuario();
        colaEspera.sacar(u);
        colaA.sacarSinMostrar(u);
        usuarioAtendidoA = u;
        return u;
    }
    /*La función del monitor A es controlar que los usuarios que quieren usar su
    tobogán cumplan con la edad fijada para este tobogán.*/
    public void accionMonitorA(Usuario u) 
    {
        semMonA.release();
        enToboganA = u;
        usuarioAtendidoA = null;
        tA.setText(u.getIdCompleto());
    }
    /*Se saca el siguiente usuario de la cola de espera del tobogán B (que aunque
    no se muestra por pantalla, se controla) que va a ser atendido por el monitor B.*/
    public Usuario siguienteUsuarioB() 
    {
        Usuario u = colaB.seleccionarUsuario();
        colaEspera.sacar(u);
        colaB.sacarSinMostrar(u);
        usuarioAtendidoB = u;
        return u;
    }
    /*La función del monitor B es controlar que los usuarios que quieren usar su
    tobogán cumplan con la edad fijada para este tobogán.*/
    public void accionMonitorB(Usuario u) 
    {
        semMonB.release();
        enToboganB = u;
        usuarioAtendidoB = null;
        tB.setText (u.getIdCompleto());
    }
    /*Se saca el siguiente usuario de la cola de espera del tobogán C (que aunque
    no se muestra por pantalla, se controla) que va a ser atendido por el monitor C.*/
    public Usuario siguienteUsuarioC() 
    {
        Usuario u =  colaC.seleccionarUsuario();
        colaEspera.sacar(u);
        colaC.sacarSinMostrar(u);
        usuarioAtendidoC = u;
        return u;
    }
    /*La función del monitor C es controlar que los usuarios que quieren usar su
    tobogán cumplan con la edad fijada para este tobogán.*/
    public void accionMonitorC(Usuario u) 
    {
        semMonC.release();
        enToboganC = u;
        usuarioAtendidoC = null;
        tC.setText(u.getIdCompleto());
    }
}
