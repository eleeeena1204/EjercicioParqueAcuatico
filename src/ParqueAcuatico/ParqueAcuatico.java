/*
***PARQUE ACUÁTICO***
* Aforo: 100 usuarios
*/
package ParqueAcuatico;

/*En los import, a parte de las librerías que vamos a necesitar para la ejecución,
importamos también las clases que vamos a tocar que tenemos en otro package.*/
import java.util.concurrent.Semaphore;
import javax.swing.JTextField;
import Usuarios.Listado;
import Usuarios.Usuario;
import java.util.ArrayList;

public class ParqueAcuatico 
{
    /*Declaramos los atributos privados que nos hacen falta para controlar el flujo,
    al ser en Parque Acuático necesitamos tener dentro todos los demás objetos.*/
    private final int aforo;
    private int numMenores;
    private final Semaphore sem;
    private Listado colaEntrada;
    private PiscinaGrande piscinaGrande;
    private PiscinaNiños piscinaNiños;
    private PiscinaOlas piscinaOlas;
    private Toboganes toboganes;
    private Tumbonas tumbonas;
    private Vestuario vestuario; 
    
    /*En el constructor vamos a necesitar principalmente los JTextField que tenemos
    que modificar para mostrar los usuarios por pantalla que, a su vez se los pasamos 
    para crear los objetos de cada actividad.*/
    public ParqueAcuatico(JTextField listaColaEntradaParqueAcuatico, JTextField listaColaEntradaVestuario, JTextField listaUsuariosDentroVestuario, JTextField listaColaEntradaPiscinaGrande, JTextField listaUsuariosDentroPiscinaGrande, JTextField listaColaEntradaPiscinaNiños, JTextField listaNiñosDentroPiscinaNiños, JTextField listaAdultosDentroPiscinaNiños, JTextField listaColaEntradaPiscinaOlas, JTextField listaColaEntradaToboganes, JTextField usuarioToboganA, JTextField usuarioToboganB, JTextField usuarioToboganC, JTextField listaColaEntradaTumbonas, JTextField listaUsuariosDentroTumbonas, JTextField usuarioAtendidoVestuario, JTextField usuarioAtendidoPiscinaGrande, JTextField usuarioAtendidoPiscinaNiños, JTextField usuarioAtendidoPiscinaOlas, JTextField usuarioAtendidoTumbonas, JTextField listaUsuariosDentroPiscinaOlas, JTextField usuarioAtendidoToboganA, JTextField usuarioAtendidoToboganB, JTextField usuarioAtendidoToboganC)
    {
        aforo = 100;
        numMenores = 0;
        vestuario = new Vestuario(listaColaEntradaVestuario, listaUsuariosDentroVestuario, usuarioAtendidoVestuario);
        piscinaNiños = new PiscinaNiños(listaColaEntradaPiscinaNiños, listaNiñosDentroPiscinaNiños, listaAdultosDentroPiscinaNiños, usuarioAtendidoPiscinaNiños);
        piscinaGrande = new PiscinaGrande(listaColaEntradaPiscinaGrande, listaUsuariosDentroPiscinaGrande, usuarioAtendidoPiscinaGrande);
        piscinaOlas = new PiscinaOlas(listaColaEntradaPiscinaOlas, listaUsuariosDentroPiscinaOlas, usuarioAtendidoPiscinaOlas);
        tumbonas = new Tumbonas(listaColaEntradaTumbonas,listaUsuariosDentroTumbonas, usuarioAtendidoTumbonas);
        toboganes = new Toboganes(piscinaGrande, listaColaEntradaToboganes, usuarioToboganA, usuarioToboganB, usuarioToboganC);
        colaEntrada =  new Listado (listaColaEntradaParqueAcuatico);
        sem =  new Semaphore(aforo, true);
    }

    /*Se añaden los métodos getter y setter que necesitamos para poder actualizar
    o usar la información desde otras clases o desde la nuestra mismaa.*/
    public int getNumMenores() 
    {
        return numMenores;
    }
    public void setNumMenores(int numMenores) 
    {
        this.numMenores = numMenores;
    }
    public PiscinaGrande getPiscinaGrande() 
    {
        return piscinaGrande;
    }
    public void setPiscinaGrande(PiscinaGrande piscinaGrande) 
    {
        this.piscinaGrande = piscinaGrande;
    }
    public PiscinaNiños getPiscinaNiños() 
    {
        return piscinaNiños;
    }
    public void setPiscinaNiños(PiscinaNiños piscinaNiños) 
    {
        this.piscinaNiños = piscinaNiños;
    }
    public PiscinaOlas getPiscinaOlas() 
    {
        return piscinaOlas;
    }
    public void setPiscinaOlas(PiscinaOlas piscinaOlas) 
    {
        this.piscinaOlas = piscinaOlas;
    }
    public Toboganes getToboganes() 
    {
        return toboganes;
    }
    public void setToboganes(Toboganes toboganes) 
    {
        this.toboganes = toboganes;
    }
    public Tumbonas getTumbonas() 
    {
        return tumbonas;
    }
    public void setTumbonas(Tumbonas tumbonas) 
    {
        this.tumbonas = tumbonas;
    }
    public Vestuario getVestuario() 
    {
        return vestuario;
    }
    public void setVestuario(Vestuario vestuario) 
    {
        this.vestuario = vestuario;
    }
    public ArrayList<Usuario> getColaEntrada() 
    {
        return colaEntrada.getListadoUsuarios();
    }
    public void setColaEntrada(Listado colaEsperaEntradaParque) 
    {
        this.colaEntrada = colaEsperaEntradaParque;
    }
    
    /*El método entrar controla que los usuarios pasen al parque hasta completar
    el aforo, en ese caso empezarán a ubicarse dentro de la cola de espera, se 
    controla además el paso de los menores.*/
    public void entrar(Usuario u) 
    {
        try 
        {
            colaEntrada.meter(u);
            sem.acquire();
        } 
        catch (InterruptedException e) 
        {
            System.out.println("Error al entrar al Parque acuático.");
        }
        colaEntrada.sacar(u);
        numMenores++;
    }
    /*Con el método salir se controla de forma inversa que al entrar, que se dejan
    libres los permisos de los semáforos para que pueda pasar el siguiente usuario
    y se actualiza el número de menores en caso de que el usuario que salga lo sea.*/
    public void salir(Usuario u) 
    {
        if (u.getEdad() < 18) 
        {
            numMenores--;
        }
        sem.release();
    }
}
